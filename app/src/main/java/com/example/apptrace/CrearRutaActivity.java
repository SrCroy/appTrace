package com.example.apptrace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.apptrace.model.activity.GpsBatch;
import com.example.apptrace.model.activity.GpsPointSend;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.route.RouteData;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearRutaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextInputEditText     etNombre, etDesc;
    private AutoCompleteTextView  actSport, actDiff, actPrivacy;
    private MaterialButton        btnPublicar;
    private TextView              tvGuardar, tvTraceInfo;
    private FrameLayout           flMapPreview, flMapPlaceholder;
    private MaterialButton        btnRetrazar;

    private ApiService  apiService;
    private GoogleMap   previewMap;
    private boolean     mapListo = false;

    // Puntos del trazado
    private ArrayList<Double> latsPuntos;
    private ArrayList<Double> lngsPuntos;
    private double            distanciaKm = 0;

    private ActivityResultLauncher<Intent> trazarLauncher;

    private static final String[] DEPORTES       = {"Carrera", "Caminata", "Ciclismo", "Natación", "Senderismo", "Montañismo", "Otro"};
    private static final String[] DEPORTE_KEYS   = {"carrera", "caminata", "ciclismo", "natacion", "senderismo", "montanismo", "otro"};
    private static final String[] DIFICULTADES   = {"1 - Muy fácil", "2 - Fácil", "3 - Moderado",
                                                     "4 - Difícil", "5 - Muy difícil", "6 - Extremo"};
    private static final int[]    DIFICULTAD_INT  = {1, 2, 3, 4, 5, 6};
    private static final String[] PRIVACIDADES    = {"Público", "Amigos", "Privado"};
    private static final String[] PRIVACIDAD_KEYS = {"publico", "amigos", "privado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ruta);

        etNombre       = findViewById(R.id.et_route_name);
        etDesc         = findViewById(R.id.et_route_desc);
        actSport       = findViewById(R.id.act_route_sport);
        actDiff        = findViewById(R.id.act_route_difficulty);
        actPrivacy     = findViewById(R.id.act_route_privacy);
        btnPublicar    = findViewById(R.id.btn_create_route_submit);
        tvGuardar      = findViewById(R.id.tv_save_route);
        flMapPreview   = findViewById(R.id.fl_map_preview);
        flMapPlaceholder = findViewById(R.id.fl_map_placeholder);
        btnRetrazar    = findViewById(R.id.btn_retrazar);

        // Inicializar mapa de preview (en background, listo cuando el usuario trace)
        SupportMapFragment mapFrag = SupportMapFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_map_preview, mapFrag);
        ft.commit();
        mapFrag.getMapAsync(this);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Dropdowns
        actSport.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, DEPORTES));
        actSport.setText(DEPORTES[0], false);

        actDiff.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, DIFICULTADES));
        actDiff.setText(DIFICULTADES[2], false);

        actPrivacy.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, PRIVACIDADES));
        actPrivacy.setText(PRIVACIDADES[0], false);

        // Launcher para volver de TrazarRutaActivity
        trazarLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        latsPuntos  = (ArrayList<Double>) result.getData()
                                .getSerializableExtra(TrazarRutaActivity.RESULT_LATS);
                        lngsPuntos  = (ArrayList<Double>) result.getData()
                                .getSerializableExtra(TrazarRutaActivity.RESULT_LNGS);
                        distanciaKm = result.getData().getDoubleExtra(
                                TrazarRutaActivity.RESULT_DIST, 0);
                        mostrarResumenTrazado();
                    }
                }
        );

        // Botón trazar (placeholder) y retrazar
        findViewById(R.id.btn_select_on_map).setOnClickListener(v ->
                trazarLauncher.launch(new Intent(this, TrazarRutaActivity.class)));
        btnRetrazar.setOnClickListener(v ->
                trazarLauncher.launch(new Intent(this, TrazarRutaActivity.class)));

        // Botón publicar
        btnPublicar.setOnClickListener(v -> publicarRuta());

        // X cerrar
        findViewById(R.id.iv_close).setOnClickListener(v -> finish());

        // TV guardar (también publica)
        tvGuardar.setOnClickListener(v -> publicarRuta());
    }

    private void mostrarResumenTrazado() {
        if (latsPuntos == null) return;

        // Cambiar de placeholder a mapa
        flMapPlaceholder.setVisibility(View.GONE);
        flMapPreview.setVisibility(View.VISIBLE);
        btnRetrazar.setVisibility(View.VISIBLE);

        // Dibujar polilínea si el mapa ya está listo
        if (mapListo) dibujarPreview();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        previewMap = gMap;
        mapListo   = true;
        try {
            previewMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark));
        } catch (Exception ignored) { }
        previewMap.getUiSettings().setAllGesturesEnabled(false);
        previewMap.getUiSettings().setMapToolbarEnabled(false);
        previewMap.getUiSettings().setZoomControlsEnabled(false);

        // Si el usuario ya trazó antes de que el mapa cargara
        if (latsPuntos != null) dibujarPreview();
    }

    private void dibujarPreview() {
        if (previewMap == null || latsPuntos == null || latsPuntos.size() < 2) return;

        previewMap.clear();
        PolylineOptions opts = new PolylineOptions()
                .color(Color.parseColor("#39FF14"))
                .width(8f)
                .geodesic(true);

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (int i = 0; i < latsPuntos.size(); i++) {
            LatLng ll = new LatLng(latsPuntos.get(i), lngsPuntos.get(i));
            opts.add(ll);
            bounds.include(ll);
        }
        previewMap.addPolyline(opts);
        try {
            previewMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(bounds.build(), 60));
        } catch (Exception ignored) { }
    }

    private void publicarRuta() {
        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String desc   = etDesc.getText()   != null ? etDesc.getText().toString().trim()   : "";

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingresa un nombre para la ruta", Toast.LENGTH_SHORT).show();
            return;
        }
        if (latsPuntos == null || latsPuntos.size() < 2) {
            Toast.makeText(this,
                    "Traza la ruta en el mapa primero (mínimo 2 puntos)",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String deporte      = claveDeporte();
        int    dificultad   = claveDificultad();
        String privacidad   = clavePrivacidad();

        btnPublicar.setEnabled(false);
        btnPublicar.setText("Publicando...");

        MediaType plain = MediaType.parse("text/plain");
        RequestBody rNombre    = RequestBody.create(plain, nombre);
        RequestBody rDesc      = RequestBody.create(plain, desc);
        RequestBody rDificultad= RequestBody.create(plain, String.valueOf(dificultad));
        RequestBody rTipo      = RequestBody.create(plain, deporte);
        RequestBody rDist      = RequestBody.create(plain,
                String.format(Locale.US, "%.2f", distanciaKm));
        RequestBody rPriv      = RequestBody.create(plain, privacidad);

        apiService.crearRuta(rNombre, rDesc, rDificultad, rTipo, rDist, rPriv)
                .enqueue(new Callback<ApiResponse<RouteData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<RouteData>> call,
                                           Response<ApiResponse<RouteData>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()
                                && response.body().getData() != null) {
                            int rutaId = response.body().getData().getId();
                            enviarPuntosGps(rutaId);
                        } else {
                            resetBoton();
                            Toast.makeText(CrearRutaActivity.this,
                                    "Error al crear la ruta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<RouteData>> call, Throwable t) {
                        resetBoton();
                        Toast.makeText(CrearRutaActivity.this,
                                "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void enviarPuntosGps(int rutaId) {
        List<GpsPointSend> puntos = new ArrayList<>();
        for (int i = 0; i < latsPuntos.size(); i++) {
            puntos.add(new GpsPointSend(
                    latsPuntos.get(i), lngsPuntos.get(i),
                    0, 0, 0, i + 1, ""));
        }

        apiService.agregarPuntosRuta(rutaId, new GpsBatch(puntos))
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call,
                                           Response<ApiResponse<Object>> response) {
                        Toast.makeText(CrearRutaActivity.this,
                                "¡Ruta publicada!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        // La ruta ya se creó, solo fallaron los puntos GPS
                        Toast.makeText(CrearRutaActivity.this,
                                "Ruta creada pero sin puntos GPS",
                                Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private void resetBoton() {
        btnPublicar.setEnabled(true);
        btnPublicar.setText("Publicar Ruta");
    }

    private String claveDeporte() {
        String sel = actSport.getText().toString();
        for (int i = 0; i < DEPORTES.length; i++) {
            if (DEPORTES[i].equals(sel)) return DEPORTE_KEYS[i];
        }
        return "trail";
    }

    private int claveDificultad() {
        String sel = actDiff.getText().toString();
        for (int i = 0; i < DIFICULTADES.length; i++) {
            if (DIFICULTADES[i].equals(sel)) return DIFICULTAD_INT[i];
        }
        return 3;
    }

    private String clavePrivacidad() {
        String sel = actPrivacy.getText().toString();
        for (int i = 0; i < PRIVACIDADES.length; i++) {
            if (PRIVACIDADES[i].equals(sel)) return PRIVACIDAD_KEYS[i];
        }
        return "publico";
    }
}
