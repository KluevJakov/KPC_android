package com.devblok.kpc.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnimalEditActivity extends AppCompatActivity {

    protected ImageView imageView;
    protected Animal currentAnimal;
    int SELECT_PICTURE = 200;
    protected UUID currentAnimalUUID;
    protected EditText typeField;
    protected EditText sexField;
    protected EditText ageField;
    protected EditText nameField;
    protected EditText breedField;
    protected EditText ownerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_edit);

        imageView = findViewById(R.id.imageView15);
        typeField = findViewById(R.id.typeField);
        sexField = findViewById(R.id.sexField);
        ageField = findViewById(R.id.ageField);
        nameField = findViewById(R.id.nameField);
        breedField = findViewById(R.id.breedField);
        ownerField = findViewById(R.id.ownerField);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageView buttonClose = findViewById(R.id.close_btn6);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.animals);
            ActivityTools.closeAllConnections();
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

        imageView.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PICTURE);
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentAnimalUUID = UUID.fromString((String) bundle.get("id"));
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

                        typeField.setText(currentAnimal.getType());
                        sexField.setText(currentAnimal.getSex());
                        ageField.setText(currentAnimal.getAge());
                        nameField.setText(currentAnimal.getNickOrNumber());
                        breedField.setText(currentAnimal.getBreed());
                        ownerField.setText(currentAnimal.getOwner());

                        new DownloadImageTask(imageView).execute(WebConstants.backendUrlBase + currentAnimal.getAvatar());

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

        if (typeField.getText().toString().isEmpty() || sexField.getText().toString().isEmpty() ||
                ageField.getText().toString().isEmpty() || nameField.getText().toString().isEmpty() ||
                breedField.getText().toString().isEmpty() || ownerField.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Проверьте заполненность всех полей", Toast.LENGTH_SHORT).show();
            return;
        }

        currentAnimal.setType(typeField.getText().toString());
        currentAnimal.setSex(sexField.getText().toString());
        currentAnimal.setAge(ageField.getText().toString());
        currentAnimal.setNickOrNumber(nameField.getText().toString());
        currentAnimal.setBreed(breedField.getText().toString());
        currentAnimal.setOwner(ownerField.getText().toString());

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
                        ActivityTools.closeAllConnections();
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    protected void updateAvatar(String path) throws Exception {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        currentAnimal.setAvatar(path);

        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"),
                mapper.writeValueAsString(currentAnimal));

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
                        Gson gson = new Gson();
                        Animal savedAnimal = gson.fromJson(myResponse, new TypeToken<Animal>() {}.getType());
                        if (currentAnimal.getId() == null || currentAnimalUUID == null) {
                            currentAnimal.setId(savedAnimal.getId());
                            currentAnimalUUID = savedAnimal.getId();
                        }

                        Toast.makeText(getApplicationContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);

                OkHttpClient client = new OkHttpClient();
                File file = new File(getPath(data.getData()));
                RequestBody multipartBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                        .build();

                Request request1 = new Request.Builder()
                        .url(WebConstants.backendUrlBase + "/upload")
                        .post(multipartBody)
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
                                updateAvatar(myResponse.toString());
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }
}