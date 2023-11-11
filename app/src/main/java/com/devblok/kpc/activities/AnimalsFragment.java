package com.devblok.kpc.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devblok.kpc.R;
import com.devblok.kpc.adapter.AnimalAdapter;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.tools.WebConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnimalsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;

    public AnimalsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals, container, false);
        recyclerView = view.findViewById(R.id.recyclerAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        searchView = view.findViewById(R.id.searchView);

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

        Request request1 = new Request.Builder().url(WebConstants.backendUrl + "/animals").build();

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
                        ArrayList<Animal> animals = gson.fromJson(myResponse, new TypeToken<List<Animal>>() {}.getType());
                        AnimalAdapter animalAdapter = new AnimalAdapter(animals, getContext());
                        recyclerView.setAdapter(animalAdapter);

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                if (recyclerView.getAdapter() != null) {
                                    ((AnimalAdapter) recyclerView.getAdapter()).filter(s);
                                }
                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                if (recyclerView.getAdapter() != null) {
                                    ((AnimalAdapter) recyclerView.getAdapter()).filter(s);
                                }
                                return true;
                            }
                        });
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