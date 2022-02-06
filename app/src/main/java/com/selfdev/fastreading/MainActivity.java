package com.selfdev.fastreading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.selfdev.fastreading.Objects.Info;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    AdView mAdView;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);
        bannerAd();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mSettings = getSharedPreferences("FRconfig", Context.MODE_PRIVATE);
        String login = mSettings.getString("login", null);
        if (login!=null){
            ImageButton ib = findViewById(R.id.imageButton);
            ib.setImageResource(R.drawable.ic_person_avatar_account_user_icon_cabinet);
        }
    }

    void bannerAd()
    {
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
    }


    public void onCheckSpeedClick(View view) {
        Intent intent = new Intent(this, CheckSpeed.class);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, Authorisation.class);
        startActivity(intent);
    }

    public void onBookClick(View view) {
        Intent intent = new Intent(this, Book.class);
        startActivity(intent);
    }

    public void onTrenajeersClick(View view) {
        Intent intent = new Intent(this, Trenajears.class);
        startActivity(intent);
    }

    public void onRecordsClick(View view) {
        Intent intent = new Intent(this, Records.class);
        startActivity(intent);
    }

    public void onInfoClick(View view) {
        Intent intent = new Intent(this, Information.class);
        startActivity(intent);
    }

    public void onSettingClick(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}