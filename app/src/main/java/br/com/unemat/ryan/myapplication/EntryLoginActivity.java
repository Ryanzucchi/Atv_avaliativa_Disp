package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EntryLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrylogin);
        Button btnLogin = findViewById(R.id.btn_Elogin);
        TextView btnLinkReg = findViewById(R.id.link_Eregister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryLoginActivity.this, ActivityMain.class);
                startActivity(intent);
            }
        });
        btnLinkReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EntryLoginActivity.this, AccountCreateActivity.class);
                startActivity(intent);
            }
        });

    }
}