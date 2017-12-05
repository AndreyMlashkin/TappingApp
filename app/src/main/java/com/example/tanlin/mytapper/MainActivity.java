package com.example.tanlin.mytapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static long RESET_DURATION = 2000;
    TempoCalculator tempoCalculator;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempoCalculator = new TempoCalculator();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }

        tempoCalculator.clearTimes();
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

        TextView tapButtonView = (TextView) findViewById(R.id.tapButtonView);
        tapButtonView.setTypeface(font);
    }

    private void setupTouchListener() {
        View tapButton = (View) findViewById(R.id.tapButtonView);
        tapButton.setOnTouchListener(new OnTouchListener() {
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
        //saturateBackground();
        tempoCalculator.recordTime();
        restartResetTimer();
        updateView();
    }

    private void saturateBackground() {
        if (!tempoCalculator.isRecording()) {
            View view = (View) findViewById(R.id.appView);
            TransitionDrawable background = (TransitionDrawable) view
                    .getBackground();
            background.startTransition((int) RESET_DURATION);
        }
    }

    private void resetBackground() {
        View view = (View) findViewById(R.id.appView);
        TransitionDrawable background = (TransitionDrawable) view
                .getBackground();
        background.reverseTransition((int) (RESET_DURATION / 5));
    }

    private void updateView() {
        String displayValue;

        if (tempoCalculator.times.size() >= 2) {
            double tempo = tempoCalculator.getTempo();
            displayValue = Double.valueOf(tempo).toString();
        } else {
            displayValue = "tap_again";
        }

        TextView tempoTextView = (TextView) findViewById(R.id.tempoTextView);
        tempoTextView.setText(displayValue);
    }

    private void restartResetTimer() {
        stopResetTimer();
        startResetTimer();
    }

    private void startResetTimer() {
        timer = new Timer("reset-tempo-calculator", true);
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                tempoCalculator.clearTimes();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //resetBackground();
                    }
                });
            }
        }, RESET_DURATION);
    }

    private void stopResetTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
