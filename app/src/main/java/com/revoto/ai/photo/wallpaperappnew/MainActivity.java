package com.revoto.ai.photo.wallpaperappnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.revoto.ai.photo.ADS.AdName;
import com.revoto.ai.photo.ADS.AdsManager;
import com.revoto.ai.photo.ADS.InterstitialAD;
import com.revoto.ai.photo.wallpaperappnew.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String category = "";
    InterstitialAD helper;
    AdsManager adsManager = null;
    String[] categoryList = {"Classic", "Nature", "Technology", "Travel", "Arts", "Music", "Flowers", "Creative","Car","Sport","Food","Cow","Birds","Mountain"};
    private ArrayList<String> wallpaperArrayList = new ArrayList<>();
    public static int gridfeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainbgcolor));

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ADDEMO", Context.MODE_PRIVATE);
        gridfeed  = sharedPreferences.getInt(AdName.GridAdFeed ,0);

        adsManager = new AdsManager(this);
        helper = new InterstitialAD(this, this, adsManager);

        showAlertDialog(this);

        showWallPaper();

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = binding.mtSearchBar.getText().toString();
                if (category.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Type Something...", Toast.LENGTH_SHORT).show();
                } else {
                    category = binding.mtSearchBar.getText().toString();
                    getWallpapersByCategory(category);
                }
            }
        });

        Category_Adapter adapter = new Category_Adapter(MainActivity.this, categoryList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.rvCategories.setLayoutManager(manager);
        binding.rvCategories.setAdapter(adapter);


    }

    public void getWallpapersByCategory(String category) {
        wallpaperArrayList.clear();

        String url = "https://api.pexels.com/v1/search?query=" + category + "&per_page=100&page=1";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //on below line we are extracting the data from our response and passing it to our array list.
                try {
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);

                        New_Ad_Adapter imagesAdapter = new New_Ad_Adapter(MainActivity.this, wallpaperArrayList);
                        RecyclerView.LayoutManager manager1 = new GridLayoutManager(MainActivity.this, 2);
                        binding.rvAllImages.setLayoutManager(manager1);
                        binding.rvAllImages.setAdapter(imagesAdapter);

                    }
                } catch (JSONException e) {
                    //handling json exception on below line.
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying a simple toast message on error response.
                Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //in this method passing headers as key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "aCDwgBHzEC0dHgyzzcG8DxCFV6RyaqFJDoai4EFtoKMtdAy56osMoUUm");
                //at last returning headers.
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void showWallPaper() {
        wallpaperArrayList.clear();

        String url = "https://api.pexels.com/v1/curated?per_page=80&page=1";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");

                        wallpaperArrayList.add(imgUrl);

                        New_Ad_Adapter imagesAdapter = new New_Ad_Adapter(MainActivity.this, wallpaperArrayList);
                        RecyclerView.LayoutManager manager1 = new GridLayoutManager(MainActivity.this, 2);
                        binding.rvAllImages.setLayoutManager(manager1);
                        binding.rvAllImages.setAdapter(imagesAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to get data...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "aCDwgBHzEC0dHgyzzcG8DxCFV6RyaqFJDoai4EFtoKMtdAy56osMoUUm");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void showAlertDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Data Loading..."); // Set your loading message
        progressDialog.setCancelable(false);
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        helper.showCounterInterstitialAd(new InterstitialAD.AdLoadListeners() {
            @Override
            public void onAdLoadFailed() {
                finish();
            }

            @Override
            public void onInterstitialDismissed() {
                finish();
            }
        });
        super.onBackPressed();
    }

}