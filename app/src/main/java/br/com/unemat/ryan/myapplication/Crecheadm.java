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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.creche_adm); // seu layout XML


        ListView listaView = findViewById(R.id.lista_convites);

        ArrayList<Convite> lista = new ArrayList<>();
        lista.add(new Convite("Edson, 4 anos", "Bairro do limoeiro"));
        lista.add(new Convite("Maria, 3 anos", "Centro"));

        CriancaAdpter adapter = new CriancaAdpter(this, lista);
        listaView.setAdapter(adapter);
    }
}
