package br.com.unemat.ryan.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Crecheadm extends Activity implements CriancaAdpter.CriancaAdapterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.creche_adm);

        ListView listaView = findViewById(R.id.lista_convites);

        // --- AGORA CRIAMOS OS DADOS FORNECENDO UM STATUS PARA CADA CRIANÇA ---
        ArrayList<Convite> lista = new ArrayList<>();

        // Para ter documentos de exemplo, criamos uma lista para cada um
        List<Object> docsEdson = new ArrayList<>(Arrays.asList(R.drawable.d1));
        lista.add(new Convite("Edson, 4 anos", "Bairro do limoeiro", "Em análise", docsEdson));

        List<Object> docsMaria = new ArrayList<>(); // Maria não tem documentos ainda
        lista.add(new Convite("Maria, 3 anos", "Centro", "Aguardando envio de documentos", docsMaria));

        List<Object> docsLucas = new ArrayList<>(Arrays.asList(R.drawable.d2, R.drawable.d3));
        lista.add(new Convite("Lucas, 5 anos", "Jardim das Flores", "Convocado", docsLucas));

        // Criamos o adaptador passando "this" como listener
        CriancaAdpter adapter = new CriancaAdpter(this, lista, this);
        listaView.setAdapter(adapter);
    }

    @Override
    public void onAcaoConfirmada(Convite convite, String motivo, boolean foiConvocado) {
        String acao = foiConvocado ? "Convocado" : "Recusado";
        String mensagem = "Ação: " + acao + "\n" +
                "Criança: " + convite.getNomeIdade() + "\n" +
                "Motivo: " + motivo;

        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
        // Aqui você atualizaria o status da criança no banco de dados e no adapter.
    }
}