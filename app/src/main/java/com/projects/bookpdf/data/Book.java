package com.projects.bookpdf.data;

public class Book {
    private int bookId;
    private String bookName;
    private String bookUrl;
    private String bookImageURL;
    private String bookDescription;
    private String bookPage;
    private String bookLanguage;
    private String bookYear;
    private String bookSize;
    private String bookTotalDownload;
    private String authors;
    private String downloadUrl;
    private boolean areDetailsFetched;

    public Book(int bookId, String bookName, String bookUrl, String bookImageURL, String bookDescription, String bookPage, String bookYear, String bookSize, String bookTotalDownload,String authors,String bookLanguage,String downloadUrl,boolean areDetailsFetched) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookUrl = bookUrl;
        this.bookImageURL = bookImageURL;
        this.bookDescription = bookDescription;
        this.bookPage = bookPage;
        this.bookYear = bookYear;
        this.bookSize = bookSize;
        this.bookTotalDownload = bookTotalDownload;
        this.authors=authors;
        this.bookLanguage=bookLanguage;
        this.downloadUrl=downloadUrl;
        this.areDetailsFetched=areDetailsFetched;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public String getBookImageURL() {
        return bookImageURL;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public String getBookPage() {
        return bookPage;
    }

    public String getBookYear() {
        return bookYear;
    }

    public String getBookSize() {
        return bookSize;
    }

    public String getBookTotalDownload() {
        return bookTotalDownload;
    }

    public String getAuthors() {
        return authors;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public boolean areDetailsFetched() {
        return areDetailsFetched;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setAreDetailsFetched(boolean areDetailsFetched) {
        this.areDetailsFetched = areDetailsFetched;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                ", bookImageURL='" + bookImageURL + '\'' +
                ", bookDescription='" + bookDescription + '\'' +
                ", bookPage='" + bookPage + '\'' +
                ", bookYear=" + bookYear +
                ", bookSize='" + bookSize + '\'' +
                ", bookTotalDownload='" + bookTotalDownload + '\'' +
                ", authors='" + authors + '\'' +
                ", bookLanguage='" + bookLanguage + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", areDetailsFetched='" + areDetailsFetched + '\'' +
                '}';
    }
}
