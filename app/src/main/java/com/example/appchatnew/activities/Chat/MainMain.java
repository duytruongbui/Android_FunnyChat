package com.example.appchatnew.activities.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchatnew.Domain.CategoryDomain;
import com.example.appchatnew.Domain.ChatDomain;
import com.example.appchatnew.R;
import com.example.appchatnew.activities.Coin.CoinActivity;
import com.example.appchatnew.activities.Map.MapActivity;
import com.example.appchatnew.activities.Profile.ProFileActivity;
import com.example.appchatnew.adapter.CategoryAdapter;
import com.example.appchatnew.adapter.PopularAdapter;
import com.example.appchatnew.utilities.Constants;
import com.example.appchatnew.utilities.StoreUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainMain extends AppCompatActivity {

    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    private TextView textView4;

    CircleImageView imgAvatar;

    //ActivityMainMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMainMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_main_main);

        textView4 = findViewById(R.id.textView4);
        imgAvatar = findViewById(R.id.imgAvatar);

        Glide.with(getApplicationContext())
                .load(StoreUtils.get(getApplicationContext(),Constants.avatar))
                .into(imgAvatar);

        textView4.setText("Hi " + StoreUtils.get(MainMain.this, Constants.fullname));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),
                                MapActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.money:
                        startActivity(new Intent(getApplicationContext(),
                                CoinActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(),
                                ProFileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        recyclerViewCategory();
        recyclerViewPopular();
    }

    private void recyclerViewPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularList = findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);
        ArrayList<ChatDomain> chatlist = new ArrayList<>();
        chatlist.add(new ChatDomain("RonnieNguyen", "pizza1", "slices pepperoni", 18.0));
        chatlist.add(new ChatDomain("Ruby Linh", "burger", "slices pepperoni", 22.0));
        chatlist.add(new ChatDomain("Ring Long", "pizza2", "slices pepperoni", 29.0));
        chatlist.add(new ChatDomain("Tony Stark", "burger", "slices pepperoni", 30.0));
        chatlist.add(new ChatDomain("Captain Marvel", "burger", "slices pepperoni", 16.0));
        chatlist.add(new ChatDomain("Captain Marvel", "burger", "slices pepperoni", 18.0));
        chatlist.add(new ChatDomain("Captain Marvel", "burger", "slices pepperoni", 23.0));
        chatlist.add(new ChatDomain("Captain Marvel", "burger", "slices pepperoni", 60.0));
        chatlist.add(new ChatDomain("Captain Marvel", "burger", "slices pepperoni", 50.0));
        chatlist.add(new ChatDomain("Captain Marvel", "burger", "slices pepperoni", 37.0));

        adapter2 = new PopularAdapter(chatlist);
        recyclerViewPopularList.setAdapter(adapter2);

    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayout);

        ArrayList<CategoryDomain> categoryList = new ArrayList<>();
        categoryList.add(new CategoryDomain("Social", "cat_1"));
        categoryList.add(new CategoryDomain("Coin", "cat_2"));
        categoryList.add(new CategoryDomain("Tech", "cat_3"));
        categoryList.add(new CategoryDomain("Pet", "cat_4"));
        categoryList.add(new CategoryDomain("Laptop", "cat_5"));
        categoryList.add(new CategoryDomain("Casino", "cat_6"));

        adapter = new CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(adapter);
        recyclerViewCategoryList.setClickable(true);


    }
}