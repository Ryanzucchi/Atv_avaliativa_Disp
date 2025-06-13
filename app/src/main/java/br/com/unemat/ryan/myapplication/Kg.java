package br.com.unemat.ryan.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Kg extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    BottomNavigationView bottomNavigationView;
    private ListView listaCreches;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kidengardens);

        // 1. Inicializa a ListView
        listaCreches = findViewById(R.id.list_view_creches);
        if (listaCreches == null) {
            Log.e("KgActivity", "ListView 'list_view_creches' não encontrada.");
            Toast.makeText(this, "Erro: ListView não carregada.", Toast.LENGTH_SHORT).show();
        }

        // Lista de creches (exemplo)
        List<String> creches = Arrays.asList(
                "Creche A - Rua A - Bairro A - número 0 - cidade A - Estado A",
                "Creche B - Rua B - Bairro B - número 1 - cidade B - Estado B",
                "Creche C - Rua C - Bairro C - número 2 - cidade C - Estado C"
        );

        // Configura o adapter para a ListView
        CrecheAdapter adapter = new CrecheAdapter(this, creches);
        listaCreches.setAdapter(adapter);

        // 2. Inicializa o BottomNavigationView
        bottomNavigationView = findViewById(R.id.dashbottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_dashboard) { // Item "Dashboard" -> ActivityMain
                        startActivity(new Intent(Kg.this, ActivityMain.class));
                        // finish(); // Opcional: finalize a atividade atual se não quiser voltar para ela
                        return true;
                    } else if (itemId == R.id.nav_register) { // Item "Registrar Criança" -> KidRegistry
                        startActivity(new Intent(Kg.this, KidRegistry.class));
                        // finish();
                        return true;
                    }
                    return false;
                }
            });

            // CORREÇÃO: Define o item "Creches" (nav_settings) como selecionado ao iniciar a Kg Activity
            bottomNavigationView.setSelectedItemId(R.id.nav_settings);
            Log.d("KgActivity", "setSelectedItemId chamado para R.id.nav_settings (Creches).");

        } else {
            Log.e("KgActivity", "BottomNavigationView 'dashbottom_navigation' não encontrado em kidengardens.xml");
            Toast.makeText(this, "Erro interno: Navegação não disponível.", Toast.LENGTH_LONG).show();
        }

        // 3. Inicializa o fragmento do Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            Log.d("KgActivity", "Fragmento do mapa encontrado, solicitando o mapa assincronamente.");
        } else {
            Log.e("KgActivity", "Fragmento do mapa 'map_fragment' não encontrado no layout.");
            Toast.makeText(this, "Erro: Componente do mapa não carregado.", Toast.LENGTH_LONG).show();
        }

        // 4. Inicializa o FusedLocationProviderClient (para obter localização atual)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 5. Inicializa o campo de busca
        searchEditText = findViewById(R.id.input_pass);
        if (searchEditText != null) {
            searchEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch();
                    return true;
                }
                return false;
            });
        } else {
            Log.e("KgActivity", "EditText 'input_pass' (campo de busca) não encontrado.");
            Toast.makeText(this, "Erro: Campo de busca não carregado.", Toast.LENGTH_LONG).show();
        }
    }

    // Callback quando o mapa está pronto para ser usado
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        Log.d("KgActivity", "GoogleMap está pronto.");

        // Verifica e solicita permissões de localização
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("KgActivity", "Permissão de localização já concedida. Habilitando minha localização.");
            enableMyLocation();
        } else {
            Log.d("KgActivity", "Permissão de localização não concedida. Solicitando permissão.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Habilita a camada "Minha Localização" no mapa
    private void enableMyLocation() {
        if (mGoogleMap != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
                getLastLocation();
            } else {
                Toast.makeText(this, "Permissão de localização não concedida. Não é possível mostrar sua posição no mapa.", Toast.LENGTH_SHORT).show();
                Log.w("KgActivity", "Tentativa de habilitar minha localização sem permissão.");
            }
        }
    }

    // Tenta obter a última localização conhecida
    private void getLastLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                            Log.d("KgActivity", "Localização atual obtida: " + currentLatLng.latitude + ", " + currentLatLng.longitude);
                        } else {
                            Toast.makeText(Kg.this, "Não foi possível obter a localização atual. Verifique se o GPS está ativado e tente novamente.", Toast.LENGTH_LONG).show();
                            Log.w("KgActivity", "getLastLocation retornou null.");
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        Toast.makeText(Kg.this, "Erro ao obter localização: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("KgActivity", "Erro ao obter localização FusedLocationProviderClient", e);
                    });
        } catch (SecurityException e) {
            Log.e("KgActivity", "SecurityException: Permissão de localização não concedida ou problema ao acessar getLastLocation.", e);
            Toast.makeText(this, "Permissão de localização é necessária.", Toast.LENGTH_LONG).show();
        }
    }

    // Lida com o resultado da solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("KgActivity", "Permissão de localização concedida pelo usuário.");
                enableMyLocation();
            } else {
                Toast.makeText(this, "Permissão de localização é necessária para exibir sua posição no mapa.", Toast.LENGTH_LONG).show();
                Log.d("KgActivity", "Permissão de localização negada pelo usuário.");
            }
        }
    }

    // Executa a busca no mapa com base no texto do campo de busca
    private void performSearch() {
        String searchText = searchEditText.getText().toString().trim();
        if (searchText.isEmpty()) {
            Toast.makeText(this, "Por favor, digite um local para buscar.", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(searchText, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng searchLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                if (mGoogleMap != null) {
                    mGoogleMap.clear();
                    mGoogleMap.addMarker(new MarkerOptions().position(searchLatLng).title(address.getAddressLine(0)));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLatLng, 15));
                    Toast.makeText(this, "Buscando por: " + address.getAddressLine(0), Toast.LENGTH_SHORT).show();
                    Log.d("KgActivity", "Busca bem-sucedida para: " + searchText);
                } else {
                    Toast.makeText(this, "Mapa não está pronto para a busca.", Toast.LENGTH_SHORT).show();
                    Log.w("KgActivity", "performSearch chamado, mas mGoogleMap é null.");
                }
            } else {
                Toast.makeText(this, "Nenhum resultado encontrado para: " + searchText, Toast.LENGTH_SHORT).show();
                Log.d("KgActivity", "Nenhum resultado de Geocoder para: " + searchText);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Erro na busca: Verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
            Log.e("KgActivity", "Erro no Geocoder para '" + searchText + "': " + e.getMessage(), e);
        }
    }
}