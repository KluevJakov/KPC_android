package com.devblok.kpc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devblok.kpc.R;
import com.devblok.kpc.activities.ReadActivity;
import com.devblok.kpc.entity.Book;
import com.devblok.kpc.tools.ActivityTools;
import com.devblok.kpc.tools.DownloadImageTask;
import com.devblok.kpc.tools.WebConstants;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> books;
    private List<Book> itemsCopy = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        new DownloadImageTask(holder.getImageBook()).execute(WebConstants.backendUrlBase+books.get(position).getAvatar());

        holder.getTitleView().setText(books.get(position).getTitle());
        holder.getTypeView().setText(books.get(position).getType());
        holder.getAuthorView().setText(books.get(position).getAuthor());
        holder.getYearView().setText(books.get(position).getPublishYear());
        holder.getReadBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReadActivity.class);
                Bundle b = new Bundle();
                b.putString("title", books.get(position).getTitle());
                b.putString("author", books.get(position).getAuthor());
                b.putString("text", books.get(position).getText());
                intent.putExtras(b);
                ActivityTools.closeAllConnections();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public BookAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
        itemsCopy.addAll(books);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageBook;
        private final TextView titleView;
        private final TextView typeView;
        private final TextView authorView;
        private final TextView yearView;
        private final Button readBtn;

        public ViewHolder(View view) {
            super(view);
            imageBook = (ImageView) view.findViewById(R.id.imageBook);
            titleView = (TextView) view.findViewById(R.id.titleView);
            typeView = (TextView) view.findViewById(R.id.typeView);
            authorView = (TextView) view.findViewById(R.id.authorView);
            yearView = (TextView) view.findViewById(R.id.yearView);
            readBtn = (Button) view.findViewById(R.id.readBtn);
        }

        public ImageView getImageBook() {
            return imageBook;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getTypeView() {
            return typeView;
        }

        public TextView getAuthorView() {
            return authorView;
        }

        public TextView getYearView() {
            return yearView;
        }

        public Button getReadBtn() {
            return readBtn;
        }
    }

    public void filter(String text) {
        books.clear();
        if(text.isEmpty()){
            books.addAll(itemsCopy);
        } else{
            for(Book item: itemsCopy){
                if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                    books.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
