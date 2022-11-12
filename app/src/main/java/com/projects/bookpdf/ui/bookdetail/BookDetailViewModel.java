package com.projects.bookpdf.ui.bookdetail;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.material.card.MaterialCardView;
import com.projects.bookpdf.R;
import com.projects.bookpdf.activity.MainActivity;
import com.projects.bookpdf.data.ObjectCollection;
import com.projects.bookpdf.database.DBHelper;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

class BookDetailViewModel extends ViewModel implements Observer {
    private static final String TAG = "BookDetailViewModel";
    private Context context;
    private FragmentActivity activity;
    private MutableLiveData<Integer> loadRemainingData;
    private RewardedAd rewardedAd;

    BookDetailViewModel(Context context, FragmentActivity activity) {
        super();
        this.activity = activity;
        this.context = context;
        loadRemainingData = new MutableLiveData<>();
        loadRemainingData.setValue(0);
        ObjectCollection.bookDetailNotifier.addObserver(BookDetailViewModel.this);
        rewardedAd = new RewardedAd(context,
                context.getString(R.string.ad_unit_reward));
    }

    //TODO: Download the book!!
    void downloadBook(String downloadUrl, String bookName,String bookImgUrl) {
        Log.e("Download url f method", downloadUrl);
        if (downloadUrl.length() > 0) {
            Log.e("AJM", "calling download task");
            View view = LayoutInflater.from(context).inflate(R.layout.confirm_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            AlertDialog alertDialog = null;
            builder.setView(view);
            alertDialog = builder.create();
            TextView title = view.findViewById(R.id.txt_search_dialog_title);
            title.setText("Watch a video to download the book");
            MaterialCardView yes, no;
            yes = view.findViewById(R.id.material_card_yes);
            no = view.findViewById(R.id.material_card_no);
            AlertDialog finalAlertDialog1 = alertDialog;
            yes.setOnClickListener(v -> {
                finalAlertDialog1.dismiss();
                MainActivity.showProgressDialog();
                /*rewardedAd.loadAd(new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                    @Override
                    public void onRewardedAdLoaded() {
                        super.onRewardedAdLoaded();
                        MainActivity.stopProgressDialog();
                        rewardedAd.show(activity, new RewardedAdCallback() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                new DownloadTask(bookImgUrl, bookName).execute(downloadUrl, bookName);
                            }

                            @Override
                            public void onRewardedAdFailedToShow(int i) {
                                super.onRewardedAdFailedToShow(i);
                                MainActivity.showFailureDialog("Error loading the video");
                            }
                        });
                    }

                    @Override
                    public void onRewardedAdFailedToLoad(int i) {
                        super.onRewardedAdFailedToLoad(i);
                        MainActivity.stopProgressDialog();
                        Log.e(TAG, "onRewardedAdFailedToLoad: i :" + i);
                        new DownloadTask(bookImgUrl, bookName).execute(downloadUrl, bookName);
                    }
                });*/
                try {
                    StartAppAd startAppAd = new StartAppAd(context);
                    startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                        @Override
                        public void onReceiveAd(Ad ad) {
                            Log.e(TAG, "onReceiveAd: ");
                            startAppAd.setVideoListener(() -> {
                                // Grant user with the reward
                                MainActivity.stopProgressDialog();
                                MainActivity.showInfoDialog("Your should will be downloaded soon");
                                new DownloadTask(bookImgUrl, bookName).execute(downloadUrl, bookName);
                            });
                        }

                        @Override
                        public void onFailedToReceiveAd(Ad ad) {
                            MainActivity.stopProgressDialog();
                            MainActivity.showInfoDialog("Your should will be downloaded soon");
                            new DownloadTask(bookImgUrl, bookName).execute(downloadUrl, bookName);
                            Log.e(TAG, "onFailedToReceiveAd: ad: " + ad.getErrorMessage());
                        }
                    });
                } catch (Exception e) {
                    MainActivity.stopProgressDialog();
                    MainActivity.showInfoDialog("Your should will be downloaded soon");
                    new DownloadTask(bookImgUrl, bookName).execute(downloadUrl, bookName);
                    Log.e(TAG, "downloadBook: Reward ad exception ", e);
                }
            });
            AlertDialog finalAlertDialog = alertDialog;
            no.setOnClickListener(v -> finalAlertDialog.dismiss());
            alertDialog.show();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        MainActivity.stopProgressDialog();
        if (o instanceof ObjectCollection.BookDetailNotifier)
        {
            if(arg != null)
            {
                if((int)arg==0) {
                    loadRemainingData.setValue(loadRemainingData.getValue() + 1);
                }
            }

            }
    }

