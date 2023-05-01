package com.example.siangapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.AuthInterface;
import com.example.siangapp.util.DataApi;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextView tvLogin;
    EditText etNamaLengkap, etAlamat, etAsalSekolah, etAlamatSekolah,
    etJurusan, etNimm,etEmail, etTelepon,etPassword, etPasswordKonfirm;
    Button btnRegister;
    Spinner spJk, spAgama;
    String agama, jenisKelamin;
    String [] opsiJk = {"Laki-laki", "Perempuan"};
    String [] opsiAgama = {"Islam", "Kristen", "Katolik", "Hindu", "Budha"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        ArrayAdapter adapterAgama = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, opsiAgama);
        adapterAgama.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgama.setAdapter(adapterAgama);

        ArrayAdapter adapterJk = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, opsiJk);
        adapterJk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJk.setAdapter(adapterJk);

        spJk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (opsiJk[position].equals("Laki-laki")) {
                    jenisKelamin = "L";
                }else {
                    jenisKelamin = "P";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spAgama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agama = opsiAgama[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNamaLengkap.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field nama tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                } else if (etAlamat.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field alamat tidak boleh kosong", Toasty.LENGTH_SHORT).show();

                }else if (etAsalSekolah.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field asal sekolah tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else if (etAlamatSekolah.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field alamat sekolah tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else if (etJurusan.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field jurusan tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else if (etNimm.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field nim tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else if (etEmail.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field email tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else if (etTelepon.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field telepon tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else if (etPassword.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field password tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else if (etPasswordKonfirm.getText().toString().isEmpty()) {
                    Toasty.error(getApplicationContext(), "Field konfirmasi password tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                }else {
                    if (!etPasswordKonfirm.getText().toString().equals(etPassword.getText().toString())) {
                        Toasty.error(getApplicationContext(), "Password tidak sesuai", Toasty.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                        alert.setCancelable(false).setMessage("Registrasi user...").setTitle("Loading");
                        AlertDialog progressDialog = alert.create();
                        progressDialog.show();

                        HashMap map = new HashMap();
                        map.put("nama_peserta", RequestBody.create(MediaType.parse("text/plain"), etNamaLengkap.getText().toString()));
                        map.put("nim", RequestBody.create(MediaType.parse("text/plain"), etNimm.getText().toString()));
                        map.put("alamat", RequestBody.create(MediaType.parse("text/plain"), etAlamat.getText().toString()));
                        map.put("asal_sekolah", RequestBody.create(MediaType.parse("text/plain"), etAsalSekolah.getText().toString()));
                        map.put("alamat_sekolah", RequestBody.create(MediaType.parse("text/plain"), etAlamatSekolah.getText().toString()));
                        map.put("jurusan", RequestBody.create(MediaType.parse("text/plain"), etJurusan.getText().toString()));
                        map.put("no_telp", RequestBody.create(MediaType.parse("text/plain"), etTelepon.getText().toString()));
                        map.put("password", RequestBody.create(MediaType.parse("text/plain"), etPasswordKonfirm.getText().toString()));
                        map.put("email", RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString()));
                        map.put("jeniskel", RequestBody.create(MediaType.parse("text/plain"), jenisKelamin));
                        map.put("agama", RequestBody.create(MediaType.parse("text/plain"), agama));

                        AuthInterface authInterface = DataApi.getClient().create(AuthInterface.class);
                        authInterface.register(map).enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                ResponseModel responseModel = response.body();
                                if (response.isSuccessful() && responseModel.getCode() == 200) {

                                    Toasty.success(getApplicationContext(), "Berhasi registrasi", Toasty.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                    progressDialog.dismiss();
                                } else {
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
                    if (etNamaLengkap.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field nama tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etAlamat.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field alamat tidak boleh kosong", Toasty.LENGTH_SHORT).show();

                    } else if (etAsalSekolah.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field asal sekolah tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etAlamatSekolah.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field alamat sekolah tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etJurusan.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field jurusan tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etNimm.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field nim tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etEmail.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field email tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etTelepon.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field telepon tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etPassword.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field password tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else if (etPasswordKonfirm.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "Field konfirmasi password tidak boleh kosong", Toasty.LENGTH_SHORT).show();
                    } else {
                        if (!etPasswordKonfirm.getText().toString().equals(etPassword.getText().toString())) {
                            Toasty.error(getApplicationContext(), "Password tidak sesuai", Toasty.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                            alert.setCancelable(false).setMessage("Registrasi user...").setTitle("Loading");
                            AlertDialog progressDialog = alert.create();
                            progressDialog.show();

                            HashMap map = new HashMap();
                            map.put("nama_peserta", RequestBody.create(MediaType.parse("text/plain"), etNamaLengkap.getText().toString()));
                            map.put("nim", RequestBody.create(MediaType.parse("text/plain"), etNimm.getText().toString()));
                            map.put("alamat", RequestBody.create(MediaType.parse("text/plain"), etAlamat.getText().toString()));
                            map.put("asal_sekolah", RequestBody.create(MediaType.parse("text/plain"), etAsalSekolah.getText().toString()));
                            map.put("alamat_sekolah", RequestBody.create(MediaType.parse("text/plain"), etAlamatSekolah.getText().toString()));
                            map.put("jurusan", RequestBody.create(MediaType.parse("text/plain"), etJurusan.getText().toString()));
                            map.put("no_telp", RequestBody.create(MediaType.parse("text/plain"), etTelepon.getText().toString()));
                            map.put("password", RequestBody.create(MediaType.parse("text/plain"), etPasswordKonfirm.getText().toString()));
                            map.put("email", RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString()));
                            map.put("jeniskel", RequestBody.create(MediaType.parse("text/plain"), jenisKelamin));
                            map.put("agama", RequestBody.create(MediaType.parse("text/plain"), agama));

                            AuthInterface authInterface = DataApi.getClient().create(AuthInterface.class);
                            authInterface.register(map).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    ResponseModel responseModel = response.body();
                                    if (response.isSuccessful() && responseModel.getCode() == 200) {

                                        Toasty.success(getApplicationContext(), "Berhasi registrasi", Toasty.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        finish();
                                        progressDialog.dismiss();
                                    } else {
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
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void init() {
        tvLogin = findViewById(R.id.tvLogin);
        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etAlamat = findViewById(R.id.etAlamat);
        etAsalSekolah = findViewById(R.id.etAsalSekolah);
        etAlamatSekolah = findViewById(R.id.etAlamatSekolah);
        etJurusan = findViewById(R.id.etJurusan);
        etNimm = findViewById(R.id.etNim);
        etEmail = findViewById(R.id.etEmail);
        etTelepon = findViewById(R.id.etTelepon);
        etPassword = findViewById(R.id.etPassword);
        etPasswordKonfirm = findViewById(R.id.etPassKonfir);

        spJk = findViewById(R.id.spJk);
        btnRegister = findViewById(R.id.btnRegister);
        spAgama = findViewById(R.id.spAgama);


    }


}