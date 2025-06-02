package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EntryLoginAdmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entryloginadm);

        Button btnAlogin = findViewById(R.id.btn_Alogin);

        // Botão de cadastro
        btnAlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryLoginAdmActivity.this, Crecheadm.class);
                startActivity(intent);
            }
        });
    }
}