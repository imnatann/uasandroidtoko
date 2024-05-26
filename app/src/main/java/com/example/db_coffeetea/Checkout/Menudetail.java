    package com.example.db_coffeetea.Checkout;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.TextView;

    import androidx.appcompat.app.AppCompatActivity;

    import com.example.db_coffeetea.MhsAdapter;
    import com.example.db_coffeetea.Order;
    import com.example.db_coffeetea.R;
    import com.example.db_coffeetea.cart.CartItem;
    import com.example.db_coffeetea.cart.CartManager;

    public class Menudetail extends AppCompatActivity {

        private TextView namaMinumanTextView;
        private TextView hargaAwalTextView;
        private TextView quantityTextView;
        CheckoutAdapter adapter;

        private int quantity = 0;
        private int hargaAwal = 0;
        private String itemImage = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.itemcheckout);

            // Ambil referensi ke TextView yang diperlukan
            namaMinumanTextView = findViewById(R.id.namaMinumanTextView);
            hargaAwalTextView = findViewById(R.id.hargaAwalTextView);
            quantityTextView = findViewById(R.id.quantityTextView);

            // Ambil data menu dari CartManager (contoh: mengambil CartItem pertama)
            CartItem cartItem = CartManager.getCartItems().get(0); // Mengambil CartItem pertama dari CartManager

            // Ambil data itemName dan price dari CartItem
            String namaMinuman = cartItem.getItemName();
            hargaAwal = cartItem.getPrice();
            itemImage = cartItem.getItemImage();

            // Tampilkan data pada TextView
            namaMinumanTextView.setText(namaMinuman);
            hargaAwalTextView.setText("Rp. " + hargaAwal);

            // Set listener untuk tombol Plus dan Minus
            findViewById(R.id.buttonMinus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (quantity > 0) {
                        quantity--;
                        quantityTextView.setText(String.valueOf(quantity));
                        updateTotalPrice();
                        addItemToCart(); // Tambahkan pemanggilan addItemToCart() di sini
                    }
                }
            });

            findViewById(R.id.buttonPlus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity++;
                    quantityTextView.setText(String.valueOf(quantity));
                    updateTotalPrice();
                    addItemToCart(); // Tambahkan pemanggilan addItemToCart() di sini
                }
            });

            // Inisialisasi total harga awal
            updateTotalPrice();
        }

        // Method untuk mengupdate total harga berdasarkan quantity
        private void updateTotalPrice() {
            TextView totalPriceTextView = findViewById(R.id.quantityTextView);

            int totalPrice = hargaAwal * quantity;
            totalPriceTextView.setText("Rp. " + totalPrice);
        }

        private void addItemToCart() {
            // Buat string yang berisi informasi item yang akan disimpan di SharedPreferences
            String itemName = namaMinumanTextView.getText().toString();
            String itemData = itemName + "," + hargaAwal + "," + quantity + "," + itemImage;

            // Simpan informasi item ke SharedPreferences
            saveItemToPreferences(itemName, hargaAwal, itemImage);

            // Tambahkan item baru ke keranjang belanja
            CartManager.initializeCart(this);
            CartManager.addToCart(new CartItem(itemName, hargaAwal, quantity, itemImage), this);

            // Kembali ke activity Order
            startActivity(new Intent(Menudetail.this, Order.class));
        }
        private void saveItemToPreferences(String itemName, int itemPrice, String itemImage) {
            SharedPreferences sharedPreferences = getSharedPreferences("OrderPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String itemData = itemName + "," + itemPrice + "," + itemImage;
            editor.putString("item_" + itemName, itemData);
            editor.apply();
        }

    }
