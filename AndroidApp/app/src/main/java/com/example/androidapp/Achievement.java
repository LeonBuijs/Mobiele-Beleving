package com.example.androidapp;

public class Achievement {
    private int image;
    private String name;
    private String description;
    private boolean achieved;

    public Achievement(int image, String name, String description, boolean achieved) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.achieved = achieved;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }
}
