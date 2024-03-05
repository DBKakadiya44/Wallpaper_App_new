package com.revoto.ai.photo.ADS;

import android.content.Context;
import android.content.SharedPreferences;

public class AdsManager {
    private final SharedPreferences sharedPreferences;

    public AdsManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AdsManager", Context.MODE_PRIVATE);
    }

    public void incrementClickCount() {
        int clickCount = getClickCount();
        sharedPreferences.edit().putInt("click_count", clickCount + 1).apply();
    }

    public int getClickCount() {
        return sharedPreferences.getInt("click_count", 0);
    }

    public void resetClickCount() {
        sharedPreferences.edit().putInt("click_count", 0).apply();
    }
}