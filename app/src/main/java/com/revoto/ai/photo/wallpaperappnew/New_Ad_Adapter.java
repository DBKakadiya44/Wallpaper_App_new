package com.revoto.ai.photo.wallpaperappnew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revoto.ai.photo.ADS.AdsManager;
import com.revoto.ai.photo.ADS.InterstitialAD;
import com.revoto.ai.photo.ADS.Native;
import com.revoto.ai.photo.wallpaperappnew.databinding.ItemGridAdsBinding;
import com.revoto.ai.photo.wallpaperappnew.databinding.ItemImagesBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class New_Ad_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<String> imageArray;
    public static int ITEM_VIEW = 1;
    public static int AD_VIEW = 2;
    InterstitialAD helper;
    AdsManager adsManager = null;
    int ITEM_FEED_COUNT = MainActivity.gridfeed;
    public New_Ad_Adapter(Context context, ArrayList<String> imageArray)
    {
        this.context=context;
        this.imageArray=imageArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == ITEM_VIEW) {
            view = layoutInflater.inflate(R.layout.item_images, parent, false);
            return new MainViewHolder(view);
        } else if (viewType == AD_VIEW) {
            view = layoutInflater.inflate(R.layout.item_grid_ads, parent, false);
            return new AdsViewHolder(view);
        } else {
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        adsManager = new AdsManager(context);
        helper = new InterstitialAD(context, (Activity) context,adsManager);

        if (holder.getItemViewType() == ITEM_VIEW) {
            ((MainViewHolder) holder).bindData(imageArray, position);

        } else if (holder.getItemViewType() == AD_VIEW) {
            ((AdsViewHolder) holder).bindAdData();
        }
    }
    @Override
    public int getItemViewType(int position) {

        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        } else {
            return ITEM_VIEW;
        }
    }
    @Override
    public int getItemCount() {
        return imageArray.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        ItemImagesBinding binding;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemImagesBinding.bind(itemView);
        }

        private void bindData(ArrayList<String> image, int position) {
            Picasso.with(context)
                    .load(image.get(position))
                    .into(binding.ivImages);

            binding.ivImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    helper.showCounterInterstitialAd(new InterstitialAD.AdLoadListeners() {
                        @Override
                        public void onAdLoadFailed() {
                            Intent intent = new Intent(context, Image_Preview.class);
                            intent.putExtra("position",position);
                            intent.putExtra("imglist",image);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onInterstitialDismissed() {
                            Intent intent = new Intent(context, Image_Preview.class);
                            intent.putExtra("position",position);
                            intent.putExtra("imglist",image);
                            context.startActivity(intent);
                        }
                    });


                }
            });
        }
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder {
        ItemGridAdsBinding binding;
        public AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemGridAdsBinding.bind(itemView);
        }
        private void bindAdData() {
            Native aNative = new Native((Activity) context);
            aNative.ShowNative((Activity) context, binding.llNativeGrid, 3);
        }
    }
}
