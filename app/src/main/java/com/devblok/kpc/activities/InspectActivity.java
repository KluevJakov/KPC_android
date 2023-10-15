package com.devblok.kpc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.WebConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InspectActivity extends AppCompatActivity {
    protected Animal currentAnimal;
    protected UUID currentAnimalUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageView buttonClose = findViewById(R.id.close_btn6);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.animals);
            startActivity(intent);
        });

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(view -> {
            try {
                save();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentAnimalUUID = UUID.fromString((String) bundle.get("animalId"));
            try {
                run();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
            }
        } else {
            currentAnimal = new Animal();
        }
    }

    protected void run() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/animal?animalId=" + currentAnimalUUID.toString())
                .get()
                .build();

        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                runOnUiThread(() -> {
                    try {
                        Gson gson = new Gson();
                        currentAnimal = gson.fromJson(myResponse, new TypeToken<Animal>() {}.getType());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    protected void save() throws Exception {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(currentAnimal));

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/animal")
                .post(formBody)
                .build();

        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                runOnUiThread(() -> {
                    try {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        ActivityTools.setupBackLastFragment(intent, R.id.animals);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}