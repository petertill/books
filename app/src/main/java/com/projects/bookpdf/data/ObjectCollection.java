package com.projects.bookpdf.data;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.projects.bookpdf.activity.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

public class ObjectCollection {
    public static SearchBook searchBook = null;
    public static HomePageBook homePageBook = null;
    public static HomePageNotifier homePageNotifier = new HomePageNotifier();
    public static BookDetailNotifier bookDetailNotifier = new BookDetailNotifier();
    public static SearchResultNotifier searchResultNotifier = new SearchResultNotifier();
    public static MoreSearchPagesNotifier moreSearchPagesNotifier = new MoreSearchPagesNotifier();

    public static SubCategoryNamesNotifier subCategoryNamesNotifier = new SubCategoryNamesNotifier();
    public static BooksForCategoryNotifier booksForCategoryNotifier = new BooksForCategoryNotifier();
    public static BooksForSubCategoryNotifier booksForSubCategoryNotifier = new BooksForSubCategoryNotifier();
    public static SubCategoryDataNotifier subCategoryDataNotifier = new SubCategoryDataNotifier();
    public static GeneralCategoryLoadedNotifier generalCategoryLoadedNotifier = new GeneralCategoryLoadedNotifier();
    public static LoadMorePagesForCategoryNotifier loadMorePagesForCategoryNotifer = new LoadMorePagesForCategoryNotifier();


    public static LinkedHashMap<String, Category> category = new LinkedHashMap<>();

