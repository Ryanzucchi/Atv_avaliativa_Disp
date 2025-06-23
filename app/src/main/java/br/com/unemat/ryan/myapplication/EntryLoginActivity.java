package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable; // Import for Editable
import android.text.TextWatcher; // Import for TextWatcher
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Import for EditText
import android.widget.TextView;
import android.widget.Toast; // Import for Toast

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Import for ContextCompat

public class EntryLoginActivity extends AppCompatActivity {

    private EditText inputCpf;
    private EditText inputPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrylogin);

        inputCpf = findViewById(R.id.input_cpf);
        inputPassword = findViewById(R.id.input_pasSs);
        btnLogin = findViewById(R.id.btn_Elogin);
        TextView btnLinkReg = findViewById(R.id.link_Eregister);

        checkFormCompletion();

        inputCpf.addTextChangedListener(formTextWatcher);
        inputPassword.addTextChangedListener(formTextWatcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    Intent intent = new Intent(EntryLoginActivity.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EntryLoginActivity.this, "Por favor, insira seu CPF e senha.", Toast.LENGTH_SHORT).show();
                }
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


    /**
     * TextWatcher for monitoring changes in EditText fields.
     */
    private TextWatcher formTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFormCompletion();
        }

        @Override
        public void afterTextChanged(Editable s) {
            //
        }
    };

    /**
     * Checks if all required login fields are filled.
     * Updates the "Login" button's enabled state and color.
     */
    private void checkFormCompletion() {
        boolean isCpfFilled = inputCpf.getText().toString().trim().replaceAll("[^\\d]", "").length() == 11;
        boolean isPasswordFilled = !inputPassword.getText().toString().trim().isEmpty();

        boolean isFormComplete = isCpfFilled && isPasswordFilled;

        btnLogin.setEnabled(isFormComplete);

        if (isFormComplete) {
            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary)); // Green color
        } else {
            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray)); // Greyed out
        }
    }

    /**
     * Helper method to validate the form when the "Login" button is clicked.
     * @return true if both email/CPF and password fields are filled, false otherwise.
     */
    private boolean isFormValid() {

        return btnLogin.isEnabled();
    }
}