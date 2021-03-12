package com.example.kitabinisat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {
    private Toolbar userInfoToolbar;
    private EditText userPersonalInfoName, userPersonalInfoAddress, userPersonalInfoPhone;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String selectedProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        findViewById();
        toolbar();
        getCurrentUserPersonalInfo();
    }

    public void userPersonalInfoButtonClick(View view) {
        String name = userPersonalInfoName.getText().toString().trim();
        String address = userPersonalInfoAddress.getText().toString().trim();
        String phone = userPersonalInfoPhone.getText().toString().trim();
        Map<String, Object> currentUser = new HashMap<>();
        currentUser.put("userName", name);
        currentUser.put("userProvince", address);
        currentUser.put("userPhone",phone);

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                .update(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UserInfoActivity.this, "Bilgileriniz Kaydedildi", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserInfoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void findViewById() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userPersonalInfoName = findViewById(R.id.userPersonalInfoName);
        userPersonalInfoAddress = findViewById(R.id.userPersonalInfoAddress);
        userPersonalInfoPhone = findViewById(R.id.userPersonalInfoPhone);
    }

    public void toolbar() {
        userInfoToolbar = findViewById(R.id.userInfoToolbar);
        userInfoToolbar.setTitle("Bilgilerim");
        userInfoToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(userInfoToolbar);
        userInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getCurrentUserPersonalInfo() {

        final DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
                userPersonalInfoName.setText(currentUser.getUserName());
                userPersonalInfoAddress.setText(currentUser.getUserAddress());
                userPersonalInfoPhone.setText(currentUser.getUserPhone());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserInfoActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void locationClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
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
                userPersonalInfoAddress.setText(selectedProvince);
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