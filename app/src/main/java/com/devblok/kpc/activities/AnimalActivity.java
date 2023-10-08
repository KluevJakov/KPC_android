package com.devblok.kpc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;

public class AnimalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        Bundle bundle = getIntent().getExtras();
        TextView titleText4 = findViewById(R.id.titleRead4);
        ImageView imageView16 = findViewById(R.id.imageView16);
        TextView textView21 = findViewById(R.id.textView21);

        titleText4.setText(bundle.getString("nickOrNumber"));
        new DownloadImageTask(imageView16).execute(WebConstants.backendUrlBase+bundle.getString("avatar"));
        textView21.setText(generateInfo(bundle));

        ImageView backBtn = findViewById(R.id.backBtn4);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.animals);
            startActivity(intent);
        });

        Button editBtn = findViewById(R.id.saveBtn2);
        editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AnimalEditActivity.class);
            Bundle b = new Bundle();
            b.putString("id", (String) bundle.get("id"));
            intent.putExtras(b);
            startActivity(intent);
        });

        Button addSickBtn = findViewById(R.id.saveBtn3);

        Button planInspectBtn = findViewById(R.id.saveBtn4);

    }

    protected String generateInfo(Bundle bundle) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Вид животного - ");
        stringBuilder.append(bundle.getString("type"));
        stringBuilder.append("\n");
        stringBuilder.append("Пол - ");
        stringBuilder.append(bundle.getString("sex"));
        stringBuilder.append("\n");
        stringBuilder.append("Возраст - ");
        stringBuilder.append(bundle.getString("age"));
        stringBuilder.append("\n");
        stringBuilder.append("Кличка или номер бирки - ");
        stringBuilder.append(bundle.getString("nickOrNumber"));
        stringBuilder.append("\n");
        stringBuilder.append("Масть или приметы - ");
        stringBuilder.append(bundle.getString("breed"));
        stringBuilder.append("\n");
        stringBuilder.append("Название подразделения фермы (отделения) или ФИО владельца животного - ");
        stringBuilder.append(bundle.getString("owner"));
        return stringBuilder.toString();
    }
}