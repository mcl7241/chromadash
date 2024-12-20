package com.linmarina.lab12;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class GraphActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        setupGraph();
    }

    private void setupGraph() {
        LineChart chart = findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        List<Integer> scores = getHighScores();
        for (int i = 0; i < scores.size(); i++) {
            entries.add(new Entry(i, scores.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "High Scores");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
    public void saveHighScore(int newScore) {
        SharedPreferences prefs = getSharedPreferences("HighScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get current high scores, if none found, use an empty string
        String highScores = prefs.getString("HighScoresList", "");

        // Append the new score to the existing string
        highScores += newScore + ","; // comma-separated values

        // Save the updated high scores
        editor.putString("HighScoresList", highScores);
        editor.apply();
    }

    private List<Integer> getHighScores() {
        SharedPreferences prefs = getSharedPreferences("HighScores", MODE_PRIVATE);
        String highScoresString = prefs.getString("HighScoresList", "");

        List<Integer> scoresList = new ArrayList<>();
        if (!highScoresString.equals("")) {
            for (String scoreStr : highScoresString.split(",")) {
                scoresList.add(Integer.parseInt(scoreStr.trim()));
            }
        }
        return scoresList;
    }

}