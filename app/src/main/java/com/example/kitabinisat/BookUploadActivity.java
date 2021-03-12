package com.example.kitabinisat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookUploadActivity extends AppCompatActivity {
    private Toolbar bookUploadToolbar;
    private ImageView bookUploadImage;
    private EditText bookUploadTitle, bookUploadPrice, bookUploadDetail,bookUploadLocation;
    private Button bookUploadButton;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    Uri imageData;
    Bitmap selectedImage;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference,getPhotoFromStorage;
    private String selectedProvince;

    UUID uuid = UUID.randomUUID();
    String bookId = String.valueOf(uuid);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_upload);
        findViewById();
        toolbar();

    }

    public void bookUploadButtonClick(View view) {

        String bookTitle = bookUploadTitle.getText().toString().trim();
        String bookDetail = bookUploadDetail.getText().toString().trim();
        double bookPrice = Double.parseDouble(bookUploadPrice.getText().toString().trim());
        String location = bookUploadLocation.getText().toString().trim();

        long addingTime = System.currentTimeMillis();

        Book newBook = new Book(bookId,firebaseAuth.getUid(),bookTitle,"null",bookDetail,
                location,bookPrice,addingTime,0,0);

        firebaseFirestore.collection("UserFields")
                .document(firebaseAuth.getUid()).collection("SellingBook")
                .document(bookId).set(newBook);
        firebaseFirestore.collection("Books").document(bookId).set(newBook)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BookUploadActivity.this, "Kitabınız Eklendi.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookUploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        addBookPhoto();
    }

    public void toolbar(){
        bookUploadToolbar.setTitle("Kitap Ekle");
        bookUploadToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        bookUploadToolbar.setTitleTextColor(Color.WHITE);
        bookUploadToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        bookUploadToolbar = findViewById(R.id.bookUploadToolbar);
        bookUploadImage = findViewById(R.id.bookUploadImage);
        bookUploadTitle = findViewById(R.id.bookUploadTitle);
        bookUploadPrice = findViewById(R.id.bookUploadPrice);
        bookUploadDetail = findViewById(R.id.bookUploadDetail);
        bookUploadButton = findViewById(R.id.bookUploadButton);
        bookUploadLocation = findViewById(R.id.bookUploadLocation);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        getPhotoFromStorage = firebaseStorage.getReference();
    }


    public void addBookPhoto(){
        if(imageData != null){

            UUID uuid = UUID.randomUUID();
            final String imageName = "images" + uuid + ".jpg";

            storageReference.child("bookImages").child(firebaseAuth.getUid()).child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    getPhotoFromStorage = getPhotoFromStorage.child("bookImages").child(firebaseAuth.getUid()).child(imageName);
                    getPhotoFromStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            Map Url = new HashMap();
                            Url.put("bookImageUrl",downloadUrl);
                            System.out.println(downloadUrl);
                            firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                                    .collection("SellingBook").document(bookId)
                                    .update(Url);

                            firebaseFirestore.collection("Books").document(bookId).update(Url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookUploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BookUploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    public void selectedImageClick(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
            ,1);
        }else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData = data.getData();

            try {

                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    bookUploadImage.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    bookUploadImage.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void locationClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BookUploadActivity.this);
        builder.setTitle("Şehir Seçiniz")
                .setSingleChoiceItems(R.array.provinces, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] provinces = getResources().getStringArray(R.array.provinces);
                        selectedProvince = provinces[i];
                    }
                });

        builder.setCancelable(false);

        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bookUploadLocation.setText(selectedProvince);
            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}