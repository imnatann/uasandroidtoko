package com.example.db_coffeetea;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottonnav);

        // Set "Dashboard" menu item as selected when Home activity starts
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Set listener for BottomNavigationView item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                switch (item.getItemId()) {
                    case R.id.home:
                        updateSelectedMenuItem(item);
                        return true;
                    case R.id.dashbord:
                        startActivity(new Intent(Home.this, MenuDrink.class));
                        return true;
                    case R.id.users:
                        startActivity(new Intent(Home.this, Order.class));
                        return true;
                    case R.id.user:
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        // Terima data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra("username");
            if (username != null) {
                // Gunakan username yang diterima di sini
                Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                // Misalnya, tampilkan username di TextView
                TextView textViewUsername = findViewById(R.id.textViewUsername);
                textViewUsername.setText("Hello " +username+", ");
            }


        }

    }

    private void updateSelectedMenuItem(MenuItem selectedItem) {
        // Set selected item for BottomNavigationView
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.dashbord).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.users).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.user).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(false);
    }
}