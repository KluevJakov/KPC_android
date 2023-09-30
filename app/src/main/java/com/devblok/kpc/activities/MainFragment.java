package com.devblok.kpc.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devblok.kpc.R;

public class MainFragment extends Fragment {

    RecyclerView recyclerView;

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ImageView buttonHelp = (ImageView) view.findViewById(R.id.imageView4);
        buttonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Помощь")
                    .setMessage("Look at this dialog!")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        });

        ImageView buttonLk = (ImageView) view.findViewById(R.id.imageView5);
        buttonLk.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), ProfileActivity.class);
            startActivity(intent);
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        return view;
    }
}