package com.example.tanlin.mytapper;

/**
 * Created by tanlin on 03.12.17.
 */

import java.util.ArrayList;

public class TempoCalculator {
    private static final Long MILLISECONDS_IN_A_MINUTE = 60000L;
    private ArrayList<Long> m_times;
    private boolean m_isRecording;

    public TempoCalculator() {
        m_times = new ArrayList<Long>();
        m_isRecording = false;
    }

    public void recordTime() {
        Long time = System.currentTimeMillis();
        m_times.add(time);
        m_isRecording = true;
    }

    public int getTempo()
    {
        if(m_times.size() < 2)
            return -1;
        ArrayList<Long> deltas = getDeltas();
        return calculateTempo(deltas);
    }

    public void clearTimes() {
        m_times.clear();
        m_isRecording = false;
    }

    public ArrayList<Long> getDeltas() {
        ArrayList<Long> deltas = new ArrayList<Long>();

        for (int i = 0; i < m_times.size() - 1; i++) {
            Long delta = m_times.get(i + 1) - m_times.get(i);
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
        return m_isRecording;
    }
}