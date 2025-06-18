package br.com.unemat.ryan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Import for EditText
import android.widget.Toast; // Import for Toast

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Import for ContextCompat

public class EntryLoginAdmActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText inputEmailOrCpf;
    private EditText inputPassword;
    private Button btnLogin; // Renamed for clarity to match its function

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entryloginadm);

        // Initialize UI elements
        inputEmailOrCpf = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_pass); // Ensure this ID matches your XML
        btnLogin = findViewById(R.id.btn_Alogin); // This is your "Login" button for admin

        // Set initial state of the login button
        checkFormCompletion();

        // Add TextWatchers to the EditText fields
        inputEmailOrCpf.addTextChangedListener(formTextWatcher);
        inputPassword.addTextChangedListener(formTextWatcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    // If the form is valid, proceed to Crecheadm
                    Intent intent = new Intent(EntryLoginAdmActivity.this, Crecheadm.class);
                    startActivity(intent);
                    finish(); // Optionally finish this activity
                } else {
                    // If the form is not valid, show a toast message
                    Toast.makeText(EntryLoginAdmActivity.this, "Por favor, preencha todos os campos para fazer login.", Toast.LENGTH_SHORT).show();
                }
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
            // Use your primary color defined in colors.xml
            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
        } else {
            // Use a darker gray to indicate it's disabled
            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
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