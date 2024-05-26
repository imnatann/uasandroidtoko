package com.example.db_coffeetea;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent untuk berpindah ke halaman lain (misalnya halaman Home)
                Intent intent = new Intent(MainActivity.this, LandingPage.class);
                startActivity(intent);
                finish(); // Optional, untuk menutup halaman saat ini
            }
        }, 1500); // Penundaan dalam milidetik (5000 ms = 5 detik)
    }


}