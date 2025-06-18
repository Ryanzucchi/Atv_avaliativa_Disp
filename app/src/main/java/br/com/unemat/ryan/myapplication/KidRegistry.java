package br.com.unemat.ryan.myapplication;

import android.app.DatePickerDialog; // Import for DatePickerDialog
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.text.SimpleDateFormat; // Import for SimpleDateFormat
import java.util.ArrayList;
import java.util.Calendar; // Import for Calendar
import java.util.List;
import java.util.Locale; // Import for Locale

public class KidRegistry extends AppCompatActivity {

    private static final String TAG = "KidRegistry";
    private static final int PICK_FILE_REQUEST = 1;
    private static final int MAX_FILES_DISPLAY = 15;
    private static final int MIN_REQUIRED_FILES = 5;

    // UI Elements Declaration
    private Button btnSelectCreche;
    private Button btnEnviar;
    private Button btnUploadArquivos;
    private Button btnSelecaoAutomatica;
    private LinearLayout selectedFilesContainer;

    private Spinner spinnerDeficiencia;
    private Spinner spinnerTipoDeficiencia;
    private TextView textViewTipoDeficiencia;

    private TextView textViewDescricaoOutraDeficiencia;
    private EditText editTextDescricaoOutraDeficiencia;

    // New UI elements for validation
    private EditText inputName;
    private EditText inputDateOfBirth; // This is the one we'll add the DatePicker to
    private Spinner spinnerGender;


    // List to store URIs of selected files, declared as a class member
    private List<Uri> selectedFileUris = new ArrayList<>();

    // Bottom Navigation View
    private BottomNavigationView bottomNavigationView;

