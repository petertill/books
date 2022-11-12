package com.projects.bookpdf.ui.bookdetail;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.projects.bookpdf.R;
import com.projects.bookpdf.activity.MainActivity;
import com.projects.bookpdf.data.Book;
import com.projects.bookpdf.data.ObjectCollection;
import com.startapp.sdk.ads.banner.Cover;

import java.util.Objects;

public class BookDetailFragment extends Fragment implements ViewModelStoreOwner {

    public static int cameFromHome = 0;
    private BookDetailViewModel bookDetailViewModel;
    private View view;
    private ImageView bookCover;
    private MaterialCardView btnDownload;
    private TextView txtTitle, txtYear, txtSize, txtTotalDownloads, txtAuthor, txtPages, txtLanguage;
    private String headerText = "";
    private int position = -1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookDetailViewModel =
                new ViewModelProvider(BookDetailFragment.this
                        , new BookDetailViewModelFactory(requireContext(), requireActivity()))
                        .get(BookDetailViewModel.class);
        Log.e("AJM", "catvm :" + bookDetailViewModel);
        view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;

        //TODO : setting variable for navigation purpose
        setCameFrom(bundle);

        //TODO: Determining if the source is HomePage RecyclerView or Search RecyclerView and as per it, initializing views
        Book book;
        if (bundle.getBoolean("coming_from_home") && !bundle.getBoolean("coming_from_search")) {
            headerText = bundle.getString("header_text");
            position = bundle.getInt("current_book_position");
            book = Objects.requireNonNull(ObjectCollection.homePageBook.getBooks().get(headerText)).get(position);
            //TODO : checking and calling method to fetch book details
            if (!book.areDetailsFetched()) {
                MainActivity.showProgressDialog();
                ObjectCollection.getIndividualBookDetails(headerText, position, book.getBookUrl(), getActivity());
            }
            initializeViews(book);
            //TODO: observer for loading remaining data
            bookDetailViewModel.getLoadRemainingData().observe(getViewLifecycleOwner(), integer -> {
                if (integer > 0) {
                    Book b = Objects.requireNonNull(ObjectCollection.homePageBook.getBooks().get(headerText)).get(position);
                    if (b.getAuthors().length() <= 0)
                        txtAuthor.setText(getString(R.string.info_not_available));
                    else
                        txtAuthor.setText(b.getAuthors());
                    if (b.getBookLanguage().length() <= 0)
                        txtLanguage.setText(getString(R.string.info_not_available));
                    else
                        txtLanguage.setText(b.getBookLanguage());
                    txtLanguage.setText(b.getBookLanguage());
                    btnDownload.setOnClickListener(v -> bookDetailViewModel.downloadBook(b.getDownloadUrl(), b.getBookName(), b.getBookImageURL()));
                }
            });
        } else if (!bundle.getBoolean("coming_from_home") && bundle.getBoolean("coming_from_search")) {
            position = bundle.getInt("current_book_position");
            book = ObjectCollection.searchBook.getBooks().get(position);
            //TODO : checking and calling method to fetch book details
            if (!book.areDetailsFetched()) {
                MainActivity.showProgressDialog();
                ObjectCollection.getIndividualBookDetails(position, book.getBookUrl(), getActivity());
            }
            initializeViews(book);
            //TODO: observer for loading remaining data
            bookDetailViewModel.getLoadRemainingData().observe(getViewLifecycleOwner(), integer -> {
                if (integer > 0) {
                    Book b = ObjectCollection.searchBook.getBooks().get(position);
                    if (b.getAuthors().length() <= 0)
                        txtAuthor.setText(getString(R.string.info_not_available));
                    else
                        txtAuthor.setText(b.getAuthors());
                    if (b.getBookLanguage().length() <= 0)
                        txtLanguage.setText(getString(R.string.info_not_available));
                    else
                        txtLanguage.setText(b.getBookLanguage());
                    btnDownload.setOnClickListener(v -> bookDetailViewModel.downloadBook(b.getDownloadUrl(), b.getBookName(), b.getBookImageURL()));
                }
            });
        } else {
            position = bundle.getInt("current_book_position");
            if (bundle.getString("current_sub_category") == null) {
                book = ObjectCollection.category.get(bundle.getString("current_category")).getBooks().get(position);
                //TODO : checking and calling method to fetch book details
                if (!book.areDetailsFetched()) {
                    MainActivity.showProgressDialog();
                    ObjectCollection.getIndividualBookDetails(position, book.getBookUrl(), bundle.getString("current_category"), getActivity());
                }
            } else {
                book = ObjectCollection.category.get(bundle.getString("current_category")).getSubCategory().get(bundle.getString("current_sub_category")).getBooks().get(position);
                //TODO : checking and calling method to fetch book details
                if (!book.areDetailsFetched()) {
                    MainActivity.showProgressDialog();
                    ObjectCollection.getIndividualBookDetails(position, book.getBookUrl(), bundle.getString("current_category"), bundle.getString("current_sub_category"), getActivity());
                }
            }

            initializeViews(book);
            //TODO: observer for loading remaining data
            bookDetailViewModel.getLoadRemainingData().observe(getViewLifecycleOwner(), integer -> {
                if (integer > 0) {
                    Book b;
                    if (bundle.getString("current_sub_category") == null)
                        b = ObjectCollection.category.get(bundle.getString("current_category")).getBooks().get(position);
                    else
                        b = ObjectCollection.category.get(bundle.getString("current_category")).getSubCategory().get(bundle.getString("current_sub_category")).getBooks().get(position);

                    if (b.getAuthors().length() <= 0)
                        txtAuthor.setText(getString(R.string.info_not_available));
                    else
                        txtAuthor.setText(b.getAuthors());
                    if (b.getBookLanguage().length() <= 0)
                        txtLanguage.setText(getString(R.string.info_not_available));
                    else
                        txtLanguage.setText(b.getBookLanguage());
                    btnDownload.setOnClickListener(v -> bookDetailViewModel.downloadBook(b.getDownloadUrl(), b.getBookName(), b.getBookImageURL()));
                }
            });
        }
        loadTopAdBanner();
        return view;
    }

    private void loadTopAdBanner() {
        LinearLayout linearLayout = view.findViewById(R.id.ad_linear_layout);
        Cover startAppCover = new Cover(requireContext());
        LinearLayout.LayoutParams coverParameters =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        coverParameters.gravity = Gravity.CENTER;
        linearLayout.addView(startAppCover);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initializeViews(Book book) {
        bookCover = view.findViewById(R.id.img_book_cover);
        Glide.with(requireContext())
                .load(book.getBookImageURL())
                .into(bookCover);

        txtTitle = view.findViewById(R.id.txt_book_title);
        txtTitle.setText(book.getBookName());

        txtLanguage = view.findViewById(R.id.txt_book_language);
        if (book.getBookLanguage().length() <= 0)
            txtLanguage.setText(getString(R.string.info_not_available));
        else
            txtLanguage.setText(book.getBookLanguage());

        txtPages = view.findViewById(R.id.txt_pages_of_book);
        if (book.getBookPage().length() <= 0)
            txtPages.setText(getString(R.string.info_not_available));
        else
            txtPages.setText(book.getBookPage().split(" ")[0]);

        txtYear = view.findViewById(R.id.txt_year);
        if (book.getBookYear().length() <= 0)
            txtYear.setText(getString(R.string.info_not_available));
        else
            txtYear.setText(book.getBookYear());

        txtSize = view.findViewById(R.id.txt_book_size);
        if (book.getBookSize().length() <= 0)
            txtSize.setText(getString(R.string.info_not_available));
        else
            txtSize.setText(book.getBookSize());

        txtTotalDownloads = view.findViewById(R.id.txt_downloads);
        if (book.getBookTotalDownload().length() <= 0)
            txtTotalDownloads.setText(getString(R.string.info_not_available));
        else
            txtTotalDownloads.setText(book.getBookTotalDownload().split(" ")[0]);

        txtAuthor = view.findViewById(R.id.txt_author);
        if (book.getAuthors().length() <= 0)
            txtAuthor.setText(getString(R.string.info_not_available));
        else
            txtAuthor.setText(book.getAuthors());

        btnDownload = view.findViewById(R.id.material_card_download);
        btnDownload.setOnClickListener(v -> bookDetailViewModel.downloadBook(book.getDownloadUrl(), book.getBookName(), book.getBookImageURL()));
    }

    private String calculateTotalDownloads(String totalDownloads) {
        String x = totalDownloads.split(" ")[0];
        String[] y = x.split(",");
        String newY = "";
        int i = 0;
        while (i < y.length) {
            newY += y[i];
            i++;
        }
        long td = Long.parseLong(newY);
        int firstDigit = Integer.parseInt(String.valueOf(newY.toCharArray()[0]));
        String tempTxtDownloads = "";
        if (td <= 10)
            tempTxtDownloads = String.valueOf(td);
        if (td >= firstDigit * 10)
            tempTxtDownloads = firstDigit + "0 +";
        if (td >= firstDigit * 100)
            tempTxtDownloads = firstDigit + "00 +";
        if (td >= firstDigit * 1000)
            tempTxtDownloads = firstDigit + "000 +";
        if (td >= firstDigit * 10000)
            tempTxtDownloads = firstDigit + "0000 +";
        if (td >= firstDigit * 100000)
            tempTxtDownloads = firstDigit + "00000 +";
        if (td >= firstDigit * 1000000)
            tempTxtDownloads = firstDigit + "000000 +";
        if (td >= firstDigit * 10000000)
            tempTxtDownloads = firstDigit + "0000000 +";
        if (td >= firstDigit * 100000000)
            tempTxtDownloads = firstDigit + "00000000 +";
        if (td >= firstDigit * 1000000000)
            tempTxtDownloads = firstDigit + "000000000 +";
        return tempTxtDownloads;
    }

    private void setCameFrom(Bundle bundle) {
        assert bundle != null;
        if (bundle.getBoolean("coming_from_home") && !bundle.getBoolean("coming_from_search"))
            cameFromHome = 1;
        else if (!bundle.getBoolean("coming_from_home") && bundle.getBoolean("coming_from_search"))
            cameFromHome = 2;
        else
            cameFromHome = 0;
    }

}
