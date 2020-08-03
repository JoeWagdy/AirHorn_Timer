package com.joewagdy.airHornTimer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView timerTextView;
    Button controllerButton;
    Boolean counterIsActive = false;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*60/100,0);

        timerSeekBar = findViewById(R.id.timerSeekBar);
        timerTextView = findViewById(R.id.timerTextView);
        controllerButton = findViewById(R.id.controllerButton);

        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(90);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void countDown(View view) {
        if (!counterIsActive) {
            counterIsActive = true;
            timerSeekBar.setEnabled(false);
            controllerButton.setText(R.string.stop);


            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / 1000);
                    timerSeekBar.setProgress((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    updateTimer(0);
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.air_horn);
                    mediaPlayer.start();
                    controllerButton.setText(R.string.reset);
                }
            }.start();
        }else if(controllerButton.getText() == getString(R.string.reset)){
            resetTimer();
        }
        else {
            resetTimer();
        }
    }

    private void updateTimer(int secLeft) {
        int minutes = secLeft / 60;
        int seconds = secLeft - minutes * 60;
        timerTextView.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
    }

    private void resetTimer(){
        countDownTimer.cancel();
        counterIsActive = false;
        timerTextView.setText("01:30");
        timerSeekBar.setProgress(90);
        controllerButton.setText(R.string.go);
        timerSeekBar.setEnabled(true);
    }
}
