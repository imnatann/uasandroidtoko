package com.example.db_coffeetea.login_register;
import java.util.HashMap;
import java.util.Map;

public class StaticAccounts {

    // Membuat map untuk menyimpan username dan password
    private static final Map<String, String> accounts = new HashMap<>();

    // Membuat konstruktor statis untuk menginisialisasi akun statis
    static {
        // Menambahkan akun-akun statis ke dalam map
        accounts.put("Admin", "admin123");
        accounts.put("Bagas", "bagas123");
        // Tambahkan akun lainnya sesuai kebutuhan
    }

    // Method untuk memeriksa apakah username dan password yang dimasukkan valid
    public static boolean isValid(String username, String password) {
        // Ambil password yang sesuai dengan username dari map
        String storedPassword = accounts.get(username);
        // Jika password yang dimasukkan sama dengan password yang tersimpan, maka kembalikan true
        return storedPassword != null && storedPassword.equals(password);
    }
}
