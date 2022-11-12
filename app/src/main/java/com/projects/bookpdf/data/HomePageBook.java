package com.projects.bookpdf.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class HomePageBook {



    private LinkedHashMap<String, ArrayList<Book>> books=new LinkedHashMap<String,ArrayList<Book>>();
    public static String url="https://www.pdfdrive.com";
    public HomePageBook() {
    }

    public LinkedHashMap<String, ArrayList<Book>> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "HomePageBook{" +
                "books=" + books +
                '}';
    }
}
