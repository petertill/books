package com.projects.bookpdf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static String dbName = "Books";
    public static int version = 1;
    public static String tblDownloadedBooks = "tbl_downloaded_books";
    private SQLiteDatabase db;
    public DBHelper(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTblDownloadedBooks = "create table " + tblDownloadedBooks
                + "(book_name TEXT PRIMARY KEY" +
                ",book_img_url TEXT)";
        db.execSQL(createTblDownloadedBooks);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDBForWrite()
    {
        db=getWritableDatabase();
    }
    public long insertIntoTblDownloadedBooks(String bookName,String bookImgUrl)
    {
        long result;
        ContentValues contentValues=new ContentValues();
        contentValues.put("book_name",bookName);
        contentValues.put("book_img_url",bookImgUrl);
        result = db.insert(tblDownloadedBooks,null,contentValues);
        Log.e("DBHelper","insertIntoTblDownloadedBooks(bookName :"+bookName+", bookUrl: "+bookImgUrl+") result : "+result);
        return result;
    }

    public Cursor getDownloadedBooksData()
    {
        db=getReadableDatabase();
        Cursor cursor;
        cursor=db.query(tblDownloadedBooks
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null);
        Log.e("DBHelper","getDownloadedBooks() : cursor.count() :"+cursor.getCount());
        return cursor;
    }
    public String getDownloadedBooksData(String bookName)
    {
        db=getReadableDatabase();
        Cursor cursor;
        cursor=db.query(tblDownloadedBooks
                ,null
                ,"book_name=?"
                ,new String[]{bookName}
                ,null
                ,null
                ,null);
        Log.e("DBHelper","getDownloadedBooks(bookName: "+bookName+") : cursor.count() :"+cursor.getCount());
        String imgUrl=null;
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            imgUrl = cursor.getString(cursor.getColumnIndex("book_img_url"));
        }
        Log.e("DBHelper","getDownloadedBooks(bookName: "+bookName+") : imgUrl :"+imgUrl);
        return imgUrl;
    }
}

