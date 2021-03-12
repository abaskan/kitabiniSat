package com.example.kitabinisat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BookDetailActivity extends AppCompatActivity {
    private Toolbar bookDetailToolbar;
    private ImageView bookDetailImage, bookDetailAddFavorite;
    private TextView bookDetailPrice, bookDetailTitle, bookDetailTime, bookDetailViews, bookDetailFavorites, bookDetailDetails,toSellerProfileUserIcon,toSellerProfileUserName;
    private Button bookDetailToChatButton;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        findViewById();
        toolbar();
        getBookDetail();


    }

    public void getBookDetail (){
        final Book bookDetail = (Book) getIntent().getSerializableExtra("book");
        String value = getIntent().getStringExtra("value");
        if (value.matches("favori")){
            bookDetailAddFavorite.setImageResource(R.drawable.icon_delete_grey);

            bookDetailAddFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookDetailActivity.this);
                    alertDialog.setTitle("Ürün Favorilerimden Silinsin Mi ?");
                    alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                                    .collection("FavoriteBooks").document(bookDetail.getBookId()).delete();

                            Toast.makeText(BookDetailActivity.this, "Ürün favorilerinizden silindi", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.create().show();

                }
            });

        }

        else {
            bookDetailAddFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookDetailActivity.this);
                    alertDialog.setTitle("Ürün Favorilerime Eklensin Mi ?");
                    alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid())
                                    .collection("FavoriteBooks").document(bookDetail.getBookId()).set(bookDetail);

                            Toast.makeText(BookDetailActivity.this, "Ürün favorilerinize eklendi", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.create().show();
                }
            });
        }

        Picasso.get().load(bookDetail.getBookImageUrl()).into(bookDetailImage);
        bookDetailTitle.setText(bookDetail.getBookTitle());
        bookDetailPrice.setText(String.format("%s TL", bookDetail.getBookPrice()));
        bookDetailDetails.setText(bookDetail.getBookDetail());
        bookDetailFavorites.setText(String.valueOf(bookDetail.getFavorites()));
        bookDetailViews.setText(String.valueOf(bookDetail.getViews()));

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(bookDetail.getUserId());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String sellerName = documentSnapshot.getString("userName");
                toSellerProfileUserIcon.setText(sellerName.substring(0,1).toUpperCase());
                toSellerProfileUserName.setText(sellerName);
                Random random = new Random();
                final int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                ((GradientDrawable) toSellerProfileUserIcon.getBackground()).setColor(color);
            }
        });


        long addingTime = bookDetail.getAddingTime();
        long currentTime = System.currentTimeMillis();
        long time = (currentTime - addingTime) / 1000;

        if (time < 3600){
            bookDetailTime.setText(time / 60 + " dakika");
        }
        else if (time > 3600 && time < 86400) {
            bookDetailTime.setText(time / 3600 + " saat");
        }

        else {
            bookDetailTime.setText(time / 86400 + " gün");
        }
    }

    public void toolbar(){
        Book bookDetail = (Book) getIntent().getSerializableExtra("book");
        bookDetailToolbar.setTitle(bookDetail.getBookTitle());
        bookDetailToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        bookDetailToolbar.setTitleTextColor(Color.WHITE);
        bookDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById (){
        bookDetailToolbar = findViewById(R.id.bookDetailToolbar);
        bookDetailImage = findViewById(R.id.bookDetailImage);
        bookDetailAddFavorite = findViewById(R.id.bookDetailAddFavorite);
        bookDetailPrice = findViewById(R.id.bookDetailPrice);
        bookDetailTitle = findViewById(R.id.bookDetailTitle);
        bookDetailTime = findViewById(R.id.bookDetailTime);
        bookDetailViews = findViewById(R.id.bookDetailViews);
        bookDetailFavorites = findViewById(R.id.bookDetailFavorites);
        bookDetailDetails = findViewById(R.id.bookDetailDetails);
        bookDetailToChatButton = findViewById(R.id.bookDetailToChatButton);
        toSellerProfileUserIcon = findViewById(R.id.toSellerProfileUserIcon);
        toSellerProfileUserName = findViewById(R.id.toSellerProfileUserName);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void bookDetailToChatButtonClick(View view) {
        final Book bookDetail = (Book) getIntent().getSerializableExtra("book");
        final Map<String, Object> newChat = new HashMap<>();
        final Map<String,Object> newChat2 = new HashMap<>();
        newChat.put("sellerId",bookDetail.getUserId());
        newChat.put("userId",firebaseAuth.getUid());
        newChat.put("date",getDate());

        newChat2.put("userId",bookDetail.getUserId());
        newChat2.put("sellerId",firebaseAuth.getUid());
        newChat2.put("date",getDate());

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(bookDetail.getUserId());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String sellerName = documentSnapshot.getString("userName");
                newChat.put("sellerName",sellerName);
                newChat2.put("userName",sellerName);

                DocumentReference documentReference2 = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
                documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String userName = (String) documentSnapshot.getString("userName");
                        newChat.put("userName",userName);
                        newChat2.put("sellerName",userName);
                        firebaseFirestore.collection("UserFields").document(firebaseAuth.getUid()).collection("Chats")
                                .document(bookDetail.getUserId()).set(newChat);
                        firebaseFirestore.collection("UserFields").document(bookDetail.getUserId()).collection("Chats")
                                .document(firebaseAuth.getUid()).set(newChat2);
                    }
                });


                Intent toChat = new Intent(BookDetailActivity.this,ChatActivity.class);
                toChat.putExtra("sellerId",bookDetail.getUserId());
                toChat.putExtra("sellerName", sellerName);
                toChat.putExtra("userId",firebaseAuth.getUid());

                startActivity(toChat);
                finish();
            }
        });


    }

    public void bookDetailAddFavoriteClick(View view) {

    }


    public String getDate (){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = dateFormat.format(today);
        return date;
    }

    public void toSellerProfileCardClick(View view) {
        Book bookDetail = (Book) getIntent().getSerializableExtra("book");
        Intent toSeller = new Intent(BookDetailActivity.this,SellerProfileActivity.class);
        toSeller.putExtra("sellerId",bookDetail.getUserId());
        startActivity(toSeller);

    }
}