package com.example.beatalert;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ImageButton logout;
    private RecyclerView recyclerView;
    private List<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize RecyclerView and set layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load the songs dynamically from arrays.xml
        songList = loadSongsFromResources();

        // Initialize adapter and set it to RecyclerView
        SongAdapter adapter = new SongAdapter(this, songList);
        recyclerView.setAdapter(adapter);

        // Logout button setup
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                Toast.makeText(DashboardActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to load songs from arrays.xml
    private List<Song> loadSongsFromResources() {
        List<Song> songs = new ArrayList<>();

        // Load song titles and resources dynamically from arrays.xml
        String[] songTitles = getResources().getStringArray(R.array.song_titles);
        TypedArray songResources = getResources().obtainTypedArray(R.array.song_resources);

        // Iterate over song titles and resources, and add them to the song list
        for (int i = 0; i < songTitles.length; i++) {
            int resourceId = songResources.getResourceId(i, -1); // Get the resource ID of the song
            songs.add(new Song(songTitles[i], resourceId)); // Add song to the list
        }

        songResources.recycle();  // Recycle the TypedArray after use
        return songs;
    }
}
