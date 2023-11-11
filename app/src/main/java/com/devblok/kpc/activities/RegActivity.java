package com.devblok.kpc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.User;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.WebConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTools.fullscreenMode(getWindow());
        setContentView(R.layout.activity_reg);

        ImageView buttonClose = findViewById(R.id.close_btn);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            ActivityTools.closeAllConnections();
        });

        User user = new User();

        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        EditText accept = findViewById(R.id.editTextTextPassword2);

        Button buttonReg = findViewById(R.id.button);
        buttonReg.setOnClickListener(view -> {
            if (StringUtils.isEmpty(email.getText().toString()) || StringUtils.isBlank(email.getText().toString())) {
                email.requestFocus();
                return;
            }

            if (StringUtils.isEmpty(password.getText().toString()) || StringUtils.isBlank(password.getText().toString())) {
                password.requestFocus();
                return;
            }

            if (StringUtils.isEmpty(accept.getText().toString()) || StringUtils.isBlank(accept.getText().toString())) {
                accept.requestFocus();
                return;
            }

            if (!password.getText().toString().equals(accept.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                user.setActivated(false);
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                run(user);
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

    protected void run(User user) throws Exception {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(user));

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/reg")
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
                        Boolean authResponse = gson.fromJson(myResponse, new TypeToken<Boolean>() {}.getType());

                        if (!authResponse) {
                            Toast.makeText(getApplicationContext(), "Такой email уже зарегистрирован", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "Успешная регистрация! Дождитесь активации профиля", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                        ActivityTools.closeAllConnections();
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}