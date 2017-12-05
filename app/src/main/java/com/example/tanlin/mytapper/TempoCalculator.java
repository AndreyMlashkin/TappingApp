package com.example.tanlin.mytapper;

/**
 * Created by tanlin on 03.12.17.
 */

import java.util.ArrayList;

public class TempoCalculator {
    private static final Long MILLISECONDS_IN_A_MINUTE = 60000L;
    public ArrayList<Long> times;
    private boolean isRecording;

    public TempoCalculator() {
        times = new ArrayList<Long>();
        isRecording = false;
    }

    public void recordTime() {
        Long time = System.currentTimeMillis();
        times.add(time);
        isRecording = true;
    }

    public int getTempo() {
        ArrayList<Long> deltas = getDeltas();
        return calculateTempo(deltas);
    }

    public void clearTimes() {
        times.clear();
        isRecording = false;
    }

    private ArrayList<Long> getDeltas() {
        ArrayList<Long> deltas = new ArrayList<Long>();

        for (int i = 0; i < times.size() - 1; i++) {
            Long delta = times.get(i + 1) - times.get(i);
            deltas.add(delta);
        }

        return deltas;
    }

    private int calculateTempo(ArrayList<Long> deltas) {
        Long sum = 0L;

        for (Long delta : deltas) {
            sum = sum + delta;
        }

        Long average = sum / deltas.size();

        return (int) (MILLISECONDS_IN_A_MINUTE / average);
    }

    public boolean isRecording() {
        return isRecording;
    }
}