package com.devblok.kpc.activities;

import static com.devblok.kpc.tools.WebConstants.SHARED_PREFS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnimalActivity extends AppCompatActivity {

    private TextView titleText4;
    private ImageView imageView16;
    private TextView textView21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        TextView titleText4 = findViewById(R.id.titleRead4);
        ImageView imageView16 = findViewById(R.id.imageView16);
        TextView textView21 = findViewById(R.id.textView21);

        /*
            From bundle
         */
        if (getIntent().getData() != null) {
            SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            if (sharedpreferences.getString("USER_ID_KEY", null) != null) {
                try {
                    Uri data = getIntent().getData();
                    UUID uuid = UUID.fromString(data.getQueryParameter("id"));
                    runIntent(uuid);
                } catch (Exception e) {
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Произошла ошибка, проверьте корректность ссылки", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Произошла ошибка, вы не авторизованы", Toast.LENGTH_SHORT).show();
            }
        } else {
            Bundle bundle = getIntent().getExtras();

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
            addSickBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), SickActivity.class);
                Bundle b = new Bundle();
                b.putString("animalId", (String) bundle.get("id"));
                intent.putExtras(b);
                startActivity(intent);
            });

            Button planInspectBtn = findViewById(R.id.saveBtn4);

            Button removeBtn = findViewById(R.id.saveBtn5);
            removeBtn.setOnClickListener(view -> {
                try {
                    run(bundle.getString("id"));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    protected void run(String uuid) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/animal?animalId="+uuid)
                .delete()
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
                        Toast.makeText(getApplicationContext(), "Животное удалено", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        ActivityTools.setupBackLastFragment(intent, R.id.animals);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    protected void runIntent(UUID uuid) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/animal?animalId="+uuid).get().build();

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
                        Animal animal = gson.fromJson(myResponse, new TypeToken<Animal>() {}.getType());
                        Bundle bFromUrl = new Bundle();
                        bFromUrl.putString("nickOrNumber", animal.getNickOrNumber());
                        bFromUrl.putString("type", animal.getType());
                        bFromUrl.putString("sex", animal.getSex());
                        bFromUrl.putString("age", animal.getAge());
                        bFromUrl.putString("breed", animal.getBreed());
                        bFromUrl.putString("owner", animal.getOwner());
                        bFromUrl.putString("id", animal.getId().toString());

                        ((TextView) findViewById(R.id.titleRead4)).setText(animal.getNickOrNumber());
                        new DownloadImageTask(findViewById(R.id.imageView16)).execute(WebConstants.backendUrlBase+animal.getAvatar());
                        ((TextView) findViewById(R.id.textView21)).setText(generateInfo(bFromUrl));

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
                            b.putString("id", (String) bFromUrl.get("id"));
                            intent.putExtras(b);
                            startActivity(intent);
                        });

                        Button addSickBtn = findViewById(R.id.saveBtn3);
                        addSickBtn.setOnClickListener(view -> {
                            Intent intent = new Intent(getApplicationContext(), SickActivity.class);
                            Bundle b = new Bundle();
                            b.putString("animalId", (String) bFromUrl.get("id"));
                            intent.putExtras(b);
                            startActivity(intent);
                        });

                        Button planInspectBtn = findViewById(R.id.saveBtn4);

                        Button removeBtn = findViewById(R.id.saveBtn5);
                        removeBtn.setOnClickListener(view -> {
                            try {
                                run(bFromUrl.getString("id"));
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}