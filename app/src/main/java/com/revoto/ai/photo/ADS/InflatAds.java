package com.revoto.ai.photo.ADS;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdOptionsView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.revoto.ai.photo.wallpaperappnew.R;

import java.util.ArrayList;
import java.util.List;

public class InflatAds {
    private  Activity activity;
    NativeAdView adView;
    View inflate;
    public InflatAds(Activity activity) {
        this.activity = activity;
    }

    public void inflat_admobnative(NativeAd unifiedNativeAd, ViewGroup viewGroup , int i) {
        if (activity == null || viewGroup == null) {
            return;
        }


        if(i==1){
            adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ads_native_admob, null);
        }else if(i==2)
        {
            adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ads_native_small, null);
        } else if (i==3)
        {
            adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_native_grid, null);
        }


        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        TextView button = adView.findViewById(R.id.ad_call_to_action);
        adView.setCallToActionView(button);
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        }
        if (unifiedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        }
        if (unifiedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(unifiedNativeAd);
        viewGroup.removeAllViews();
        viewGroup.addView(adView);
    }


    public void inflat_facebooknative(com.facebook.ads.NativeAd nativeAd, ViewGroup viewGroup , int i) {
        if (activity == null || viewGroup == null) {
            return;
        }

        nativeAd.unregisterView();


        if(i==1){
            inflate = LayoutInflater.from(activity).inflate(R.layout.ads_native_fb, null);
        } else if (i==2)
        {
            inflate = LayoutInflater.from(activity).inflate(R.layout.ads_native_fb_small, null);
        } else if (i==3)
        {
            inflate = LayoutInflater.from(activity).inflate(R.layout.ads_native_fb_grid, null);
        }

        viewGroup.removeAllViews();
        viewGroup.addView(inflate);

        LinearLayout adChoicesContainer = inflate.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, activity.findViewById(R.id.nativview));
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        com.facebook.ads.MediaView nativeAdIcon = inflate.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = inflate.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = inflate.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = inflate.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = inflate.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = inflate.findViewById(R.id.native_ad_sponsored_label);
        TextView nativeAdCallToAction = inflate.findViewById(R.id.native_ad_call_to_action);

//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(inflate, nativeAdMedia, nativeAdIcon, clickableViews);
    }

}
