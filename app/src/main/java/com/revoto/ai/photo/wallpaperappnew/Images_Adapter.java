package com.revoto.ai.photo.wallpaperappnew;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Images_Adapter extends RecyclerView.Adapter<Images_Adapter.ViewHolder>
{
    MainActivity mainActivity;
    ArrayList<String> imgUrl;

    public Images_Adapter(MainActivity mainActivity, ArrayList<String> imgUrl) {
        this.mainActivity = mainActivity;
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_images,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Picasso.with(mainActivity)
                .load(imgUrl.get(position))
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, Image_Preview.class);
                intent.putExtra("position",position);
                intent.putExtra("imglist",imgUrl);
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgUrl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivImage);
        }
    }
}
