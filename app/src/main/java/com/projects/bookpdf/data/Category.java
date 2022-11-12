package com.projects.bookpdf.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Category {
    private int categoryId;
    private String categoryTitle;
    private String categoryUrl;
    private String categoryImageUrl;
    private int totalPage;
    private int totalLoadedPage = 1;
    //TODO: books Array List will contain all the books appearing under this particular category/sub category
    private ArrayList<Book> books = new ArrayList<>();
    //TODO: subCategory hashMap will contain sub category name and sub category data as a Category object
    private LinkedHashMap<String, Category> subCategory = new LinkedHashMap<String, Category>();
    //TODO: subCategoryName hashMap will contain sub category name and sub category image url
    private LinkedHashMap<String,String> subCategoryName =null;

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books.addAll(books);
    }

    public void setSubCategory(LinkedHashMap<String, Category> subCategory) {
        this.subCategory = subCategory;
    }

    public void setSubCategoryName(LinkedHashMap<String, String> subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public LinkedHashMap<String, Category> getSubCategory() {
        return subCategory;
    }

    public LinkedHashMap<String, String> getSubCategoryName() {
        return subCategoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalLoadedPage() {
        return totalLoadedPage;
    }

    public void setTotalLoadedPage(int totalLoadedPage) {
        this.totalLoadedPage = totalLoadedPage;
    }

    public Category(String categoryTitle, String categoryUrl, String categoryImageUrl) {
        this.categoryTitle = categoryTitle;
        this.categoryUrl = categoryUrl;
        this.categoryImageUrl = categoryImageUrl;
    }

    public Category(int categoryId, String categoryTitle, String categoryUrl, String categoryImageUrl, int totalPage, int totalLoadedPage) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.categoryUrl = categoryUrl;
        this.categoryImageUrl = categoryImageUrl;
        this.totalPage = totalPage;
        this.totalLoadedPage = totalLoadedPage;
    }



    @Override
    public String toString() {
        return "Category{" +
                "categoryTitle='" + categoryTitle + '\'' +
                ", categoryUrl='" + categoryUrl + '\'' +
                ", categoryImageUrl='" + categoryImageUrl + '\'' +
                '}';
    }
}