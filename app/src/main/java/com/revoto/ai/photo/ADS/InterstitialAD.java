package com.revoto.ai.photo.ADS;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.revoto.ai.photo.wallpaperappnew.R;

public class InterstitialAD {
    private Context context;
    private Activity activity;
    private AdsManager adsManager;
    private InterstitialAd GoogleinterstitialAd;
    private com.facebook.ads.InterstitialAd FacebookinterstitialAd;
    private AdLoadListeners adLoadListeners;
    private FirebaseDatabase database;
    private DatabaseReference adsRef;
    private DatabaseReference mainref;
    private DatabaseReference adsRef2;
    private static Dialog dialog;
    private boolean googleAdFailed = false;
    private boolean facebookAdFailed = false;
    private boolean applovinAdFailed = false;
    private boolean qurekaAdFailed = false;


    public InterstitialAD(Context context, Activity activity, AdsManager adsManager) {
        this.context = context;
        this.activity = activity;
        this.adsManager = adsManager;

        database = FirebaseDatabase.getInstance();
        adsRef = database.getReference("googlead");
        mainref = database.getReference("mainsetting");
        adsRef2 = database.getReference("facebookad");


    }

    public interface AdLoadListeners {
        void onAdLoadFailed();

        void onInterstitialDismissed();
    }

    public void showInterstitialAD(AdLoadListeners adLoadListeners) {
        AppLovinSdk.initializeSdk(context);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);

        String mainsetting = sharedPreferences.getString(AdName.mainsetting, "");
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.ad_load_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        String InterstitialPriority = sharedPreferences.getString(AdName.InterstitialPriority, "");

        Log.d("InterstitialPriority", "showInterstitialAD: "+InterstitialPriority);


