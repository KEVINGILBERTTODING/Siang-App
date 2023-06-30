package com.example.siangapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.example.siangapp.util.PesertaInterface;

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
    private PesertaInterface pesertaInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        if (sharedPreferences.getBoolean("logged_in", false)){
            if (sharedPreferences.getString("role_id", null).equals("3")) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else if (sharedPreferences.getString("role_id", null).equals("1")) {
                startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                finish();

            }else if (sharedPreferences.getString("role_id", null).equals("2")) {
                startActivity(new Intent(LoginActivity.this, PenyeliaMainActiviy.class));
                finish();

            }
        }

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

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
                                        if (response.body().getRoleId() == 3) { // peserta magang
                                            editor.putBoolean("logged_in", true);
                                            editor.putString("nama", responseModel.getNamaPeserta());
                                            editor.putString("user_id", responseModel.getUserId());
                                            editor.putString("role_id", String.valueOf(responseModel.getRoleId()));
                                            editor.apply();
                                            Toasty.success(getApplicationContext(), "Selamat datang " + responseModel.getNamaPeserta()).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                            progressDialog.dismiss();
                                        } else if (response.body().getRoleId() == 1) { // admin
                                            editor.putBoolean("logged_in", true);
                                            editor.putString("nama", responseModel.getNamaPeserta());
                                            editor.putString("user_id", responseModel.getUserId());
                                            editor.putString("role_id", String.valueOf(responseModel.getRoleId()));
                                            editor.apply();
                                            Toasty.success(getApplicationContext(), "Selamat datang " + responseModel.getNamaPeserta()).show();
                                            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                                            finish();
                                            progressDialog.dismiss();

                                        } else if (response.body().getRoleId() == 2) { // penyelia

                                            editor.putBoolean("logged_in", true);
                                            editor.putString("nama", responseModel.getNamaPeserta());
                                            editor.putString("user_id", responseModel.getUserId());
                                            editor.putString("role_id", String.valueOf(responseModel.getRoleId()));
                                            editor.putString("divisi", response.body().getDivisi());
                                            editor.apply();
                                            Toasty.success(getApplicationContext(), "Selamat datang " + responseModel.getNamaPeserta()).show();
                                            startActivity(new Intent(LoginActivity.this, PenyeliaMainActiviy.class));
                                            finish();
                                            progressDialog.dismiss();
                                        }
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


    private void register() {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setTitle("Loading").setMessage("Cek kuota magang").setCancelable(false);
        AlertDialog pd = alert.create();
        pd.show();

        pesertaInterface.cekKuotaMagang().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }else {
                    Dialog dialog = new Dialog(LoginActivity.this);
                    dialog.setContentView(R.layout.layout_alert);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnOke = dialog.findViewById(R.id.btnOke);
                    dialog.show();

                    btnOke.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toasty.error(LoginActivity.this, "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();

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
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
    }

}