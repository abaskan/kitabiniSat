package com.example.kitabinisat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class SellingDetailActivity extends AppCompatActivity {
    private Toolbar sellingDetailToolbar;
    private ImageView sellingDetailImage;
    private TextView sellingDetailPrice, sellingDetailTitle, sellingDetailTime, sellingDetailViews, sellingDetailFavorites, sellingDetailDetails;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_detail);
        findViewById();
        toolbar();
        getsellingDetail();
    }

    public void getsellingDetail (){
        Book sellingDetail = (Book) getIntent().getSerializableExtra("book");
        Picasso.get().load(sellingDetail.getBookImageUrl()).into(sellingDetailImage);
        sellingDetailTitle.setText(sellingDetail.getBookTitle());
        sellingDetailPrice.setText(String.format("%s TL", sellingDetail.getBookPrice()));
        sellingDetailDetails.setText(sellingDetail.getBookDetail());


    }

    public void toolbar(){
        sellingDetailToolbar.setTitle("Ürün Detayları");
        sellingDetailToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        sellingDetailToolbar.setTitleTextColor(Color.WHITE);
        sellingDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById (){
        sellingDetailToolbar = findViewById(R.id.sellingDetailToolbar);
        sellingDetailImage = findViewById(R.id.sellingDetailImage);
        sellingDetailPrice = findViewById(R.id.sellingDetailPrice);
        sellingDetailTitle = findViewById(R.id.sellingDetailTitle);
        sellingDetailTime = findViewById(R.id.sellingDetailTime);
        sellingDetailViews = findViewById(R.id.sellingDetailViews);
        sellingDetailFavorites = findViewById(R.id.sellingDetailFavorites);
        sellingDetailDetails = findViewById(R.id.sellingDetailDetails);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }
}