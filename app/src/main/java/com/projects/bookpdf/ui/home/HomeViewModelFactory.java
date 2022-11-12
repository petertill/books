package com.projects.bookpdf.ui.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.projects.bookpdf.ui.category.CategoryViewModel;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    HomeViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(HomeViewModel.class))
            return (T) new HomeViewModel(context);
        return null;
    }
}
