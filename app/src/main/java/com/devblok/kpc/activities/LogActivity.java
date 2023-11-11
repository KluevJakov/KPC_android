package com.devblok.kpc.activities;

import static com.devblok.kpc.tools.WebConstants.SHARED_PREFS;
import static com.devblok.kpc.tools.WebConstants.USER_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.dto.AuthResponse;
import com.devblok.kpc.entity.dto.AuthStatus;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.WebConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTools.fullscreenMode(getWindow());
        setContentView(R.layout.activity_log);

        EditText emailField = findViewById(R.id.editTextTextEmailAddress2);
        EditText passwordField = findViewById(R.id.editTextTextPassword3);

        ImageView buttonClose = findViewById(R.id.close_btn2);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            ActivityTools.closeAllConnections();
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            if (StringUtils.isEmpty(emailField.getText().toString()) || StringUtils.isBlank(emailField.getText().toString())) {
                emailField.requestFocus();
                return;
            }

            if (StringUtils.isEmpty(passwordField.getText().toString()) || StringUtils.isBlank(passwordField.getText().toString())) {
                passwordField.requestFocus();
                return;
            }

            try {
                run(emailField.getText().toString(), passwordField.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityTools.fullscreenMode(getWindow());
    }

    protected void run(String email, String password) throws Exception {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("login", email)
                .add("password", password)
                .build();

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/auth")
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
                        Gson gson = new Gson();
                        AuthResponse authResponse = gson.fromJson(myResponse, new TypeToken<AuthResponse>() {}.getType());

                        if (authResponse.getStatus() == AuthStatus.NO_SUCH_USER.ordinal()) {
                            Toast.makeText(getApplicationContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                        } else if (authResponse.getStatus() == AuthStatus.INCORRECT_PASSWORD.ordinal()) {
                            Toast.makeText(getApplicationContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                        } else if (authResponse.getStatus() == AuthStatus.DEACTIVATED_USER.ordinal()) {
                            Toast.makeText(getApplicationContext(), "Пользователь не активирован, обратитесь к администратору", Toast.LENGTH_LONG).show();
                        } else if (authResponse.getStatus() == AuthStatus.SUCCESS.ordinal()) {
                            Toast.makeText(getApplicationContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(USER_ID_KEY, authResponse.getUser().getId().toString());
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            ActivityTools.setupBackLastFragment(intent, R.id.main);
                            ActivityTools.closeAllConnections();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Неизвестная ошибка, попробуйте позже", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}