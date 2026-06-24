package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.route.EditRouteRequest;
import com.example.apptrace.model.route.RouteData;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarRutaActivity extends AppCompatActivity {

    public static final String EXTRA_RUTA_ID = "ruta_id";
    public static final String EXTRA_RUTA    = "ruta_data";

    private TextInputEditText    etNombre, etDesc;
    private AutoCompleteTextView actSport, actDiff, actPrivacy;
    private MaterialButton       btnGuardar, btnEliminar;

    private ApiService apiService;
    private int        rutaId;
    private RouteData  rutaData;

    private static final String[] DEPORTES       = {"Carrera","Caminata","Ciclismo","Natación","Senderismo","Montañismo","Otro"};
    private static final String[] DEPORTE_KEYS   = {"carrera","caminata","ciclismo","natacion","senderismo","montanismo","otro"};
    private static final String[] DIFICULTADES   = {"1 - Muy fácil","2 - Fácil","3 - Moderado","4 - Difícil","5 - Muy difícil","6 - Extremo"};
    private static final int[]    DIFICULTAD_INT = {1,2,3,4,5,6};
    private static final String[] PRIVACIDADES    = {"Público","Amigos","Privado"};
    private static final String[] PRIVACIDAD_KEYS = {"publico","amigos","privado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ruta);

        etNombre   = findViewById(R.id.et_nombre);
        etDesc     = findViewById(R.id.et_desc);
        actSport   = findViewById(R.id.act_sport);
        actDiff    = findViewById(R.id.act_diff);
        actPrivacy = findViewById(R.id.act_privacy);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnEliminar= findViewById(R.id.btn_eliminar);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        rutaId   = getIntent().getIntExtra(EXTRA_RUTA_ID, -1);
        rutaData = (RouteData) getIntent().getSerializableExtra(EXTRA_RUTA);

        actSport.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, DEPORTES));
        actDiff.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, DIFICULTADES));
        actPrivacy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, PRIVACIDADES));

        // Pre-llenar con datos actuales
        if (rutaData != null) prefill(rutaData);

        findViewById(R.id.iv_close).setOnClickListener(v -> finish());
        findViewById(R.id.tv_save).setOnClickListener(v -> guardar());
        btnGuardar.setOnClickListener(v -> guardar());
        btnEliminar.setOnClickListener(v -> confirmarEliminar());
    }

    private void prefill(RouteData r) {
        if (r.getNombre()    != null) etNombre.setText(r.getNombre());
        if (r.getDescripcion() != null) etDesc.setText(r.getDescripcion());

        // Deporte
        String tipoKey = r.getTipo() != null ? r.getTipo() : "carrera";
        for (int i = 0; i < DEPORTE_KEYS.length; i++) {
            if (DEPORTE_KEYS[i].equals(tipoKey)) { actSport.setText(DEPORTES[i], false); break; }
        }
        if (actSport.getText().toString().isEmpty()) actSport.setText(DEPORTES[0], false);

        // Dificultad: el backend devuelve el entero como String ("4") o como número
        String difStr = r.getDificultad();
        int difIdx = 2; // default moderado
        if (difStr != null) {
            try {
                int val = Integer.parseInt(difStr.trim());
                if (val >= 1 && val <= 6) difIdx = val - 1;
            } catch (NumberFormatException ignored) { }
        }
        actDiff.setText(DIFICULTADES[difIdx], false);

        // Privacidad — RouteData no tiene campo privacidad, default Público
        actPrivacy.setText(PRIVACIDADES[0], false);
    }

    private void guardar() {
        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String desc   = etDesc.getText()   != null ? etDesc.getText().toString().trim()   : "";

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rutaId < 0) {
            Toast.makeText(this, "Error: ruta sin ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String deporte    = selDeporte();
        int    dificultad = selDificultad();
        String privacidad = selPrivacidad();

        btnGuardar.setEnabled(false);
        btnGuardar.setText("Guardando...");

        EditRouteRequest req = new EditRouteRequest(nombre, desc, dificultad, deporte, privacidad);
        apiService.editarRuta(rutaId, req).enqueue(new Callback<ApiResponse<RouteData>>() {
            @Override
            public void onResponse(Call<ApiResponse<RouteData>> call,
                                   Response<ApiResponse<RouteData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(EditarRutaActivity.this, "Ruta actualizada", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    btnGuardar.setEnabled(true);
                    btnGuardar.setText("Guardar cambios");
                    Toast.makeText(EditarRutaActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RouteData>> call, Throwable t) {
                btnGuardar.setEnabled(true);
                btnGuardar.setText("Guardar cambios");
                Toast.makeText(EditarRutaActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarEliminar() {
        new AlertDialog.Builder(this, R.style.Theme_Trace_Dialog)
                .setTitle("Eliminar ruta")
                .setMessage("¿Seguro que quieres eliminar esta ruta? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (d, w) -> eliminar())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminar() {
        btnEliminar.setEnabled(false);
        apiService.eliminarRuta(rutaId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {
                Toast.makeText(EditarRutaActivity.this, "Ruta eliminada", Toast.LENGTH_SHORT).show();
                // Volver a la lista de rutas
                Intent intent = new Intent(EditarRutaActivity.this, RutasActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                btnEliminar.setEnabled(true);
                Toast.makeText(EditarRutaActivity.this,
                        "Error al eliminar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String selDeporte() {
        String sel = actSport.getText().toString();
        for (int i = 0; i < DEPORTES.length; i++) if (DEPORTES[i].equals(sel)) return DEPORTE_KEYS[i];
        return "carrera";
    }

    private int selDificultad() {
        String sel = actDiff.getText().toString();
        for (int i = 0; i < DIFICULTADES.length; i++) if (DIFICULTADES[i].equals(sel)) return DIFICULTAD_INT[i];
        return 3;
    }

    private String selPrivacidad() {
        String sel = actPrivacy.getText().toString();
        for (int i = 0; i < PRIVACIDADES.length; i++) if (PRIVACIDADES[i].equals(sel)) return PRIVACIDAD_KEYS[i];
        return "publico";
    }
}
