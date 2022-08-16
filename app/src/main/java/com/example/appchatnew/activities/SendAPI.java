package com.example.appchatnew.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appchatnew.R;
import com.example.appchatnew.utilities.RealPathUtil;
import com.example.appchatnew.api.ApiService;
import com.example.appchatnew.api.Const;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendAPI extends AppCompatActivity {

    private static final int PHUCDEPTRAI = 1306;

    private static final String TAG = SendAPI.class.getName();
    private EditText username, password;
    private ImageView imgFromGal, imgFromAPI;
    private Button btnSelectImage, btnUploadImage;
    private TextView tvUsername, tvPassword;

    private Uri mUri;
    private ProgressDialog mProgressDialog;


    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            imgFromGal.setImageBitmap(bitmap);

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_a_p_i);

        anhxa();

        mProgressDialog = new  ProgressDialog(this);
        mProgressDialog.setMessage("Wait...");


        btnSelectImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickRequest();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUri != null){
                    callApiRegisterAcc();
                }
            }
        });
    }

    private void callApiRegisterAcc() {
        mProgressDialog.show();
        String strUsername = username.getText().toString().trim();
        String strPassword = password.getText().toString().trim();
        RequestBody requestBodyUsername = RequestBody.create(MediaType.parse("multipart/form-data"), strUsername);
        RequestBody requestBodyPassword = RequestBody.create(MediaType.parse("multipart/form-data"), strPassword);

        String strRealPath = RealPathUtil.getRealPath(this, mUri);
        Log.e("Phuc", strRealPath);
        File file = new File(strRealPath);

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part multipartBodyAvt = MultipartBody.Part.createFormData(Const.KEY_AVT, file.getName(), reqBody);

        ApiService.apiService.registerAccount(requestBodyUsername,requestBodyPassword,multipartBodyAvt).enqueue(new Callback<User>() {
                                                                                                                            @Override
                                                                                                                            public void onResponse(Call<User> call, Response<User> response) {
                                                                                                                                   mProgressDialog.dismiss();
                                                                                                                                   User user = response.body();
                                                                                                                                   if(user != null){
                                                                                                                                       tvUsername.setText(user.getUsername());
                                                                                                                                       tvPassword.setText(user.getPassword());
                                                                                                                                       Glide.with(SendAPI.this).load(user.getAvt()).into(imgFromAPI);
                                                                                                                                   }
                                                                                                                            }

                                                                                                                            @Override
                                                                                                                            public void onFailure(Call<User> call, Throwable t) {
                                                                                                                                    mProgressDialog.dismiss();
                                                                                                                                    Toast.makeText(SendAPI.this, "FAIL", Toast.LENGTH_SHORT).show();
                                                                                                                            }
                                                                                                                        });
    }

    private void onClickRequest() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else {
            String []  permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,PHUCDEPTRAI);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PHUCDEPTRAI) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));

    }

    private void anhxa() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        imgFromGal = findViewById(R.id.img_from_gallery);
        imgFromAPI = findViewById(R.id.img_from_api);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnUploadImage = findViewById(R.id.btn_upload_image);
        tvUsername = findViewById(R.id.tv_username);
        tvPassword = findViewById(R.id.tv_password);
    }
}