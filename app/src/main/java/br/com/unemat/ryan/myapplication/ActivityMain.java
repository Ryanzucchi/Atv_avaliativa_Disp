package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ActivityMain extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Certifique-se de que o XML correto é esse

        bottomNavigationView = findViewById(R.id.dashbottom_navigation);  // ID deve existir no XML

        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_dashboard) {
                        // Aqui você pode apenas recarregar ou fazer algo
                        return true;
                    } else if (itemId == R.id.nav_register) {
                        startActivity(new Intent(ActivityMain.this, KidRegistry.class));
                        return true;
                    } else if (itemId == R.id.nav_settings) {
                        startActivity(new Intent(ActivityMain.this, Kg.class));
                        return true;
                    }
                    return false;
                }
            });
        } else {
            // Evita crash se o ID estiver errado ou não existir
            throw new NullPointerException("dashbottom_navigation não encontrado em activity_main.xml");
        }
    }
}
