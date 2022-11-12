package com.projects.bookpdf.ui.search;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.bookpdf.adapter.RecyclerAdapterSearchBooks;
import com.projects.bookpdf.data.ObjectCollection;

import java.util.Observable;
import java.util.Observer;

class SearchViewModel extends ViewModel implements Observer {
    private Context context;
    private RecyclerView recyclerSearchBooks;
    private RecyclerAdapterSearchBooks recyclerAdapterSearchBooks;
    SearchViewModel(Context context) {
        this.context = context;
        ObjectCollection.moreSearchPagesNotifier.addObserver(SearchViewModel.this);
    }

    void setAdapter(RecyclerView recyclerView, FragmentActivity activity) {
        this.recyclerSearchBooks=recyclerView;
        recyclerAdapterSearchBooks=new RecyclerAdapterSearchBooks(context,activity);
        this.recyclerSearchBooks.setAdapter(recyclerAdapterSearchBooks);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof ObjectCollection.MoreSearchPagesNotifier) {
            if (recyclerAdapterSearchBooks != null) {
                RecyclerAdapterSearchBooks.newIncomingDataReached = true;
                recyclerAdapterSearchBooks.notifyDataSetChanged();
            }
        }
    }

}