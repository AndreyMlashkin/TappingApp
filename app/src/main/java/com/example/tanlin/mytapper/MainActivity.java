package com.example.tanlin.mytapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static long RESET_DURATION = 2000;
    TempoCalculator m_tempoCalculator;
    Timer m_timer;
    ArrayList<Long> m_rythm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_tempoCalculator = new TempoCalculator();

        m_rythm = new ArrayList<>();
        for(int i = 0; i < 360; ++i)
        {
            m_rythm.add(1000L);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    protected void onDestroy() {
        if (m_timer != null) {
            m_timer.cancel();
        }

        m_tempoCalculator.clearTimes();
        super.onDestroy();
    }

    private void initialize() {
        TextView tempoTextView = (TextView) findViewById(R.id.tempoTextView);
        tempoTextView.setText("0");
        //initializeFonts();
        setupTouchListener();
    }

    private void initializeFonts() {
        Typeface font = Typeface.createFromAsset(getAssets(),
                "SourceSansPro-Light.ttf");

        TextView instructionalTextView = (TextView) findViewById(R.id.instructionalLabelTextView);
        instructionalTextView.setTypeface(font);

        TextView tempoLabelTextView = (TextView) findViewById(R.id.tempoLabelTextView);
        tempoLabelTextView.setTypeface(font);

        TextView tempoTextView = (TextView) findViewById(R.id.tempoTextView);
        tempoTextView.setTypeface(font);
    }

    private void setupTouchListener()
    {
        View view = (View) findViewById(R.id.appView);
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handleTouch();
                    return true;
                }
                return false;
            }
        });
    }

    public void handleTouch() {
        saturateBackground();
        m_tempoCalculator.recordTime();
        restartResetTimer();
        updateView();
    }

    private void saturateBackground()
    {
        if (!m_tempoCalculator.isRecording())
        {
            View view = (View) findViewById(R.id.appView);
            view.setBackgroundColor(0xFFED1139);
        }
    }

    private void resetBackground()
    {
        View view = (View) findViewById(R.id.appView);
        view.setBackgroundColor(0xFF11E6ED);
    }

    private void updateView() {
        String displayValue;

        double tempo = m_tempoCalculator.getTempo();
        if(tempo > 0)
            displayValue = Double.valueOf(tempo).toString();
        else
            displayValue = "tap_again";

        TextView tempoTextView = (TextView) findViewById(R.id.tempoTextView);
        tempoTextView.setText(displayValue);

        TextView clickTimes = (TextView) findViewById(R.id.times);
        String deltasText = m_tempoCalculator.getDeltas().toString();
        deltasText = deltasText.replace(", ", "\n");
        clickTimes.setText(deltasText);

        TextView rythm = (TextView) findViewById(R.id.rythm);
        String rythmText = m_rythm.toString();
        rythmText = rythmText.replace(", ", "\n");
        rythm.setText(rythmText);
    }

    private void restartResetTimer() {
        stopResetTimer();
        startResetTimer();
    }

    private void startResetTimer() {
        m_timer = new Timer("reset-tempo-calculator", true);
        m_timer.schedule(new TimerTask() {

            @Override
            public void run() {
                m_tempoCalculator.clearTimes();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run()
                    {
                        resetBackground();
                        updateView();
                    }
                });
            }
        }, RESET_DURATION);
    }

    private void stopResetTimer() {
        if (m_timer != null) {
            m_timer.cancel();
        }
    }
}
