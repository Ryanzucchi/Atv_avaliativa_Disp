package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AccountCreateActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputCpf;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPhone;
    private EditText inputPassword;
    private CheckBox checkboxTerms;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountcreate);

        // Inicializa os campos
        inputEmail = findViewById(R.id.input_email);
        inputCpf = findViewById(R.id.input_cpf);
        inputFirstName = findViewById(R.id.input_psnome);
        inputLastName = findViewById(R.id.input_snome);
        inputPhone = findViewById(R.id.input_telefone);
        inputPassword = findViewById(R.id.input_pass);
        checkboxTerms = findViewById(R.id.checkbox_terms);
        btnCreateAccount = findViewById(R.id.btn_Clogin);
        TextView btnTologin = findViewById(R.id.link_login);

        // Validação dinâmica
        checkFormCompletion();

        inputEmail.addTextChangedListener(formTextWatcher);
        inputCpf.addTextChangedListener(formTextWatcher);
        inputFirstName.addTextChangedListener(formTextWatcher);
        inputLastName.addTextChangedListener(formTextWatcher);
        inputPhone.addTextChangedListener(formTextWatcher);
        inputPassword.addTextChangedListener(formTextWatcher);

        checkboxTerms.setOnCheckedChangeListener((buttonView, isChecked) -> checkFormCompletion());

        btnCreateAccount.setOnClickListener(v -> {
            if (isFormValid()) {
                Intent intent = new Intent(AccountCreateActivity.this, ActivityMain.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AccountCreateActivity.this, "Por favor, preencha todos os campos e aceite os termos.", Toast.LENGTH_LONG).show();
            }
        });

        btnTologin.setOnClickListener(v -> {
            Intent intent = new Intent(AccountCreateActivity.this, EntryLoginActivity.class);
            startActivity(intent);
        });
    }

    private final TextWatcher formTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFormCompletion();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void checkFormCompletion() {
        boolean isEmailFilled = !inputEmail.getText().toString().trim().isEmpty();
        boolean isCpfFilled = inputCpf.getText().toString().trim().replaceAll("[^\\d]", "").length() == 11; // verifica to
        boolean isFirstNameFilled = !inputFirstName.getText().toString().trim().isEmpty();
        boolean isLastNameFilled = !inputLastName.getText().toString().trim().isEmpty();
        boolean isPhoneFilled = !inputPhone.getText().toString().trim().isEmpty();
        boolean isPasswordFilled = !inputPassword.getText().toString().trim().isEmpty();
        boolean areTermsAccepted = checkboxTerms.isChecked();

        boolean isFormComplete = isEmailFilled && isCpfFilled && isFirstNameFilled &&
                isLastNameFilled && isPhoneFilled && isPasswordFilled && areTermsAccepted;

        btnCreateAccount.setEnabled(isFormComplete);

        btnCreateAccount.setBackgroundTintList(ContextCompat.getColorStateList(this,
                isFormComplete ? R.color.colorPrimary : android.R.color.darker_gray));
    }

    private boolean isFormValid() {
        return btnCreateAccount.isEnabled();
    }
}
