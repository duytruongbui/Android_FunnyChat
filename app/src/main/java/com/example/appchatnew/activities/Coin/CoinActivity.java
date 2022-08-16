package com.example.appchatnew.activities.Coin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appchatnew.R;
import com.example.appchatnew.activities.Chat.MainMain;
import com.example.appchatnew.activities.Map.MapActivity;
import com.example.appchatnew.activities.Profile.ProFileActivity;
import com.example.appchatnew.databinding.ActivityCoinBinding;
import com.example.appchatnew.utilities.Constants;
import com.example.appchatnew.utilities.StoreUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoinActivity extends AppCompatActivity {

    ActivityCoinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(getApplicationContext())
                .load(StoreUtils.get(getApplicationContext(), Constants.avatar))
                .error(R.drawable.ic_baseline_supervised_user_circle_24)
                .into(binding.imageView6);

        binding.textView3.setText(StoreUtils.get(getApplicationContext(),Constants.fullname));
        binding.changefullname.setText(StoreUtils.get(getApplicationContext(),Constants.fullname));
        binding.changeusername.setText(StoreUtils.get(getApplicationContext(),Constants.username));
        binding.changePassword.setText(StoreUtils.get(getApplicationContext(),Constants.password));
        binding.changeTelephone.setText(StoreUtils.get(getApplicationContext(),Constants.telephone));

        binding.bottomNavigation.setSelectedItemId(R.id.money);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),
                                MainMain.class));
                        overridePendingTransition(0, 0);
                        return  true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),
                                MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.money:
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(),
                                ProFileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}