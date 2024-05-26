package com.example.db_coffeetea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.db_coffeetea.DB.konfigurasi;

import org.json.JSONException;
import org.json.JSONObject;

public class ProcessPayment extends AppCompatActivity {

    private TextView transactionCodeTextView, orderTotalTextView, paymentMethodTextView;
    private String transactionCode;
    private Handler handler = new Handler();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_payment);

        // Initialize the TextViews
        transactionCodeTextView = findViewById(R.id.transactioncode);
        orderTotalTextView = findViewById(R.id.totalorder);
        paymentMethodTextView = findViewById(R.id.method);

        // Get data from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transactionCode = extras.getString("TRANSACTION_CODE");
            String orderTotal = extras.getString("ORDER_TOTAL");
            String paymentMethod = extras.getString("PAYMENT_METHOD");

            // Prepend '#' to the transaction code
            String formattedTransactionCode = "#" + transactionCode;

            // Set the data to the TextViews
            transactionCodeTextView.setText(formattedTransactionCode);
            orderTotalTextView.setText(orderTotal);
            paymentMethodTextView.setText(paymentMethod);
        }

        // Start checking the status
        handler.postDelayed(statusChecker, 5000); // Check every 5 seconds
    }

    private Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            checkPaymentStatus();
            handler.postDelayed(this, 5000); // Check every 5 seconds
        }
    };

    private void checkPaymentStatus() {
        String url = konfigurasi.URLCHECK + transactionCode;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("transactionCode") && jsonObject.has("status")) {
                                String newTransactionCode = jsonObject.getString("transactionCode");
                                String status = jsonObject.getString("status");

                                // Tambahkan log untuk debug
                                Log.d("ProcessPayment", "Response: " + response);
                                Log.d("ProcessPayment", "New Transaction Code: " + newTransactionCode);
                                Log.d("ProcessPayment", "Status: " + status);

                                // Tambahkan Toast untuk debug
                                Toast.makeText(ProcessPayment.this, "Transaction Code: " + newTransactionCode + ", Status: " + status, Toast.LENGTH_LONG).show();

                                if (!newTransactionCode.equals(transactionCode) || "Paid".equals(status)) {
                                    handler.removeCallbacks(statusChecker); // Stop checking
                                    navigateToSuccessPage(newTransactionCode);
                                } else {
                                    // Menampilkan "Menunggu Pembayaran" setiap kali ada respons dari server dan status bukan "Paid"
                                    Toast.makeText(ProcessPayment.this, "Menunggu Pembayaran", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("ProcessPayment", "Invalid JSON response format");
                                Toast.makeText(ProcessPayment.this, "Error parsing status", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProcessPayment.this, "Error parsing status", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ProcessPayment.this, "Menunggu Pembayaran", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void navigateToSuccessPage(String newTransactionCode) {
        // Simpan data order total dan metode pembayaran di SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Transaksi", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TRANSACTION_CODE", newTransactionCode);
        editor.putString("orderTotal", orderTotalTextView.getText().toString());
        editor.putString("selectedPaymentMethod", paymentMethodTextView.getText().toString());
        editor.apply();

        // Navigasi ke halaman SuccessPayment dengan membawa notransaksi terkini
        Intent intent = new Intent(ProcessPayment.this, SuccessPayment.class);
        intent.putExtra("TRANSACTION_CODE", newTransactionCode);
        startActivity(intent);

        // Tampilkan toast "Pembayaran Berhasil!"
        Toast.makeText(ProcessPayment.this, "Pembayaran Berhasil!", Toast.LENGTH_SHORT).show();

        // Menghapus semua data keranjang dan SharedPreferences "OrderPreferences"
        clearCart();
        clearOrderPreferences(this);

        finish();
    }

    private void clearCart() {
        // Implementasi untuk menghapus semua data keranjang
        // Misalnya, jika data keranjang disimpan dalam suatu list atau database lokal, hapus semua entri di sini.
    }

    private void clearOrderPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("OrderPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Menghapus semua data dari SharedPreferences
        editor.apply();
    }
}
