package com.ogungor.Instaclonefirebase;

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

public class SingUpActivity extends AppCompatActivity {

    EditText emailText, passText;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        emailText = findViewById(R.id.emailText);
        passText = findViewById(R.id.passText);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(SingUpActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();

        }
    }

    public void signinclicked(View view) {

        String email = emailText.getText().toString();
        String pass = passText.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(SingUpActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SingUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();


            }
        });


    }

    public void signupclicked(View view) {

        String email = emailText.getText().toString();
        String pass = passText.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SingUpActivity.this, "Created user", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(SingUpActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
}