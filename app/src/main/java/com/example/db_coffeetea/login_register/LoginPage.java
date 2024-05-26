package com.example.db_coffeetea.login_register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.db_coffeetea.DB.DbContract;
import com.example.db_coffeetea.DB.VolleyConnection;
import com.example.db_coffeetea.Home;
import com.example.db_coffeetea.MenuDrink;
import com.example.db_coffeetea.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginPage extends AppCompatActivity {

    EditText username, password;
    Button login, register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Inisialisasi elemen UI
        username = findViewById(R.id.edt_usernameLogin);
        password = findViewById(R.id.edt_passwordLogin);
        login = findViewById(R.id.btn_loginLogin);
        register = findViewById(R.id.btn_registerLogin);
        progressDialog = new ProgressDialog(LoginPage.this);
        progressDialog.setMessage("Loading...");

        // Set listener untuk tombol register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginPage.this, Register.class);
                startActivity(registerIntent);
            }
        });

        // Set listener untuk tombol login
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Ketika tombol disentuh, atur warna latar belakang menjadi coklat dan warna teks menjadi putih
                        login.setBackgroundColor(ContextCompat.getColor(LoginPage.this, R.color.coklat));
                        login.setTextColor(ContextCompat.getColor(LoginPage.this, R.color.white));
                        break;
                    case MotionEvent.ACTION_UP:
                        // Ketika tombol dilepas, kembalikan warna latar belakang dan teks ke default
                        login.setBackgroundColor(ContextCompat.getColor(LoginPage.this, R.color.white));
                        login.setTextColor(ContextCompat.getColor(LoginPage.this, R.color.black));
                        break;
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tampilkan loading menggunakan SweetAlertDialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(LoginPage.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                // Ambil username dan password dari input
                String sUsername = username.getText().toString();
                String sPassword = password.getText().toString();

                // Panggil method untuk melakukan login
                checkLogin(sUsername, sPassword, pDialog); // Sertakan SweetAlertDialog sebagai parameter
            }
        });
    }

    // Method untuk melakukan login dengan SweetAlertDialog sebagai parameter
    // Method untuk melakukan login dengan SweetAlertDialog sebagai parameter
    // Method untuk melakukan login dengan SweetAlertDialog sebagai parameter
    private void checkLogin(final String username, final String password, final SweetAlertDialog pDialog) {
        // Cek apakah akun valid secara statis
        if (StaticAccounts.isValid(username, password)) {
            // Jika akun valid secara statis, langsung arahkan ke halaman beranda
            Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
            Intent dashboardIntent = new Intent(LoginPage.this, Home.class);
            startActivity(dashboardIntent);
            finish(); // Finish LoginActivity agar tidak dapat kembali dengan tombol back
            return; // Langsung keluar dari metode checkLogin
        }

        // Lanjutkan dengan pemeriksaan ke server jika akun tidak valid secara statis
        if (checkNetworkConnection()) {
            // Buat request String untuk login
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Tutup SweetAlertDialog setelah mendapatkan respons
                            pDialog.dismiss();

                            // Check jika respons bukan JSON
                            if (!response.startsWith("{")) {
                                // Jika respons tidak berupa JSON, tampilkan SweetAlertDialog dengan pesan kesalahan
                                pDialog.setTitleText("Error");
                                pDialog.setContentText("Response tidak valid");
                                pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                pDialog.setCancelable(true);
                                return;
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("OK")) {
                                    // Jika akun ditemukan, lanjutkan ke halaman selanjutnya
                                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent dashboardIntent = new Intent(LoginPage.this, Home.class);
                                    dashboardIntent.putExtra("username", username); // Mengirim username ke Home.java
                                    startActivity(dashboardIntent);
                                    finish(); // Finish LoginActivity agar tidak dapat kembali dengan tombol back
                                } else {
                                    // Jika akun tidak ditemukan atau terjadi kesalahan lain
                                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                            }

                            // Tutup SweetAlertDialog setelah 1 detik (1000 milidetik)
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                }
                            }, 1000); // Menutup dialog setelah 1 detik (1000 milidetik)
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Tutup SweetAlertDialog jika terjadi error
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Set parameter untuk username dan password
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };

            // Tambahkan request ke antrian
            VolleyConnection.getInstance(LoginPage.this).addToRequestQue(stringRequest);
        } else {
            // Tampilkan pesan jika tidak ada koneksi internet
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    // Method untuk memeriksa koneksi internet
    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
