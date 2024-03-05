package com.revoto.ai.photo.wallpaperappnew;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revoto.ai.photo.ADS.AdsManager;
import com.revoto.ai.photo.ADS.InterstitialAD;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder>
{
    MainActivity mainActivity;
    String[] categoryList;
    InterstitialAD helper;
    AdsManager adsManager = null;
    public Category_Adapter(MainActivity mainActivity, String[] categoryList) {
        this.mainActivity = mainActivity;
        this.categoryList = categoryList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_categories,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.category.setText(categoryList[position]);

        adsManager = new AdsManager(mainActivity);
        helper = new InterstitialAD(mainActivity,mainActivity,adsManager);

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helper.showCounterInterstitialAd(new InterstitialAD.AdLoadListeners() {
                    @Override
                    public void onAdLoadFailed() {
                        String category = categoryList[position];
                        mainActivity.getWallpapersByCategory(category);
                    }

                    @Override
                    public void onInterstitialDismissed() {
                        String category = categoryList[position];
                        mainActivity.getWallpapersByCategory(category);
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.tvCategory);
        }
    }
}
