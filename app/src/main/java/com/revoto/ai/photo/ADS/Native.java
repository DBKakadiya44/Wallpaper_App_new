package com.revoto.ai.photo.ADS;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.revoto.ai.photo.wallpaperappnew.R;

import java.util.Random;


public class Native {
    private Context context;

    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;

    public Native(Activity activity) {
        context = activity;

        MobileAds.initialize(activity);

        AudienceNetworkAds.initialize(activity);


    }


    public void admobnativeads(Activity activity, ViewGroup viewGroup, int i) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        String adUnitId = sharedPreferences.getString(AdName.GoogleNativeId, "");
        String nativeadsetting = sharedPreferences.getString(AdName.GoogleNativesetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        if (setting.contains("ON") && nativeadsetting.contains("ON")) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, adUnitId);
            builder.forNativeAd(nativeAd -> {
                InflatAds inflatAds = new InflatAds(activity);
                inflatAds.inflat_admobnative(nativeAd, viewGroup ,i);
            });
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    fbshownative(activity, viewGroup , i);

                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();

                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build());
            builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d("patel", "onAdFailedToLoad: " + loadAdError);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.d("patel", "onAdFailedToLoad: ");
                }
            });
        }
    }

    public void fbshownative(@NonNull Activity activity, ViewGroup viewGroup, int i) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        String adUnitId = sharedPreferences.getString(AdName.FacebokNativeId, "");
        String nativeadsetting = sharedPreferences.getString(AdName.FacebokNativesetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        if (setting.contains("ON") && nativeadsetting.contains("ON")) {
            NativeAd nativeAd = new NativeAd(activity, adUnitId);

            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    applovinnative(activity, viewGroup);
                    Log.d("patel", "onError: erroe");

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    InflatAds inflatAds = new InflatAds(activity);
                    try {

                        inflatAds.inflat_facebooknative(nativeAd, viewGroup , i);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };
            nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
        }
    }


    public void applovinnative(Activity activity, ViewGroup viewGroup) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        String adUnitId = sharedPreferences.getString(AdName.ApplovinNativeId, "");
        String nativeadsetting = sharedPreferences.getString(AdName.ApplovinNativesetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");


        if (setting.contains("ON") && nativeadsetting.contains("ON")) {

            nativeAdLoader = new MaxNativeAdLoader(adUnitId, activity);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }

                    nativeAd = ad;
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._250sdp);
                    nativeAdView.setLayoutParams(new FrameLayout.LayoutParams(width, height, Gravity.BOTTOM));
                    nativeAdView.setBackgroundColor(Color.WHITE);
                    viewGroup.removeAllViews();
                    viewGroup.addView(nativeAdView);

                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    qurekanative(activity, viewGroup);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoader.loadAd();

        }else {
            Log.d("Nativeadapp", "applovinnative: ");
        }


    }


    public void qurekanative(Activity activity, ViewGroup viewGroup) {


        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        String adUnitId = sharedPreferences.getString(AdName.ApplovinNativeId, "");
        String nativeadsetting = sharedPreferences.getString(AdName.ApplovinNativesetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        if (setting.contains("ON") && nativeadsetting.contains("ON")) {
            CardView adView = (CardView) (activity).getLayoutInflater().inflate(R.layout.ads_qureka_native, null);

            ImageView qurekaimg = adView.findViewById(R.id.qurekaimg);
            int[] nativearray = new int[]{R.drawable.native_banner_1, R.drawable.native_banner_2, R.drawable.native_banner_3, R.drawable.native_banner_4, R.drawable.native_banner_5};
            int random = new Random().nextInt(6 - 1);

            try {
                qurekaimg.setImageDrawable(activity.getResources().getDrawable(nativearray[random]));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                qurekaimg.setImageDrawable(activity.getResources().getDrawable(R.drawable.native_banner_1));
            }

            ImageView qurekalogo = adView.findViewById(R.id.qurekalogo);
            int[] nativearray1 = new int[]{R.drawable.mgl_square1, R.drawable.mgl_square2, R.drawable.mgl_square3, R.drawable.mgl_square4, R.drawable.mgl_square5};
            int random1 = new Random().nextInt(6 - 1);

            try {
                qurekalogo.setImageDrawable(activity.getResources().getDrawable(nativearray1[random1]));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                qurekalogo.setImageDrawable(activity.getResources().getDrawable(R.drawable.mgl_square1));
            }

            adView.findViewById(R.id.qurekanetive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(activity, Uri.parse(adUnitId));
                }
            });

            viewGroup.removeAllViews();
            viewGroup.addView(adView);

        }




    }



    public void ShowNative(Activity activity ,ViewGroup viewGroup , int i){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        String NativePriority = sharedPreferences.getString(AdName.NativePriority , "");

            if (NativePriority.equals("AD")){
                admobnativeads(activity , viewGroup , i);
            } else if (NativePriority.equals("FB")) {
                fbshownative(activity , viewGroup , i);
            } else if (NativePriority.equals("APP")) {
                applovinnative(activity , viewGroup);
            }else if (NativePriority.equals("WEB")) {
                qurekanative(activity , viewGroup);
            }

    }
}