    // Calendar instance for DatePicker
    private Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kidregistry);

        // Initialize UI Elements
        btnSelectCreche = findViewById(R.id.escolha);
        btnEnviar = findViewById(R.id.btn_enviar);
        btnUploadArquivos = findViewById(R.id.btn_uparq);
        selectedFilesContainer = findViewById(R.id.selectedFilesContainer);
        btnSelecaoAutomatica = findViewById(R.id.selecao_automatica_button);

        spinnerDeficiencia = findViewById(R.id.spinner_deficiencia);
        spinnerTipoDeficiencia = findViewById(R.id.spinner_tipo_deficiencia);
        textViewTipoDeficiencia = findViewById(R.id.textView_tipo_deficiencia);

        textViewDescricaoOutraDeficiencia = findViewById(R.id.textView_descricao_outra_deficiencia);
        editTextDescricaoOutraDeficiencia = findViewById(R.id.editText_descricao_outra_deficiencia);

        inputName = findViewById(R.id.input_email);
        inputDateOfBirth = findViewById(R.id.input_psnome); // Initialize inputDateOfBirth
        spinnerGender = findViewById(R.id.input_snome);


        // --- DatePicker Setup for inputDateOfBirth ---
        // Set an OnClickListener to show the DatePickerDialog
        inputDateOfBirth.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        // Set the inputDateOfBirth as not editable manually, only via DatePicker
        inputDateOfBirth.setFocusable(false);
        inputDateOfBirth.setClickable(true);
        // --- End DatePicker Setup ---


        // --- Bottom Navigation View Setup ---
        bottomNavigationView = findViewById(R.id.dashbottom_navigation);
        if (bottomNavigationView != null) {
            // Set "Register" item as selected when KidRegistry starts
            bottomNavigationView.setSelectedItemId(R.id.nav_register);
            Log.d(TAG, "Item 'Register' definido como selecionado ao iniciar KidRegistry.");

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_dashboard) {
                        Log.d(TAG, "Navegando para ActivityMain (Dashboard).");
                        startActivity(new Intent(KidRegistry.this, ActivityMain.class));
                        finish();
                        return true;
                    } else if (itemId == R.id.nav_register) {
                        Log.d(TAG, "Item 'Register' clicado. Já estamos aqui.");
                        return true;
                    } else if (itemId == R.id.nav_settings) {
                        Log.d(TAG, "Navegando para Kg (Settings).");
                        startActivity(new Intent(KidRegistry.this, Kg.class));
                        return true;
                    }
                    return false;
                }
            });
        } else {
            Log.e(TAG, "BottomNavigationView 'dashbottom_navigation' não encontrado no layout kidregistry.xml");
            Toast.makeText(this, "Erro interno: Navegação não disponível.", Toast.LENGTH_LONG).show();
        }
        // --- End Bottom Navigation View Setup ---

        // Set up "Choose Creche" button listener
        if (btnSelectCreche != null) {
            btnSelectCreche.setOnClickListener(v -> {
                Intent intent = new Intent(KidRegistry.this, Kg.class);
                startActivity(intent);
            });
        } else {
            Toast.makeText(this, "Erro: Botão 'escolha' não encontrado no layout.", Toast.LENGTH_LONG).show();
        }

        // Set up "Automatic Selection" button listener
        if (btnSelecaoAutomatica != null) {
            btnSelecaoAutomatica.setOnClickListener(v -> {
                Toast.makeText(KidRegistry.this, "Seleção Automática Clicada!", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Erro: Botão 'selecao_automatica_button' não encontrado no layout.", Toast.LENGTH_LONG).show();
        }

        // Set up "Send" button listener
        if (btnEnviar != null) {
            btnEnviar.setOnClickListener(v -> {
                if (isFormValid()) {
                    Toast.makeText(KidRegistry.this, "Dados e " + selectedFileUris.size() + " arquivos enviados!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(KidRegistry.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(KidRegistry.this, "Preencha todos os campos e anexe pelo menos " + MIN_REQUIRED_FILES + " arquivos.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Erro: Botão 'btn_enviar' não encontrado no layout.", Toast.LENGTH_LONG).show();
        }

        // Set up "Upload Files" button listener
        if (btnUploadArquivos != null) {
            btnUploadArquivos.setOnClickListener(v -> {
                if (selectedFileUris.size() >= MAX_FILES_DISPLAY) {
                    Toast.makeText(KidRegistry.this, "Limite máximo de " + MAX_FILES_DISPLAY + " arquivos atingido.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, PICK_FILE_REQUEST);
            });
        } else {
            Toast.makeText(this, "Erro: Botão 'btn_uparq' não encontrado no layout.", Toast.LENGTH_LONG).show();
        }

        // Configure listener for "Disability" Spinner (Yes/No)
        if (spinnerDeficiencia != null && textViewTipoDeficiencia != null && spinnerTipoDeficiencia != null) {
            spinnerDeficiencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedOption = parent.getItemAtPosition(position).toString();
                    if (selectedOption.equals("Sim")) {
                        textViewTipoDeficiencia.setVisibility(View.VISIBLE);
                        spinnerTipoDeficiencia.setVisibility(View.VISIBLE);
                        textViewDescricaoOutraDeficiencia.setVisibility(View.GONE);
                        editTextDescricaoOutraDeficiencia.setVisibility(View.GONE);
                    } else {
                        textViewTipoDeficiencia.setVisibility(View.GONE);
                        spinnerTipoDeficiencia.setVisibility(View.GONE);
                        textViewDescricaoOutraDeficiencia.setVisibility(View.GONE);
                        editTextDescricaoOutraDeficiencia.setVisibility(View.GONE);
                    }
                    checkFormCompletion();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    checkFormCompletion();
                }
            });
        } else {
            Toast.makeText(this, "Erro: Spinners/TextViews de deficiência não encontrados.", Toast.LENGTH_LONG).show();
        }

        // Configure listener for "Type of Disability" Spinner
        if (spinnerTipoDeficiencia != null && textViewDescricaoOutraDeficiencia != null && editTextDescricaoOutraDeficiencia != null) {
            spinnerTipoDeficiencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedOption = parent.getItemAtPosition(position).toString();
                    if (selectedOption.equals("Outra") || selectedOption.equals("Múltipla")) {
                        textViewDescricaoOutraDeficiencia.setVisibility(View.VISIBLE);
                        editTextDescricaoOutraDeficiencia.setVisibility(View.VISIBLE);
                    } else {
                        textViewDescricaoOutraDeficiencia.setVisibility(View.GONE);
                        editTextDescricaoOutraDeficiencia.setVisibility(View.GONE);
                    }
                    checkFormCompletion();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    checkFormCompletion();
                }
            });
        } else {
            Toast.makeText(this, "Erro: Elementos para descrição de deficiência não encontrados.", Toast.LENGTH_LONG).show();
        }

        // Add TextWatchers to EditText fields for real-time validation
        inputName.addTextChangedListener(formTextWatcher);
        // inputDateOfBirth no longer needs a TextWatcher because its value is set by the DatePicker
        editTextDescricaoOutraDeficiencia.addTextChangedListener(formTextWatcher);

        // Add OnItemSelectedListeners to Spinners for real-time validation
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkFormCompletion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                checkFormCompletion();
            }
        });

        // Initial check when the activity starts
        checkFormCompletion();
    }

    // TextWatcher for EditText fields
    private TextWatcher formTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFormCompletion();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    // --- DatePicker Methods ---
    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInView();
            checkFormCompletion(); // Re-check form completion after date is set
        };

        new DatePickerDialog(
                KidRegistry.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateDateInView() {
        String myFormat = "dd/MM/yyyy"; // In Brazil, date format is dd/mm/yyyy
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR")); // Use Brazilian locale
        inputDateOfBirth.setText(sdf.format(calendar.getTime()));
    }
    // --- End DatePicker Methods ---


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            int currentFilesCount = selectedFileUris.size();

            if (data.getClipData() != null) { // Multiple files selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    if (currentFilesCount < MAX_FILES_DISPLAY) {
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
                        selectedFileUris.add(fileUri);
                        displayFilePreview(fileUri);
                        currentFilesCount++;
                    } else {
                        Toast.makeText(this, "Limite máximo de " + MAX_FILES_DISPLAY + " arquivos atingido. Alguns arquivos não foram adicionados.", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            } else if (data.getData() != null) { // A single file selected
                if (currentFilesCount < MAX_FILES_DISPLAY) {
                    Uri fileUri = data.getData();
                    selectedFileUris.add(fileUri);
                    displayFilePreview(fileUri);
                } else {
                    Toast.makeText(this, "Limite de " + MAX_FILES_DISPLAY + " arquivos atingido. O arquivo não foi adicionado.", Toast.LENGTH_LONG).show();
                }
            }

            if (!selectedFileUris.isEmpty()) {
                Toast.makeText(this, selectedFileUris.size() + " arquivo(s) selecionado(s).", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nenhum arquivo selecionado.", Toast.LENGTH_SHORT).show();
            }
            checkFormCompletion();
        }
    }

    private void displayFilePreview(Uri fileUri) {
        if (selectedFilesContainer == null) {
            Toast.makeText(this, "Erro interno: Container de arquivos não encontrado.", Toast.LENGTH_LONG).show();
            return;
        }

        LinearLayout fileEntryLayout = new LinearLayout(this);
        fileEntryLayout.setOrientation(LinearLayout.HORIZONTAL);
        fileEntryLayout.setPadding(8, 8, 8, 8);
        fileEntryLayout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.preview_icon_size),
                (int) getResources().getDimension(R.dimen.preview_icon_size));
        imageParams.setMarginEnd((int) getResources().getDimension(R.dimen.margin_small));
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        String fileName = "Arquivo Desconhecido";
        String mimeType = getContentResolver().getType(fileUri);

        if (fileUri != null) {
            Cursor cursor = getContentResolver().query(fileUri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        textView.setText(fileName);

        if (mimeType != null && mimeType.startsWith("image/")) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                Toast.makeText(this, "Erro ao carregar prévia da imagem.", Toast.LENGTH_SHORT).show();
            }
        } else if (mimeType != null && mimeType.equals("application/pdf")) {
            imageView.setImageResource(android.R.drawable.ic_menu_agenda);
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_upload);
        }

        fileEntryLayout.addView(imageView);
        fileEntryLayout.addView(textView);

        selectedFilesContainer.addView(fileEntryLayout);
    }

    private void checkFormCompletion() {
        boolean isNameFilled = !inputName.getText().toString().trim().isEmpty();
        boolean isDateOfBirthFilled = !inputDateOfBirth.getText().toString().trim().isEmpty(); // Check if date field is filled
        boolean isGenderSelected = spinnerGender.getSelectedItemPosition() > 0;

        boolean isDeficiencyHandled = false;
        String selectedDeficiencyOption = spinnerDeficiencia.getSelectedItem().toString();
        if (selectedDeficiencyOption.equals("Não")) {
            isDeficiencyHandled = true;
        } else if (selectedDeficiencyOption.equals("Sim")) {
            boolean isTypeOfDeficiencySelected = spinnerTipoDeficiencia.getSelectedItemPosition() > 0;
            String selectedTypeOfDeficiency = spinnerTipoDeficiencia.getSelectedItem().toString();

            if (selectedTypeOfDeficiency.equals("Outra") || selectedTypeOfDeficiency.equals("Múltipla")) {
                isDeficiencyHandled = isTypeOfDeficiencySelected && !editTextDescricaoOutraDeficiencia.getText().toString().trim().isEmpty();
            } else {
                isDeficiencyHandled = isTypeOfDeficiencySelected;
            }
        }

        boolean areEnoughFilesUploaded = selectedFileUris.size() >= MIN_REQUIRED_FILES;

        boolean isFormComplete = isNameFilled && isDateOfBirthFilled && isGenderSelected && isDeficiencyHandled && areEnoughFilesUploaded;

        if (btnEnviar != null) {
            btnEnviar.setEnabled(isFormComplete);
            if (isFormComplete) {
                btnEnviar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
            } else {
                btnEnviar.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
            }
        }
    }

    private boolean isFormValid() {
        boolean isNameFilled = !inputName.getText().toString().trim().isEmpty();
        boolean isDateOfBirthFilled = !inputDateOfBirth.getText().toString().trim().isEmpty();
        boolean isGenderSelected = spinnerGender.getSelectedItemPosition() > 0;

        boolean isDeficiencyValid = false;
        String selectedDeficiencyOption = spinnerDeficiencia.getSelectedItem().toString();
        if (selectedDeficiencyOption.equals("Não")) {
            isDeficiencyValid = true;
        } else if (selectedDeficiencyOption.equals("Sim")) {
            boolean isTypeOfDeficiencySelected = spinnerTipoDeficiencia.getSelectedItemPosition() > 0;
            String selectedTypeOfDeficiency = spinnerTipoDeficiencia.getSelectedItem().toString();
            if (selectedTypeOfDeficiency.equals("Outra") || selectedTypeOfDeficiency.equals("Múltipla")) {
                isDeficiencyValid = isTypeOfDeficiencySelected && !editTextDescricaoOutraDeficiencia.getText().toString().trim().isEmpty();
            } else {
                isDeficiencyValid = isTypeOfDeficiencySelected;
            }
        }

        boolean areEnoughFilesUploaded = selectedFileUris.size() >= MIN_REQUIRED_FILES;

        return isNameFilled && isDateOfBirthFilled && isGenderSelected && isDeficiencyValid && areEnoughFilesUploaded;
    }
}