package com.devblok.kpc.activities;

import static com.devblok.kpc.tools.WebConstants.SHARED_PREFS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.User;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    protected EditText fioField;
    protected DatePicker birthdayField;
    protected EditText courseField;
    protected EditText phoneField;
    protected EditText emailField;
    protected EditText passwordField;
    protected ImageView imageView6;
    protected User currentUser;
    int SELECT_PICTURE = 200;

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

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageView buttonClose = findViewById(R.id.close_btn3);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.main);
            ActivityTools.closeAllConnections();
            startActivity(intent);
        });

        Button buttonDeauth = findViewById(R.id.button4);
        buttonDeauth.setOnClickListener(view -> {

            SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            ActivityTools.closeAllConnections();
            startActivity(intent);
        });

        Button buttonEdit = findViewById(R.id.button5);
        buttonEdit.setOnClickListener(view -> {
            try {
                save();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonAddAnimal = findViewById(R.id.button3);
        buttonAddAnimal.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AnimalEditActivity.class);
            ActivityTools.closeAllConnections();
            startActivity(intent);
        });

        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PICTURE);
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
                        currentUser = gson.fromJson(myResponse, new TypeToken<User>() {}.getType());

                        fioField.setText(currentUser.getFio());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(currentUser.getBirthday());
                        birthdayField.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
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

    protected void save() throws Exception {
        OkHttpClient client = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        if (fioField.getText().toString().isEmpty() || phoneField.getText().toString().isEmpty() ||
            emailField.getText().toString().isEmpty() || passwordField.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Проверьте заполненность всех полей", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setFio(fioField.getText().toString());
        currentUser.setPhone(phoneField.getText().toString());
        currentUser.setEmail(emailField.getText().toString());
        currentUser.setPassword(passwordField.getText().toString());
        Calendar cal = Calendar.getInstance();
        cal.set(birthdayField.getYear(), birthdayField.getMonth(), birthdayField.getDayOfMonth());
        currentUser.setBirthday(cal.getTime());

        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"),
                mapper.writeValueAsString(currentUser));

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/user")
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

        currentUser.setAvatar(path);

        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"),
                mapper.writeValueAsString(currentUser));

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/user")
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
                imageView6.setImageURI(selectedImage);

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
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

}