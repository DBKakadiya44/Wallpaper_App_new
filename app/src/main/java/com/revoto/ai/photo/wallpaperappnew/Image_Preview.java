package com.revoto.ai.photo.wallpaperappnew;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.revoto.ai.photo.wallpaperappnew.databinding.ActivityImagePreviewBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Image_Preview extends AppCompatActivity {

    ActivityImagePreviewBinding binding;
    WallpaperManager wallpaperManager;
    ArrayList<String> imglist;
    String urls;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainbgcolor));

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        imglist = getIntent().getStringArrayListExtra("imglist");
        position = getIntent().getIntExtra("position",0);
        urls = imglist.get(position);

//        Glide.with(Image_Preview.this).load(url).into(binding.vpFullImage);

        Custom_Pager_Adapter adapter = new Custom_Pager_Adapter(Image_Preview.this,imglist,position);
        binding.vpFullImage.setAdapter(adapter);
        binding.vpFullImage.setCurrentItem(position);
        binding.tvSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Glide.with(Image_Preview.this)
                        .asBitmap().load(urls)
                        .listener(new RequestListener<Bitmap>() {
                                      @Override
                                      public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                          Toast.makeText(Image_Preview.this, "Fail to load image..", Toast.LENGTH_SHORT).show();
                                          return false;
                                      }

                                      @Override
                                      public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                          try {
                                              wallpaperManager.setBitmap(bitmap);
                                              Toast.makeText(Image_Preview.this, "Set wallpaper Sucessfully", Toast.LENGTH_SHORT).show();
                                              return true;
                                          } catch (IOException e) {
                                              Toast.makeText(Image_Preview.this, "Fail to set wallpaper", Toast.LENGTH_SHORT).show();
                                              e.printStackTrace();
                                          }
                                          return true;
                                      }
                                  }
                        ).submit();

                Toast.makeText(Image_Preview.this, "Set wallpaper Sucessfully", Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    URL url = new URL(urls);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream input = connection.getInputStream();

                        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "4K Wallpaper");
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        Random r = new Random();
                        int randomNumber = r.nextInt();

                        File file = new File(directory, randomNumber + "image.jpg");

                        OutputStream output = new FileOutputStream(file);

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = input.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }

                        input.close();
                        output.close();

                        MediaScannerConnection.scanFile(
                                Image_Preview.this,
                                new String[]{file.getAbsolutePath()},
                                new String[]{"image/jpeg"}, // Adjust the MIME type if necessary
                                null
                        );
                        Toast.makeText(Image_Preview.this, "Downloaded Sucessfully", Toast.LENGTH_SHORT).show();

                    } else {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}