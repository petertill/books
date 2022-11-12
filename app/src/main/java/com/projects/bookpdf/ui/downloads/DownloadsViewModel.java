package com.projects.bookpdf.ui.downloads;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.bookpdf.activity.MainActivity;
import com.projects.bookpdf.adapter.RecyclerAdapterDownloadedBooks;
import com.projects.bookpdf.database.DBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class DownloadsViewModel extends ViewModel {
    private File[] listOfBooks;
    private String path;
    private Context context;
    private DBHelper dbHelper;
    private RecyclerView recyclerDownloadedBooks;
    private RecyclerAdapterDownloadedBooks adapterDownloadedBooks;

    DownloadsViewModel(Context context) {
        this.context = context;
        path = context.getFilesDir().getPath() + "/BookPDF";
        dbHelper = new DBHelper(context);
        Log.e("Download VM", "constructor() : path : " + path);
    }

    void getListOfBooks() {
        MainActivity.showProgressDialog();
        try {
            int k = 0;
            File dir = new File(path);
            HashMap<Integer, ArrayList<File>> fileList = new HashMap<>();
            HashMap<Integer, ArrayList<String>> imgList = new HashMap<>();
            if (dir.exists())
                listOfBooks = dir.listFiles();
            else {
                Log.e("Download VM", "fileList.size() : " + fileList.size());
                Log.e("Download VM", "imgList.size() : " + imgList.size());
                adapterDownloadedBooks = new RecyclerAdapterDownloadedBooks(context, fileList, imgList, true);
                recyclerDownloadedBooks.setAdapter(adapterDownloadedBooks);
                MainActivity.stopProgressDialog();
                return;
            }
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < listOfBooks.length; i++) {
                Log.e("Download VM", "getListOfBooks i=" + i);
                Log.e("Download VM", "getListOfBooks bookName" + listOfBooks[i].getName());
                String imgUrl = dbHelper.getDownloadedBooksData(listOfBooks[i].getName());
                Log.e("Download VM", "getListOfBooks image URL" + imgUrl);
                files.add(listOfBooks[i]);
                strings.add(imgUrl);
                if ((files.size() == 1 && strings.size() == 1) || i == listOfBooks.length - 1) {
                    Log.e("Download VM", "b4 putting into fileList: files :" + files.toString());
                    fileList.put(k, files);
                    Log.e("Download VM", "b4 putting into imgList: strings :" + strings.toString());
                    imgList.put(k, strings);
                    k++;
                    files = new ArrayList<>();
                    strings = new ArrayList<>();
                }
            }
            Log.e("Download VM", "fileList.size() : " + fileList.toString());
            Log.e("Download VM", "imgList.size() : " + imgList.toString());
            boolean found = false;
            if (fileList.size() <= 0)
                found = true;
            adapterDownloadedBooks = new RecyclerAdapterDownloadedBooks(context, fileList, imgList, found);
            recyclerDownloadedBooks.setAdapter(adapterDownloadedBooks);
            MainActivity.stopProgressDialog();
        } catch (Exception e) {
            Log.e("Download VM", "getListOfBooks() : " + e.getMessage());
            MainActivity.showFailureDialog(e.getMessage());
            Log.e("Download VM", "getListOfBooks() : " + Arrays.toString(e.getStackTrace()));
            MainActivity.stopProgressDialog();
        }
    }

    public void setView(RecyclerView recyclerDownloadedBooks) {
        this.recyclerDownloadedBooks = recyclerDownloadedBooks;
        getListOfBooks();
    }
}