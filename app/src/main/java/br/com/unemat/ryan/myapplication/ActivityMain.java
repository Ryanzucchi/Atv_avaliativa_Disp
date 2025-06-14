package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "ActivityMain";
    BottomNavigationView bottomNavigationView;
    private Button btnStatusSolicitacao;
    private Button btnAtualizarDocumentos;
    private EditText inputSearch;

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
                    if (itemId == R.id.nav_dashboard) {
                        // Já estamos na ActivityMain, então não faz nada, apenas retorna true.
                        Log.d(TAG, "Item 'Dashboard' clicado. Já estamos aqui.");
                        return true;
                    } else if (itemId == R.id.nav_register) {
                        Log.d(TAG, "Navegando para KidRegistry.");
                        startActivity(new Intent(ActivityMain.this, KidRegistry.class));
                        // Não chame finish() aqui. KidRegistry não tem BottomNav, então ActivityMain
                        // deve permanecer na pilha para que o usuário possa voltar facilmente.
                        return true;
                    } else if (itemId == R.id.nav_settings) {
                        Log.d(TAG, "Navegando para Kg.");
                        startActivity(new Intent(ActivityMain.this, Kg.class));
                        // Não chame finish() aqui, Kg tem BottomNav e gerencia sua própria seleção.
                        return true;
                    }
                    return false;
                }
            });
            // **IMPORTANTE**: Define o item "Dashboard" como selecionado ao iniciar a ActivityMain
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
            Log.d(TAG, "Item 'Dashboard' definido como selecionado ao iniciar.");
        } else {
            Log.e(TAG, "BottomNavigationView 'dashbottom_navigation' não encontrado em activity_main.xml");
            Toast.makeText(this, "Erro interno: Navegação não disponível.", Toast.LENGTH_LONG).show();
        }

        btnStatusSolicitacao = findViewById(R.id.btn_status_solicitacao);
        if (btnStatusSolicitacao != null) {
            btnStatusSolicitacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStatusDialog();
                }
            });
        } else {
            Log.e(TAG, "Botão 'btn_status_solicitacao' não encontrado.");
        }

        btnAtualizarDocumentos = findViewById(R.id.Idteste);
        if (btnAtualizarDocumentos != null) {
            btnAtualizarDocumentos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Botão 'Atualizar documentos' clicado.");
                }
            });
        } else {
            Log.e(TAG, "Botão 'Idteste' não encontrado.");
        }

        inputSearch = findViewById(R.id.input_pass);
        if (inputSearch == null) {
            Log.e(TAG, "EditText 'input_pass' (campo de busca) não encontrado.");
        }
    }

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
        Log.d(TAG, "Diálogo de status de solicitação exibido.");
    }
}