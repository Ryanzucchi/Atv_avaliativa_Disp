package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable; // Import for Editable
import android.text.TextWatcher; // Import for TextWatcher
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox; // Import for CheckBox
import android.widget.EditText; // Import for EditText
import android.widget.TextView;
import android.widget.Toast; // Import for Toast

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Import for ContextCompat

public class AccountCreateActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText inputEmailOrCpf;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPhone;
    private EditText inputPassword;
    private CheckBox checkboxTerms;
    private Button btnCreateAccount; // Renamed for clarity to match its function

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountcreate);

        // Initialize UI elements
        inputEmailOrCpf = findViewById(R.id.input_email);
        inputFirstName = findViewById(R.id.input_psnome);
        inputLastName = findViewById(R.id.input_snome);
        inputPhone = findViewById(R.id.input_telefone);
        inputPassword = findViewById(R.id.input_pass);
        checkboxTerms = findViewById(R.id.checkbox_terms);
        btnCreateAccount = findViewById(R.id.btn_Clogin); // This is your "Criar Conta" button
        TextView btnTologin = findViewById(R.id.link_login);

        // Set initial state of the button
        checkFormCompletion();

        // Add TextWatchers to all EditText fields
        inputEmailOrCpf.addTextChangedListener(formTextWatcher);
        inputFirstName.addTextChangedListener(formTextWatcher);
        inputLastName.addTextChangedListener(formTextWatcher);
        inputPhone.addTextChangedListener(formTextWatcher);
        inputPassword.addTextChangedListener(formTextWatcher);

        // Add OnCheckedChangeListener to the CheckBox
        checkboxTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkFormCompletion(); // Re-check form completion when checkbox state changes
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    // If the form is valid, proceed to the next activity
                    Intent intent = new Intent(AccountCreateActivity.this, ActivityMain.class);
                    startActivity(intent);
                    finish(); // Optionally finish this activity to remove it from the back stack
                } else {
                    // If the form is not valid, show a toast message
                    Toast.makeText(AccountCreateActivity.this, "Por favor, preencha todos os campos e aceite os termos.", Toast.LENGTH_LONG).show();
                }
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

    /**
     * TextWatcher for monitoring changes in EditText fields.
     */
    private TextWatcher formTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFormCompletion(); // Re-check form completion on text change
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /**
     * Checks if all required fields are filled and the terms are accepted.
     * Updates the "Criar Conta" button's enabled state and color.
     */
    private void checkFormCompletion() {
        boolean isEmailOrCpfFilled = !inputEmailOrCpf.getText().toString().trim().isEmpty();
        boolean isFirstNameFilled = !inputFirstName.getText().toString().trim().isEmpty();
        boolean isLastNameFilled = !inputLastName.getText().toString().trim().isEmpty();
        boolean isPhoneFilled = !inputPhone.getText().toString().trim().isEmpty();
        boolean isPasswordFilled = !inputPassword.getText().toString().trim().isEmpty();
        boolean areTermsAccepted = checkboxTerms.isChecked();

        // All fields must be filled AND terms must be accepted
        boolean isFormComplete = isEmailOrCpfFilled && isFirstNameFilled && isLastNameFilled &&
                isPhoneFilled && isPasswordFilled && areTermsAccepted;

        // Set button enabled state
        btnCreateAccount.setEnabled(isFormComplete);

        // Change button color based on completion
        if (isFormComplete) {
            btnCreateAccount.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary)); // Green color (from your KidRegistry)
        } else {
            btnCreateAccount.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray)); // Greyed out
        }
    }

    /**
     * Helper method to validate the form when the "Criar Conta" button is clicked.
     * This method provides a final check and can be used to show specific error messages.
     * @return true if all conditions are met, false otherwise.
     */
    private boolean isFormValid() {
        // We can simply call checkFormCompletion()'s logic here,
        // or re-evaluate each field for more specific error messages if needed.
        // For simplicity, we'll just check the enabled state of the button,
        // which reflects the result of checkFormCompletion().
        return btnCreateAccount.isEnabled();
    }
}