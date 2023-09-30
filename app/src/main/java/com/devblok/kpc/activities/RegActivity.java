package com.devblok.kpc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.tools.ActivityTools;

public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTools.fullscreenMode(getWindow());
        setContentView(R.layout.activity_reg);

        Button buttonReg = (Button) findViewById(R.id.button);
        buttonReg.setOnClickListener(view -> {
            Toast.makeText(this.getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        });

        ImageView buttonClose = (ImageView) findViewById(R.id.close_btn);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityTools.fullscreenMode(getWindow());
    }
}