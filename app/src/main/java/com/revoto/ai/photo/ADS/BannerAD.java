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
import android.widget.LinearLayout;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.revoto.ai.photo.wallpaperappnew.R;

import java.util.Random;

public class BannerAD {
    private final Activity activity;
    private final LinearLayout adLayout;
    private final FirebaseDatabase database;
    private final DatabaseReference mainref;
    private com.google.android.gms.ads.AdView GoogleadView;
    private com.facebook.ads.AdView FacebookadView;
    private MaxAdView adView;

    private final SharedPreferences sharedPreferences;

    public BannerAD(Activity activity, LinearLayout adLayout) {
        this.activity = activity;
        this.adLayout = adLayout;
        database = FirebaseDatabase.getInstance();
        mainref = database.getReference("mainsetting");

        sharedPreferences = activity.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
    }

    public void loadBannerAd() {
        String BannerPriority = sharedPreferences.getString(AdName.NativePriority, "");


        if (BannerPriority.equals("AD")) {
            showGoogleBannerAD();
        } else if (BannerPriority.equals("FB")) {
            showFBBannerAD();
        }else if (BannerPriority.equals("APP")) {
            showApplovinBannerAD();
        }else if (BannerPriority.equals("WEB")) {
            showQurekabanner();
        }

    }

    public void showGoogleBannerAD() {
        String adUnitId = sharedPreferences.getString(AdName.GoogleAdBannerId, "");
        String banneradsetting = sharedPreferences.getString(AdName.GoogleAdBannersetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        Log.d("showGoogleBannerAD", "showGoogleBannerAD: "+adUnitId);

        if (setting != null && setting.contains("ON") && banneradsetting != null && banneradsetting.contains("ON")) {
            GoogleadView = new com.google.android.gms.ads.AdView(activity);
            GoogleadView.setAdUnitId(adUnitId);
            GoogleadView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
            AdRequest adRequest = new AdRequest.Builder().build();
            GoogleadView.loadAd(adRequest);
            adLayout.addView(GoogleadView);
            GoogleadView.setVisibility(View.VISIBLE);
            adLayout.setVisibility(View.VISIBLE);
            GoogleadView.setAdListener(new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError p0) {
                    super.onAdFailedToLoad(p0);
                    showFBBannerAD();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }
            });

        } else {
            if (GoogleadView != null) {
                GoogleadView.setVisibility(View.GONE);
            }
        }
    }

    public void showFBBannerAD() {
        String adUnitId = sharedPreferences.getString(AdName.FacebokAdBannerId, "");
        String banneradsetting = sharedPreferences.getString(AdName.FacebookAdBannersetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        if (setting.contains("ON") && banneradsetting.contains("ON")) {
            FacebookadView = new com.facebook.ads.AdView(
                    activity,
                    adUnitId,
                    com.facebook.ads.AdSize.BANNER_HEIGHT_50
            );
            adLayout.addView(FacebookadView);
            FacebookadView.setVisibility(View.VISIBLE);
            adLayout.setVisibility(View.VISIBLE);

            com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                   showApplovinBannerAD();
                    Log.d("Harshil", "onError: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d("Harshil", "onAdLoaded: done");
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.d("Harshil", "onAdClicked: click");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.d("Harshil", "onLoggingImpression: login");
                }
            };

            FacebookadView.loadAd(FacebookadView.buildLoadAdConfig().withAdListener(adListener).build());

        } else {
            if (FacebookadView != null) {
                FacebookadView.setVisibility(View.GONE);
            }
        }
    }

    public void showApplovinBannerAD() {

        String adUnitId = sharedPreferences.getString(AdName.ApplovinBannerId, "");
        String banneradsetting = sharedPreferences.getString(AdName.ApplovinBannersetting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        if (setting.contains("ON") && banneradsetting.contains("ON")) {
            adView = new MaxAdView(adUnitId, activity);
            adView.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {

                }

                @Override
                public void onAdLoaded(MaxAd ad) {
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {

                }

                @Override
                public void onAdHidden(MaxAd ad) {

                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                   showGoogleBannerAD();
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                }
            });

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._50sdp);
            adView.setLayoutParams(new FrameLayout.LayoutParams(width, height, Gravity.BOTTOM));
            adView.setBackgroundColor(Color.WHITE);

            adLayout.addView(adView);
            adView.loadAd();
        }else {
            if (adView != null) {
                adView.setVisibility(View.GONE);
            }
        }


    }

    public void showQurekabanner() {

        String adUnitId = sharedPreferences.getString(AdName.QurekaBannerId, "");
        String banneradsetting = sharedPreferences.getString(AdName.QurekaBannersrtting, "");
        String setting = sharedPreferences.getString(AdName.setting, "");

        Log.d("showGoogleBannerAD", "showGoogleBannerAD: "+adUnitId);


        if (setting.contains("ON") && banneradsetting.contains("ON")) {
            CardView adView = (CardView) (activity).getLayoutInflater().inflate(R.layout.ads_qureka_banner, null);


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

            adLayout.removeAllViews();
            adLayout.addView(adView);

        }

    }


}
