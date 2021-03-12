package com.example.kitabinisat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    private EditText signupUserName,signupUserEmail,signupUserPassword, signupUserAddress;
    private FirebaseAuth firebaseAuth;
    private String selectedProvince;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById();
    }

    public void signupButtonClick(View view) {
        String email = signupUserEmail.getText().toString().trim();
        String password = signupUserPassword.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                userInfoToFirebase();
                Intent toMain = new Intent(SignupActivity.this,MainActivity.class);
                startActivity(toMain);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void userInfoToFirebase(){
        String userName = signupUserName.getText().toString().trim();
        String email = signupUserEmail.getText().toString().trim();
        String address = signupUserAddress.getText().toString().trim();

        User newUser = new User(firebaseAuth.getUid(),userName,"null"
                ,email,address,"null",
                System.currentTimeMillis());

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid())
                .set(newUser);
    }

    public void signupToLoginTextClick(View view) {
        Intent toLogin = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(toLogin);
    }

    public void findViewById(){
        signupUserName = findViewById(R.id.signupUserName);
        signupUserEmail = findViewById(R.id.signupUserEmail);
        signupUserPassword = findViewById(R.id.signupUserPassword);
        signupUserAddress = findViewById(R.id.signupUserAddress);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void locationClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
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
                signupUserAddress.setText(selectedProvince);
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