// Crecheadm.java
package br.com.unemat.ryan.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class Crecheadm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Garante que o tema noturno não será usado
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Define o layout principal que contém a ListView
        setContentView(R.layout.creche_adm); // Verifique se este XML tem a ListView com o ID "lista_convites"

        // Encontra a ListView no seu layout
        ListView listaView = findViewById(R.id.lista_convites);

        // Cria a lista de dados (como você já tinha feito)
        ArrayList<Convite> lista = new ArrayList<>();
        lista.add(new Convite("Edson, 4 anos", "Bairro do limoeiro"));
        lista.add(new Convite("Maria, 3 anos", "Centro"));
        lista.add(new Convite("Lucas, 5 anos", "Jardim das Flores"));

        // Cria e define o adaptador na ListView
        // O adaptador agora tem toda a lógica de clique
        CriancaAdpter adapter = new CriancaAdpter(this, lista);
        listaView.setAdapter(adapter);
    }
}