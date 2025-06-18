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

    // Declare UI elements
    private EditText inputEmailOrCpf;
    private EditText inputPassword;
    private Button btnLogin; // Renamed for clarity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrylogin);

        // Initialize UI elements
        inputEmailOrCpf = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_pasSs); // Corrected ID to match XML: input_pasSs
        btnLogin = findViewById(R.id.btn_Elogin);
        TextView btnLinkReg = findViewById(R.id.link_Eregister);

        // Set initial state of the login button
        checkFormCompletion();

        // Add TextWatchers to the EditText fields
        inputEmailOrCpf.addTextChangedListener(formTextWatcher);
        inputPassword.addTextChangedListener(formTextWatcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    // If the form is valid, proceed to ActivityMain
                    Intent intent = new Intent(EntryLoginActivity.this, ActivityMain.class);
                    startActivity(intent);
                    finish(); // Optionally finish this activity
                } else {
                    // If the form is not valid, show a toast message
                    Toast.makeText(EntryLoginActivity.this, "Por favor, insira seu CPF/email e senha.", Toast.LENGTH_SHORT).show();
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
            // Not needed for this implementation
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Called when text changes, re-check form completion
            checkFormCompletion();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not needed for this implementation
        }
    };

    /**
     * Checks if all required login fields are filled.
     * Updates the "Login" button's enabled state and color.
     */
    private void checkFormCompletion() {
        boolean isEmailOrCpfFilled = !inputEmailOrCpf.getText().toString().trim().isEmpty();
        boolean isPasswordFilled = !inputPassword.getText().toString().trim().isEmpty();

        boolean isFormComplete = isEmailOrCpfFilled && isPasswordFilled;

        // Set button enabled state
        btnLogin.setEnabled(isFormComplete);

        // Change button color based on completion
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
        // We can simply check the enabled state of the button,
        // which reflects the result of checkFormCompletion().
        return btnLogin.isEnabled();
    }
}