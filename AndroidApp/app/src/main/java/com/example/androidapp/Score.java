package com.example.androidapp;

import java.time.LocalDateTime;

public class Score{
    private int num;
    private String name;
    private int score;
    private LocalDateTime dateTime;

    public Score(int num, String name, int score) {
        this.num = num;
        this.name = name;
        this.score = score;
        this.dateTime = LocalDateTime.now();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDateTime() {
        return dateTime.getDayOfMonth() + "-" + dateTime.getMonth() + "-" + dateTime.getYear();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
