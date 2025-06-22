package br.com.unemat.ryan.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityMain extends AppCompatActivity implements PerfilAdapter.PerfilAdapterListener {

    private static final String TAG = "ActivityMain";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_FILE_REQUEST_CODE = 101;

    BottomNavigationView bottomNavigationView;
    ListView listaPerfis;
    PerfilAdapter adapter;
    ArrayList<Convite> listaDePerfis;
    private int itemPositionParaAtualizar = -1;
    private int documentoIndexParaAtualizar = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNavigation();
        setupListaPerfis();
    }

    @Override
    public void onAdicionarDocumentoClick(int position) {
        this.itemPositionParaAtualizar = position;
        this.documentoIndexParaAtualizar = 0;
        verificarPermissaoEabrirSeletorDeArquivos();
    }

    @Override
    public void onAlterarDocumentoClick(int position, int documentoIndex) {
        this.itemPositionParaAtualizar = position;
        this.documentoIndexParaAtualizar = documentoIndex;
        verificarPermissaoEabrirSeletorDeArquivos();
    }

    private void verificarPermissaoEabrirSeletorDeArquivos() {
        String permissaoNecessaria;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissaoNecessaria = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permissaoNecessaria = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        if (ContextCompat.checkSelfPermission(this, permissaoNecessaria) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permissaoNecessaria}, PERMISSION_REQUEST_CODE);
        } else {
            abrirSeletorDeArquivos();
        }
    }

    private void abrirSeletorDeArquivos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirSeletorDeArquivos();
        } else {
            Toast.makeText(this, "Permissão para acessar arquivos é necessária.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri arquivoUri = data.getData();
            if (itemPositionParaAtualizar != -1) {
                Convite conviteParaAtualizar = listaDePerfis.get(itemPositionParaAtualizar);
                List<Object> documentos = conviteParaAtualizar.getDocumentos();

                if (documentoIndexParaAtualizar == 0) { // Adicionar
                    if (documentos.size() < 15) {
                        documentos.add(arquivoUri);
                        Toast.makeText(this, "Documento adicionado!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Limite de 15 documentos atingido.", Toast.LENGTH_SHORT).show();
                    }
                } else { // Alterar
                    int indexNaLista = documentoIndexParaAtualizar - 1;
                    if (indexNaLista >= 0 && indexNaLista < documentos.size()) {
                        documentos.set(indexNaLista, arquivoUri);
                        Toast.makeText(this, "Documento alterado!", Toast.LENGTH_SHORT).show();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setupListaPerfis() {
        listaPerfis = findViewById(R.id.lista_perfis);
        listaDePerfis = new ArrayList<>();

        List<Object> docsRyan = new ArrayList<>(Arrays.asList(R.drawable.d1, R.drawable.d2));
        String situacaoRyan = "Convocado com documentos faltantes. Por favor, comparecer pessoalmente à creche A.";
        listaDePerfis.add(new Convite("Ryan, 5 anos", "Jardim Imperial", situacaoRyan, docsRyan));

        List<Object> docsJessica = new ArrayList<>(Arrays.asList(R.drawable.d3, R.drawable.d4, R.drawable.d5));
        String situacaoJessica = "Em análise";
        listaDePerfis.add(new Convite("Jéssica, 4 anos", "Centro", situacaoJessica, docsJessica));

        adapter = new PerfilAdapter(this, listaDePerfis, this);
        listaPerfis.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.dashbottom_navigation);
        if (bottomNavigationView == null) { return; }
        // ... (seu código de navegação)
    }
}