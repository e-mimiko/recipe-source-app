package com.mimiko.recipesourceapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mimiko.recipesourceapp.R;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

         user = FirebaseAuth.getInstance().getCurrentUser();
    }

    //Checks if user is signed in or not

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null){
                    //signed in, go to main activity
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }else{
                    //not logged in. Go to login
                    startActivity(new Intent(SplashScreenActivity.this, LoginCreateAccountActivity.class));
                }
                finish();
            }
        },1000);//delay for a second
    }
}