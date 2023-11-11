package com.devblok.kpc.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.tools.ActivityTools;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener  {

    BottomNavigationView bottomNavigationView;
    CalendarFragment calendarFragment = new CalendarFragment();
    HealthFragment healthFragment = new HealthFragment();
    MainFragment mainFragment = new MainFragment();
    InspectFragment inspectFragment = new InspectFragment();
    AnimalsFragment animalsFragment = new AnimalsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTools.fullscreenMode(getWindow());
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        Integer lastFragment = bundle.getInt("lastFragment");

        bottomNavigationView = findViewById(R.id.menu);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(lastFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityTools.fullscreenMode(getWindow());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.connectionPool().connectionCount();
        ActivityTools.closeAllConnections();
        if (item.getItemId() == R.id.calendar) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, calendarFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.health) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, healthFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.main) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, mainFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.inspect) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, inspectFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.animals) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, animalsFragment)
                    .commit();
            return true;
        }
        return false;
    }
}