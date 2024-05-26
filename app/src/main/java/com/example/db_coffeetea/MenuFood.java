package com.example.db_coffeetea;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.db_coffeetea.DB.konfigurasi;
import com.example.db_coffeetea.cart.CartItem;
import com.example.db_coffeetea.cart.CartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuFood extends AppCompatActivity implements MhsAdapter.OnItemClickListener {

    BottomNavigationView bottomNavigationView;
    RecyclerView list;
    private LinearLayout refreshImageView;
    private ScrollView scrollView;
    private EditText searchBar;

    List<ConfigureData> itemList = new ArrayList<>();
    List<ConfigureData> filteredItemList = new ArrayList<>();
    MhsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isAtTop = true;
    TextView minuman, makanan, coffee, tea;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_food);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        refreshImageView = findViewById(R.id.refresh);
        scrollView = findViewById(R.id.scrollView);
        searchBar = findViewById(R.id.searchBar);

        // Initialize RecyclerView and its adapter
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MhsAdapter(filteredItemList); // Use filteredItemList for adapter
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        // Call method to fetch data from the server
        callVolley();

        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setSelectedItemId(R.id.dashbord);

        minuman = (TextView) findViewById(R.id.Tminuman);
        makanan = (TextView) findViewById(R.id.Tmakanan);
        coffee = (TextView) findViewById(R.id.Tcoffee);
        tea = (TextView) findViewById(R.id.Ttea);

//        ImageView chocosun = findViewById(R.id.chocosun);
//        ImageView blueMilk = findViewById(R.id.bluemilk);
//        ImageView redVelvet = findViewById(R.id.redvelvet);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MenuFood.this, "Refreshing...", Toast.LENGTH_SHORT).show();
                callVolley();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                isAtTop = scrollView.getScrollY() == 0;
                swipeRefreshLayout.setEnabled(isAtTop);
            }
        });

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAtTop) {
                    swipeRefreshLayout.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 1500);
                }
            }
        });

        minuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        makanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MenuFood.this, MenuFood.class);
                startActivity(loginIntent);
            }
        });

//        chocosun.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showConfirmationDialog("Choco Sundae", "harga_choco_sundae", "gambar_choco_sundae");
//            }
//        });
//
//        blueMilk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showConfirmationDialog("Blueberry Milkshake", "harga_blue_milkshake", "gambar_blue_milkshake");
//            }
//        });
//
//        redVelvet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showConfirmationDialog("Red Velvet Vanilla", "harga_red_velvet", "gambar_red_velvet");
//            }
//        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(MenuFood.this, Home.class));
                        return true;
                    case R.id.dashbord:
                        startActivity(new Intent(MenuFood.this, MenuFood.class));
                        updateSelectedMenuItem(item);
                        return true;
                    case R.id.users:
                        startActivity(new Intent(MenuFood.this, Order.class));
                        return true;
                    case R.id.user:
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        // Set up TextWatcher for searchBar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItemList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateSelectedMenuItem(MenuItem selectedItem) {
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.dashbord).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.users).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.user).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(false);
    }

    private void showConfirmationDialog(final String itemName, final String itemPrice, final String itemImage) {
        if (isItemInPreferences(itemName)) {
            new SweetAlertDialog(MenuFood.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error")
                    .setContentText("Kamu sudah memasukkan menu ini ke dalam keranjang!")
                    .show();
        } else {
            if (itemName.equals("Choco Sundae") || itemName.equals("Blueberry Milkshake") || itemName.equals("Red Velvet Vanilla")) {
                new SweetAlertDialog(MenuFood.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(itemName + " sudah terjual habis!")
                        .show();
            } else {
                final SweetAlertDialog confirmationDialog = new SweetAlertDialog(MenuFood.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah ingin dimasukkan ke dalam keranjang?")
                        .setContentText(itemName)
                        .setConfirmText("Ya, Masukkan")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();

                                final SweetAlertDialog loadingDialog = new SweetAlertDialog(MenuFood.this, SweetAlertDialog.PROGRESS_TYPE);
                                loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                loadingDialog.setTitleText("Loading");
                                loadingDialog.setCancelable(false);
                                loadingDialog.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissWithAnimation();

                                        new SweetAlertDialog(MenuFood.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(itemName + " dimasukkan ke dalam keranjang!")
                                                .setContentText("Ingin order yang lain?")
                                                .show();
                                        CartManager.initializeCart(MenuFood.this);
                                        CartManager.addToCart(new CartItem(itemName, Integer.parseInt(itemPrice), 1, itemImage), MenuFood.this);
                                        saveItemToPreferences(itemName, Integer.parseInt(itemPrice), itemImage);
                                    }
                                }, 1000);
                            }
                        })
                        .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });

                confirmationDialog.show();
            }
        }
    }

    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();

        JsonArrayRequest jArr = new JsonArrayRequest(konfigurasi.URLTAMPIL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if (obj.getString("tipe").equals("Food")) {
                            ConfigureData item = new ConfigureData();
                            item.setId(obj.getString("id"));
                            item.setNama(obj.getString("name"));
                            item.setHarga(obj.getString("harga"));
                            item.setStok(obj.getString("stok"));
                            item.setStatus(obj.getString("status"));
                            item.setGambar(obj.getString("gambar"));
                            item.setTipe(obj.getString("tipe"));
                            itemList.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                filterItemList(searchBar.getText().toString()); // Apply filter after loading data
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuFood.this, "Gagal koneksi ke server, cek setingan koneksi anda", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);
    }

    private void filterItemList(String query) {
        filteredItemList.clear();
        if (query.isEmpty()) {
            filteredItemList.addAll(itemList);
        } else {
            for (ConfigureData item : itemList) {
                if (item.getNama().toLowerCase().contains(query.toLowerCase())) {
                    filteredItemList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void saveItemToPreferences(String itemName, int itemPrice, String itemImage) {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String itemData = itemName + "," + itemPrice + "," + itemImage;
        editor.putString("item_" + itemName, itemData);
        editor.apply();
    }

    private boolean isItemInPreferences(String itemName) {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
        return sharedPreferences.contains("item_" + itemName);
    }

    private boolean isItemInCart(String itemName) {
        List<CartItem> cartItems = CartManager.getCartItems();
        for (CartItem item : cartItems) {
            if (item.getItemName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(ConfigureData item) {
        showConfirmationDialog(item.getNama(), item.getHarga(), item.getGambar());
    }
}
