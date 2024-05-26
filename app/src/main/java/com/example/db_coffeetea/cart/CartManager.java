package com.example.db_coffeetea.cart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class CartManager {

    private static final String CART_PREFS_NAME = "OrderPreferences";
    private static final String CART_ITEMS_KEY = "cart_items";
    private static List<CartItem> cartItems = new ArrayList<>();

    public static void initializeCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CART_ITEMS_KEY, null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        if (json != null) {
            cartItems = gson.fromJson(json, type);
        } else {
            cartItems = new ArrayList<>();
        }
    }

    private static void saveCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString(CART_ITEMS_KEY, json);
        editor.apply();
    }

    public static void addToCart(CartItem cartItem, Context context) {
        cartItems.add(cartItem);
        saveCart(context);
    }

    public static void removeFromCart(CartItem cartItem, Context context) {
        cartItems.remove(cartItem);
        saveCart(context);

        // Hapus data dari SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("item_" + cartItem.getItemName()); // Menghapus item berdasarkan namanya
        editor.apply();
    }


    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static void clearCart(Context context) {
        cartItems.clear();
        saveCart(context);
    }
}
