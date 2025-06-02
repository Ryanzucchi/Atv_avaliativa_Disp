package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.Arrays;
import java.util.List;

public class Kg extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private ListView listaCreches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kidengardens);  // Certifique-se que o layout está correto

        // Inicializa a ListView
        listaCreches = findViewById(R.id.list_view_creches);

        // Lista de creches
        List<String> creches = Arrays.asList(
                "Creche A - Rua A - Bairro A - número 0 - cidade A - Estado A",
                "Creche B - Rua B - Bairro B - número 1 - cidade B - Estado B",
                "Creche C - Rua C - Bairro C - número 2 - cidade C - Estado C"
        );

        // Configura o adapter para a ListView
        CrecheAdapter adapter = new CrecheAdapter(this, creches);
        listaCreches.setAdapter(adapter);

        // Inicializa o BottomNavigationView
        bottomNavigationView = findViewById(R.id.dashbottom_navigation);  // Certifique-se de que esse ID está correto

        // Configura o listener para navegação
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_dashboard) {
                        // Navegar para a ActivityMain
                        startActivity(new Intent(Kg.this, ActivityMain.class));
                        return true;
                    } else if (itemId == R.id.nav_register) {
                        // Navegar para o KidRegistry
                        startActivity(new Intent(Kg.this, KidRegistry.class));
                        return true;
                    } else if (itemId == R.id.nav_settings) {
                        // Aqui você pode adicionar ações relacionadas às configurações, se necessário
                        return true;
                    }
                    return false;
                }
            });
        } else {
            // Em caso de erro de ID não encontrado
            throw new NullPointerException("dashbottom_navigation não encontrado em kidengardens.xml");
        }
    }
}
