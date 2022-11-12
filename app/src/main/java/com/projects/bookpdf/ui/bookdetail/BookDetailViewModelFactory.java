package com.projects.bookpdf.ui.bookdetail;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class BookDetailViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private FragmentActivity fragmentActivity;

    public BookDetailViewModelFactory(Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(BookDetailViewModel.class))
            return (T) new BookDetailViewModel(context, fragmentActivity);
        return null;

    }
}
