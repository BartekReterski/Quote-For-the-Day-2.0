package com.dayquote.quotefortheday.models;

public class SettingsModel {
    private String imageDescription;
    private int image;

    public SettingsModel(String imageDescription, int image) {
        this.imageDescription = imageDescription;
        this.image = image;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
