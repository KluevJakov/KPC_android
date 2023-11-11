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
import com.devblok.kpc.adapter.InspectAdapter;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.entity.Inspect;
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

public class InspectFragment extends Fragment {

    Spinner spinner2;
    RecyclerView recyclerInspects;

    public InspectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspect, container, false);

        spinner2 = view.findViewById(R.id.spinner2);
        recyclerInspects = view.findViewById(R.id.recyclerInspects);
        recyclerInspects.setLayoutManager(new LinearLayoutManager(this.getContext()));

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //животные
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((InspectAdapter) recyclerInspects.getAdapter()).filterByAnimal(adapterView.getSelectedItem().toString());
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

        Request request2 = new Request.Builder().url(WebConstants.backendUrl + "/animals").build();
        Request request3 = new Request.Builder().url(WebConstants.backendUrl + "/inspects").build();

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
                        ArrayList<Inspect> inspects = gson.fromJson(myResponse, new TypeToken<List<Inspect>>() {}.getType());
                        InspectAdapter inspectAdapter = new InspectAdapter(inspects, getContext());
                        recyclerInspects.setAdapter(inspectAdapter);
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