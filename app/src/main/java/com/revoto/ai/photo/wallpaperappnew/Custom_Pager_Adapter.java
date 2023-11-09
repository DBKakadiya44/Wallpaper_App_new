package com.revoto.ai.photo.wallpaperappnew;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Custom_Pager_Adapter extends PagerAdapter
{
    Image_Preview imagePreview;
    ArrayList<String> imglist;
    int pos;

    public Custom_Pager_Adapter(Image_Preview imagePreview, ArrayList<String> imglist, int pos) {
        this.imagePreview = imagePreview;
        this.imglist = imglist;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return imglist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(imagePreview);
        View view = inflater.inflate(R.layout.item_full_image, container, false);

        container.addView(view);

        ImageView imageView = view.findViewById(R.id.ivFullImage);
            Glide.with(imagePreview).load(imglist.get(position)).into(imageView);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
