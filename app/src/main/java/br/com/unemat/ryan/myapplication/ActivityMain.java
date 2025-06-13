package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View; // Importe para View
import android.widget.Button; // Importe para Button
import android.widget.EditText; // Importe para EditText
import android.widget.TextView; // Importe para TextView
import androidx.annotation.NonNull;
import android.widget.ScrollView;
import androidx.appcompat.app.AlertDialog; // Importe para AlertDialog
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Firebase;

public class ActivityMain extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Button btnStatusSolicitacao; // Declarar como variável de classe
    private Button btnAtualizarDocumentos; // Declarar como variável de classe
    private EditText inputSearch; // Declarar como variável de classe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.dashbottom_navigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                    int itemId = item.getItemId();
                    // Assumindo que nav_dashboard é a ActivityMain (Dashboard)
                    if (itemId == R.id.nav_dashboard) {
                        // Já estamos na ActivityMain, então não faz nada ou pode reiniciar a mesma Activity
                        // Para evitar loop, apenas retorna true.
                        return true;
                    } else if (itemId == R.id.nav_register) { // "Registrar Criança"
                        startActivity(new Intent(ActivityMain.this, KidRegistry.class));
                        // finish(); // Opcional: finalize a atividade atual se não quiser voltar para ela
                        return true;
                    } else if (itemId == R.id.nav_settings) { // "Creches"
                        startActivity(new Intent(ActivityMain.this, Kg.class));
                        // finish();
                        return true;
                    }
                    return false;
                }
            });
            // Define o item "Dashboard" como selecionado ao iniciar a ActivityMain
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        } else {
            // Evita crash se o ID estiver errado ou não existir
            throw new NullPointerException("dashbottom_navigation não encontrado em activity_main.xml");
        }

        // Inicializa o botão "Status de solicitação"
        btnStatusSolicitacao = findViewById(R.id.btn_status_solicitacao);
        if (btnStatusSolicitacao != null) {
            btnStatusSolicitacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStatusDialog(); // Chama o método para exibir o diálogo
                }
            });
        }

        // Inicializa o botão "Atualizar documentos"
        btnAtualizarDocumentos = findViewById(R.id.Idteste);
        if (btnAtualizarDocumentos != null) {
            btnAtualizarDocumentos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lógica para atualizar documentos
                    // Exemplo: startActivity(new Intent(ActivityMain.this, UpdateDocumentsActivity.class));
                }
            });
        }

        // Inicializa o campo de busca
        inputSearch = findViewById(R.id.input_pass);
        // Não há listener específico aqui no seu código, mas a referência está correta
    }

    // Método para exibir o diálogo com o status da solicitação
    private void showStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Status da Solicitação");

        final TextView messageView = new TextView(this);
        messageView.setText("edson - convocado com documentos pendentes, trazer documentos pessoalmente na creche");
        messageView.setPadding(20, 20, 20, 20);
        messageView.setMovementMethod(new ScrollingMovementMethod());

        final ScrollView scrollView = new ScrollView(this);
        scrollView.addView(messageView);

        builder.setView(scrollView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}