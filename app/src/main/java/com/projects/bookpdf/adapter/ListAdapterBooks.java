package com.projects.bookpdf.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.projects.bookpdf.R;
import com.projects.bookpdf.data.Book;
import com.projects.bookpdf.data.ObjectCollection;

import java.util.ArrayList;
import java.util.Objects;

public class ListAdapterBooks extends BaseAdapter {
    private ArrayList<Book> bookList;
    private Context context;
    private String currentCategory;
    private String currentSubCategory;
    public static boolean morePagesLoaded;
    public ListAdapterBooks(ArrayList<Book> bookList, Context context, String currentCategory, String currentSubCategory) {
        this.bookList = bookList;
        this.context = context;
        this.currentCategory = currentCategory;
        this.currentSubCategory = currentSubCategory;
        morePagesLoaded=true;
        Log.e("ListAdapterBooks","bookList.size()  :"+bookList.size());
    }

    public void setMorePages()
    {
        Log.e("ListAdapterBooks","inside setMorePage() ");
        Log.e("ListAdapterBooks","bookList.size() : "+bookList.size());
        ArrayList<Book> tmpCollection;
        if (currentSubCategory == null)
            tmpCollection = Objects.requireNonNull(ObjectCollection.category.get(currentCategory)).getBooks();
        else
            tmpCollection = Objects.requireNonNull(Objects.requireNonNull(ObjectCollection.category.get(currentCategory)).getSubCategory().get(currentSubCategory)).getBooks();
        Log.e("ListAdapterBooks","tmpCOllection.size() : "+tmpCollection.size());
        Log.e("ListAdapterBooks","bookList.size() : "+bookList.size());
        bookList=tmpCollection;
        notifyDataSetChanged();
        morePagesLoaded=true;
    }
    @Override
    public int getCount() {
        return bookList.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_books_item, parent, false);
        CardView cardView=convertView.findViewById(R.id.card_view_left);
        ImageView imgCover=convertView.findViewById(R.id.img_book_cover_left);
        TextView txtTitle=convertView.findViewById(R.id.txt_title_left);
        TextView txtYear=convertView.findViewById(R.id.txt_year_left);
        Glide.with(parent.getContext())
                .load(bookList.get(position).getBookImageURL())
                .into(imgCover);
        String tempTitle = bookList.get(position).getBookName();
        if (tempTitle.length() > 60) {
            tempTitle = tempTitle.substring(0, 59) + "...";
        }
       txtTitle.setText(tempTitle);
        if (bookList.get(position).getBookYear().length()>0)
            txtYear.setText(String.valueOf(bookList.get(position).getBookYear()));
        else
            txtYear.setText("NA");

        cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("coming_from_home",false);
            bundle.putBoolean("coming_from_search",false);
            bundle.putString("current_category",currentCategory);
            bundle.putString("current_sub_category",currentSubCategory);
            bundle.putInt("current_book_position",position);
            Navigation.findNavController(v).navigate(R.id.book_details,bundle);
        });
        return convertView;
    }
}
