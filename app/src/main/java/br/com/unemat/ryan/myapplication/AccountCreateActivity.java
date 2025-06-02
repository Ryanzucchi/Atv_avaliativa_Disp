package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccountCreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountcreate);

        Button btnClogin = findViewById(R.id.btn_Clogin);
        TextView btnTologin = findViewById(R.id.link_login);

        btnClogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountCreateActivity.this, ActivityMain.class);
                startActivity(intent);
            }
        });
        btnTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountCreateActivity.this, EntryLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}