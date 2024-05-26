package com.example.db_coffeetea;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class SuccessPayment extends AppCompatActivity {

    private TextView textViewOrderTotal;
    private TextView textViewPaymentMethod;
    private TextView transactionCodeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_payment);

        textViewOrderTotal = findViewById(R.id.promo); // Ganti dengan id TextView order total di layout
        textViewPaymentMethod = findViewById(R.id.promos); // Ganti dengan id TextView metode pembayaran di layout
        transactionCodeTextView = findViewById(R.id.transactioncode);

        // Ambil data dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Transaksi", MODE_PRIVATE);
        String orderTotal = preferences.getString("orderTotal", "0");
        String selectedPaymentMethod = preferences.getString("selectedPaymentMethod", "");
        String transactionCode = preferences.getString("TRANSACTION_CODE", "");

        // Tambahkan "#" di depan nomor transaksi
//        String formattedTransactionCode = "#" + transactionCode;

        String formattedTransactionCode = "No Transaksi : #P0001";

        // Tampilkan order total, metode pembayaran, dan nomor transaksi ke dalam TextView
        textViewOrderTotal.setText(orderTotal);
        textViewPaymentMethod.setText(selectedPaymentMethod);
        transactionCodeTextView.setText(formattedTransactionCode);
    }
}