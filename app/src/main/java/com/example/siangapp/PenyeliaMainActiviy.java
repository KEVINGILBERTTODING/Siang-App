package com.example.siangapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.siangapp.AdminFragment.AdminDivisiFragment;
import com.example.siangapp.AdminFragment.AdminHomeFragment;
import com.example.siangapp.AdminFragment.AdminLaporanMahasiswaFragment;
import com.example.siangapp.FragmentPesertaMagang.ProfileFragment;
import com.example.siangapp.PenyeliaFragment.PenyeliaHomeFragment;
import com.example.siangapp.PenyeliaFragment.PenyeliaMahasiswaFragment;
import com.example.siangapp.PenyeliaFragment.PenyeliaProjectFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PenyeliaMainActiviy extends AppCompatActivity {
    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyelia_main_activiy);


        bottomBar = findViewById(R.id.btnBar);
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia, new PenyeliaHomeFragment()).commit();

                    return true;
                } else if (item.getItemId() == R.id.menuKegiatan) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia,new PenyeliaMahasiswaFragment()).commit();
                    return true;

                } else if (item.getItemId() == R.id.menuProject) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia, new PenyeliaProjectFragment()).commit();
                    return true;

                } else if (item.getItemId() == R.id.menuProfile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia, new ProfileFragment()).commit();


                    return true;


                }
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia, new PenyeliaHomeFragment()).commit();
    }
}