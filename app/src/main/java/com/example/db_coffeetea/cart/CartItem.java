package com.example.db_coffeetea.cart;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CartItem {
    private String itemName;
    private int price;
    private int quantity;
    private String image;

    // Harga default jika tidak ditemukan
    private static final int DEFAULT_PRICE = 0;

    // Peta harga untuk setiap item
    private static final Map<String, Integer> priceMap = new HashMap<>();


    public CartItem(String itemName, int itemPrice, int quantity, String itemImage) {
        this.itemName = itemName;
        this.price = itemPrice;  // Set harga berdasarkan nama item
        this.quantity = quantity;  // Menggunakan nilai yang diteruskan sebagai parameter konstruktor
        this.image = itemImage; // Set gambar

    }


    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Mengambil harga berdasarkan nama item dari peta harga
    private int getPriceByName(String itemName) {
        return priceMap.getOrDefault(itemName, DEFAULT_PRICE);
    }

    public String getItemImage() {
        return image;
    }

    // Metode untuk menambah jumlah item di keranjang
    public void incrementQuantity() {
        quantity++;
    }

    // Metode untuk mengurangi jumlah item di keranjang jika lebih dari 0
    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    // Getter untuk peta harga (misalnya, jika ingin digunakan di luar kelas)
    public static Map<String, Integer> getPriceMap() {
        return priceMap;
    }

    public int setQuantity(int newQuantity) {
        return quantity = newQuantity;
    }

    public String getPriceFormatted() {
        // Format harga dengan pemisah ribuan (misalnya, 5.000)
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return "Rp. " + decimalFormat.format(price);
    }

}
