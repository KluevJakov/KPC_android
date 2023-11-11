package com.devblok.kpc.activities;

import static com.devblok.kpc.tools.WebConstants.SHARED_PREFS;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
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
                    ActivityTools.closeAllConnections();
                    runIntent(uuid, this);
                } catch (Exception e) {
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    ActivityTools.closeAllConnections();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Произошла ошибка, проверьте корректность ссылки", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                ActivityTools.closeAllConnections();
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Произошла ошибка, вы не авторизованы", Toast.LENGTH_SHORT).show();
            }
        } else {
            Bundle bundle = getIntent().getExtras();

            if (StringUtils.isNotEmpty(bundle.getString("animalId"))) {
                try {
                    ActivityTools.closeAllConnections();
                    runIntent(UUID.fromString(bundle.getString("animalId")), this);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Произошла ошибка, вы не авторизованы", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            titleText4.setText(bundle.getString("nickOrNumber"));
            new DownloadImageTask(imageView16).execute(WebConstants.backendUrlBase+bundle.getString("avatar"));
            textView21.setText(generateInfo(bundle));

            ImageView backBtn = findViewById(R.id.backBtn4);
            backBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                ActivityTools.setupBackLastFragment(intent, R.id.animals);
                ActivityTools.closeAllConnections();
                startActivity(intent);
            });

            Button editBtn = findViewById(R.id.saveBtn2);
            editBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), AnimalEditActivity.class);
                Bundle b = new Bundle();
                b.putString("id", (String) bundle.get("id"));
                intent.putExtras(b);
                ActivityTools.closeAllConnections();
                startActivity(intent);
            });

            Button addSickBtn = findViewById(R.id.saveBtn3);
            addSickBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), SickActivity.class);
                Bundle b = new Bundle();
                b.putString("animalId", (String) bundle.get("id"));
                intent.putExtras(b);
                ActivityTools.closeAllConnections();
                startActivity(intent);
            });

            Button planInspectBtn = findViewById(R.id.saveBtn4);
            planInspectBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), InspectActivity.class);
                Bundle b = new Bundle();
                b.putString("animalId", (String) bundle.get("id"));
                intent.putExtras(b);
                ActivityTools.closeAllConnections();
                startActivity(intent);
            });

            Button removeBtn = findViewById(R.id.saveBtn5);
            removeBtn.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Подтвердите удаление")
                        .setMessage("Вы уверены, что хотите удалить данного животного?")
                        .setPositiveButton("Отмена", (dialogInterface, i) -> dialogInterface.cancel())
                        .setNegativeButton("Да, удалить", (dialogInterface, i) -> {
                            try {
                                run(bundle.getString("id"));
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                            }
                        }).create();
                builder.show();
            });

            /* */

            TextView info3 = findViewById(R.id.info3);
            info3.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                ActivityTools.setupBackLastFragment(intent, R.id.health);
                Bundle b = new Bundle();
                b.putString("animalSearchId", (String) bundle.get("id"));
                intent.putExtras(b);
                ActivityTools.closeAllConnections();
                startActivity(intent);
            });

            TextView info2 = findViewById(R.id.info2);
            info2.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                ActivityTools.setupBackLastFragment(intent, R.id.inspect);
                Bundle b = new Bundle();
                b.putString("animalSearchId", (String) bundle.get("id"));
                intent.putExtras(b);
                ActivityTools.closeAllConnections();
                startActivity(intent);
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
                        ActivityTools.closeAllConnections();
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    protected void runIntent(UUID uuid, Context context) throws Exception {
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
                            ActivityTools.closeAllConnections();
                            startActivity(intent);
                        });

                        Button editBtn = findViewById(R.id.saveBtn2);
                        editBtn.setOnClickListener(view -> {
                            Intent intent = new Intent(getApplicationContext(), AnimalEditActivity.class);
                            Bundle b = new Bundle();
                            b.putString("id", (String) bFromUrl.get("id"));
                            intent.putExtras(b);
                            ActivityTools.closeAllConnections();
                            startActivity(intent);
                        });

                        Button addSickBtn = findViewById(R.id.saveBtn3);
                        addSickBtn.setOnClickListener(view -> {
                            Intent intent = new Intent(getApplicationContext(), SickActivity.class);
                            Bundle b = new Bundle();
                            b.putString("animalId", (String) bFromUrl.get("id"));
                            intent.putExtras(b);
                            ActivityTools.closeAllConnections();
                            startActivity(intent);
                        });

                        Button planInspectBtn = findViewById(R.id.saveBtn4);
                        planInspectBtn.setOnClickListener(view -> {
                            Intent intent = new Intent(getApplicationContext(), InspectActivity.class);
                            Bundle b = new Bundle();
                            b.putString("animalId", (String) bFromUrl.get("id"));
                            intent.putExtras(b);
                            ActivityTools.closeAllConnections();
                            startActivity(intent);
                        });

                        Button removeBtn = findViewById(R.id.saveBtn5);
                        removeBtn.setOnClickListener(view -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Подтвердите удаление")
                                    .setMessage("Вы уверены, что хотите удалить данного животного?")
                                    .setPositiveButton("Отмена", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .setNegativeButton("Да, удалить", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            try {
                                                run(bFromUrl.getString("id"));
                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).create();
                            builder.show();
                        });
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}