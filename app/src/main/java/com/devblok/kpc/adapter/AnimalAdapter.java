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
import com.devblok.kpc.entity.Animal;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;

import java.util.ArrayList;
import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    private List<Animal> animals;
    private List<Animal> itemsCopy = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public AnimalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimalAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalAdapter.ViewHolder holder, int position) {
        new DownloadImageTask(holder.getImageAnimal()).execute(WebConstants.backendUrlBase+animals.get(position).getAvatar());

        holder.getNameView().setText(animals.get(position).getNickOrNumber());
        holder.getInfo().setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AnimalActivity.class);
            Bundle b = new Bundle();
            b.putString("nickOrNumber", animals.get(position).getNickOrNumber());
            b.putString("type", animals.get(position).getType());
            b.putString("sex", animals.get(position).getSex());
            b.putString("age", animals.get(position).getAge());
            b.putString("breed", animals.get(position).getBreed());
            b.putString("owner", animals.get(position).getOwner());
            b.putString("avatar", animals.get(position).getAvatar());
            b.putString("id", animals.get(position).getId().toString());
            intent.putExtras(b);
            ActivityTools.closeAllConnections();
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public AnimalAdapter(List<Animal> animals, Context context) {
        this.animals = animals;
        this.context = context;
        itemsCopy.addAll(animals);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageAnimal;
        private final TextView nameView;
        private final TextView info;

        public ViewHolder(View view) {
            super(view);
            imageAnimal = (ImageView) view.findViewById(R.id.imageAnimal);
            nameView = (TextView) view.findViewById(R.id.nameView);
            info = (TextView) view.findViewById(R.id.info);
        }

        public ImageView getImageAnimal() {
            return imageAnimal;
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getInfo() {
            return info;
        }
    }

    public void filter(String text) {
        animals.clear();
        if(text.isEmpty()){
            animals.addAll(itemsCopy);
        } else{
            for(Animal item: itemsCopy){
                if(item.getNickOrNumber().toLowerCase().contains(text.toLowerCase())){
                    animals.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
