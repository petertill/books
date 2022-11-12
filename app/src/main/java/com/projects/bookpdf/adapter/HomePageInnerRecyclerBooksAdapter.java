package com.projects.bookpdf.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.bookpdf.R;
import com.projects.bookpdf.data.Book;

import java.util.ArrayList;

public class HomePageInnerRecyclerBooksAdapter extends RecyclerView.Adapter<HomePageInnerRecyclerBooksAdapter.ViewHolder> {
    private ArrayList<Book> bookList;
    private Context context;
    private String headerText;
    public HomePageInnerRecyclerBooksAdapter(String headerText,ArrayList<Book> bookList, Context context) {
        this.headerText=headerText;
        this.bookList = bookList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(bookList.get(position).getBookImageURL()).into(holder.imgBook);
        String tempTitle = bookList.get(position).getBookName();
        if (tempTitle.length() > 30) {
            tempTitle = tempTitle.substring(0, 29) + "...";
        }
        holder.txtTitle.setText(tempTitle);
        if (bookList.get(position).getBookYear().length()>0)
            holder.txtYear.setText(String.valueOf(bookList.get(position).getBookYear()));
        else
            holder.txtYear.setText("NA");
        holder.holderCard.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("coming_from_home",true);
            bundle.putBoolean("coming_from_search",false);
            bundle.putString("header_text",headerText);
            bundle.putInt("current_book_position",position);
            Navigation.findNavController(v).navigate(R.id.book_details,bundle);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtYear;
        ImageView imgBook;
        CardView holderCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_book_title);
            txtYear = itemView.findViewById(R.id.txt_year);
            imgBook = itemView.findViewById(R.id.book_image);
            holderCard=itemView.findViewById(R.id.card_view_holder);
        }
    }
}
