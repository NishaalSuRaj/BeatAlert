package com.example.beatalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {

    private Button playButton, pauseButton, stopButton;
    private SeekBar seekBar;
    private ImageButton logout;
    private TextView songTitle, currentTime, totalDuration;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private String selectedSongTitle;
    private LinearLayout backgroundImageView;

    // Array of background images
    private int[] backgroundImages = {
            R.drawable.ic_img2, // Add your image resource names here
            R.drawable.ic_img3,
            R.drawable.ic_img4,
            R.drawable.ic_img5,
            R.drawable.ic_img6,
            R.drawable.ic_img7,
            R.drawable.ic_img8,
            R.drawable.ic_img9,
            R.drawable.ic_img10
    };

    private int currentImageIndex = 0; // To keep track of the current image index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayer);

        // Initialize UI components
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        seekBar = findViewById(R.id.seekBar);
        songTitle = findViewById(R.id.songTitle);
        currentTime = findViewById(R.id.currentTime);
        totalDuration = findViewById(R.id.totalDuration);
        logout = findViewById(R.id.logout);

        // Get reference to the LinearLayout to change its background dynamically
        backgroundImageView = findViewById(R.id.linearlayout);

        // Start changing the background image
        startBackgroundImageChanger();

        // Retrieve song details from Intent
        Intent intent = getIntent();
        int songResource = intent.getIntExtra("songResource", 0);
        selectedSongTitle = intent.getStringExtra("songTitle");

        // Set the song title dynamically
        if (selectedSongTitle != null) {
            songTitle.setText(selectedSongTitle);
        } else {
            songTitle.setText("Song Playing  😊😊 "); // Fallback if title not provided
        }

        // Initialize MediaPlayer with the selected song
        mediaPlayer = MediaPlayer.create(this, songResource);

        // Set up SeekBar
        seekBar.setMax(100);

        mediaPlayer.start();
        updateSeekBar();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress * mediaPlayer.getDuration() / 100);
                    updateCurrentTime();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Set total duration
        totalDuration.setText(formatTime(mediaPlayer.getDuration()));

        // Play Button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    updateSeekBar();
                }
            }
        });

        // Pause Button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    handler.removeCallbacks(updateSeekBarRunnable);
                }
            }
        });

        // Stop Button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayer.this, DashboardActivity.class);
                Toast.makeText(MusicPlayer.this, "Back", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        // Logout button click listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicPlayer.this, LoginActivity.class);
                Toast.makeText(MusicPlayer.this, "Logging out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to start changing the background image every second
    private void startBackgroundImageChanger() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                backgroundImageView.setBackgroundResource(backgroundImages[currentImageIndex]);
                currentImageIndex = (currentImageIndex + 1) % backgroundImages.length;
                handler.postDelayed(this, 5000);
            }
        }, 1000);
    }

    // Update SeekBar as the song plays
    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration());
        updateCurrentTime();
        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(updateSeekBarRunnable, 1000);
        }
    }

    private void updateCurrentTime() {
        currentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
    }

    private Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    // Format time in mm:ss format
    private String formatTime(int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            handler.removeCallbacks(updateSeekBarRunnable);
        }
    }
}
