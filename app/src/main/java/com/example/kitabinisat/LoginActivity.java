package com.example.kitabinisat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText loginUserEmail,loginUserPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById();

        if (firebaseUser != null) {
            Intent loginToMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(loginToMain);
            finish();
        }
    }

    public void loginButtonClick(View view) {
        String email = loginUserEmail.getText().toString().trim();
        String password = loginUserPassword.getText().toString().trim();

        System.out.println(email + " " + password);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent toMain = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(toMain);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginToSignupTextClick(View view) {
        Intent toSignup = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(toSignup);
    }

    public void findViewById(){
        loginUserEmail = findViewById(R.id.loginUserEmail);
        loginUserPassword = findViewById(R.id.loginUserPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }
}
