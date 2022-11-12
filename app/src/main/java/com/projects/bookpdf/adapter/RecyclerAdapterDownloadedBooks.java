package com.projects.bookpdf.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.bookpdf.BuildConfig;
import com.projects.bookpdf.R;
import com.projects.bookpdf.activity.MainActivity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RecyclerAdapterDownloadedBooks extends RecyclerView.Adapter<RecyclerAdapterDownloadedBooks.ViewHolder> {
    private Context context;
    private HashMap<Integer, ArrayList<File>> downloadedBooksList;
    private HashMap<Integer, ArrayList<String>> downloadedBooksImageList;
    private boolean show404 = false;

    public RecyclerAdapterDownloadedBooks(Context context, HashMap<Integer, ArrayList<File>> downloadedBooksList, HashMap<Integer, ArrayList<String>> downloadedBooksImageList, boolean found) {
        this.context = context;
        this.downloadedBooksList = downloadedBooksList;
        this.downloadedBooksImageList = downloadedBooksImageList;
        show404 = found;
        if (show404) {
            downloadedBooksImageList.put(0, null);
            downloadedBooksList.put(0, null);
        }
        Log.e("AdapterDownloadBook", "downloadBooksList.size() : " + downloadedBooksList.size());
        Log.e("AdapterDownloadBook", "downloadBooksImageList.size() : " + downloadedBooksImageList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (!show404)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_recycler_item, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_downloads_available, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (!show404) {
                /*if (position == downloadedBooksList.size() - 1) {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.leftCard.getLayoutParams();
                    lp.bottomMargin = 200;
                    holder.leftCard.setLayoutParams(lp);
                } else {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.leftCard.getLayoutParams();
                    lp.bottomMargin = 8;
                    holder.leftCard.setLayoutParams(lp);
                }*/
                ArrayList<File> file = downloadedBooksList.get(position);
                ArrayList<String> images = downloadedBooksImageList.get(position);
                Log.e("AdapterDownloadBooks", "file.size() :" + file.size());
                Log.e("AdapterDownloadBooks", "images.size() :" + images.size());
                assert file != null;
                assert images != null;
                if (file.size() == 1 && images.size() == 1) {
                    //TODO: setting left components
                    if (images.get(0) != null && images.get(0).length() > 0)
                        Glide.with(context)
                                .load(images.get(0))
                                .into(holder.bookImageLeft);
                    else
                        holder.bookImageLeft.setVisibility(View.INVISIBLE);
                    holder.bookImageLeft.setOnClickListener(v -> holder.bookNameLeft.callOnClick());

                    holder.bookNameLeft.setText(file.get(0).getName());
                    holder.bookNameLeft.setOnClickListener(v -> {
                        MainActivity.showProgressDialog();
                        launchIntent(file.get(0));
                    });

                    holder.leftCard.setOnClickListener(v -> holder.bookNameLeft.callOnClick());

                    holder.btnMoreLeft.setOnClickListener(v -> {
                        try {
                            showMoreDialog(v,file.get(0),position);
                        }
                        catch (Exception e)
                        {
                            Log.e("AdapterDownloadBook","btnMoreLeft.onCLick() :Exception :"+e.getMessage());
                            Log.e("AdapterDownloadBook","btnMoreLeft.onCLick() :Exception :"+ Arrays.toString(e.getStackTrace()));
                        }
                    });

                    //TODO: setting right components
                    holder.rightCard.setVisibility(View.GONE);

                }
                else if (file.size() == 2 && images.size() == 2) {
                    //TODO: setting left components
                    if (images.get(0) != null && images.get(0).length() > 0)
                        Glide.with(context)
                                .load(images.get(0))
                                .into(holder.bookImageLeft);
                    else
                        holder.bookImageLeft.setVisibility(View.INVISIBLE);
                    holder.bookImageLeft.setOnClickListener(v -> holder.bookNameLeft.callOnClick());

                    holder.bookNameLeft.setText(file.get(0).getName());
                    holder.bookNameLeft.setOnClickListener(v -> {
                        MainActivity.showProgressDialog();
                        launchIntent(file.get(0));
                    });

                    holder.leftCard.setOnClickListener(v -> holder.bookNameLeft.callOnClick());

                    holder.btnMoreLeft.setOnClickListener(v -> {
                    });

                    //TODO: setting right components
                    if (images.get(1) != null && images.get(1).length() > 0)
                        Glide.with(context)
                                .load(images.get(1))
                                .into(holder.bookImageRight);
                    else
                        holder.bookImageRight.setVisibility(View.INVISIBLE);
                    holder.bookImageRight.setOnClickListener(v -> holder.bookNameRight.callOnClick());

                    holder.bookNameRight.setText(file.get(1).getName());
                    holder.bookNameRight.setOnClickListener(v -> {
                        MainActivity.showProgressDialog();
                        launchIntent(file.get(1));
                    });

                    holder.rightCard.setOnClickListener(v -> holder.bookNameRight.callOnClick());

                    holder.btnMoreRight.setOnClickListener(v -> {
                    });
                }
            }
        }
        catch (Exception e)
        {
            Log.e("AdapterDownloadBook","Exception :"+e.getMessage());
            Log.e("AdapterDownloadBook","Exception :"+ Arrays.toString(e.getStackTrace()));
        }
    }
    private void showMoreDialog(View v,File file,int position) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PopupMenu popupMenu = new PopupMenu(context, v);
        Method method = popupMenu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
        method.setAccessible(true);
        method.invoke(popupMenu.getMenu(), true);
        popupMenu.getMenuInflater().inflate(R.menu.downloaded_book_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.share_book:
                    Intent i = new Intent(Intent.ACTION_SEND);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                    else
                        uri=Uri.fromFile(file);
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.setType("application/pdf");
                    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Intent chooser = Intent.createChooser(i, "Share Book Using");
                    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(chooser);
                    break;
                case R.id.delete_book:
                    if(file.delete())
                    {
                        downloadedBooksList.remove(position);
                        notifyItemRemoved(position);
                    }
            }
            return false;
        });
        popupMenu.show();
    }

    private void launchIntent(File file) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID+".provider", file);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent = Intent.createChooser(intent, "Open File");
            context.startActivity(intent);
        }
        else
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent = Intent.createChooser(intent, "Open File");
            context.startActivity(intent);
        }
        MainActivity.stopProgressDialog();
    }

    @Override
    public int getItemCount() {
        return downloadedBooksList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookNameLeft, bookNameRight;
        ImageView bookImageLeft, bookImageRight;
        CardView leftCard, rightCard;
        ImageButton btnMoreLeft, btnMoreRight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftCard = itemView.findViewById(R.id.card_view_left);
            rightCard = itemView.findViewById(R.id.card_view_right);
            bookNameLeft = itemView.findViewById(R.id.txt_book_name_left);
            bookNameRight = itemView.findViewById(R.id.txt_book_name_right);
            bookImageLeft = itemView.findViewById(R.id.img_book_cover_left);
            bookImageRight = itemView.findViewById(R.id.img_book_cover_right);
            btnMoreLeft = itemView.findViewById(R.id.img_btn_more_left);
            btnMoreRight = itemView.findViewById(R.id.img_btn_more_right);
        }
    }
}
