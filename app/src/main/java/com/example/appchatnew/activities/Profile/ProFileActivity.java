package com.example.appchatnew.activities.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appchatnew.R;
import com.example.appchatnew.SendData;
import com.example.appchatnew.activities.Chat.ChatActivity;
import com.example.appchatnew.activities.Chat.MainMain;
import com.example.appchatnew.activities.Coin.CoinActivity;
import com.example.appchatnew.activities.Map.MapActivity;
import com.example.appchatnew.adapter.AdapterListChat;
import com.example.appchatnew.api.ApiGetListUser;
import com.example.appchatnew.api.ApiJoinRoom;
import com.example.appchatnew.api.RetrofitClient;
import com.example.appchatnew.databinding.ActivityProFileBinding;
import com.example.appchatnew.model.response.InfoUser;
import com.example.appchatnew.model.response.ResponseBody;
import com.example.appchatnew.utilities.Constants;
import com.example.appchatnew.utilities.StoreUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProFileActivity extends AppCompatActivity {

    ActivityProFileBinding binding;
    AdapterListChat adapterListChat;
    List<InfoUser> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userList = new ArrayList<>();

        binding.textView4.setText("Hi " + StoreUtils.get(ProFileActivity.this, Constants.fullname));

        getListUser();

        binding.bottomNavigation.setSelectedItemId(R.id.person);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),
                                MainMain.class));
                        overridePendingTransition(0, 0);
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
                        return true;
                }
                return false;
            }
        });
    }

    public void getListUser() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = RetrofitClient.getClient()
                .create(ApiGetListUser.class)
                .getListUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<InfoUser>>() {
                    @Override
                    public void accept(List<InfoUser> infoUsers) throws Exception {
                        List<InfoUser> infoUserList = new ArrayList<>();

                        for (InfoUser infoUser : infoUsers) {
                            if (!StoreUtils.get(ProFileActivity.this, Constants.username).equals(infoUser.getUsername())) {
                                infoUserList.add(infoUser);
                            }
                        }

                        adapterListChat = new AdapterListChat(ProFileActivity.this, infoUserList, new SendData() {
                            @Override
                            public void sendData(InfoUser infoUser) {
                                createDataBase(infoUser);
                            }
                        });
                        binding.recyclerView.setAdapter(adapterListChat);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ProFileActivity.this));
                        binding.recyclerView.setHasFixedSize(true);


                        binding.editTextTextPersonName.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                               // Toast.makeText(ProFileActivity.this, ""+editable.toString(), Toast.LENGTH_SHORT).show();
                              if (editable.toString().equals("")){
                                  List<InfoUser> infoUserList = new ArrayList<>();

                                  for (InfoUser infoUser : infoUsers) {
                                      if (!StoreUtils.get(ProFileActivity.this, Constants.username).equals(infoUser.getUsername())) {
                                          infoUserList.add(infoUser);
                                      }
                                  }

                                  adapterListChat = new AdapterListChat(ProFileActivity.this, infoUserList, new SendData() {
                                      @Override
                                      public void sendData(InfoUser infoUser) {
                                          createDataBase(infoUser);
                                      }
                                  });
                                  binding.recyclerView.setAdapter(adapterListChat);
                                  binding.recyclerView.setLayoutManager(new LinearLayoutManager(ProFileActivity.this));
                                  binding.recyclerView.setHasFixedSize(true);
                              }else {
                                  List<InfoUser> infoUserList = new ArrayList<>();

                                  for (InfoUser infoUser : infoUsers) {
                                      if (!StoreUtils.get(ProFileActivity.this, Constants.username).equals(infoUser.getUsername())) {
                                          infoUserList.add(infoUser);
                                      }
                                  }

                                  userList.clear();
                                  for (int i=0;i<infoUserList.size();i++){
                                      if (infoUserList.get(i).getUsername().contains(editable.toString())){
                                            userList.add(infoUserList.get(i));
                                      }
                                  }

                                  adapterListChat = new AdapterListChat(ProFileActivity.this, userList, new SendData() {
                                      @Override
                                      public void sendData(InfoUser infoUser) {
                                          createDataBase(infoUser);
                                      }
                                  });
                                  binding.recyclerView.setAdapter(adapterListChat);
                                  binding.recyclerView.setLayoutManager(new LinearLayoutManager(ProFileActivity.this));
                                  binding.recyclerView.setHasFixedSize(true);
                              }
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void createDataBase(InfoUser infoUser) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = RetrofitClient.getClient()
                .create(ApiJoinRoom.class)
                .create(StoreUtils.get(ProFileActivity.this, Constants.username),infoUser.getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        getListIntent(infoUser);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getListIntent(infoUser);
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void getListIntent(InfoUser infoUser) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("user",infoUser);
        startActivity(intent);
    }

}