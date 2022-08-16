package com.example.appchatnew.activities.SignInUp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appchatnew.activities.Chat.MainMain;
import com.example.appchatnew.activities.Map.GPSActivity;
import com.example.appchatnew.activities.SendAPI;
import com.example.appchatnew.api.ApiLogin;
import com.example.appchatnew.api.RetrofitClient;
import com.example.appchatnew.databinding.ActivitySignInBinding;
import com.example.appchatnew.model.response.InfoUser;
import com.example.appchatnew.utilities.Constants;
import com.example.appchatnew.utilities.StoreUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.buttonGPS.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), GPSActivity.class)));
        binding.buttonsendapi.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SendAPI.class)));

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.inputEmail.getText().toString().equals("") &&
                        binding.inputPassword.getText().toString().equals("")) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập tài khoản và mật khẩu !!!", Toast.LENGTH_SHORT).show();
                } else if (binding.inputEmail.getText().toString().equals("")) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập tài khoản !!!", Toast.LENGTH_SHORT).show();
                } else if (binding.inputPassword.getText().toString().equals("")) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập mật khẩu !!!", Toast.LENGTH_SHORT).show();
                } else {
                    sendDataLogin(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString());
                }
            }
        });
    }

    public void sendDataLogin(String username, String password) {
        hud = KProgressHUD.create(SignInActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = RetrofitClient.getClient()
                .create(ApiLogin.class)
                .sendData(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<InfoUser>>() {
                    @Override
                    public void accept(List<InfoUser> infoUsers) throws Exception {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int dem = 0;
                                for (InfoUser infoUser : infoUsers) {
                                    if (infoUser.getUsername().equals(binding.inputEmail.getText().toString()) &&
                                            infoUser.getPassword().equals(binding.inputPassword.getText().toString())) {

                                        StoreUtils.save(SignInActivity.this, Constants.username,infoUser.getUsername());
                                        StoreUtils.save(SignInActivity.this, Constants.telephone,infoUser.getTelephone());
                                        StoreUtils.save(SignInActivity.this, Constants.privateKeyFirst,infoUser.getPrivateKeyFirst());
                                        StoreUtils.save(SignInActivity.this, Constants.password,infoUser.getPassword());
                                        StoreUtils.save(SignInActivity.this, Constants.fullname,infoUser.getFullname());
                                        StoreUtils.save(SignInActivity.this, Constants.avatar,infoUser.getAvatar());

                                        Intent intent = new Intent(SignInActivity.this, MainMain.class);
                                        startActivity(intent);
//                                        SignInActivity.this.finish();
                                        break;
                                    } else {
                                        dem++;
                                    }
                                }

                                if (dem == infoUsers.size()) {
                                    Toast.makeText(SignInActivity.this, "Vui lòng nhập lại tài khoản mật khẩu !!!", Toast.LENGTH_SHORT).show();
                                }
                                if (hud.isShowing()) {
                                    hud.dismiss();
                                }
                            }
                        }, 2500);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hud.isShowing()) {
            hud.dismiss();
        }
    }
}