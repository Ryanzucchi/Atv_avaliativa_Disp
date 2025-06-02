package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class KidRegistry extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kidregistry);

        Button btnselect = findViewById(R.id.escolha);
        Button btnenviar = findViewById(R.id.btn_enviar);
        Button btnUparq = findViewById(R.id.btn_uparq); // Botão de upload
        TextView fileNameText = findViewById(R.id.fileNameText); // Exibe nome do arquivo

        btnselect.setOnClickListener(v -> {
            Intent intent = new Intent(KidRegistry.this, Kg.class);
            startActivity(intent);
        });

        btnenviar.setOnClickListener(v -> {
            Intent intent = new Intent(KidRegistry.this, ActivityMain.class);
            startActivity(intent);
        });

        btnUparq.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Selecione um arquivo"), PICK_FILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            String path = fileUri.getLastPathSegment();

            TextView fileNameText = findViewById(R.id.fileNameText); // Atualiza nome do arquivo na tela
            fileNameText.setText("Selecionado: " + path);

            Toast.makeText(this, "Arquivo selecionado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }
}
