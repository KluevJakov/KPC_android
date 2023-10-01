package com.devblok.kpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devblok.kpc.R;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Bundle b = getIntent().getExtras();

        TextView readText = findViewById(R.id.readText);
        readText.setText(b.getString("text"));
        TextView titleRead = findViewById(R.id.titleRead);
        titleRead.setText(b.getString("title"));
        TextView authorRead = findViewById(R.id.authorRead);
        authorRead.setText(b.getString("author"));

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        SeekBar fontSizeBar = findViewById(R.id.fontSizeBar);
        fontSizeBar.setMax(100);
        fontSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    readText.setTextSize(14);
                } else {
                    readText.setTextSize(14 + (i * 0.14f));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}