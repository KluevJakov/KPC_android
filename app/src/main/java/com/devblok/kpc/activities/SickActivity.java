package com.devblok.kpc.activities;

import static com.devblok.kpc.tools.WebConstants.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.entity.Disease;
import com.devblok.kpc.entity.Sick;
import com.devblok.kpc.entity.User;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SickActivity extends AppCompatActivity {


    protected ImageView imageView;
    protected Animal currentAnimal;
    protected Disease currentDisease;
    protected UUID currentAnimalUUID;
    protected UUID currentDiseaseUUID;

    /* A lot of fields */
    protected DatePicker dateToTherapy;
    protected EditText firstDiagnosis;
    protected EditText secondDiagnosis;
    protected EditText anamnesis;
    protected EditText temperature;
    protected EditText pulse;
    protected EditText breath;
    protected EditText commonHealth;
    protected EditText fatness;
    protected EditText externalSkinStatus;
    protected EditText internalShellStatus;
    protected EditText lymphStatus;
    protected EditText gastroSystemResearch;
    protected EditText breathSystemResearch;
    protected EditText heartSystemResearch;
    protected EditText nervousSystemResearch;
    protected EditText urogenitalSystemResearch;
    protected Spinner sickSpinner;
    /* --------------- */
    protected TextView titleRead2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imageView = findViewById(R.id.imageView15);
        dateToTherapy = findViewById(R.id.dateToTherapy);
        firstDiagnosis = findViewById(R.id.firstDiagnosis);
        secondDiagnosis = findViewById(R.id.secondDiagnosis);
        anamnesis = findViewById(R.id.anamnesis);
        temperature = findViewById(R.id.temperature);
        pulse = findViewById(R.id.pulse);
        breath = findViewById(R.id.breath);
        commonHealth = findViewById(R.id.commonHealth);
        fatness = findViewById(R.id.fatness);
        externalSkinStatus = findViewById(R.id.externalSkinStatus);
        internalShellStatus = findViewById(R.id.internalShellStatus);
        lymphStatus = findViewById(R.id.lymphStatus);
        gastroSystemResearch = findViewById(R.id.gastroSystemResearch);
        breathSystemResearch = findViewById(R.id.breathSystemResearch);
        heartSystemResearch = findViewById(R.id.heartSystemResearch);
        nervousSystemResearch = findViewById(R.id.nervousSystemResearch);
        urogenitalSystemResearch = findViewById(R.id.urogenitalSystemResearch);
        sickSpinner = findViewById(R.id.sickSpinner);
        titleRead2 = findViewById(R.id.titleRead2);

        ImageView buttonClose = findViewById(R.id.backBtn2);
        buttonClose.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.health);
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

        Button removeBtn = findViewById(R.id.removeBtn);
        removeBtn.setOnClickListener(view -> {
            try {
                delete(currentDisease.getId().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ошибка при удалении данных с сервера..", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle.get("diseaseId") != null) {
                currentDiseaseUUID = UUID.fromString((String) bundle.get("diseaseId"));
                load();
            } else {
                currentDisease = new Disease();
                removeBtn.setVisibility(View.INVISIBLE);
            }

            if (bundle.get("animalId") != null) {
                currentAnimalUUID = UUID.fromString((String) bundle.get("animalId"));
                run();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
        }
    }

    protected void run() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request2 = new Request.Builder().url(WebConstants.backendUrl + "/sicks").build();
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
                        currentDisease.setDateStart(new Date());
                        currentAnimal = new Gson().fromJson(myResponse, new TypeToken<Animal>() {}.getType());
                        new DownloadImageTask(imageView).execute(WebConstants.backendUrlBase + currentAnimal.getAvatar());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        client.newCall(request2).enqueue(new Callback() {
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
                        ArrayList<Sick> sicks = gson.fromJson(myResponse, new TypeToken<List<Sick>>() {}.getType());
                        List<String> sicksStr = sicks.stream().map(e -> e.getName()).collect(Collectors.toList());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, sicksStr.toArray(new String[0]));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sickSpinner.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    protected void load() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request2 = new Request.Builder().url(WebConstants.backendUrl + "/sicks").build();
        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/disease?diseaseId=" + currentDiseaseUUID.toString())
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
                        currentDisease = new Gson().fromJson(myResponse, new TypeToken<Disease>() {}.getType());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(currentDisease.getDateToTherapy());
                        dateToTherapy.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                        firstDiagnosis.setText(currentDisease.getFirstDiagnosis());
                        secondDiagnosis.setText(currentDisease.getSecondDiagnosis());
                        anamnesis.setText(currentDisease.getAnamnesis());
                        temperature.setText(Float.toString(currentDisease.getTemperature()));
                        pulse.setText(Integer.toString(currentDisease.getPulse()));
                        breath.setText(Integer.toString(currentDisease.getBreath()));
                        commonHealth.setText(currentDisease.getCommonHealth());
                        fatness.setText(currentDisease.getFatness());
                        externalSkinStatus.setText(currentDisease.getExternalSkinStatus());
                        internalShellStatus.setText(currentDisease.getInternalShellStatus());
                        lymphStatus.setText(currentDisease.getLymphStatus());
                        gastroSystemResearch.setText(currentDisease.getGastroSystemResearch());
                        breathSystemResearch.setText(currentDisease.getBreathSystemResearch());
                        heartSystemResearch.setText(currentDisease.getHeartSystemResearch());
                        nervousSystemResearch.setText(currentDisease.getNervousSystemResearch());
                        urogenitalSystemResearch.setText(currentDisease.getUrogenitalSystemResearch());

                        titleRead2.setText(currentDisease.getAnimal().getNickOrNumber());

                        int pos = ((ArrayAdapter<String>) sickSpinner.getAdapter()).getPosition(currentDisease.getSick().getName());
                        sickSpinner.setSelection(pos);

                        currentAnimalUUID = currentDisease.getAnimal().getId();
                        currentAnimal = currentDisease.getAnimal();
                        new DownloadImageTask(imageView).execute(WebConstants.backendUrlBase + currentDisease.getAnimal().getAvatar());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        client.newCall(request2).enqueue(new Callback() {
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
                        ArrayList<Sick> sicks = gson.fromJson(myResponse, new TypeToken<List<Sick>>() {}.getType());
                        List<String> sicksStr = sicks.stream().map(e -> e.getName()).collect(Collectors.toList());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, sicksStr.toArray(new String[0]));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sickSpinner.setAdapter(adapter);
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

        Calendar cal = Calendar.getInstance();
        cal.set(dateToTherapy.getYear(), dateToTherapy.getMonth(), dateToTherapy.getDayOfMonth());
        currentDisease.setDateToTherapy(cal.getTime());
        currentDisease.setFirstDiagnosis(firstDiagnosis.getText().toString());
        currentDisease.setSecondDiagnosis(secondDiagnosis.getText().toString());
        currentDisease.setAnamnesis(anamnesis.getText().toString());
        currentDisease.setTemperature(NumberUtils.toFloat(temperature.getText().toString(), 0f));
        currentDisease.setPulse(NumberUtils.toInt(pulse.getText().toString(), 0));
        currentDisease.setBreath(NumberUtils.toInt(breath.getText().toString(), 0));
        currentDisease.setCommonHealth(commonHealth.getText().toString());
        currentDisease.setFatness(fatness.getText().toString());
        currentDisease.setExternalSkinStatus(externalSkinStatus.getText().toString());
        currentDisease.setInternalShellStatus(internalShellStatus.getText().toString());
        currentDisease.setLymphStatus(lymphStatus.getText().toString());
        currentDisease.setGastroSystemResearch(gastroSystemResearch.getText().toString());
        currentDisease.setBreathSystemResearch(breathSystemResearch.getText().toString());
        currentDisease.setHeartSystemResearch(heartSystemResearch.getText().toString());
        currentDisease.setNervousSystemResearch(nervousSystemResearch.getText().toString());
        currentDisease.setUrogenitalSystemResearch(urogenitalSystemResearch.getText().toString());
        Animal animal = new Animal();
        animal.setId(currentAnimalUUID);
        currentDisease.setAnimal(animal);
        Sick sick = new Sick();
        sick.setName((String) sickSpinner.getSelectedItem());
        currentDisease.setSick(sick);
        User user = new User();
        SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        UUID uuid = UUID.fromString(sharedpreferences.getString("USER_ID_KEY", null));
        user.setId(uuid);
        currentDisease.setCurator(user);

        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(currentDisease));

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/disease")
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
                        ActivityTools.setupBackLastFragment(intent, R.id.health);
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

    protected void delete(String uuid) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url(WebConstants.backendUrl + "/disease?diseaseId="+uuid)
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
                        Toast.makeText(getApplicationContext(), "Болезнь удалена", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        ActivityTools.setupBackLastFragment(intent, R.id.health);
                        ActivityTools.closeAllConnections();
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при удалении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}