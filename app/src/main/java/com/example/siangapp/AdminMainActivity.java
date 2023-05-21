package com.example.siangapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.siangapp.AdminFragment.AdminHomeFragment;
import com.example.siangapp.FragmentPesertaMagang.HomeFragment;
import com.example.siangapp.FragmentPesertaMagang.PesertaKegiatanFragment;
import com.example.siangapp.FragmentPesertaMagang.PesertaProjectFragment;
import com.example.siangapp.FragmentPesertaMagang.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMainActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomBar = findViewById(R.id.btnBar);
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, new AdminHomeFragment()).commit();

                    return true;
                } else if (item.getItemId() == R.id.menuDataMahasiswa) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, new PesertaProjectFragment()).commit();
                    return true;

                } else if (item.getItemId() == R.id.menuLaporan) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, new PesertaKegiatanFragment()).commit();
                    return true;

                } else if (item.getItemId() == R.id.menuProfile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, new ProfileFragment()).commit();


                    return true;


                }
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, new AdminHomeFragment()).commit();

    }
}
