package com.example.siangapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.AuthInterface;
import com.example.siangapp.util.DataApi;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView tvDaftar;
    Button btnLogin;
    EditText etEmail, etPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        if (sharedPreferences.getBoolean("logged_in", false)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if (etEmail.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field email tidak boleh kosong", Toasty.LENGTH_SHORT).show();

                }else if (etPassword.getText().toString().isEmpty()){
                    Toasty.error(getApplicationContext(), "Field password tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setCancelable(false).setMessage("Tunggu sebentar...").setTitle("Loading");
                    AlertDialog progressDialog = alert.create();
                    progressDialog.show();

                    AuthInterface authInterface = DataApi.getClient().create(AuthInterface.class);
                    authInterface.login(etEmail.getText().toString(), etPassword.getText().toString())
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    ResponseModel responseModel = response.body();
                                    if (response.isSuccessful() && responseModel.getCode() == 200) {
                                        editor.putBoolean("logged_in", true);
                                        editor.putString("nama", responseModel.getNamaPeserta());
                                        editor.putString("user_id", responseModel.getUserId());
                                        editor.apply();
                                        Toasty.success(getApplicationContext(), "Selamat datang " + responseModel.getNamaPeserta()).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                        progressDialog.dismiss();
                                    }else {
                                        Toasty.error(getApplicationContext(), responseModel.getMessage(), Toasty.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    Toasty.error(getApplicationContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });
                }
            }
        });
    }

    private void init() {
        tvDaftar = findViewById(R.id.tvDaftar);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}