package com.example.appchatnew.activities.SignInUp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.appchatnew.api.ApiRegister;
import com.example.appchatnew.api.RetrofitClient;
import com.example.appchatnew.databinding.ActivitySignUpBinding;
import com.example.appchatnew.model.response.ResponseBody;
import com.example.appchatnew.utilities.Extensions;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private String encodedImage;
    Uri uriImage;
    KProgressHUD hud;
    public static final int REQUEST_CODE_CAMERA = 456;
    public static final int SELECT_PICTURE = 123;
    private static final int REQUEST_PHOTO_GALLERY = 100;
    private static final int REQUEST_CAPTURE_IMAGE = 110;

    private StorageReference reference = FirebaseStorage.getInstance().getReference();


    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

    }

    private void setListeners() {
        binding.textSginIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            binding.buttonSignUp.setEnabled(false);
            binding.buttonSignUp.setClickable(false);
            uploadToFirebase(uriImage);
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChooseImage();
            }
        });

    }

    private void uploadToFirebase(Uri uri) {
        hud = KProgressHUD.create(SignUpActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Date dte=new Date();
                                    long milliSeconds = dte.getTime();
                                    String key = Long.toString(milliSeconds);

                                    HashMap<String,String> hashMap = new HashMap<>();
                                    hashMap.put("fullname",binding.inputFullName.getText().toString());
                                    hashMap.put("username", binding.inputUsername.getText().toString());
                                    hashMap.put("password", binding.inputPassword.getText().toString());
                                    hashMap.put("telephone", binding.inputPhone.getText().toString());
                                    hashMap.put("privateKeyFirst", key);
                                    hashMap.put("avatar",uri.toString());

                                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                                    Disposable disposable = RetrofitClient.getClient()
                                            .create(ApiRegister.class)
                                            .registerUser(hashMap)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.newThread())
                                            .subscribe(new Consumer<ResponseBody>() {
                                                @Override
                                                public void accept(ResponseBody responseBody) throws Exception {
                                                    binding.buttonSignUp.setEnabled(false);
                                                    binding.buttonSignUp.setClickable(false);
                                                    hud.dismiss();
                                                    Toast.makeText(SignUpActivity.this, responseBody.getStatus(), Toast.LENGTH_SHORT).show();
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    throwable.printStackTrace();
                                                    binding.buttonSignUp.setEnabled(false);
                                                    binding.buttonSignUp.setClickable(false);
                                                    hud.dismiss();
                                                }
                                            });
                                    compositeDisposable.add(disposable);

                                }
                            },2500);

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


    protected Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth,
                                                int reqHeight, ContentResolver contentResolver) throws IOException {
        InputStream in = contentResolver.openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int width = options.outWidth / reqWidth + 1;
        int height = options.outHeight / reqHeight + 1;
        options.inSampleSize = Math.max(width, height);
        options.inJustDecodeBounds = false;
        in = contentResolver.openInputStream(uri);
        Bitmap tmpImg = BitmapFactory.decodeStream(in, null, options);
        in.close();
        return tmpImg;
    }

    public void dialogChooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        ArrayList<String> list = new ArrayList<String>();

        list.add("Máy ảnh");
        list.add("Album");

        CharSequence[] items = list.toArray(new CharSequence[list.size()]);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
                        break;
                    case 1:
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_PICTURE);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO_GALLERY);
    }

    protected String getPath(ContentResolver contentResolver, Uri uri) {
        String[] columns = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
            } else if (requestCode == SELECT_PICTURE) {
                callGallery();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private Uri mPhotoUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path = null;
        Bitmap tmpImg = null;
        File cache = null;
        if (requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                cache = Extensions.cache(this, in);
                path = cache.getAbsolutePath();

            } catch (FileNotFoundException e) {
                throw new AssertionError();
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                tmpImg = this.decodeSampledBitmapFromUri(data.getData(), bitmap.getWidth(), bitmap.getHeight(), getContentResolver());
                uriImage = data.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            final boolean existsData = data != null && data.getData() != null;
            Uri uri = existsData ? data.getData() : mPhotoUri;
            uriImage = uri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                tmpImg = this.decodeSampledBitmapFromUri(uri, bitmap.getWidth(), bitmap.getHeight(), getContentResolver());
            } catch (IOException e) {
                e.printStackTrace();
            }
            path = this.getPath(getContentResolver(), uri);
        }
        if (path == null) {
            return;
        }

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exif == null && tmpImg == null) {
            return;
        }

        Matrix mat = new Matrix();
        String exifDate = null;
        if (exif != null) {
            exifDate = exif.getAttribute(ExifInterface.TAG_DATETIME);
            mat = Extensions.asMatrixByOrientation(exif);
        }

        Bitmap img = null;
        try {
            img = Bitmap.createBitmap(tmpImg, 0, 0, tmpImg.getWidth(), tmpImg.getHeight(), mat, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (img.getConfig() != null) {
            switch (img.getConfig()) {
                case RGBA_F16:
                    img = img.copy(Bitmap.Config.ARGB_8888, true);
                    break;
            }
        }
        try {
            binding.imageProfile.setImageBitmap(img);
            bitmap = img;


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cache != null) ((File) cache).delete();
    }
}