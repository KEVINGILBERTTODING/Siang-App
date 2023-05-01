package com.example.siangapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    TextView tvDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        Toasty.success(getApplicationContext(), "Berhasi registrasi", Toasty.LENGTH_SHORT).show();
    }

    private void init() {
        tvDaftar = findViewById(R.id.tvDaftar);
    }
}