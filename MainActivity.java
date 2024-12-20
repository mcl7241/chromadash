package com.linmarina.lab12;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.graphics.RectF;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DrawView drawView;
    private TextToSpeech textToSpeech;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 60 seconds for example
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = findViewById(R.id.drawView);
        timerTextView = findViewById(R.id.timerTextView);
        score = 0;
        startTimer();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        drawView.setTextToSpeech(textToSpeech);
    }
    public void movedownLeft(View view) {
        drawView.sprite.setdX(-3);//set horizontal speed to move left
        drawView.sprite.setdY(3);
    }

    public void movedownRight(View view) {
        drawView.sprite.setdX(3);//set horizontal speed to move right
        drawView.sprite.setdY(3);
    }

    public void moveupLeft(View view) {
        drawView.sprite.setdY(-3);//set horizontal speed to move left
        drawView.sprite.setdX(-3);
    }

    public void moveupRight(View view) {
        drawView.sprite.setdY(-3);//set horizontal speed to move right
        drawView.sprite.setdX(3);
    }
    public void redCheckBoxClicked(View view) {
        setColor();
    }

    public void greenCheckBoxClicked(View view) {
        setColor();
    }
    public void setColor() {
        CheckBox greenCheckBox = findViewById(R.id.greenCheckBox);
        CheckBox redCheckBox = findViewById(R.id.redCheckBox);
        if (redCheckBox.isChecked()) {
            if (greenCheckBox.isChecked())
                drawView.sprite.setColor(Color.YELLOW);
            else drawView.sprite.setColor(Color.RED);
        } else if (greenCheckBox.isChecked())
            drawView.sprite.setColor(Color.GREEN);
        else drawView.sprite.setColor(Color.BLUE);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");
                endGame();
                // Handle time's up event
            }
        }.start();
    }

    private void endGame() {
        // Stop any game logic if necessary
        int final_score = drawView.getScore();
        // Display Game Over message with the score
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Game Over")
                .setMessage("Your score: " + final_score)
                .setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Navigate to the Main Menu or Exit
                        finish(); // Close this activity
                    }
                })
                .setNegativeButton("View Graph", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                        startActivity(intent);

                    }
                })
                .setCancelable(false)
                .show();
    }
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "Time: %02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }


}