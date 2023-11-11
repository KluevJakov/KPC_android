package com.devblok.kpc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devblok.kpc.R;
import com.devblok.kpc.activities.AnimalActivity;
import com.devblok.kpc.entity.Inspect;
import com.devblok.kpc.tools.ActivityTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InspectAdapter extends RecyclerView.Adapter<InspectAdapter.ViewHolder> {
    private List<Inspect> inspects;
    private List<Inspect> itemsCopy = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public InspectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InspectAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspect, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InspectAdapter.ViewHolder holder, int position) {
        holder.getMonthYear().setText("Осмотр запланирован на " + getMonth(inspects.get(position).getPlanDate()) + " " + (inspects.get(position).getPlanDate().getYear()+1900));
        holder.getDate().setText(String.valueOf(inspects.get(position).getPlanDate().getDate()).toUpperCase());
        holder.getDay().setText(getDay(inspects.get(position).getPlanDate()).toUpperCase());
        holder.getAnimal().setText(inspects.get(position).getAnimal().getNickOrNumber().toUpperCase());

        holder.getInfo().setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AnimalActivity.class);
            Bundle b = new Bundle();
            b.putString("animalId", inspects.get(position).getAnimal().getId().toString());
            intent.putExtras(b);
            ActivityTools.closeAllConnections();
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return inspects.size();
    }

    public InspectAdapter(List<Inspect> inspects, Context context) {
        this.inspects = inspects;
        this.context = context;
        itemsCopy.addAll(inspects);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView animal;
        private final TextView info;
        private final TextView monthYear;
        private final TextView date;
        private final TextView day;


        public ViewHolder(View view) {
            super(view);
            animal = view.findViewById(R.id.animal);
            info = view.findViewById(R.id.info);
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
        return new SimpleDateFormat("MMM", new Locale("ru")).format(date).toUpperCase();
    }

    public String getDay(Date date) {
        return new SimpleDateFormat("EEE", new Locale("ru")).format(date).toUpperCase();
    }

    public void filterByAnimal(String text) {
        if (text.equals("Все животные")) {
            inspects.clear();
            inspects.addAll(itemsCopy);
            notifyDataSetChanged();
            return;
        }
        inspects.clear();
        if(text.isEmpty()){
            inspects.addAll(itemsCopy);
        } else{
            for(Inspect item: itemsCopy){
                if(item.getAnimal().getNickOrNumber().toLowerCase().equals(text.toLowerCase())){
                    inspects.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
