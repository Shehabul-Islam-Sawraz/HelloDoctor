package com.exercise.thesis.hellodoc.model;

import android.net.Uri;

public class Image {
    private String imageUri;

    public Image(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
