package com.devblok.kpc.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devblok.kpc.R;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.entity.Sick;
import com.devblok.kpc.tools.WebConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HealthFragment extends Fragment {

    Spinner spinner, spinner2;

    public HealthFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        spinner = view.findViewById(R.id.spinner);
        spinner2 = view.findViewById(R.id.spinner2);

        try {
            run();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    protected void run() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request1 = new Request.Builder().url(WebConstants.backendUrl + "/sicks").build();
        Request request2 = new Request.Builder().url(WebConstants.backendUrl + "/animals").build();

        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(() -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Sick> sicks = gson.fromJson(myResponse, new TypeToken<List<Sick>>() {}.getType());
                        List<String> sicksStr = sicks.stream().map(e -> e.getName()).collect(Collectors.toList());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sicksStr.toArray(new String[0]));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
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
                getActivity().runOnUiThread(() -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Animal> animals = gson.fromJson(myResponse, new TypeToken<List<Animal>>() {}.getType());
                        List<String> animalsStr = animals.stream().map(e -> e.getNickOrNumber()).collect(Collectors.toList());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, animalsStr.toArray(new String[0]));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}