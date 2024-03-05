package com.revoto.ai.photo.ADS;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.firebase.FirebaseApp;
import com.revoto.ai.photo.wallpaperappnew.SplashActivity;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";
    private static String AD_UNIT_ID = "";
    private static boolean isShowingAd = false;
    private static boolean adLoaded = false;

    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback = null;
    private SplashActivity myApplication;
    private SharedPreferences sharedPreferences;

    public AppOpenManager(SplashActivity myApplication) {
        this.myApplication = myApplication;
        // Initialize Firebase
        FirebaseApp.initializeApp(myApplication);
    }

    public static boolean isAdLoaded() {
        return adLoaded;
    }

    public static boolean isAdShowing() {
        return isShowingAd;
    }

    public void fetchAd() {
        isShowingAd = false;
        if (isAdAvailable()) {
            return;
        }

        sharedPreferences = myApplication.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);

        AD_UNIT_ID = sharedPreferences.getString(AdName.openadid, "");
        String appopenadsetting = sharedPreferences.getString(AdName.openadsetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        if (setting.contains("ON") && appopenadsetting.contains("ON")) {
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    appOpenAd = ad;
                    adLoaded = true;
                    Log.d(LOG_TAG, "Load Success");
//                    showAdIfAvailable();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error.
                    Log.d(LOG_TAG, "Load fail" + loadAdError.toString());
                    myApplication.intentToHomeScreen();
                    myApplication.stopCountdown();
                }
            };

            AdRequest request = getAdRequest();
            AppOpenAd.load(myApplication, AD_UNIT_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        } else {
            Log.d(LOG_TAG, "Setting off");
        }
    }

    public void showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    appOpenAd = null;
                    isShowingAd = false;
                    adLoaded = false;
                    fetchAd();
                    myApplication.intentToHomeScreen();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                }
            };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(myApplication);
        } else {
            Log.d(LOG_TAG, "Cannot show ad.");
            fetchAd();
        }
    }

    public boolean isAdAvailable() {
        return adLoaded && appOpenAd != null;
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        myApplication = (SplashActivity) activity;
        fetchAd();
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
