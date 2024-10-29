package com.example.beatalert;

public class Song {
    private String title;
    private int resourceId;

    public Song(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }
}
