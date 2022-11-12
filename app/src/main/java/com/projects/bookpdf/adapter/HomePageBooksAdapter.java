package com.projects.bookpdf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cruxlab.sectionedrecyclerview.lib.BaseSectionAdapter;
import com.cruxlab.sectionedrecyclerview.lib.SectionAdapter;
import com.projects.bookpdf.R;
import com.projects.bookpdf.data.Book;

import java.util.ArrayList;

public class HomePageBooksAdapter extends SectionAdapter<HomePageBooksAdapter.ItemViewHolder, HomePageBooksAdapter.HeaderViewHolder> {
    private String headerText;
    private ArrayList<Book> bookList;
    private boolean isThisLast;
    private Context context;
    public HomePageBooksAdapter(boolean isHeaderVisible, boolean isHeaderPinned,Context context,String headerText,ArrayList<Book> bookList,boolean isLast) {
        super(isHeaderVisible, isHeaderPinned);
        this.context=context;
        this.headerText=headerText;
        this.bookList=bookList;
        isThisLast=isLast;
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_section_header,parent,false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder) {
        holder.headerText.setText(headerText);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, short type) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_item,parent,false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(ItemViewHolder holder, int position) {
        if(isThisLast)
        {
            ConstraintLayout.LayoutParams params= (ConstraintLayout.LayoutParams) holder.bookList.getLayoutParams();
            params.bottomMargin=184;
            holder.bookList.setLayoutParams(params);
        }
        HomePageInnerRecyclerBooksAdapter booksAdapter=new HomePageInnerRecyclerBooksAdapter(headerText,bookList,context);
        holder.bookList.setAdapter(booksAdapter);
    }

    static class HeaderViewHolder extends BaseSectionAdapter.HeaderViewHolder
    {
        CardView headerCard;
        TextView headerText;
        HeaderViewHolder(View itemView) {
            super(itemView);
            headerCard=itemView.findViewById(R.id.material_card_home_page_section_header);
            headerText=itemView.findViewById(R.id.txt_home_page_section_header);
        }
    }

    static class ItemViewHolder extends BaseSectionAdapter.ItemViewHolder
    {
        RecyclerView bookList;
        ItemViewHolder(View itemView) {
            super(itemView);
            bookList=itemView.findViewById(R.id.recycler_home_page_books);
        }
    }

    public String getHeaderText() {
        return headerText;
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public boolean isThisLast() {
        return isThisLast;
    }
}
