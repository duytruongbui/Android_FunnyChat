package com.example.appchatnew.activities.Chat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appchatnew.R;
import com.example.appchatnew.SendDataMessage;
import com.example.appchatnew.activities.SignInUp.SignInActivity;
import com.example.appchatnew.activities.SignInUp.SignUpActivity;
import com.example.appchatnew.adapter.AdapterListMessage;
import com.example.appchatnew.api.ApiDeleteMessage;
import com.example.appchatnew.api.ApiGetListChat;
import com.example.appchatnew.api.ApiLogin;
import com.example.appchatnew.api.ApiRegister;
import com.example.appchatnew.api.ApiSendMessage;
import com.example.appchatnew.api.RetrofitClient;

import com.example.appchatnew.databinding.ActivityChatBinding;
import com.example.appchatnew.model.response.ChatResponse;
import com.example.appchatnew.model.response.InfoUser;
import com.example.appchatnew.model.response.ResponseBody;
import com.example.appchatnew.utilities.Constants;
import com.example.appchatnew.utilities.Extensions;
import com.example.appchatnew.utilities.StoreUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityChatBinding binding;
    InfoUser infoUser;
    GoogleMap map;
    FusedLocationProviderClient client;
    String locationString = "";

    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    public static final int REQUEST_CODE_CAMERA = 456;
    public static final int SELECT_PICTURE = 123;

    private static final int REQUEST_PHOTO_GALLERY = 100;
    private static final int REQUEST_CAPTURE_IMAGE = 110;
    Uri uriImage;
    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check) {
                    binding.linearLayoutAdd.setVisibility(View.VISIBLE);
                    check = false;
                } else {
                    binding.linearLayoutAdd.setVisibility(View.GONE);
                    check = true;
                }
            }
        });

        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            infoUser = (InfoUser) intent.getSerializableExtra("user");
            binding.textName.setText(infoUser.getUsername());
            StoreUtils.save(ChatActivity.this, Constants.customer, infoUser.getAvatar());

            getListChat();
        }

        binding.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        binding.imageAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.linearLayoutAdd.setVisibility(View.VISIBLE);
                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_PICTURE);
            }
        });

        binding.imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.linearLayoutAdd.setVisibility(View.VISIBLE);
                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
            }
        });

        binding.imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatActivity.this, "Chức năng này chưa hoàn thiện !!!", Toast.LENGTH_SHORT).show();
            }
        });

        this.client = LocationServices.getFusedLocationProviderClient((Activity) this);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 44);
        }

    }

    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }


    public void sendMessage() {

        Date dte = new Date();
        long milliSeconds = dte.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Log.i("TAG", "sendMessage: " + simpleDateFormat.format(dte.getTime()));

        String key = simpleDateFormat.format(dte.getTime());

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = RetrofitClient.getClient()
                .create(ApiSendMessage.class)
                .sendMessage(StoreUtils.get(ChatActivity.this, Constants.username), infoUser.getUsername(),
                        binding.edtTextSend.getText().toString(), "1234564455", locationString, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if (responseBody.getStatus().equals("success")) {
                            getListChat();
                            binding.edtTextSend.setText("");
                            Log.i("TAG", "accept: " + responseBody.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            public void onSuccess(final Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i("Send", "onMapReady: " + location.getLatitude() + "---" + location.getLongitude());
                locationString = (String.valueOf(location.getLatitude()) + "" + String.valueOf(location.getLongitude()));
            }
        });
    }

    Uri mPhotoUri;

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44 && grantResults.length > 0 && grantResults[0] == 0) {
            getCurrentLocation();
        }

        if (requestCode == REQUEST_CODE_CAMERA) {
            String filename = System.currentTimeMillis() + ".jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, filename);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            Bundle bundleOptions = new Bundle();
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE, bundleOptions);
        }

        if (requestCode == SELECT_PICTURE) {
            callGallery();
        }
    }

    public void getListChat() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = RetrofitClient.getClient()
                .create(ApiGetListChat.class)
                .getListChat(StoreUtils.get(ChatActivity.this, Constants.username), infoUser.getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<ChatResponse>>() {
                    @Override
                    public void accept(List<ChatResponse> chatResponses) throws Exception {
                        AdapterListMessage adapterListMessage = new AdapterListMessage(getApplicationContext(), chatResponses, new SendDataMessage() {
                            @Override
                            public void sendData(ChatResponse chatResponse) {
                                delete(chatResponse);
                            }
                        });
                        binding.chatRecyclerView.setHasFixedSize(true);
                        binding.chatRecyclerView.setAdapter(adapterListMessage);
                        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void delete(ChatResponse chatResponse) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xóa không ?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        Disposable disposable = RetrofitClient.getClient()
                                .create(ApiDeleteMessage.class)
                                .deleteMessage(StoreUtils.get(ChatActivity.this, Constants.username), infoUser.getUsername(), chatResponse.getTime())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.newThread())
                                .subscribe(new Consumer<ResponseBody>() {
                                    @Override
                                    public void accept(ResponseBody responseBody) throws Exception {
                                        if (responseBody.getStatus().equals("success")) {
                                            getListChat();
                                        }
                                    }
                                });
                        compositeDisposable.add(disposable);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            uriImage = data.getData();
            uploadToFirebase(uriImage);
            Log.i("Image", uriImage.toString());
        } else if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            final boolean existsData = data != null && data.getData() != null;
            Uri uri = existsData ? data.getData() : mPhotoUri;
            uploadToFirebase(uri);
        }
    }

    private void uploadToFirebase(Uri uri) {
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Date dte = new Date();
                        long milliSeconds = dte.getTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
                        Log.i("TAG", "sendMessage: " + simpleDateFormat.format(dte.getTime()));
                        String key = simpleDateFormat.format(dte.getTime());

                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        Disposable disposable = RetrofitClient.getClient()
                                .create(ApiSendMessage.class)
                                .sendMessage(StoreUtils.get(ChatActivity.this, Constants.username), infoUser.getUsername(),
                                        uri.toString(), "1234564455", locationString, key)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.newThread())
                                .subscribe(new Consumer<ResponseBody>() {
                                    @Override
                                    public void accept(ResponseBody responseBody) throws Exception {
                                        if (responseBody.getStatus().equals("success")) {
                                            getListChat();
                                            Log.i("TAG", "accept: " + responseBody.getMessage());
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                    }
                                });
                        compositeDisposable.add(disposable);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }


}