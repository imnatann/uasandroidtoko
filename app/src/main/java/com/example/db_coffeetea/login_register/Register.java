package com.example.db_coffeetea.login_register;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.db_coffeetea.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText username, email, passsword, confPassword;
    Button login, register;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.edt_usernameRegister);
        email = (EditText) findViewById(R.id.edt_emailRegister);
        passsword = (EditText) findViewById(R.id.edt_passwordRegister);
        confPassword = (EditText) findViewById(R.id.edt_confPasswordRegister);
     /*   login = (Button) findViewById(R.id.btn_loginRegister);*/
        register = (Button) findViewById(R.id.btn_registerRegister);
        progressDialog = new ProgressDialog(Register.this);

/*
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Register.this, LoginPage.class);
                startActivity(loginIntent);
            }
        });*/

        register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Ketika tombol disentuh, atur warna latar belakang menjadi coklat dan warna teks menjadi putih
                        register.setBackgroundColor(ContextCompat.getColor(Register.this, R.color.coklat));
                        register.setTextColor(ContextCompat.getColor(Register.this, R.color.white));
                        break;
                    case MotionEvent.ACTION_UP:
                        // Ketika tombol dilepas, kembalikan warna latar belakang dan teks ke default
                        register.setBackgroundColor(ContextCompat.getColor(Register.this, R.color.white));
                        register.setTextColor(ContextCompat.getColor(Register.this, R.color.black));
                        // Lakukan pemeriksaan dan proses pendaftaran pengguna
                        String sUsername = username.getText().toString();
                        String sEmail = email.getText().toString();
                        String sPassword = passsword.getText().toString();
                        if (!sPassword.equals("")) {
                            CreateDataToServer(sUsername, sEmail, sPassword);
                            Intent loginIntent = new Intent(Register.this, LoginPage.class);
                            startActivity(loginIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal! Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });

    }

    public void CreateDataToServer(final String username, final String email, final String password) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"OK\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            VolleyConnection.getInstance(Register.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
