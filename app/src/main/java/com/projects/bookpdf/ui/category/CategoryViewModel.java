package com.projects.bookpdf.ui.category;

import android.content.Context;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.bookpdf.activity.MainActivity;
import com.projects.bookpdf.adapter.ListAdapterBooks;
import com.projects.bookpdf.adapter.RecyclerAdapterCategory;
import com.projects.bookpdf.adapter.RecyclerAdapterSubCategory;
import com.projects.bookpdf.data.Book;
import com.projects.bookpdf.data.ObjectCollection;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class CategoryViewModel extends ViewModel implements Observer {
    private Context context;
    private GridView gridBooks;
    private RecyclerView recyclerCategory;
    private RecyclerView recyclerSubCategory;
    private RecyclerAdapterCategory recyclerAdapterCategory;
    private RecyclerAdapterSubCategory recyclerAdapterSubCategory;
    private ListAdapterBooks listAdapterBooks;
    private MutableLiveData<Boolean> toggleSubCategoryVisibility;
    private String selectedCategory;
    private String selectedSubCategory=null;
    private static final String tag="CategoryVM";
    FragmentActivity activity;
    CategoryViewModel(Context context) {
        this.context = context;
        recyclerAdapterCategory=new RecyclerAdapterCategory(context);
        toggleSubCategoryVisibility=new MutableLiveData<>();
        recyclerAdapterCategory.getCategorySelectedNotifier().addObserver(CategoryViewModel.this);
        ObjectCollection.subCategoryNamesNotifier.addObserver(CategoryViewModel.this);
        ObjectCollection.booksForCategoryNotifier.addObserver(CategoryViewModel.this);
        ObjectCollection.booksForSubCategoryNotifier.addObserver(CategoryViewModel.this);
        ObjectCollection.subCategoryDataNotifier.addObserver(CategoryViewModel.this);
        ObjectCollection.generalCategoryLoadedNotifier.addObserver(CategoryViewModel.this);
        ObjectCollection.loadMorePagesForCategoryNotifer.addObserver(CategoryViewModel.this);
    }



    void setViews(GridView books, RecyclerView category, RecyclerView subCategory, FragmentActivity activity) {
        MainActivity.showProgressDialog();
        gridBooks=books;
        recyclerCategory=category;
        recyclerSubCategory=subCategory;
        this.activity=activity;
        //TODO: assigning adapter to category list
        recyclerCategory.setAdapter(recyclerAdapterCategory);
        if(ObjectCollection.category.size()>0)
            MainActivity.stopProgressDialog();
    }

    @Override
    public void update(Observable o, Object arg) {
        MainActivity.stopProgressDialog();
        if(o instanceof ObjectCollection.LoadMorePagesForCategoryNotifier)
        {
            Log.e(tag,"notifier of load more pages");
            String[] x=(String[])arg;
            if(x[1]==null&&selectedSubCategory==null&&x[0].equalsIgnoreCase(selectedCategory))
                listAdapterBooks.setMorePages();
            else if(x[1] != null && x[0].equalsIgnoreCase(selectedCategory) && x[1].equalsIgnoreCase(selectedSubCategory))
                listAdapterBooks.setMorePages();
        }
        if(o instanceof ObjectCollection.GeneralCategoryLoadedNotifier)
        {
            recyclerAdapterCategory.notifyDataSetChanged();

        }
        if(o instanceof RecyclerAdapterCategory.CategorySelectedNotifier)
        {
            selectedCategory=(String) arg;
            selectedSubCategory=null;
            Log.e(tag,"Selected Category : "+selectedCategory);
            if(ObjectCollection.category.get(selectedCategory).getSubCategoryName()==null)
            {
                Log.e(tag,"Selected Category : getSubCategoryName==null ");
                //TODO : Initialize subCategoryNames LinkedHashMap of Category class object
                ObjectCollection.getSubCategoryNamesForCategory(selectedCategory,activity);
            }
            else if(ObjectCollection.category.get(selectedCategory).getSubCategoryName().size()>0)
            {
                Log.e(tag,"Selected Category : getSubCategoryName.size()>0");
                toggleSubCategoryVisibility.setValue(true);
                //TODO: set recyclerSubCategory adapter
                setRecyclerSubCategoryToAdapter();
            }
            else if(ObjectCollection.category.get(selectedCategory).getSubCategoryName().size()<=0)
            {
                Log.e(tag,"Selected Category : getSubCategoryName.size()<=0");
                toggleSubCategoryVisibility.setValue(false);
            }

            if(ObjectCollection.category.get(selectedCategory).getBooks().size()<=0)
            {
                //TODO: load books for selected category
                Log.e(tag,"Selected Category :getBooks.size<=0");
                ObjectCollection.getBooksForCategory(selectedCategory,activity);
            }
            else
            {
                //TODO : set recyclerBook adapter
                Log.e(tag,"Selected Category :getBooks.size>0");
                setRecyclerBooksToAdapter(Objects.requireNonNull(ObjectCollection.category.get(selectedCategory)).getBooks());
            }
        }
        if(o instanceof ObjectCollection.SubCategoryNamesNotifier)
        {
            Log.e(tag,"inside subCategoryNameNptifier: selected cat : "+selectedCategory);
            Log.e(tag,"inside subCategoryNameNptifier: arg : "+arg.toString());
            Log.e(tag,"inside subCategoryNameNptifier: subCAtegoryName.size() : "+ObjectCollection.category.get(selectedCategory).getSubCategoryName().size());
            if(ObjectCollection.category.get(selectedCategory).getSubCategoryName().size()>0)
            {
                Log.e(tag,"inside if of subcategoryname notifier : getSubCategoryName.size()>0");
                toggleSubCategoryVisibility.setValue(true);
                //TODO: set recyclerSubCategory adapter
                setRecyclerSubCategoryToAdapter();
            }
            else
            {
                toggleSubCategoryVisibility.setValue(false);
            }
        }
        if(o instanceof ObjectCollection.BooksForCategoryNotifier)
        {
            Log.e(tag,"inside BooksForCategoryNotifier : arg ="+arg.toString());
            Log.e(tag,"inside BooksForCategoryNotifier : selectedCategory ="+selectedCategory);
            if(((String) arg).equalsIgnoreCase(selectedCategory))
            {
                //TODO : set recyclerBook adapter
                setRecyclerBooksToAdapter(Objects.requireNonNull(ObjectCollection.category.get(selectedCategory)).getBooks());
            }
        }
        //TODO: notifier for selected sub category
        if(o instanceof RecyclerAdapterSubCategory.SubCategorySelectedNotifier)
        {
            selectedSubCategory= (String) arg;
            if(ObjectCollection.category.get(selectedCategory).getSubCategory().containsKey(selectedSubCategory))
            {
                setBooksForSubCategory();
            }
            else
            {
                //TODO: load the selected sub category data
                ObjectCollection.getSubCategoryData(selectedCategory,selectedSubCategory,activity);
            }
        }
        if(o instanceof ObjectCollection.SubCategoryDataNotifier)
        {
            String[] x=(String[]) arg;
            if(x[0].equalsIgnoreCase(selectedCategory)&&x[1].equalsIgnoreCase(selectedSubCategory))
            {
                if(ObjectCollection.category.get(selectedCategory).getSubCategory().containsKey(selectedSubCategory))
                {
                    setBooksForSubCategory();
                }
            }
        }
        if(o instanceof ObjectCollection.BooksForSubCategoryNotifier)
        {
            String[] x=(String[]) arg;
            if(x[0].equalsIgnoreCase(selectedCategory)&&x[1].equalsIgnoreCase(selectedSubCategory))
            {
                //TODO : set recyclerBook adapter
                setRecyclerBooksToAdapter(ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getBooks());
            }
        }

    }

    private void setRecyclerBooksToAdapter(ArrayList<Book> books) {
        Log.e(tag,"inside setRecyclerBooksToAdapter : books : "+books.size());
        listAdapterBooks = new ListAdapterBooks(books,context,selectedCategory,selectedSubCategory);
        gridBooks.setAdapter(listAdapterBooks);
        gridBooks.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(ListAdapterBooks.morePagesLoaded&&firstVisibleItem+visibleItemCount>=totalItemCount)
                {
                    if (selectedSubCategory == null) {
                        Log.e(tag,"for category : toalLoadedPages : "+ObjectCollection.category.get(selectedCategory).getTotalLoadedPage());
                        Log.e(tag,"for category : toalPages : "+ObjectCollection.category.get(selectedCategory).getTotalPage());
                        if (ObjectCollection.category.get(selectedCategory).getTotalLoadedPage() + 1
                                <=
                                ObjectCollection.category.get(selectedCategory).getTotalPage()) {
                            ListAdapterBooks.morePagesLoaded = false;
                            ObjectCollection.loadMorePagesForCategory(ObjectCollection.category.get(selectedCategory).getTotalLoadedPage() + 1, selectedCategory, activity);
                        }
                    } else {
                        Log.e(tag,"for category : toalLoadedPages : "+ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getTotalLoadedPage());
                        Log.e(tag,"for category : toalPages : "+ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getTotalPage());
                        if (ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getTotalLoadedPage() + 1
                                <=
                                ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getTotalPage()) {
                            ListAdapterBooks.morePagesLoaded = false;
                            ObjectCollection.loadMorePagesForCategory(ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getTotalLoadedPage() + 1, selectedCategory, selectedSubCategory, activity);
                        }
                    }
                }
            }
        });
    }

    private void setBooksForSubCategory() {
        if(ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getBooks().size()<=0)
        {
            //TODO : load books for selected sub category
            ObjectCollection.getBooksForSubCategory(selectedCategory,selectedSubCategory,activity);
        }
        else
        {
            //TODO : set recyclerBook adapter
            setRecyclerBooksToAdapter(ObjectCollection.category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getBooks());
        }
    }

    private void setRecyclerSubCategoryToAdapter()
    {
        Log.e(tag,"Settign subCAtegoruy to adapter : "+ObjectCollection.category.get(selectedCategory).getSubCategoryName().toString());
        recyclerAdapterSubCategory=new RecyclerAdapterSubCategory(context
                ,ObjectCollection.category.get(selectedCategory).getSubCategoryName());
        recyclerAdapterSubCategory.getSubCategorySelectedNotifier().addObserver(CategoryViewModel.this);
        recyclerSubCategory.setAdapter(recyclerAdapterSubCategory);
    }
    public MutableLiveData<Boolean> getToggleSubCategoryVisibility() {
        return toggleSubCategoryVisibility;
    }
}