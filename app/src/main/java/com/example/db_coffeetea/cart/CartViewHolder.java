package com.example.db_coffeetea.cart;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db_coffeetea.R;

public class CartViewHolder extends RecyclerView.ViewHolder {
    public TextView buttonHapus;
    private TextView itemNameTextView;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
        buttonHapus = itemView.findViewById(R.id.buttonHapus); // Inisialisasi buttonHapus

    }

    public void bind(CartItem cartItem) {
        itemNameTextView.setText(cartItem.getItemName());
        // Tambahkan logika lain untuk menampilkan informasi lainnya
    }
}
