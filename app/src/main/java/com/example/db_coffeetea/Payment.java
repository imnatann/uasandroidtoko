package com.example.db_coffeetea;

import static com.example.db_coffeetea.Checkout.CheckoutAdapter.checkoutItems;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.db_coffeetea.DB.konfigurasi;
import com.example.db_coffeetea.cart.CartItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment extends AppCompatActivity {

    private CardView cardCreditCard, cardPaypal, cardIndomaret, cardAlfamart;
    private RadioGroup radioGroupPaymentMethod;
    private RadioButton credit, paypal, indomaret, alfamart;
    private TextView tax, orderTotal;
    private Button proceedButton;
    private String selectedPaymentMethod = "";
    private int previousTaxAmount = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize ImageView and load images using Glide
        ImageView credits = findViewById(R.id.cc);
        String card = "https://i.pinimg.com/564x/bb/2d/3d/bb2d3d28e3421d901d8d431c33f0c6ed.jpg";

        ImageView pay = findViewById(R.id.pp);
        String pal = "https://i.pinimg.com/564x/90/8a/72/908a727f0ac89e9d67d1bc71e3a673b5.jpg";

        ImageView indo = findViewById(R.id.im);
        String maret = "https://i.pinimg.com/564x/29/a0/73/29a073b7a1815ef0f6b9a38f480baf9c.jpg";

        ImageView alfa = findViewById(R.id.am);
        String mart = "https://i.pinimg.com/736x/b7/01/d8/b701d8734c39c7ed953aec9f5b67c6b1.jpg";

        Glide.with(this).load(card).into(credits);
        Glide.with(this).load(pal).into(pay);
        Glide.with(this).load(maret).into(indo);
        Glide.with(this).load(mart).into(alfa);

        // Initialize CardView and RadioButton
        cardCreditCard = findViewById(R.id.cardCreditCard);
        cardPaypal = findViewById(R.id.cardPaypal);
        cardIndomaret = findViewById(R.id.cardIndomaret);
        cardAlfamart = findViewById(R.id.cardAlfamart);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
        tax = findViewById(R.id.tax);
        orderTotal = findViewById(R.id.textViewOrderTotal);
        proceedButton = findViewById(R.id.proceedButton);

        credit = findViewById(R.id.credit);
        paypal = findViewById(R.id.paypal);
        indomaret = findViewById(R.id.indomaret);
        alfamart = findViewById(R.id.Alfamart);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ORDER_TOTAL")) {
            String orderTotalStr = intent.getStringExtra("ORDER_TOTAL");
            orderTotal.setText(orderTotalStr);
        }

        // Set RadioGroup check change listener
        radioGroupPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            resetCardBorders();
            switch (checkedId) {
                case R.id.credit:
                    cardCreditCard.setBackgroundResource(R.drawable.card_border); // Set background with border for credit card
                    tax.setText("Rp. 10.000"); // Set tax text for credit card payment
                    selectedPaymentMethod = "Credit Card";
                    updateTax(10000);
                    break;
                case R.id.paypal:
                    cardPaypal.setBackgroundResource(R.drawable.card_border); // Set background with border for paypal
                    tax.setText("Rp. 15.000"); // Set tax text for paypal payment
                    selectedPaymentMethod = "Paypal";
                    updateTax(15000);
                    break;
                case R.id.indomaret:
                    cardIndomaret.setBackgroundResource(R.drawable.card_border); // Set background with border for indomaret
                    tax.setText("Rp. 5.000"); // Set tax text for indomaret payment
                    selectedPaymentMethod = "Indomaret";
                    updateTax(5000);
                    break;
                case R.id.Alfamart:
                    cardAlfamart.setBackgroundResource(R.drawable.card_border); // Set background with border for alfamart
                    tax.setText("Rp. 8.000"); // Set tax text for alfamart payment
                    selectedPaymentMethod = "Alfamart";
                    updateTax(8000);
                    break;
            }
        });

        proceedButton.setOnClickListener(v -> {
            if (selectedPaymentMethod.isEmpty()) {
                Toast.makeText(Payment.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else {

                // Generate transaction code
                String transactionCode = generateTransactionCode();

                // Send data to the server
                sendDataToServer(transactionCode);

                // Proceed to the next activity
                Intent processIntent = new Intent(Payment.this, ProcessPayment.class);
                processIntent.putExtra("TRANSACTION_CODE", transactionCode);
                processIntent.putExtra("ORDER_TOTAL", orderTotal.getText().toString());
                processIntent.putExtra("PAYMENT_METHOD", selectedPaymentMethod);
                startActivity(processIntent);
            }
        });

    }

    private void resetCardBorders() {
        cardCreditCard.setBackground(null); // Reset background for credit card
        cardPaypal.setBackground(null); // Reset background for paypal
        cardIndomaret.setBackground(null); // Reset background for indomaret
        cardAlfamart.setBackground(null); // Reset background for alfamart
    }

    private String generateTransactionCode() {
        return "P" + System.currentTimeMillis();
    }

    // Payment Activity

    private void sendDataToServer(String transactionCode) {
        String orderTotalStr = orderTotal.getText().toString();
        String paymentMethod = selectedPaymentMethod;
        String status = "Pending"; // Default status
        String orderItemsJson = getOrderItemsJsonFromCart(); // Retrieve order items from cart
        String taxAmount = tax.getText().toString().replace("Rp. ", "").replace(".", ""); // Get the tax amount

        // Log untuk memeriksa data sebelum dikirim
        Log.d("SendDataToServer", "Transaction Code: " + transactionCode);
        Log.d("SendDataToServer", "Order Total: " + orderTotalStr);
        Log.d("SendDataToServer", "Payment Method: " + paymentMethod);
        Log.d("SendDataToServer", "Order Items: " + orderItemsJson); // <-- Pastikan ini berisi data yang benar
        Log.d("SendDataToServer", "Tax Amount: " + taxAmount);
        Log.d("SendDataToServer", "Status: " + status);

        String url = konfigurasi.URL_ADD;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle response from the server
                    Toast.makeText(Payment.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error
                    Toast.makeText(Payment.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("transactionCode", transactionCode);
                params.put("orderTotal", orderTotalStr);
                params.put("paymentMethod", paymentMethod);
                params.put("orderItems", orderItemsJson); // Kirim sebagai string JSON
                params.put("tax", taxAmount); // Sertakan nilai pajak dalam params
                params.put("status", status);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private String getOrderItemsJsonFromCart() {
        List<Map<String, String>> orderItemsList = new ArrayList<>();
        for (CartItem item : checkoutItems) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("name", item.getItemName());
            itemMap.put("price", String.valueOf(item.getPrice()));
            itemMap.put("quantity", String.valueOf(item.getQuantity()));
            itemMap.put("image", item.getItemImage()); // Path gambar
            orderItemsList.add(itemMap);
        }
        return new Gson().toJson(orderItemsList); // Mengubah list ke JSON string
    }

    private void updateTotalWithTax(int taxAmount) {
        String orderTotalStr = orderTotal.getText().toString().replace("Rp. ", "").replace(".", "");
        int orderTotalInt = Integer.parseInt(orderTotalStr);
        int totalWithTax = orderTotalInt + taxAmount;
        orderTotal.setText("Rp. " + String.format("%,d", totalWithTax).replace(",", "."));
    }

    private void updateTax(int newTaxAmount) {
        int difference = newTaxAmount - previousTaxAmount;
        updateTotalWithTax(difference);
        previousTaxAmount = newTaxAmount;
    }
}
