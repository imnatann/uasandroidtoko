package com.example.db_coffeetea;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.db_coffeetea.login_register.LoginPage;
import com.example.db_coffeetea.login_register.Register;


public class LandingPage extends AppCompatActivity {
    TextView signin;
    Button signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        signin = (TextView) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.button);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LandingPage.this, LoginPage.class);
                startActivity(loginIntent);
            }
        });


        signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Ketika tombol disentuh, atur warna latar belakang menjadi coklat dan warna teks menjadi putih
                        signup.setBackgroundColor(ContextCompat.getColor(LandingPage.this, R.color.coklat));
                        signup.setTextColor(ContextCompat.getColor(LandingPage.this, R.color.white));
                        break;
                    case MotionEvent.ACTION_UP:
                        // Ketika tombol dilepas, kembalikan warna latar belakang dan teks ke default
                        signup.setBackgroundColor(ContextCompat.getColor(LandingPage.this, R.color.white));
                        signup.setTextColor(ContextCompat.getColor(LandingPage.this, R.color.black));
                        // Handler untuk menunda pindah ke halaman Register
                       new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent loginIntent = new Intent(LandingPage.this, Register.class);
                                startActivity(loginIntent);
                            }
                        }, 30); // Delay dalam milidetik (contoh: 500ms)
                        break;
                }
                return false;
            }
        });


    }
}