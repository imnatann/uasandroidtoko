package com.example.db_coffeetea;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.db_coffeetea.DB.konfigurasi;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MhsAdapter extends RecyclerView.Adapter<MhsAdapter.ViewHolder> {
    private List<ConfigureData> items;
    private OnItemClickListener mListener;

    public MhsAdapter(List<ConfigureData> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(ConfigureData item);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConfigureData data = items.get(position);

        // Set OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(data);
                }
            }
        });

        Log.d("Adapter", "Harga sebelum diformat: " + data.getHarga());
        Log.d("Adapter", "Harga setelah diformat: " + data.getFormattedHarga());

        holder.id.setText(data.getId());
        holder.nama.setText(data.getNama());
        holder.harga.setText(data.getFormattedHarga());
        holder.stok.setText(data.getStok());

        // Set drawable based on status and stok
        if (data.getStatus().equalsIgnoreCase("Tersedia") && Integer.parseInt(data.getStok()) > 0) {
            holder.status.setImageResource(R.drawable.ready);
        } else {
            holder.status.setImageResource(R.drawable.sold);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show sold out alert dialog
                    new SweetAlertDialog(holder.itemView.getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(data.getNama() + " sudah terjual habis!")
                            .show();
                }
            });
        }

        Glide.with(holder.itemView)
                .load(konfigurasi.IPGAMBAR + data.getGambar())
                .error(Glide.with(holder.itemView).load(data.getGambar())) // Jika gambar tidak ditemukan, gunakan URL gambar langsung
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        ImageView image;
        TextView nama;
        TextView harga;
        ImageView status;
        TextView stok;

        public ViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            image = itemView.findViewById(R.id.ImageView);
            nama = itemView.findViewById(R.id.text);
            harga = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            stok = itemView.findViewById(R.id.quantity);
        }
    }
}
