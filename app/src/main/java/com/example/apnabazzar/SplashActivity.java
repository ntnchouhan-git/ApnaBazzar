package com.example.apnabazzar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

   // private static  int SPLASH_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser  = firebaseAuth.getCurrentUser();

        if (currentUser == null)
        {
            Intent registerintent = new Intent(SplashActivity.this , RegisterActivity.class);
            startActivity(registerintent);
            finish();
        }
        else
        {
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).update("Last seen", FieldValue.serverTimestamp())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Intent mainintent = new Intent(SplashActivity.this , MainActivity.class);
                                startActivity(mainintent);
                                finish();

                            }else {
                                String error =  task.getException().getMessage();
                                Toast.makeText(SplashActivity.this,error,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}