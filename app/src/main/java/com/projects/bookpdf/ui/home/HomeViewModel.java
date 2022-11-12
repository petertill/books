package com.projects.bookpdf.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.cruxlab.sectionedrecyclerview.lib.SectionDataManager;
import com.cruxlab.sectionedrecyclerview.lib.SectionHeaderLayout;
import com.projects.bookpdf.activity.MainActivity;
import com.projects.bookpdf.adapter.HomePageBooksAdapter;
import com.projects.bookpdf.data.Book;
import com.projects.bookpdf.data.ObjectCollection;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


class HomeViewModel extends ViewModel implements Observer {
    private Context context;
    private RecyclerView recyclerHomePage;
    private SectionHeaderLayout sectionHeaderLayout;
    private SectionDataManager sectionDataManager=new SectionDataManager();
    private short s = 1;
    HomeViewModel(Context context) {
        this.context = context;
        ObjectCollection.homePageNotifier.addObserver(HomeViewModel.this);
    }

    void setAdapter() {
        MainActivity.showProgressDialog();
        RecyclerView.Adapter adapter = sectionDataManager.getAdapter();
        this.recyclerHomePage.setAdapter(adapter);
        this.sectionHeaderLayout.attachTo(this.recyclerHomePage, sectionDataManager);
        if(ObjectCollection.homePageBook!=null&&ObjectCollection.homePageBook.getBooks().size()>sectionDataManager.getSectionCount()) {
            Log.e("Home VM","homePage.getBooks().size: "+ ObjectCollection.homePageBook.getBooks().size());
            Log.e("Home VM","homePage.getBooks().size: "+ ObjectCollection.homePageBook.getBooks().size());
            for (Map.Entry<String, ArrayList<Book>> m : ObjectCollection.homePageBook.getBooks().entrySet()) {
                boolean isLast;
                isLast = s == ObjectCollection.homePageBook.getBooks().size();
                HomePageBooksAdapter homePageBooksAdapter = new HomePageBooksAdapter(
                        true
                        , true
                        , context
                        , m.getKey()
                        , m.getValue()
                        , isLast);
                sectionDataManager.addSection(homePageBooksAdapter, s);
                s++;
            }
            MainActivity.stopProgressDialog();
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof ObjectCollection.HomePageNotifier)
        {
            if(ObjectCollection.homePageBook!=null&&ObjectCollection.homePageBook.getBooks().size()>sectionDataManager.getSectionCount()) {
                int x= (int) arg;
                if(x==1)
                {
                    int c=1;
                    for(Map.Entry<String, ArrayList<Book>> m : ObjectCollection.homePageBook.getBooks().entrySet())
                    {
                        if(c==ObjectCollection.homePageBook.getBooks().size())
                        {
                            HomePageBooksAdapter homePageBooksAdapter = new HomePageBooksAdapter(
                                    true
                                    , true
                                    , context
                                    , m.getKey()
                                    , m.getValue()
                                    , false);
                            sectionDataManager.insertSection(0,homePageBooksAdapter,s);
                            s++;
                        }
                        c++;
                    }
                    MainActivity.stopProgressDialog();
                    return;
                }
                if(sectionDataManager.getSectionCount()>0)
                {
                    HomePageBooksAdapter obj=sectionDataManager.getSectionAdapter(sectionDataManager.getSectionCount()-1);
                    HomePageBooksAdapter homePageBooksAdapter = new HomePageBooksAdapter(
                            true
                            , true
                            , context
                            , obj.getHeaderText()
                            , obj.getBookList()
                            , false);
                    s--;
                    Log.e("Home VM","update() :section no : "+sectionDataManager.getSectionCount());
                    Log.e("Home VM","update() :books to be replaced : "+obj.getHeaderText());
                    sectionDataManager.replaceSection(sectionDataManager.getSectionCount()-1
                    ,homePageBooksAdapter,s);
                    s++;
                }
                int i=1;
                for (Map.Entry<String, ArrayList<Book>> m : ObjectCollection.homePageBook.getBooks().entrySet()) {
                    if(i<=sectionDataManager.getSectionCount()) {
                        i++;
                        Log.e("Home VM","section to be skiped: "+m.getKey());
                        continue;
                    }

                    boolean isLast;
                    isLast = s == ObjectCollection.homePageBook.getBooks().size();
                    HomePageBooksAdapter homePageBooksAdapter = new HomePageBooksAdapter(
                            true
                            , true
                            , context
                            , m.getKey()
                            , m.getValue()
                            , isLast);
                    Log.e("Home VM","update() :section to be added : "+m.getKey());
                    sectionDataManager.addSection(homePageBooksAdapter, s);
                    s++;
                    i++;
                }
            }
        }
        MainActivity.stopProgressDialog();
    }

    public void assignViews(RecyclerView recyclerHomePage, SectionHeaderLayout sectionHeaderLayout) {
        this.recyclerHomePage = recyclerHomePage;
        this.sectionHeaderLayout = sectionHeaderLayout;
    }
}