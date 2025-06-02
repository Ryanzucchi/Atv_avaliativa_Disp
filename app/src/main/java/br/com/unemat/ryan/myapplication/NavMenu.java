package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class NavMenu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Layout correto da NavMenu

        bottomNavigationView = findViewById(R.id.dashbottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_dashboard) {
                    startActivity(new Intent(NavMenu.this, ActivityMain.class));  // Redireciona para ActivityMain
                    return true;
                } else if (id == R.id.nav_register) {
                    startActivity(new Intent(NavMenu.this, KidRegistry.class));  // Redireciona para KidRegistry
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(NavMenu.this, Kg.class));  // Redireciona para Kg
                    return true;
                }

                return false;
            }
        });
    }
}
