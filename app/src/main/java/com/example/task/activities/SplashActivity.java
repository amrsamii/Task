package com.example.task.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.task.storage.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
