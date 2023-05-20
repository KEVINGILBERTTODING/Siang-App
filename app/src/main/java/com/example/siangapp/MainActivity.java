package com.example.siangapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.siangapp.FragmentPesertaMagang.HomeFragment;
import com.example.siangapp.FragmentPesertaMagang.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private String TAG = "dda0";
    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar = findViewById(R.id.btnBar);

        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framePesertaMagang, new HomeFragment()).commit();

                    return true;
                } else if (item.getItemId() == R.id.menuProject) {

                    return true;

                } else if (item.getItemId() == R.id.menuProfile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framePesertaMagang, new ProfileFragment()).commit();


                    return true;


                }
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.framePesertaMagang, new HomeFragment()).commit();


    }
}