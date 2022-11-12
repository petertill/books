package com.projects.bookpdf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.projects.bookpdf.R;
import com.projects.bookpdf.data.ObjectCollection;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ObjectCollection.setHomePageBook(SplashScreen.this);
        ObjectCollection.initializeCategoryObject(SplashScreen.this);
        new Handler().postDelayed(() -> {
            Intent intent=new Intent(SplashScreen.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        },1000);
    }
}
