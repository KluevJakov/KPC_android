package com.devblok.kpc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.tools.ActivityTools;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTools.fullscreenMode(getWindow());
        setContentView(R.layout.activity_welcome);

        TextView textView = findViewById(R.id.textView);
        textView.setText(String.format(getResources().getString(R.string.label_welcome), getResources().getString(R.string.app_name)));

        Button buttonReg = findViewById(R.id.button);
        buttonReg.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegActivity.class);
            ActivityTools.closeAllConnections();
            startActivity(intent);
        });

        ImageView buttonLog = findViewById(R.id.log_btn);
        buttonLog.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LogActivity.class);
            ActivityTools.closeAllConnections();
            startActivity(intent);
        });
    }
}
