package com.example.db_coffeetea.Checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.db_coffeetea.R;
import com.example.db_coffeetea.cart.CartItem;
import com.example.db_coffeetea.cart.CartManager;
import com.example.db_coffeetea.DB.konfigurasi;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    public static List<CartItem> checkoutItems;
    private QuantityChangeListener quantityChangeListener;
    private Context context;

    public interface QuantityChangeListener {
        void onQuantityChanged();
    }

    public CheckoutAdapter(List<CartItem> checkoutItems, Context context, QuantityChangeListener listener) {
        this.checkoutItems = checkoutItems;
        this.context = context;
        this.quantityChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcheckout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = checkoutItems.get(position);
        holder.bind(cartItem, quantityChangeListener, position, context);
    }

    @Override
    public int getItemCount() {
        return checkoutItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        ImageView itemImage;
        TextView textViewQuantity;
        TextView textViewResult;
        View buttonPlus;
        View buttonMinus;
        TextView buttonHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.namaMinumanTextView);
            itemPrice = itemView.findViewById(R.id.hargaAwalTextView);
            itemImage = itemView.findViewById(R.id.itemImage);
            textViewQuantity = itemView.findViewById(R.id.quantityTextView);
            textViewResult = itemView.findViewById(R.id.result);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonHapus = itemView.findViewById(R.id.buttonHapus);
        }

        public void bind(CartItem cartItem, QuantityChangeListener listener, int position, Context context) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            itemName.setText(cartItem.getItemName());
            itemPrice.setText("Rp. " + decimalFormat.format(cartItem.getPrice()));
            textViewQuantity.setText(String.valueOf(cartItem.getQuantity()));

            // Load image using Glide with fallback URL
            Glide.with(itemView.getContext())
                    .load(konfigurasi.IPGAMBAR + cartItem.getItemImage())
                    .error(Glide.with(itemView.getContext()).load(cartItem.getItemImage())) // Jika gambar tidak ditemukan, gunakan URL gambar langsung
                    .into(itemImage);

            // Set listener for button Plus
            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartItem.incrementQuantity();
                    listener.onQuantityChanged(); // Call onQuantityChanged method
                    updateViews(cartItem); // Update view after quantity change
                }
            });

            // Set listener for button Minus
            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartItem.decrementQuantity();
                    listener.onQuantityChanged(); // Call onQuantityChanged method
                    updateViews(cartItem); // Update view after quantity change
                }
            });

            // Set listener for button Hapus
            buttonHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartManager.removeFromCart(cartItem, context); // Update to pass context
                    checkoutItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, checkoutItems.size());
                    listener.onQuantityChanged(); // Call onQuantityChanged method to update total
                }
            });

            // Initial view update
            updateViews(cartItem);
        }

        // Method to update views after quantity change
        private void updateViews(CartItem cartItem) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            int totalPrice = cartItem.getPrice() * cartItem.getQuantity();
            textViewResult.setText("Rp. " + decimalFormat.format(totalPrice)); // Update result text

            // Update textViewQuantity with new quantity value
            textViewQuantity.setText(String.valueOf(cartItem.getQuantity()));
        }
    }
}
