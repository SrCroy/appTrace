package com.example.apptrace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.apptrace.model.activity.GpsPointData;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.route.RouteData;
import com.example.apptrace.model.route.RouteDetail;
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

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleRutaActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_RUTA_ID = "ruta_id";
    public static final String EXTRA_RUTA    = "ruta_data";

    private TextView tvTitle, tvDifficulty, tvSport, tvDistance, tvDescription;
    private TextView tvStatDistance, tvStatElevation;

    private ApiService apiService;
    private GoogleMap  googleMap;
    private int        rutaId;
    private RouteData  rutaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_ruta);

        tvTitle        = findViewById(R.id.tv_route_title);
        tvDifficulty   = findViewById(R.id.tv_difficulty);
        tvSport        = findViewById(R.id.tv_sport);
        tvStatDistance = findViewById(R.id.tv_stat_distance);
        tvStatElevation= findViewById(R.id.tv_stat_elevation);
        tvDescription  = findViewById(R.id.tv_description);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        rutaId   = getIntent().getIntExtra(EXTRA_RUTA_ID, -1);
        rutaData = (RouteData) getIntent().getSerializableExtra(EXTRA_RUTA);

        // Pre-fill from cached data
        if (rutaData != null) poblarBasico(rutaData);

        // Back
        findViewById(R.id.fl_back).setOnClickListener(v -> finish());

        // Editar ruta
        findViewById(R.id.fl_edit_route).setOnClickListener(v -> {
            Intent editIntent = new Intent(this, EditarRutaActivity.class);
            editIntent.putExtra(EditarRutaActivity.EXTRA_RUTA_ID, rutaId);
            editIntent.putExtra(EditarRutaActivity.EXTRA_RUTA, rutaData);
            startActivityForResult(editIntent, 1);
        });

        // Start route → launch tracking with this route
        findViewById(R.id.btn_start_route).setOnClickListener(v -> {
            Intent intent = new Intent(this, TrackingActivity.class);
            intent.putExtra(TrackingActivity.EXTRA_TITULO,
                    rutaData != null ? rutaData.getNombre() : "Ruta");
            intent.putExtra(TrackingActivity.EXTRA_TIPO,
                    tipoParaActividad(rutaData != null ? rutaData.getTipo() : null));
            intent.putExtra(TrackingActivity.EXTRA_DIFICULTAD,
                    rutaData != null ? rutaData.getDificultad() : "3");
            intent.putExtra(TrackingActivity.EXTRA_PRIVACIDAD, "publico");
            intent.putExtra(TrackingActivity.EXTRA_RUTA_ID, rutaId);
            startActivity(intent);
        });

        // Mapa
        SupportMapFragment mapFrag = SupportMapFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_map_detalle, mapFrag);
        ft.commit();
        mapFrag.getMapAsync(this);

        // Reload after edit
        if (getIntent().getBooleanExtra("refreshed", false)) cargarDetalle();

        // Load full detail with GPS points
        if (rutaId >= 0) cargarDetalle();
    }

    private void poblarBasico(RouteData r) {
        tvTitle.setText(r.getNombre());
        tvDifficulty.setText(dificultadLabel(r.getDificultad()));
        tvDifficulty.setTextColor(ContextCompat.getColor(this,
                dificultadColor(r.getDificultad())));
        tvSport.setText(tipoLabel(r.getTipo()));
        if (r.getDistanciaKm() != null) {
            tvStatDistance.setText(String.format(Locale.getDefault(),
                    "%.1f km", r.getDistanciaKm()));
        }
        if (r.getDescripcion() != null) {
            tvDescription.setText(r.getDescripcion());
        }
    }

    private void cargarDetalle() {
        apiService.obtenerRuta(rutaId).enqueue(new Callback<ApiResponse<RouteDetail>>() {
            @Override
            public void onResponse(Call<ApiResponse<RouteDetail>> call,
                                   Response<ApiResponse<RouteDetail>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null) {
                    RouteDetail detalle = response.body().getData();
                    poblarBasico(detalle);
                    dibujarRutaEnMapa(detalle.getPuntosGps());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RouteDetail>> call, Throwable t) {
                Toast.makeText(DetalleRutaActivity.this,
                        "Error al cargar ruta: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;
        try {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark));
        } catch (Exception ignored) { }
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void dibujarRutaEnMapa(List<GpsPointData> puntos) {
        if (googleMap == null || puntos == null || puntos.isEmpty()) return;

        PolylineOptions opts = new PolylineOptions()
                .color(Color.parseColor("#39FF14"))
                .width(8f)
                .geodesic(true);

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (GpsPointData pt : puntos) {
            LatLng ll = new LatLng(pt.getLatitud(), pt.getLongitud());
            opts.add(ll);
            boundsBuilder.include(ll);
        }
        googleMap.addPolyline(opts);

        try {
            LatLngBounds bounds = boundsBuilder.build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
        } catch (Exception ignored) { }
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    private String dificultadLabel(String d) {
        if (d == null) return "";
        switch (d) {
            case "1": return "Muy fácil";
            case "2": return "Fácil";
            case "3": return "Moderado";
            case "4": return "Difícil";
            case "5": return "Muy difícil";
            case "6": return "Extremo";
            default:  return d;
        }
    }

    private int dificultadColor(String d) {
        if (d == null) return R.color.text_secondary;
        switch (d) {
            case "1": case "2": return R.color.diff_easy;
            case "3":           return R.color.diff_medium;
            case "4": case "5":
            case "6":           return R.color.diff_hard;
            default:            return R.color.text_secondary;
        }
    }

    private String tipoLabel(String tipo) {
        if (tipo == null) return "";
        switch (tipo) {
            case "carrera":    return "Carrera";
            case "caminata":   return "Caminata";
            case "ciclismo":   return "Ciclismo";
            case "natacion":   return "Natación";
            case "senderismo": return "Senderismo";
            case "montanismo": return "Montañismo";
            case "otro":       return "Otro";
            default:           return tipo;
        }
    }

    // Convierte el tipo almacenado en la ruta al valor válido para actividades
    private String tipoParaActividad(String tipo) {
        if (tipo == null) return "carrera";
        switch (tipo) {
            case "carrera": case "caminata": case "ciclismo":
            case "natacion": case "senderismo": case "montanismo": case "otro":
                return tipo;
            // Valores legacy / incorrectos
            case "correr": case "trail": return "carrera";
            default: return "otro";
        }
    }
}
