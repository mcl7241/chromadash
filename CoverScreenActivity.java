package com.linmarina.lab12;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CoverScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_screen);
        // Example: Navigate to the main activity after a delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(CoverScreenActivity.this, Rules.class);
            startActivity(intent);
            finish();
        }, 2000); // 2000 ms delay

    }

    public void onContinueClicked(View view) {
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent);
    }

}
