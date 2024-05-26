package com.example.db_coffeetea;

import java.text.NumberFormat;
import java.util.Locale;

public class ConfigureData {
    private String id, nama, harga, stok, status, gambar, tipe;

    public ConfigureData() {
    }

    public ConfigureData(String id, String nama, String harga, String stok, String status, String gambar, String tipe){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.status = status;
        this.gambar = gambar;
        this.tipe = tipe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) { this.nama = nama; }

    public String getHarga() { return harga; }

    public void setHarga(String harga) { this.harga = harga; }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) { this.stok = stok; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) { this.gambar = gambar; }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }


    public String getFormattedHarga() {
        try {
            double hargaDouble = Double.parseDouble(harga);
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            return formatter.format(hargaDouble);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Rp -"; // Atur teks default jika harga tidak valid
        }
    }

}

