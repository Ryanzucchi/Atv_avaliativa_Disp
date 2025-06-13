package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
// import android.widget.Button; // Remova esta linha se não for mais usada
import android.widget.ImageButton; // Mantenha esta linha
import android.widget.TextView;
// import android.widget.Toast; // Removido Toast para manter o código original, adicione se quiser feedback

import androidx.appcompat.app.AppCompatActivity;

public class Entrance extends AppCompatActivity {

    // Declare as variáveis como ImageButton novamente
    private ImageButton btnLogin;
    private ImageButton btnCadastrar;
    private TextView btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Certifique-se de que este R.layout.entrance corresponde ao seu XML de entrada
        setContentView(R.layout.entrance);

        // Acha os botões pelo ID e os atribui a variáveis do tipo ImageButton
        btnLogin = findViewById(R.id.btn_login);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        btnAdmin = findViewById(R.id.btn_admin);

        // Botão de login
        // Adicionei verificação de nulidade para evitar crashes, é uma boa prática
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Entrance.this, EntryLoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Botão de cadastro
        if (btnCadastrar != null) {
            btnCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Entrance.this, AccountCreateActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Botão de administrador (TextView clicável)
        if (btnAdmin != null) {
            btnAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Entrance.this, EntryLoginAdmActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}