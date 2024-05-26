package com.example.db_coffeetea.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db_coffeetea.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private TextView plusButton;
    public TextView buttonHapus;
    private TextView minusButton;
    private List<CartItem> cartItems;
    private Context context;
    private int itemSpacing; // Menyimpan nilai margin atas antar item
    private View itemView;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cartlist, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind data to each item
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);

        // Set OnClickListener for buttonHapus
        holder.buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil method untuk menghapus item dari keranjang
                removeItemFromCart(position);
            }
        });
    }

    private void removeItemFromCart(int position) {
        // Remove item from cartItems list and notify adapter
        cartItems.remove(position); // Hapus item dari list
        notifyItemRemoved(position); // Beritahu adapter bahwa item telah dihapus
        notifyItemRangeChanged(position, cartItems.size()); // Refresh range item setelah posisi yang dihapus

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        public TextView buttonHapus;
        private ImageView itemImageView;
        private TextView itemNameTextView;
        private TextView priceTextView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonHapus = itemView.findViewById(R.id.buttonHapus);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            plusButton = itemView.findViewById(R.id.buttonPlus);
            minusButton = itemView.findViewById(R.id.buttonMinus);
        }


        public void bind(CartItem cartItem) {
            itemNameTextView.setText(cartItem.getItemName());
        }

//        private void setItemImageResource(String itemName) {
//            int defaultImageResource = R.drawable.lock; // Gambar default jika tidak ada cocokan
//
//            // Set sumber gambar berdasarkan nama item
//            switch (itemName) {
//                // Menu Minuman (Drink)
//                case "Mango Milkshake":
//                    itemImageView.setImageResource(R.drawable.mangomilk);
//                    break;
//                case "Strawberry Milkshake":
//                    itemImageView.setImageResource(R.drawable.strawmilk);
//                    break;
//                case "Caramel Cappucino":
//                    itemImageView.setImageResource(R.drawable.caramel);
//                    break;
//                case "Ice Cappucino":
//                    itemImageView.setImageResource(R.drawable.capcin);
//                    break;
//                case "Ice Mango":
//                    itemImageView.setImageResource(R.drawable.icemango);
//                    break;
//                case "Oreo Milkshake":
//                    itemImageView.setImageResource(R.drawable.oreo);
//                    break;
//
//                // Menu Makanan (Food)
//                case "Croissant":
//                    itemImageView.setImageResource(R.drawable.crois);
//                    break;
//                case "Oreo Cupcake":
//                    itemImageView.setImageResource(R.drawable.oreoc);
//                    break;
//                case "Donut Chocolate":
//                    itemImageView.setImageResource(R.drawable.donutc);
//                    break;
//                case "Scallion Bread":
//                    itemImageView.setImageResource(R.drawable.scal);
//                    break;
//                case "Coffee Buns":
//                    itemImageView.setImageResource(R.drawable.buns);
//                    break;
//                case "Cheese Cake":
//                    itemImageView.setImageResource(R.drawable.ches);
//                    break;
//
//                // Menu Kopi (Coffee)
//                case "Creamy Ice Machiato":
//                    itemImageView.setImageResource(R.drawable.creamy);
//                    break;
//                case "Ice Matcha Latte":
//                    itemImageView.setImageResource(R.drawable.icematcha);
//                    break;
//                case "Vanilla Coffee":
//                    itemImageView.setImageResource(R.drawable.vanilc);
//                    break;
//                case "Ice Americano Coffee":
//                    itemImageView.setImageResource(R.drawable.america);
//                    break;
//                case "Ice Latte":
//                    itemImageView.setImageResource(R.drawable.latte);
//                    break;
//                case "Caramel Latte":
//                    itemImageView.setImageResource(R.drawable.clatte);
//                    break;
//
//                // Menu Teh (Tea)
//                case "Bobba Thai Tea":
//                    itemImageView.setImageResource(R.drawable.btea);
//                    break;
//                case "Green Tea":
//                    itemImageView.setImageResource(R.drawable.gtea);
//                    break;
//                case "Lemon Tea":
//                    itemImageView.setImageResource(R.drawable.ltea);
//                    break;
//                case "Milk Tea":
//                    itemImageView.setImageResource(R.drawable.mtea);
//                    break;
//                case "Blue Forest Tea":
//                    itemImageView.setImageResource(R.drawable.bftea);
//                    break;
//                case "Fruite Tea":
//                    itemImageView.setImageResource(R.drawable.ftea);
//                    break;
//
//                // Menu Diskon (Promo)
//                case "Strawberry Cake":
//                    itemImageView.setImageResource(R.drawable.straw);
//                    break;
//                case "Vanilla Milkshake":
//                    itemImageView.setImageResource(R.drawable.vanil);
//                    break;
//                case "Sunday Ice Cream Cake":
//                    itemImageView.setImageResource(R.drawable.cake);
//                    break;
//                case "Coffee Muffins":
//                    itemImageView.setImageResource(R.drawable.muff);
//                    break;
//                case "Caramel Sundae Ice Cream":
//                    itemImageView.setImageResource(R.drawable.sundae);
//                    break;
//                case "Rainbow Cookies":
//                    itemImageView.setImageResource(R.drawable.cookies);
//                    break;
//
//                // Gambar default jika tidak ada cocokan
//                default:
//                    itemImageView.setImageResource(defaultImageResource);
//                    break;
//            }
//        }
    }
}