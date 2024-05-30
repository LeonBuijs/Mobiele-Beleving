package com.example.androidapp;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Score> {
    @Override
    public int compare(Score o1, Score o2) {
        return o2.getScore() - o1.getScore();
    }
}