    public static void setHomePageBook(Activity activity) {
        new Thread(() -> {
            try {
//                TODO:: get Original home page book
                Document doc = Jsoup.connect(HomePageBook.url).get();
                Elements allBooks = doc.select("[class=files-new]");
                Elements cats = doc.select("[class=collection-title mb-2]");
                HashMap<String, ArrayList<Book>> tempBooksCollection = new HashMap<>();
                int catIndex = 0;
                Log.e("setHomePageBook", "entering for");
                if (allBooks.size() > 0)
                    ObjectCollection.homePageBook = new HomePageBook();
                for (int index = 0; index < allBooks.size(); index++) {
                    ArrayList<Book> tempBook = new ArrayList<>();
                    Element books = allBooks.get(index);
                    if (books.hasText()) {
                        for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                            Book b;
                            if (!books.select("li").get(subIndex).hasClass("liad")) {
                                int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                                String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                                String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                                String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                                String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                                String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                                String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                                String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                                String bookDescription = "";
                                String authors = "";
                                String bookLanguage = "";
                                String downloadUrl = "";
                                boolean areDetailsFetched = false;
                                b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                                tempBook.add(b);
                            }
                        }
                        ObjectCollection.homePageBook.getBooks().put(cats.get(catIndex++).select("a").text(), tempBook);

                    }
                }
                activity.runOnUiThread(() -> homePageNotifier.signalHomeFragment(0));
                ArrayList<Book> tempBook;
//               TODO:: Manually add category for home page book
                HashMap<String, String> categoryWiseBooks = new HashMap<String, String>();
                categoryWiseBooks.put("Hacking", "https://www.pdfdrive.com/certified-ethical-hacker-books.html");
                categoryWiseBooks.put("Politics & Laws", "https://www.pdfdrive.com/category/15");
                categoryWiseBooks.put("Technology and Development", "https://www.pdfdrive.com/search?q=latest+developments&pagecount=&pubyear=2015");
//          TODO:: Get category wise book

                for (Map.Entry categoryWiseBook : categoryWiseBooks.entrySet()) {
                    doc = Jsoup.connect(categoryWiseBook.getValue().toString()).get();
                    Elements books = doc.select("[class=files-new]");
                    tempBook = new ArrayList<>();
                    for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                        Book b;
                        if (!books.select("li").get(subIndex).hasClass("liad")) {
                            int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                            String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                            String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                            String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                            String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                            String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                            String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                            String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                            String bookDescription = "";
                            String authors = "";
                            String bookLanguage = "";
                            String downloadUrl = "";
                            boolean areDetailsFetched = false;
                            b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                            tempBook.add(b);
                        }
                    }
                    ObjectCollection.homePageBook.getBooks().put(categoryWiseBook.getKey().toString(), tempBook);
                    activity.runOnUiThread(() -> homePageNotifier.signalHomeFragment(0));
                }
                tempBook = new ArrayList<Book>();
                HashMap<String, String> special = new HashMap<>();
                special.put("https://www.pdfdrive.com/search?q=chanakya%20neeti&more=true", "https://www.pdfdrive.com/chanakya-neeti-e196841625.html");
                special.put("https://www.pdfdrive.com/rich-dad-poor-dad-books.html", "https://www.pdfdrive.com/rich-dad-poor-dad-e136494023.html");
                special.put("https://www.pdfdrive.com/search?q=The+POWER+of+Your+Subconscious+Mind", "https://www.pdfdrive.com/the-power-of-your-subconscious-mind-e34352419.html");
                special.put("https://www.pdfdrive.com/search?q=Who+Will+Cry+When+You+Die&more=true", "https://www.pdfdrive.com/who-will-cry-when-you-die-life-lessons-from-the-monk-who-sold-his-ferrari-e196281352.html");
                special.put("https://www.pdfdrive.com/art-of-war-books.html", "https://www.pdfdrive.com/sun-tzu-on-the-art-of-war-e28283867.html");
                special.put("https://www.pdfdrive.com/stay-hungry-stay-foolish-e43157992.html", "https://www.pdfdrive.com/stay-hungry-stay-foolish-e43157992.html");
                special.put("https://www.pdfdrive.com/search?q=Sapiens%3A+A+Brief+History+of+Humankind", "https://www.pdfdrive.com/sapiens-a-brief-history-of-humankind-e175870479.html");
                special.put("https://www.pdfdrive.com/search?q=Why+I+Killed+the+Mahatma&pagecount=&pubyear=&searchin=&more=true", "https://www.pdfdrive.com/why-i-killed-the-mahatma-understanding-godses-defence-e195131686.html");
//               TODO:: Get category wise book
                for (Map.Entry specialBook : special.entrySet()) {
                    doc = Jsoup.connect(specialBook.getKey().toString()).get();
                    Elements books = doc.select("[class=files-new]");
                    for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                        Book b;
                        if ((!books.select("li").get(subIndex).hasClass("liad")) && (books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href").toString().equals(specialBook.getValue().toString()))) {
                            Log.e("Inside", "Inside if");
                            int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                            String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                            String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                            String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                            String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                            String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                            String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                            String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                            String bookDescription = "";
                            String authors = "";
                            String bookLanguage = "";
                            String downloadUrl = "";
                            boolean areDetailsFetched = false;
                            b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                            tempBook.add(b);
                            books = null;
                            Log.e("temp book", b.toString());
                            b = null;
                            break;
                        }
                    }
                }
                ObjectCollection.homePageBook.getBooks().put("Special Books by Us", tempBook);
                activity.runOnUiThread(() -> homePageNotifier.signalHomeFragment(1));

            } catch (Exception e) {
                Log.e("setHomePageBook", "execetion : " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            }
        }).start();
    }

    //TODO: use this method to load book details from home page object
    public static void getIndividualBookDetails(String headerText, int position, String bookUrl, FragmentActivity activity) {
        new Thread(() -> {
            try {

                Log.e("Book Url k:-", bookUrl);
                //TODO: Write code here and fill the variables above by using 'bookUrl' parameter.
                Document getDataFromBookUrl = Jsoup.connect(bookUrl).get();
                String sessionId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-preview").toString();
                String dataId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-id").toString();
                String downloadUrl = "https://www.pdfdrive.com/download.pdf?id=" + dataId + "&h=" + sessionId.split("session=")[1] + "&u=cache&ext=pdf";
                String authors = getDataFromBookUrl.select("[itemprop=creator]").text().toString();
                String bookLanguage = getDataFromBookUrl.select("[class=info-green]").last().text().toString();
                //TODO: Don't touch below code!
                Objects.requireNonNull(homePageBook.getBooks().get(headerText)).get(position).setBookLanguage(bookLanguage);
                Objects.requireNonNull(homePageBook.getBooks().get(headerText)).get(position).setAuthors(authors);
                Objects.requireNonNull(homePageBook.getBooks().get(headerText)).get(position).setDownloadUrl(downloadUrl);
                Objects.requireNonNull(homePageBook.getBooks().get(headerText)).get(position).setAreDetailsFetched(true);
                activity.runOnUiThread(() -> bookDetailNotifier.notifyBookDetailViewModel());
            } catch (Exception e) {
                Log.e("getBookDetails", "Exception : " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            }
        }).start();
    }

    //TODO: use this method to load book details from Search book object
    public static void getIndividualBookDetails(int position, String bookUrl, FragmentActivity activity) {
        new Thread(() -> {
            try {
                String bookLanguage = "";
                String authors = "";
                String downloadUrl = "";
                //TODO: Write code here and fill the variables above by using 'bookUrl' parameter.
                Log.e("Book Url k:-", bookUrl);
                //TODO: Write code here and fill the variables above by using 'bookUrl' parameter.
                Document getDataFromBookUrl = Jsoup.connect(bookUrl).get();
                String sessionId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-preview").toString();
                String dataId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-id").toString();
                downloadUrl = "https://www.pdfdrive.com/download.pdf?id=" + dataId + "&h=" + sessionId.split("session=")[1] + "&u=cache&ext=pdf";
                authors = getDataFromBookUrl.select("[itemprop=creator]").text().toString();
                bookLanguage = getDataFromBookUrl.select("[class=info-green]").last().text().toString();

                //TODO: Don't touch below code!
                Objects.requireNonNull(searchBook.getBooks()).get(position).setBookLanguage(bookLanguage);
                Objects.requireNonNull(searchBook.getBooks()).get(position).setAuthors(authors);
                Objects.requireNonNull(searchBook.getBooks()).get(position).setDownloadUrl(downloadUrl);
                Objects.requireNonNull(searchBook.getBooks()).get(position).setAreDetailsFetched(true);
                activity.runOnUiThread(() -> bookDetailNotifier.notifyBookDetailViewModel());
            } catch (Exception e) {
                Log.e("getBookDetails", "Exception : " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            }
        }).start();
    }

    //TODO: use this method to load book details from major category object
    public static void getIndividualBookDetails(int position, String bookUrl,String currentCategory, FragmentActivity activity) {
        new Thread(() -> {
            try {
                String bookLanguage = "";
                String authors = "";
                String downloadUrl = "";
                //TODO: Write code here and fill the variables above by using 'bookUrl' parameter.
                Log.e("Book Url k:-", bookUrl);
                //TODO: Write code here and fill the variables above by using 'bookUrl' parameter.
                Document getDataFromBookUrl = Jsoup.connect(bookUrl).get();
                String sessionId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-preview").toString();
                String dataId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-id").toString();
                downloadUrl = "https://www.pdfdrive.com/download.pdf?id=" + dataId + "&h=" + sessionId.split("session=")[1] + "&u=cache&ext=pdf";
                authors = getDataFromBookUrl.select("[itemprop=creator]").text().toString();
                bookLanguage = getDataFromBookUrl.select("[class=info-green]").last().text().toString();

                //TODO: Don't touch below code!
                Objects.requireNonNull(category.get(currentCategory).getBooks()).get(position).setBookLanguage(bookLanguage);
                Objects.requireNonNull(category.get(currentCategory).getBooks()).get(position).setAuthors(authors);
                Objects.requireNonNull(category.get(currentCategory).getBooks()).get(position).setDownloadUrl(downloadUrl);
                Objects.requireNonNull(category.get(currentCategory).getBooks()).get(position).setAreDetailsFetched(true);
                activity.runOnUiThread(() -> bookDetailNotifier.notifyBookDetailViewModel());
            } catch (Exception e) {
                Log.e("getBookDetails", "Exception : " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            }
        }).start();
    }

    //TODO: use this method to load book details from sub category object
    public static void getIndividualBookDetails(int position, String bookUrl,String currentCategory,String currentSubCategory,FragmentActivity activity) {
        new Thread(() -> {
            try {
                String bookLanguage = "";
                String authors = "";
                String downloadUrl = "";
                //TODO: Write code here and fill the variables above by using 'bookUrl' parameter.
                Log.e("Book Url k:-", bookUrl);
                //TODO: Write code here and fill the variables above by using 'bookUrl' parameter.
                Document getDataFromBookUrl = Jsoup.connect(bookUrl).get();
                String sessionId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-preview").toString();
                String dataId = getDataFromBookUrl.select("[id=previewButtonMain]").attr("data-id").toString();
                downloadUrl = "https://www.pdfdrive.com/download.pdf?id=" + dataId + "&h=" + sessionId.split("session=")[1] + "&u=cache&ext=pdf";
                authors = getDataFromBookUrl.select("[itemprop=creator]").text().toString();
                bookLanguage = getDataFromBookUrl.select("[class=info-green]").last().text().toString();

                //TODO: Don't touch below code!
                Objects.requireNonNull(category.get(currentCategory).getSubCategory().get(currentSubCategory).getBooks()).get(position).setBookLanguage(bookLanguage);
                Objects.requireNonNull(category.get(currentCategory).getSubCategory().get(currentSubCategory).getBooks()).get(position).setAuthors(authors);
                Objects.requireNonNull(category.get(currentCategory).getSubCategory().get(currentSubCategory).getBooks()).get(position).setDownloadUrl(downloadUrl);
                Objects.requireNonNull(category.get(currentCategory).getSubCategory().get(currentSubCategory).getBooks()).get(position).setAreDetailsFetched(true);
                activity.runOnUiThread(() -> bookDetailNotifier.notifyBookDetailViewModel());
            } catch (Exception e) {
                Log.e("getBookDetails", "Exception : " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
            }
        }).start();
    }

    public static void searchForBook(String query, Activity activity) {
        new Thread(() -> {
            try {
                String searchQuery = query.replace(" ", "+");
                String searchUrl = "https://www.pdfdrive.com/search?q=" + searchQuery;
                Document doc;
                doc = Jsoup.connect(searchUrl).get();
                Elements books = doc.select("[class=files-new]");
                int totalPage = Integer.parseInt(doc.select("[class=Zebra_Pagination]").select("li").last().previousElementSibling().text());
                searchBook = new SearchBook(searchUrl, query, totalPage);
                for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                    Book b;
                    if (!books.select("li").get(subIndex).hasClass("liad")) {
                        int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                        String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                        String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                        String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                        String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                        String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                        String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                        String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                        String bookDescription = "";
                        String authors = "";
                        String bookLanguage = "";
                        String downloadUrl = "";
                        boolean areDetailsFetched = false;
                        b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                        searchBook.getBooks().add(b);
                    }
                }
                activity.runOnUiThread(() -> searchResultNotifier.notifyHomeActivity(0));
            } catch (Exception e) {
                Log.e("searchForBook", "exception : " + e.getMessage());
                Log.e("searchForBook", "exception : " + Arrays.toString(e.getStackTrace()));
                activity.runOnUiThread(() -> searchResultNotifier.notifyHomeActivity(-1));
            }

        }).start();
    }

    public static void getOneMoreSearchPage(int pageNo, String searchUrl, FragmentActivity activity) {
        searchUrl += "&page=" + pageNo;
        Log.e("Search Url", searchUrl);
        final String finalSearchUrl = searchUrl;
        new Thread(() -> {
            try {
                Document doc;
                doc = Jsoup.connect(finalSearchUrl).get();
                Elements books = doc.select("[class=files-new]");
                int totalPage = Integer.parseInt(doc.select("[class=Zebra_Pagination]").select("li").last().previousElementSibling().text());
                for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                    Book b;
                    if (!books.select("li").get(subIndex).hasClass("liad")) {
                        int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                        String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                        String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                        String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                        String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                        String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                        String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                        String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                        String bookDescription = "";
                        String authors = "";
                        String bookLanguage = "";
                        String downloadUrl = "";
                        boolean areDetailsFetched = false;
                        b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                        searchBook.getBooks().add(b);
                    }
                }
                activity.runOnUiThread(() -> moreSearchPagesNotifier.searchViewModelNotifier());
                if (books.select("li").size() > 0) {
                    searchBook.setTotalLoadedPage(searchBook.getTotalLoadedPage() + 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void initializeCategoryObject(Activity activity)
    {
        Log.e("hello","log aaivo");
        //TODO: initialize ObjectCollection.category object with data and load the data about 1st category below
        new Thread(() -> {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.pdfdrive.com").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements allCats = doc.select("[class=categories-list]").select("li");
        String title;
        Log.e("hmm 1","mmm");
        for(Element cat:allCats)
        {
            Log.e("hmm","mmm");
            title=cat.select("a").select("img").attr("title").toString();
            String imagecat=cat.select("a").select("img").attr("abs:src").toString();
            String urlcat=cat.select("a").attr("abs:href").toString();
            Category c=new Category(title,urlcat,imagecat);
            category.put(title,c);
            Log.e("Cat",c.toString());
            c=null;
        }
        Log.e("Title :- ",category.toString());
        activity.runOnUiThread(() ->generalCategoryLoadedNotifier.notifyCategoryViewModel());
            try {
                doc=Jsoup.connect("https://www.pdfdrive.com/category/112").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Book> temp=new ArrayList<Book>();
            Elements books = doc.select("[class=files-new]");
            int totalPage = Integer.parseInt(doc.select("[class=Zebra_Pagination]").select("li").last().previousElementSibling().text());
            for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                Book b;
                if (!books.select("li").get(subIndex).hasClass("liad")) {
                    int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                    String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                    String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                    String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                    String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                    String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                    String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                    String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                    String bookDescription = "";
                    String authors = "";
                    String bookLanguage = "";
                    String downloadUrl = "";
                    boolean areDetailsFetched = false;
                    b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                    temp.add(b);
                }
            }
            category.get("Most Popular").setBooks(temp);
        }).start();
    }

    public static void getSubCategoryNamesForCategory(String selectedCategory, FragmentActivity activity) {
        //TODO: code below
        MainActivity.showProgressDialog();
        new Thread(() -> {
            LinkedHashMap<String,Category> subCategory=new LinkedHashMap<String, Category>();
            category.get(selectedCategory).setSubCategoryName(new LinkedHashMap<>());
            Document doc = null;
            try {
                doc = Jsoup.connect(category.get(selectedCategory).getCategoryUrl()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements allCats = doc.select("[class=categories-list subcategories-list mt-4]").select("li");
            String title;
            for (Element cat : allCats) {
                title = cat.select("a").select("img").attr("title").toString();
                String imagecat = cat.select("a").select("img").attr("abs:src").toString();
                String urlcat = cat.select("a").attr("abs:href").toString().split(",")[0];
                Category c = new Category(title, urlcat, imagecat);
                subCategory.put(title,c);
                category.get(selectedCategory).getSubCategoryName().put(title,imagecat);
            }
            Log.e("AJM","subCAtName.size() : "+category.get(selectedCategory).getSubCategoryName().size());
            category.get(selectedCategory).setSubCategory(subCategory);
            activity.runOnUiThread(() -> subCategoryNamesNotifier.notifyCategoryViewModel(selectedCategory));
        }).start();

    }

    public static void getBooksForCategory(String selectedCategory, FragmentActivity activity) {
        //TODO: code below
        MainActivity.showProgressDialog();
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(category.get(selectedCategory).getCategoryUrl()).get();

                ArrayList<Book> temp = new ArrayList<Book>();
                Elements books = doc.select("[class=files-new]");
                int totalPage = Integer.parseInt(doc.select("[class=Zebra_Pagination]").select("li").last().previousElementSibling().text());
                for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                    Book b;
                    if (!books.select("li").get(subIndex).hasClass("liad")) {
                        int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                        String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                        String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                        String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                        String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                        String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                        String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                        String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                        String bookDescription = "";
                        String authors = "";
                        String bookLanguage = "";
                        String downloadUrl = "";
                        boolean areDetailsFetched = false;
                        b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                        temp.add(b);
                    }
                }
                category.get(selectedCategory).setBooks(temp);
                category.get(selectedCategory).setTotalPage(totalPage);
                activity.runOnUiThread(() -> booksForCategoryNotifier.notifyCategoryViewModel(selectedCategory));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }
    public static void getBooksForSubCategory(String selectedCategory, String selectedSubCategory, FragmentActivity activity) {
        //TODO: code below
        MainActivity.showProgressDialog();
        new Thread(() -> {
            try {
                Document doc=Jsoup.connect(category.get(selectedCategory).getSubCategory().get(selectedSubCategory).getCategoryUrl()).get();

                ArrayList<Book> temp=new ArrayList<Book>();
                Elements books = doc.select("[class=files-new]");
                int totalPage = Integer.parseInt(doc.select("[class=Zebra_Pagination]").select("li").last().previousElementSibling().text());
                for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                    Book b;
                    if (!books.select("li").get(subIndex).hasClass("liad")) {
                        int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                        String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                        String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                        String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                        String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                        String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                        String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                        String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                        String bookDescription = "";
                        String authors = "";
                        String bookLanguage = "";
                        String downloadUrl = "";
                        boolean areDetailsFetched = false;
                        b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                        temp.add(b);
                    }
                }
                category.get(selectedCategory).getSubCategory().get(selectedSubCategory).setBooks(temp);
                category.get(selectedCategory).getSubCategory().get(selectedSubCategory).setTotalPage(totalPage);
                activity.runOnUiThread(() -> booksForSubCategoryNotifier.notifyCategoryViewModel(selectedCategory, selectedSubCategory));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    public static void getSubCategoryData(String selectedCategory, String selectedSubCategory, FragmentActivity activity) {
        //TODO: Code below
        MainActivity.showProgressDialog();
        new Thread(() -> {
            LinkedHashMap<String,Category> subCategory=new LinkedHashMap<String, Category>();
            Document doc = null;
            try {
                doc = Jsoup.connect(category.get(selectedCategory).getCategoryUrl()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements allCats = doc.select("[class=categories-list subcategories-list mt-4]").select("li");
            String title;
            for (Element cat : allCats) {
                title = cat.select("a").select("img").attr("title").toString();
                String imagecat = cat.select("a").select("img").attr("abs:src").toString();
                String urlcat = cat.select("a").attr("abs:href").toString().split(",")[0];
                Category c = new Category(title, urlcat, imagecat);
                subCategory.put(title,c);
            }
            category.get(selectedCategory).setSubCategory(subCategory);
            activity.runOnUiThread(() -> subCategoryDataNotifier.notifyCategoryViewModel(selectedCategory, selectedSubCategory));
        }).start();

    }

public static void loadMorePagesForCategory(int pgNoToLoad,String currentCategory, FragmentActivity activity) {
        new Thread(() -> {
            try {
                Document doc;
                Log.e("Current load more url",category.get(currentCategory).getCategoryUrl()+"/p"+pgNoToLoad);
                doc = Jsoup.connect(category.get(currentCategory).getCategoryUrl()+"/p"+pgNoToLoad).get();
                ArrayList<Book> temp = new ArrayList<Book>();
                Elements books = doc.select("[class=files-new]");
                int totalPage = Integer.parseInt(doc.select("[class=Zebra_Pagination]").select("li").last().previousElementSibling().text());
                Log.e("AJM","odl book size :"+Objects.requireNonNull(category.get(currentCategory)).getBooks().size());
                for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                    Book b;
                    if (!books.select("li").get(subIndex).hasClass("liad")) {
                        int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                        String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                        String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                        String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                        String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                        String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                        String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                        String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                        String bookDescription = "";
                        String authors = "";
                        String bookLanguage = "";
                        String downloadUrl = "";
                        boolean areDetailsFetched = false;
                        b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                        Objects.requireNonNull(category.get(currentCategory)).getBooks().add(b);
                    }
                }
                Log.e("AJM","new book size :"+Objects.requireNonNull(category.get(currentCategory)).getBooks().size());
                Objects.requireNonNull(category.get(currentCategory)).setTotalLoadedPage(pgNoToLoad+1);
                activity.runOnUiThread(() ->loadMorePagesForCategoryNotifer.notifyCategoryViewModel(currentCategory,null));
            } catch (Exception e) {
                Log.e("ObjectCollection","loadMorePagesForCategory(pgNo :"+pgNoToLoad+", category : "+currentCategory+") : eception "+e.getMessage());
                Log.e("ObjectCollection","loadMorePagesForCategory(pgNo :"+pgNoToLoad+", category : "+currentCategory+") : eception "+ Arrays.toString(e.getStackTrace()));
            }
        }).start();

    }

    public static void loadMorePagesForCategory(int pgNoToLoad,String currentCategory, String currentSubCategory, FragmentActivity activity) {
        new Thread(() -> {
            try {
                Document doc;
                doc = Jsoup.connect(category.get(currentCategory).getSubCategory().get(currentSubCategory).getCategoryUrl()+"/p"+pgNoToLoad).get();
                Log.e("Current sub load more",category.get(currentCategory).getSubCategory().get(currentSubCategory).getCategoryUrl()+"/p"+pgNoToLoad);
                ArrayList<Book> temp = new ArrayList<Book>();
                Elements books = doc.select("[class=files-new]");
                int totalPage = Integer.parseInt(doc.select("[class=Zebra_Pagination]").select("li").last().previousElementSibling().text());
                for (int subIndex = 0; subIndex < books.select("li").size(); subIndex++) {
                    Book b;
                    if (!books.select("li").get(subIndex).hasClass("liad")) {
                        int bookId = Integer.parseInt(books.select("li").get(subIndex).select("[class=file-left]").select("a").attr("data-id"));
                        String bookImageUrl = books.select("li").get(subIndex).select("[class=file-left]").select("img").attr("abs:src");
                        String bookUrl = books.select("li").get(subIndex).select("[class=file-right]").select("a").attr("abs:href");
                        String bookName = books.select("li").get(subIndex).select("[class=file-right]").select("h2").text();
                        String bookYear = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-year]").text();
                        String bookSize = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-size hidemobile]").text();
                        String bookTotalDownload = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-hit]").text();
                        String bookPage = books.select("li").get(subIndex).select("[class=file-right]").select("[class=fi-pagecount]").text();
                        String bookDescription = "";
                        String authors = "";
                        String bookLanguage = "";
                        String downloadUrl = "";
                        boolean areDetailsFetched = false;
                        b = new Book(bookId, bookName, bookUrl, bookImageUrl, bookDescription, bookPage, bookYear, bookSize, bookTotalDownload, authors, bookLanguage, downloadUrl, areDetailsFetched);
                        category.get(currentCategory).getSubCategory().get(currentSubCategory).getBooks().add(b);
                    }
                }
                category.get(currentCategory).getSubCategory().get(currentSubCategory).setTotalLoadedPage(pgNoToLoad+1);
                activity.runOnUiThread(()->loadMorePagesForCategoryNotifer.notifyCategoryViewModel(currentCategory,currentSubCategory));
            } catch (Exception e) {
                Log.e("ObjectCollection","loadMorePagesForCategory(pgNo :"+pgNoToLoad+", category : "+currentCategory+",subCategory :"+currentSubCategory+") : eception "+e.getMessage());
                Log.e("ObjectCollection","loadMorePagesForCategory(pgNo :"+pgNoToLoad+", category : "+currentCategory+",subCategory :"+currentSubCategory+") : eception "+ Arrays.toString(e.getStackTrace()));
            }
        }).start();

    }


    public static class HomePageNotifier extends Observable {
        void signalHomeFragment(int i) {
            setChanged();
            notifyObservers(i);
        }
    }

    public static class BookDetailNotifier extends Observable {
        void notifyBookDetailViewModel() {
            setChanged();
            notifyObservers();
        }
    }

    public static class SearchResultNotifier extends Observable {
        void notifyHomeActivity(int i) {
            setChanged();
            notifyObservers(i);
        }
    }

    public static class MoreSearchPagesNotifier extends Observable {
        void searchViewModelNotifier() {
            setChanged();
            notifyObservers();
        }
    }

    public static class SubCategoryNamesNotifier extends Observable {
        void notifyCategoryViewModel(String categoryName) {
            setChanged();
            notifyObservers(categoryName);
        }
    }

    public static class BooksForCategoryNotifier extends Observable {
        void notifyCategoryViewModel(String categoryName) {
            setChanged();
            notifyObservers(categoryName);
        }
    }

    public static class BooksForSubCategoryNotifier extends Observable {
        void notifyCategoryViewModel(String categoryName, String subCategoryName) {
            setChanged();
            notifyObservers(new String[]{categoryName, subCategoryName});
        }
    }

    public static class SubCategoryDataNotifier extends Observable {
        void notifyCategoryViewModel(String categoryName, String subCategoryName) {
            setChanged();
            notifyObservers(new String[]{categoryName, subCategoryName});
        }
    }

    public static class GeneralCategoryLoadedNotifier extends Observable {
        void notifyCategoryViewModel() {
            setChanged();
            notifyObservers();
        }
    }
    public static class LoadMorePagesForCategoryNotifier extends Observable {
        void notifyCategoryViewModel(String currCategory,String currSubCategory) {
            setChanged();
            notifyObservers(new String[]{currCategory,currSubCategory});
        }
    }
}
