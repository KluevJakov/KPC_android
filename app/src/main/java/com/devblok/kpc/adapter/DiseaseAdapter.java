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
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        holder.getSick().setText(diseases.get(position).getSick().getName().toUpperCase());
        holder.getMonthYear().setText(getMonth(diseases.get(position).getDateStart()) + " " + (diseases.get(position).getDateStart().getYear()+1900));
        holder.getDate().setText(String.valueOf(diseases.get(position).getDateStart().getDate()).toUpperCase());
        holder.getDay().setText(getDay(diseases.get(position).getDateStart()).toUpperCase());
        holder.getAnimal().setText(diseases.get(position).getAnimal().getNickOrNumber().toUpperCase());

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

    public String getMonth(Date date) {
        return new SimpleDateFormat("MMMM", new Locale("ru")).format(date).toUpperCase();
    }

    public String getDay(Date date) {
        return new SimpleDateFormat("EEE", new Locale("ru")).format(date).toUpperCase();
    }

}
