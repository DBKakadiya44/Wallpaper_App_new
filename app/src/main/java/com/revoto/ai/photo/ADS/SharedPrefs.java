package com.revoto.ai.photo.ADS;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SharedPrefs {
    AdName adName;
    private Context context;
    private AdsModel adsModel;
    private DatabaseReference reference;

    public SharedPrefs(Context context) {
        this.context = context;
    }

    public void loaddata() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mainref = database.getReference("mainsetting");

        mainref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adsModel = snapshot.getValue(AdsModel.class);
                editor.putString(AdName.FacebokAdBannerId, adsModel.FacebokAdBannerId);
                editor.putInt(AdName.GridAdFeed, adsModel.GridAdFeed);
                editor.putString(AdName.FacebokNativeId, adsModel.FacebokNativeId);
                editor.putString(AdName.FacebokNativesetting, adsModel.FacebokNativesetting);
                editor.putString(AdName.FacebookAdBannersetting, adsModel.FacebookAdBannersetting);
                editor.putString(AdName.FacebookAdInterstitialsetting, adsModel.FacebookAdInterstitialsetting);
                editor.putString(AdName.FacebookInterstitialId, adsModel.FacebookInterstitialId);
                editor.putString(AdName.GoogleAdBannerId, adsModel.GoogleAdBannerId);
                editor.putString(AdName.GoogleAdBannersetting, adsModel.GoogleAdBannersetting);
                editor.putString(AdName.GoogleAdInterstitialId, adsModel.GoogleAdInterstitialId);
                editor.putString(AdName.GoogleAdInterstitialsetting, adsModel.GoogleAdInterstitialsetting);
                editor.putString(AdName.GoogleNativeId, adsModel.GoogleNativeId);
                editor.putString(AdName.GoogleNativesetting, adsModel.GoogleNativesetting);
                editor.putString(AdName.clicks, adsModel.clicks);
                editor.putString(AdName.openadid, adsModel.openadid);
                editor.putString(AdName.openadsetting, adsModel.openadsetting);
                editor.putString(AdName.setting, adsModel.setting);
                editor.putString(AdName.VersionCode, adsModel.VersionCode);
                editor.putString(AdName.BannerPriority, adsModel.BannerPriority);
                editor.putString(AdName.NativePriority, adsModel.NativePriority);
                editor.putString(AdName.InterstitialPriority, adsModel.InterstitialPriority);
                editor.putString(AdName.ApplovinNativeId, adsModel.ApplovinNativeId);
                editor.putString(AdName.ApplovinNativesetting, adsModel.ApplovinNativesetting);
                editor.putString(AdName.ApplovinInterstitialId, adsModel.ApplovinInterstitialId);
                editor.putString(AdName.ApplovinInterstitialsetting, adsModel.ApplovinInterstitialsetting);
                editor.putString(AdName.ApplovinBannersetting, adsModel.ApplovinBannersetting);
                editor.putString(AdName.ApplovinBannerId, adsModel.ApplovinBannerId);
//                editor.putString(AdName.ApplovinAppOpensetting, adsModel.ApplovinAppOpensetting);
//                editor.putString(AdName.ApplovinAppOpenId, adsModel.ApplovinAppOpenId);
//                editor.putString(AdName.AppOpenPriority, adsModel.AppOpenPriority);
                editor.putString(AdName.QurekaBannersrtting, adsModel.QurekaBannersrtting);
                editor.putString(AdName.QurekaBannerId, adsModel.QurekaBannerId);
                editor.putString(AdName.QurekaNativesetting, adsModel.QurekaNativesetting);
                editor.putString(AdName.QurekaNativeId, adsModel.QurekaNativeId);
                editor.putString(AdName.QurekaInterstitialId, adsModel.QurekaInterstitialId);
                editor.putString(AdName.QurekaInterstitialsetting, adsModel.QurekaInterstitialsetting);
                editor.putString(AdName.QurekaAppOpenId, adsModel.QurekaAppOpenId);
                editor.putString(AdName.QurekaAppOpensetting, adsModel.QurekaAppOpensetting);
                editor.apply();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}