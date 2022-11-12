package com.projects.bookpdf.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.bookpdf.R;
import com.startapp.sdk.ads.banner.Banner;

public class SearchFragment extends Fragment implements ViewModelStoreOwner {
    private static final String TAG = "SearchFragment";
    public static SearchViewModel searchViewModel = null;
    public static View view=null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(searchViewModel==null)
        searchViewModel =
                new ViewModelProvider(SearchFragment.this,
                        new SearchViewModelFactory(requireContext()))
                        .get(SearchViewModel.class);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_search_books);
            searchViewModel.setAdapter(recyclerView, getActivity());
            loadTopAdBanner();
        }
        return view;
    }

    private void loadTopAdBanner() {
        LinearLayout linearLayout = view.findViewById(R.id.ad_linear_layout);
        com.startapp.sdk.ads.banner.Banner banner = new Banner(requireContext());
        banner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(banner);
        /*AdView adView = new AdView(requireContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.ad_unit_id_banner));
        linearLayout.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e(TAG, "onAdFailedToLoad: ");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e(TAG, "onAdLoaded: ");
            }

        });*/
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
