// Order.java
package com.example.db_coffeetea;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db_coffeetea.Checkout.CheckoutAdapter;
import com.example.db_coffeetea.cart.CartItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.List;

import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;

public class Order extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CheckoutAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private List<CartItem> cartItems;
    private TextView textViewOrderTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottonnav);
        textViewOrderTotal = findViewById(R.id.textViewOrderTotal);

        // Initialize cartItems from SharedPreferences
        cartItems = getCartItemsFromSharedPreferences();

        // Set up RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Initialize and set adapter for RecyclerView
        adapter = new CheckoutAdapter(cartItems, this, new CheckoutAdapter.QuantityChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateOrderTotal();
            }
        });
        recyclerView.setAdapter(adapter);

        // Set up Checkout button click listener
        Button buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Order.this, Payment.class);
                intent.putExtra("ORDER_TOTAL", textViewOrderTotal.getText().toString());
                startActivity(intent);
                // Proceed with checkout process
                // You can add logic to handle checkout here
            }
        });

        // Set "Dashboard" menu item as selected when Home activity starts
        bottomNavigationView.setSelectedItemId(R.id.users);

        // Set up bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(Order.this, Home.class));
                        return true;
                    case R.id.dashbord:
                        startActivity(new Intent(Order.this, MenuDrink.class));
                        return true;
                    case R.id.users:
                        return true;
                    case R.id.user:
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        // Initial total update
        updateOrderTotal();
    }

    private List<CartItem> getCartItemsFromSharedPreferences() {
        List<CartItem> cartItems = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);

        // Iterate through SharedPreferences keys to find items
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("item_")) {
                String[] itemData = entry.getValue().toString().split(",");
                if (itemData.length == 3) {
                    String itemName = itemData[0];
                    int itemPrice = Integer.parseInt(itemData[1]);
                    String itemImage = itemData[2];
                    cartItems.add(new CartItem(itemName, itemPrice, 1, itemImage)); // Assuming default quantity is 1
                }
            }
        }
        return cartItems;
    }

    private void updateNoItemsText() {
        TextView textNoItems = findViewById(R.id.textNoItems);
        if (cartItems.isEmpty()) {
            // Jika keranjang belanja kosong, tampilkan teks "No Item"
            textNoItems.setVisibility(View.VISIBLE);
        } else {
            // Jika keranjang belanja tidak kosong, sembunyikan teks "No Item"
            textNoItems.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartItems = getCartItemsFromSharedPreferences();
        adapter = new CheckoutAdapter(cartItems, this, new CheckoutAdapter.QuantityChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateOrderTotal();
                updateNoItemsText(); // Panggil metode untuk memperbarui teks "No Item"
                updateCheckoutButtonStatus(); // Perbarui status tombol checkout saat ada perubahan di keranjang belanja
            }
        });
        recyclerView.setAdapter(adapter);
        updateOrderTotal();
        updateNoItemsText();
        updateCheckoutButtonStatus(); // Panggil method untuk mengatur status tombol checkout saat aktivitas dimuat kembali
    }

    private void updateOrderTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTotal = decimalFormat.format(total);

        textViewOrderTotal.setText("Rp. " + formattedTotal);
    }


    private void updateCheckoutButtonStatus() {
        Button buttonCheckout = findViewById(R.id.buttonCheckout);
        if (cartItems.isEmpty()) {
            // Jika keranjang belanja kosong, nonaktifkan tombol checkout
            buttonCheckout.setEnabled(false);
        } else {
            // Jika keranjang belanja tidak kosong, aktifkan tombol checkout
            buttonCheckout.setEnabled(true);
        }
    }

}