    MutableLiveData<Integer> getLoadRemainingData() {
        return loadRemainingData;
    }

    class DownloadTask extends AsyncTask<String, Void, Void> {
        int downloadSuccess=0;
        String bookImgUrl,bookName;

        public DownloadTask(String bookImgUrl, String bookName) {
            this.bookImgUrl = bookImgUrl;
            this.bookName = bookName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.showProgressDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String path;
                path = context.getFilesDir().getPath()+ "/BookPDF";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Log.e("inBG","file :"+file.getPath());
                HttpURLConnection connection;
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(true);
                HttpURLConnection.setFollowRedirects(true);
                connection.setRequestMethod("GET");
                connection.connect();
                boolean redirect=false;
                if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK&&(connection.getResponseCode()==HttpURLConnection.HTTP_MOVED_PERM
                        ||connection.getResponseCode()==HttpURLConnection.HTTP_MOVED_TEMP
                        ||connection.getResponseCode()==HttpURLConnection.HTTP_SEE_OTHER))
                    redirect = true;

                if(redirect)
                {
                    String newUrl=connection.getHeaderField("Location");
                    String[] splitUrl=newUrl.split(" ");
                    url=new URL(splitUrl[0]);
                    connection= (HttpURLConnection) url.openConnection();
                    connection.connect();
                    Log.e("DownloadTaskBackground", "redirect = true, new url :"+ Arrays.toString(splitUrl) +"\n : responseCode() : "+connection.getResponseCode());
                    String bName="";
                    for(int i=1;i<splitUrl.length;i++)
                    {
                        if(i==splitUrl.length-1)
                            bName += splitUrl[i];
                        else
                            bName += splitUrl[i]+" ";
                    }
                    if(connection.getResponseCode()==200)
                    {
                        Log.e("DownloadTaskBackground","above saveFile() of redirect : "+bName);
                        saveFile(file,bName,connection);
                        return null;
                    }
                    else
                    {
                        return null;
                    }
                }
                else if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    saveFile(file,strings[1],connection);
                    return null;
                } else {
                    Log.e("DownloadTaskBackground","inside else block :");
                    if(connection.getResponseCode()==500)
                    {

                    }
                    //TODO: Undesired response. Show a dialog to the user with appropriate message
                    return null;
                }
            } catch (Exception e) {
                Log.e("DownloadTaskBackground", "Exception  :" + e.getMessage());
                Log.e("DownloadTaskBackground", "Exception  :" + Arrays.toString(e.getStackTrace()));
                return null;
            }
        }
        private void saveFile(File file,String bookName,HttpURLConnection connection) throws IOException {
            bookName=bookName.replace(" ","_");
            InputStream input;
            File savingFile = new File(file, bookName + ".pdf");
            int fileNo = 1;
            while (savingFile.exists()) {
                savingFile = new File(file, bookName + "(" + fileNo + ").pdf");
                fileNo++;
            }
            if(!savingFile.exists())
                savingFile.createNewFile();
            savingFile.setReadable(true);
            savingFile.setWritable(true);
            savingFile.setExecutable(true);
            FileOutputStream fos = new FileOutputStream(savingFile);
            input = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int len1;
            while ((len1 = input.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            input.close();
            downloadSuccess=1;
            DBHelper dbHelper=new DBHelper(context);
            dbHelper.openDBForWrite();
            dbHelper.insertIntoTblDownloadedBooks(savingFile.getName(),bookImgUrl);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity.stopProgressDialog();
            if(downloadSuccess==1)
            {
                MainActivity.showSuccessDialog(bookName+"\nDownloaded");
            }
            else if(downloadSuccess==0)
            {
                MainActivity.showFailureDialog("Sorry, could not Download\n"+bookName);
            }
        }
    }
}