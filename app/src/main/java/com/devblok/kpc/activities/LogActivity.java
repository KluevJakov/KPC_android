package com.devblok.kpc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.tools.ActivityTools;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTools.fullscreenMode(getWindow());
        setContentView(R.layout.activity_log);

        ImageView buttonClose = (ImageView) findViewById(R.id.close_btn2);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.main);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityTools.fullscreenMode(getWindow());
    }
}