        if (InterstitialPriority.equals("AD")) {
            if (googleAdFailed && facebookAdFailed && applovinAdFailed &&qurekaAdFailed) {
                adLoadListeners.onAdLoadFailed();
                return;
            }

            dialog.show();
            setInterstitialAdGoogle(adLoadListeners);
        } else if (InterstitialPriority.equals("FB")) {
            if (googleAdFailed && facebookAdFailed && applovinAdFailed &&qurekaAdFailed) {
                adLoadListeners.onAdLoadFailed();
                return;
            }

            dialog.show();
            setInterstitialAdFacebook(adLoadListeners);
        }else if (InterstitialPriority.equals("APP")) {
            if (googleAdFailed && facebookAdFailed && applovinAdFailed &&qurekaAdFailed) {
                adLoadListeners.onAdLoadFailed();
                Log.d("InterstitialPriority", "setInterstitialAdApplovin: ");
                return;
            }

            dialog.show();
            setInterstitialAdApplovin(activity, adLoadListeners);
        }else if (InterstitialPriority.equals("WEB")) {
            if (googleAdFailed && facebookAdFailed && applovinAdFailed &&qurekaAdFailed) {
                adLoadListeners.onAdLoadFailed();
                Log.d("InterstitialPriority", "setInterstitialAdApplovin: ");
                return;
            }

            dialog.show();
            setInterstitialAdQureka(activity, adLoadListeners);
        }
    }

    public void setInterstitialAdGoogle(AdLoadListeners listener) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);

        adLoadListeners = listener;
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        sharedPrefs.loaddata();

        String adUnitId = sharedPreferences.getString(AdName.GoogleAdInterstitialId, "");
        String setting = sharedPreferences.getString(AdName.setting, "");
        String interstitialSetting = sharedPreferences.getString(AdName.GoogleAdInterstitialsetting, "");

        Log.d("onInterstitialDismissed", "onInterstitialDismissed: "+adUnitId);
        Log.d("onInterstitialDismissed", "onInterstitialDismissed: "+interstitialSetting);
        Log.d("onInterstitialDismissed", "onInterstitialDismissed: "+setting);

        if (setting.contains("ON") && interstitialSetting.equals("ON")) {

            if (!adUnitId.isEmpty()) {
                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(context, adUnitId, adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("patelvs", "onAdFailedToLoad: " + loadAdError);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                adLoadListeners.onAdLoadFailed();
                            }
                        }, 1000);

                        googleAdFailed = true;

                        if (facebookAdFailed) {
                            adLoadListeners.onAdLoadFailed();
                            return; 
                        }

                        setInterstitialAdFacebook(adLoadListeners);
                    }

                    @Override
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        interstitialAd.show(activity);
                        dialog.dismiss();
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                adLoadListeners.onInterstitialDismissed();
                            }
                        });
                    }
                });
            }
        } else {
            dialog.dismiss();
            adLoadListeners.onInterstitialDismissed();
        }
    }

    public void setInterstitialAdFacebook(AdLoadListeners listener) {
        adLoadListeners = listener;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        sharedPrefs.loaddata();

        String setting = sharedPreferences.getString(AdName.setting, "");
        String interstitialSetting = sharedPreferences.getString(AdName.FacebookAdInterstitialsetting, "");
        String adUnitId = sharedPreferences.getString(AdName.FacebookInterstitialId, "");

        if (setting.contains("ON") && interstitialSetting.equals("ON")) {


            if (!adUnitId.isEmpty()) {
                FacebookinterstitialAd = new com.facebook.ads.InterstitialAd(context, adUnitId);
                InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        Log.e("patelvs", "Interstitial ad displayed.");
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        Log.e("patelvs", "Interstitial ad dismissed.");
                        adLoadListeners.onInterstitialDismissed();
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        String errorMessage = adError.getErrorMessage();
                        Log.e("patelvs", "Interstitial ad failed to load: " + errorMessage + "\n" + adError);

                        facebookAdFailed = true;

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                adLoadListeners.onAdLoadFailed();
                            }
                        }, 1000);

                        if (googleAdFailed) {
                            adLoadListeners.onAdLoadFailed();
                            return; // Skip loading any ads if both Google and Facebook ads have failed
                        }

                        setInterstitialAdApplovin(activity, adLoadListeners);

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        Log.d("patelvs", "Interstitial ad is loaded and ready to be displayed!");
                        FacebookinterstitialAd.show();

                        dialog.dismiss();
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        Log.d("patelvs", "Interstitial ad clicked!");
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        Log.d("patelvs", "Interstitial ad impression logged!");
                    }
                };
                FacebookinterstitialAd.loadAd(FacebookinterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
            }
        } else {
            dialog.dismiss();
            adLoadListeners.onInterstitialDismissed();
        }
    }

    public void setInterstitialAdApplovin(Activity activity, AdLoadListeners onAdsListner) {

        adLoadListeners = onAdsListner;

        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        sharedPrefs.loaddata();

        String adUnitId = sharedPreferences.getString(AdName.ApplovinInterstitialId, "");
        String interstitialSetting = sharedPreferences.getString(AdName.ApplovinInterstitialsetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");



        if (setting.equals("ON") && interstitialSetting.equals("ON")) {



            MaxInterstitialAd interstitialAd = new MaxInterstitialAd(adUnitId, activity);
            MaxAdListener adListener = new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    dialog.dismiss();
                    if (interstitialAd.isReady()) {
                        interstitialAd.showAd();
                    }
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    dialog.dismiss();

                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    onAdsListner.onInterstitialDismissed();
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    setInterstitialAdGoogle(adLoadListeners);
                    Log.d("InterstitialPriority", "setInterstitialAdApplovin: 1");

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    setInterstitialAdGoogle(adLoadListeners);
                    Log.d("InterstitialPriority", "setInterstitialAdApplovin:2 ");

                }
            };
            interstitialAd.setListener(adListener);
            interstitialAd.loadAd();

        } else {
            dialog.dismiss();
            adLoadListeners.onInterstitialDismissed();
        }


    }

    public static void setInterstitialAdQureka(Activity activity, AdLoadListeners onAdsListner) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        sharedPrefs.loaddata();

        String adUnitId = sharedPreferences.getString(AdName.QurekaInterstitialId, "");
        String interstitialSetting = sharedPreferences.getString(AdName.QurekaInterstitialsetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        Log.d("onInterstitialDismissed", "onInterstitialDismissed: "+adUnitId);
        Log.d("onInterstitialDismissed", "onInterstitialDismissed: "+interstitialSetting);
        Log.d("onInterstitialDismissed", "onInterstitialDismissed: "+setting);


        if (setting.equals("ON") && interstitialSetting.equals("ON")) {


            onAdsListner.onInterstitialDismissed();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                    httpIntent.setData(Uri.parse(adUnitId));
                    activity.startActivity(httpIntent);
                    dialog.dismiss();

                }
            }, 100);

        }


    }

    public static void Adafterqureka(Activity activity) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        SharedPrefs sharedPrefs = new SharedPrefs(activity);
        sharedPrefs.loaddata();

        String adUnitId = sharedPreferences.getString(AdName.ApplovinInterstitialId, "");
        String interstitialSetting = sharedPreferences.getString(AdName.ApplovinInterstitialsetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        if (setting.equals("ON") && interstitialSetting.equals("ON")) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();

                    Intent intent = new Intent(String.valueOf(Intent.FLAG_ACTIVITY_NO_USER_ACTION));

                    PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                            100,
                            intent,
                            PendingIntent.FLAG_MUTABLE);

                    Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_ad);
                    builder.setActionButton(bitmap, "Ad", pendingIntent);
                    customTabsIntent.launchUrl(activity, Uri.parse(adUnitId));
                }
            }, 200);
        }


    }

    public void showCounterInterstitialAd(AdLoadListeners adLoadListeners) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        String clicks = sharedPreferences.getString(AdName.clicks, "");
        int value = 1;
        try {

            value = Integer.parseInt(clicks);
        } catch (NumberFormatException e) {
            adLoadListeners.onInterstitialDismissed();

        }

        if (adsManager.getClickCount() % value == 0) {
            showInterstitialAD(adLoadListeners);
            adsManager.resetClickCount();
        } else {
            adLoadListeners.onInterstitialDismissed();
        }

        adsManager.incrementClickCount();
    }
}
