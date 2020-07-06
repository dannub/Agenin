package com.agenin.id.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.agenin.id.DBQueries;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {
    private int splash = 3000;

    private FirebaseAuth firebaseAuth;


    private ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        connectivityManager =(ConnectivityManager) SplashScreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (networkInfo !=null && networkInfo.isConnected()==true) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    UserPreference userPreference = new UserPreference(SplashScreenActivity.this);
                    userPreference.setUserPreference("user",null);
                    if (currentUser != null) {

                            if(userPreference.getUserPreference("user")==null){
                                DBQueries.requestLogin(SplashScreenActivity.this,currentUser.getEmail(),currentUser.getUid(),null,true);
                            }else {
                                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }



                    } else {
                        Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                  //  MainActivity.showCart = false;
                    startActivity(mainIntent);
                    finish();
                }

            }
            public  void  finish(){
                SplashScreenActivity.this.finish();
            }
        },splash);


    }
}