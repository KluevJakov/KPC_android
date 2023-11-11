package com.devblok.kpc.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devblok.kpc.R;
import com.devblok.kpc.adapter.DiseaseAdapter;
import com.devblok.kpc.adapter.InspectAdapter;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.entity.Disease;
import com.devblok.kpc.entity.Sick;
import com.devblok.kpc.tools.WebConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private RecyclerView recyclerView;

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
        recyclerView = view.findViewById(R.id.recyclerDiseases);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //болезни
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((DiseaseAdapter) recyclerView.getAdapter()) != null) {
                    ((DiseaseAdapter) recyclerView.getAdapter()).filterByDisease(adapterView.getSelectedItem().toString(),
                            spinner2.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //животные
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((DiseaseAdapter) recyclerView.getAdapter()) != null) {
                    ((DiseaseAdapter) recyclerView.getAdapter()).filterByAnimal(adapterView.getSelectedItem().toString(),
                            spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            run();
        } catch (Exception e) {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    protected void run() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request1 = new Request.Builder().url(WebConstants.backendUrl + "/sicks").build();
        Request request2 = new Request.Builder().url(WebConstants.backendUrl + "/animals").build();
        Request request3 = new Request.Builder().url(WebConstants.backendUrl + "/diseases").build();


        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(() -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Sick> sicks = gson.fromJson(myResponse, new TypeToken<List<Sick>>() {}.getType());
                        List<String> sicksStr = new ArrayList<>();
                        sicksStr.add("Все болезни");
                        sicksStr.addAll(sicks.stream().map(e -> e.getName()).collect(Collectors.toList()));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sicksStr.toArray(new String[0]));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    } catch (Exception e) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                        }
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

                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(() -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Animal> animals = gson.fromJson(myResponse, new TypeToken<List<Animal>>() {}.getType());
                        List<String> animalsStr = new ArrayList<>();
                        animalsStr.add("Все животные");
                        animalsStr.addAll(animals.stream().map(e -> e.getNickOrNumber()).collect(Collectors.toList()));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, animalsStr.toArray(new String[0]));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter);
                    } catch (Exception e) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        client.newCall(request3).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();

                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(() -> {
                    try {
                        Gson gson = new Gson();
                        ArrayList<Disease> diseases = gson.fromJson(myResponse, new TypeToken<List<Disease>>() {}.getType());
                        DiseaseAdapter diseaseAdapter = new DiseaseAdapter(diseases, getContext());
                        recyclerView.setAdapter(diseaseAdapter);
                    } catch (Exception e) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Ошибка при получении данных с сервера..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}