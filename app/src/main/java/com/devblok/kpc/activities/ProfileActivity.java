package com.devblok.kpc.activities;

import static com.devblok.kpc.tools.WebConstants.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devblok.kpc.R;
import com.devblok.kpc.adapter.AnimalAdapter;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.entity.User;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    protected EditText fioField;
    protected EditText birthdayField;
    protected EditText courseField;
    protected EditText phoneField;
    protected EditText emailField;
    protected EditText passwordField;
    protected ImageView imageView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fioField = findViewById(R.id.fioField);
        birthdayField = findViewById(R.id.birthdayField);
        courseField = findViewById(R.id.courseField);
        phoneField = findViewById(R.id.phoneField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        imageView6 = findViewById(R.id.imageView6);

        ImageView buttonClose = findViewById(R.id.close_btn3);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.main);
            startActivity(intent);
        });

        Button buttonDeauth = findViewById(R.id.button4);
        buttonDeauth.setOnClickListener(view -> {

            SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        });

        try {
            run();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
        }
    }

    protected void run() throws Exception {
        OkHttpClient client = new OkHttpClient();

        SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        UUID uuid = UUID.fromString(sharedpreferences.getString("USER_ID_KEY", null));

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/user?userId="+uuid.toString())
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
                        User currentUser = gson.fromJson(myResponse, new TypeToken<User>() {}.getType());

                        fioField.setText(currentUser.getFio());
                        birthdayField.setText(currentUser.getBirthday().toString());
                        //courseField.setText(currentUser.get());
                        phoneField.setText(currentUser.getPhone());
                        emailField.setText(currentUser.getEmail());
                        passwordField.setText(currentUser.getPassword());
                        new DownloadImageTask(imageView6).execute(WebConstants.backendUrlBase+currentUser.getAvatar());

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}