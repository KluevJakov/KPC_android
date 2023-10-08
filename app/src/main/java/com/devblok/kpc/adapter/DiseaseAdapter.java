package com.devblok.kpc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devblok.kpc.R;
import com.devblok.kpc.activities.AnimalActivity;
import com.devblok.kpc.activities.SickActivity;
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.entity.Disease;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;

import java.text.DateFormatSymbols;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder> {
    private List<Disease> diseases;
    private Context context;

    @NonNull
    @Override
    public DiseaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiseaseAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseAdapter.ViewHolder holder, int position) {
        holder.getSick().setText(diseases.get(position).getSick().getName());
        holder.getMonthYear().setText(getMonth(diseases.get(position).getDateStart().getMonth()) + " " + diseases.get(position).getDateStart().getYear());
        holder.getDate().setText(diseases.get(position).getDateStart().getDate());
        holder.getDay().setText(getDay(diseases.get(position).getDateStart().getDay()));
        holder.getAnimal().setText(diseases.get(position).getAnimal().getNickOrNumber());

        holder.getInfo().setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SickActivity.class);
            Bundle b = new Bundle();
            b.putString("diseaseId", diseases.get(position).getId().toString());
            intent.putExtras(b);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return diseases.size();
    }

    public DiseaseAdapter(List<Disease> diseases, Context context) {
        this.diseases = diseases;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView animal;
        private final TextView info;
        private final TextView sick;
        private final TextView monthYear;
        private final TextView date;
        private final TextView day;


        public ViewHolder(View view) {
            super(view);
            animal = view.findViewById(R.id.animal);
            info = view.findViewById(R.id.info);
            sick = view.findViewById(R.id.sick);
            monthYear = view.findViewById(R.id.monthYear);
            date = view.findViewById(R.id.date);
            day = view.findViewById(R.id.day);
        }

        public TextView getAnimal() {
            return animal;
        }

        public TextView getInfo() {
            return info;
        }

        public TextView getSick() {
            return sick;
        }

        public TextView getMonthYear() {
            return monthYear;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getDay() {
            return day;
        }
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public String getDay(int week) {
        return new DateFormatSymbols().getWeekdays()[week];
    }

}
