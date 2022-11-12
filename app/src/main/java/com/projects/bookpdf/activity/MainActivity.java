package com.projects.bookpdf.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.card.MaterialCardView;
import com.projects.bookpdf.R;
import com.projects.bookpdf.data.MainActivityData;
import com.projects.bookpdf.data.ObjectCollection;
import com.projects.bookpdf.ui.category.CategoryFragment;
import com.projects.bookpdf.ui.home.HomeFragment;
import com.projects.bookpdf.ui.search.SearchFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    public static Dialog progressDialog;
    private static TextView txtSuccessDialogInfo, txtFailureDialogInfo, txtInfoDialogInfo;
    private static AlertDialog successDialog, infoDialog, failureDialog;
    private static MaterialCardView successDialogOkCard, failureDialogOkCard, infoDialogOkCard;
    private CardView home, category, search, downloads, exit;
    private EditText editTextSearch;
    private TextView txtTitle, txtMessage;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private NavController navController;
    private View confirmDialogView, searchDialogView, progressView, successDialogView, failureDialogView, infoDialogView;
    private AlertDialog confirmDialog, searchDialog;
    private MaterialCardView yesCard, noCard, searchCard;
    private InterstitialAd interstitialAd;
    public static void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public static void stopProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static void showFailureDialog(String msg) {
        txtFailureDialogInfo.setText(msg);
        failureDialog.show();
    }

    public static void showSuccessDialog(String msg) {
        txtSuccessDialogInfo.setText(msg);
        successDialog.show();
    }

    public static void showInfoDialog(String msg) {
        txtInfoDialogInfo.setText(msg);
        infoDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id_interstitial));
        // Ads.ShowInterstitialAds(MainActivity.this);
        ObjectCollection.searchResultNotifier.addObserver(MainActivity.this);
        //TODO: setting progress dialog
        progressView = getLayoutInflater().inflate(R.layout.progress_wheel, null, false);
        progressDialog = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        progressDialog.setContentView(progressView);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(dialog -> onBackPressed());
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        SpinKitView progressDialog = progressView.findViewById(R.id.spin_kit);
        Sprite sprite = new FadingCircle();
        sprite.setColor(R.color.colorPrimary);
        progressDialog.setIndeterminateDrawable(sprite);

        //TODO : creating success dialog
        successDialogView = getLayoutInflater().inflate(R.layout.success_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(successDialogView);
        successDialog = builder.create();
        successDialogOkCard = successDialogView.findViewById(R.id.material_card_ok);
        txtSuccessDialogInfo = successDialogView.findViewById(R.id.txt_info);
        successDialogOkCard.setOnClickListener(v -> successDialog.dismiss());

        //TODO : creating failure dialog
        failureDialogView = getLayoutInflater().inflate(R.layout.failure_dialog, null);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(failureDialogView);
        failureDialog = builder.create();
        failureDialogOkCard = failureDialogView.findViewById(R.id.material_card_ok);
        txtFailureDialogInfo = failureDialogView.findViewById(R.id.txt_info);
        failureDialogOkCard.setOnClickListener(v -> failureDialog.dismiss());

        //TODO : creating Info dialog
        infoDialogView = getLayoutInflater().inflate(R.layout.info_dialog, null);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(infoDialogView);
        infoDialog = builder.create();
        infoDialogOkCard = infoDialogView.findViewById(R.id.material_card_ok);
        txtInfoDialogInfo = infoDialogView.findViewById(R.id.txt_info);
        infoDialogOkCard.setOnClickListener(v -> infoDialog.dismiss());

        //TODO: Initializing and creating views for confirmation dialog
        confirmDialogView = getLayoutInflater().inflate(R.layout.confirm_dialog, null);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(confirmDialogView);
        confirmDialog = builder.create();
        yesCard = confirmDialogView.findViewById(R.id.material_card_yes);
        yesCard.setOnClickListener(v -> finish());
        noCard = confirmDialogView.findViewById(R.id.material_card_no);
        noCard.setOnClickListener(v -> confirmDialog.dismiss());
        txtMessage = confirmDialogView.findViewById(R.id.txt_confirm_dialog_text);

        //TODO: Initializing and creating views for search dialog
        searchDialogView = getLayoutInflater().inflate(R.layout.search_dialog, null);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(searchDialogView);
        searchCard = searchDialogView.findViewById(R.id.material_card_search);
        editTextSearch = searchDialogView.findViewById(R.id.edit_txt_search);
        searchCard.setOnClickListener(v -> {
            if (editTextSearch.getText().toString().trim().length() > 0) {
                showProgressDialog();
                ObjectCollection.searchForBook(editTextSearch.getText().toString().trim(), MainActivity.this);
                //TODO: Call for Searching books
            }
        });
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO)
                searchCard.callOnClick();
            return false;
        });
        searchDialog = builder.create();

        txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText(MainActivityData.title);

        //TODO: Sliding Pane Layout.
        slidingUpPanelLayout = findViewById(R.id.umano_sliding_layout);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    txtTitle.setText(getString(R.string.select_an_option));
                } else
                    txtTitle.setText(MainActivityData.title);
            }
        });

        //TODO: NavController.
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.search && ObjectCollection.searchBook != null)
                MainActivityData.title = ObjectCollection.searchBook.getSearchQuery();
            else
                MainActivityData.title = destination.getLabel().toString();
            txtTitle.setText(MainActivityData.title);
        });

        home = findViewById(R.id.card_view_home);
        home.setOnClickListener(v -> {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            if (navController.getCurrentDestination().getId() != R.id.home) {
               /* interstitialAd.loadAd(new AdRequest.Builder().build());
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        interstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e("AJM", "Inter Ad Failed");
                    }
                });*/
                navController.navigate(R.id.home);
                StartAppAd.showAd(this);
            }
        });
        category = findViewById(R.id.card_view_category);
        category.setOnClickListener(v -> {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            if (navController.getCurrentDestination().getId() != R.id.category) {
                /*interstitialAd.loadAd(new AdRequest.Builder().build());
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        interstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e("AJM", "Inter Ad Failed : i :" + i);
                    }
                });*/
                navController.navigate(R.id.category);
                StartAppAd.showAd(this);
            }
        });
        search = findViewById(R.id.card_view_search);
        search.setOnClickListener(v -> {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            editTextSearch.setText("");
            searchDialog.show();
        });
        downloads = findViewById(R.id.card_view_downloads);
        downloads.setOnClickListener(v -> {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            if (navController.getCurrentDestination().getId() != R.id.downloads) {
                /*interstitialAd.loadAd(new AdRequest.Builder().build());
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        interstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e("AJM", "Inter Ad Failed");
                    }
                });*/
                StartAppAd.showAd(this);
                navController.navigate(R.id.downloads);
            }
        });
        exit = findViewById(R.id.card_view_exit);
        exit.setOnClickListener(v -> showConfirmationDialog(getString(R.string.txt_dialog_exit_title)));
    }

    private void showConfirmationDialog(String msg) {
        txtMessage.setText(msg);
        confirmDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else {
            NavDestination navDestination = navController.getCurrentDestination();
            assert navDestination != null;
            if (navDestination.getId() == R.id.book_details)
                navController.navigateUp();
            else if (navDestination.getId()==R.id.downloads)
                navController.navigateUp();
            else if (navDestination.getId() != R.id.home)
                navController.navigate(R.id.home);
            else {
                StartAppAd.onBackPressed(this);
                showConfirmationDialog(getString(R.string.txt_dialog_exit_title));
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ObjectCollection.SearchResultNotifier) {
            stopProgressDialog();
            searchDialog.dismiss();
            if ((int) arg == 0) {
                SearchFragment.view = null;
                SearchFragment.searchViewModel = null;
                Log.e("AJM", "Below setting search fragment view and vm = null");
               /* interstitialAd.loadAd(new AdRequest.Builder().build());
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        interstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                    }
                });*/
                StartAppAd.showAd(this);
                navController.navigate(R.id.search);
            } else if ((int) arg == -1) {
                showInfoDialog("Sorry, something unexpected occurred!\nTry again with a different search query");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HomeFragment.homeViewModel = null;
        HomeFragment.view = null;
        CategoryFragment.view=null;
        CategoryFragment.categoryViewModel=null;
    }
}
