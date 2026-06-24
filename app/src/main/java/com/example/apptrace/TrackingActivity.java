package com.example.apptrace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.apptrace.model.activity.ActivityData;
import com.example.apptrace.model.activity.FinalizarData;
import com.example.apptrace.model.activity.GpsBatch;
import com.example.apptrace.model.activity.GpsPointData;
import com.example.apptrace.model.activity.GpsPointSend;
import com.example.apptrace.model.activity.IniciarActividadRequest;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.route.RouteDetail;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_TITULO       = "titulo";
    public static final String EXTRA_TIPO         = "tipo_deporte";
    public static final String EXTRA_DIFICULTAD   = "dificultad";
    public static final String EXTRA_PRIVACIDAD   = "privacidad";
    public static final String EXTRA_RUTA_ID      = "ruta_id";
    public static final String EXTRA_RESULT_DATA  = "finalizar_data";

    private static final int    GPS_BATCH_SIZE    = 10;
    private static final long   GPS_BATCH_INTERVAL = 30_000L;
    private static final int    POLYLINE_COLOR    = Color.parseColor("#39FF14"); // primary green

    // UI
    private TextView     tvTimer, tvDistancia, tvRitmo, tvDesnivel;
    private TextView     tvStatusLabel;
    private View         vStatusDot;
    private MaterialButton btnPause, btnStop;

    private static final String MAPS_API_KEY = "AIzaSyD7_z1Byfn5Yoj280LSGJZl8QTeRnPCvbw";

    // Map
    private GoogleMap googleMap;
    private Polyline  routePolyline;       // live tracking (neon green)
    private Polyline  guidePolyline;       // ruta planeada (azul, por calles)
    private Marker    userMarker;          // marcador de posición del usuario
    private final List<LatLng> routePoints = new ArrayList<>();
    private int rutaIdNav = -1;

    // GPS
    private FusedLocationProviderClient fusedClient;
    private LocationCallback            locationCallback;
    private Location                    lastLocation;
    private int                         sequenceCounter = 1;
    private final List<GpsPointSend>    pendingPoints   = new ArrayList<>();
    private double                      totalDistanceM  = 0;
    private double                      totalAltGain    = 0;
    private boolean                     hasCentered     = false;

    // Timer
    private final Handler  timerHandler    = new Handler(Looper.getMainLooper());
    private final Handler  batchHandler    = new Handler(Looper.getMainLooper());
    private       long     elapsedSeconds  = 0;
    private       boolean  isPaused        = false;
    private       boolean  isStarted       = false;

    // API
    private ApiService apiService;
    private int        actividadId = -1;

    private ActivityResultLauncher<String> locationPermLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        tvTimer      = findViewById(R.id.tv_main_timer);
        tvDistancia  = findViewById(R.id.tv_distancia);
        tvRitmo      = findViewById(R.id.tv_ritmo);
        tvDesnivel   = findViewById(R.id.tv_desnivel);
        tvStatusLabel = findViewById(R.id.tv_status_label);
        vStatusDot   = findViewById(R.id.v_status_dot);
        btnPause     = findViewById(R.id.btn_pause);
        btnStop      = findViewById(R.id.btn_stop);

        apiService  = RetrofitClient.getClient().create(ApiService.class);
        fusedClient = LocationServices.getFusedLocationProviderClient(this);

        locationPermLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        iniciarSesion();
                    } else {
                        Toast.makeText(this,
                                "Se necesita permiso de ubicación para rastrear",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
        );

        // Back button
        findViewById(R.id.iv_back).setOnClickListener(v -> confirmarDescartar());

        btnPause.setOnClickListener(v -> {
            if (isPaused) reanudar(); else pausar();
        });

        btnStop.setOnClickListener(v -> confirmarFinalizar());

        rutaIdNav = getIntent().getIntExtra(EXTRA_RUTA_ID, -1);

        // Setup map
        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFrag != null) mapFrag.getMapAsync(this);

        solicitarPermisoUbicacion();
    }

    // ─── Map ──────────────────────────────────────────────────────────────────

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;
        try {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark));
        } catch (Exception ignored) { }

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        routePolyline = googleMap.addPolyline(new PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(10f)
                .geodesic(true));

        if (isStarted) enableMyLocation();

        // Si venimos de una ruta, cargar guía
        if (rutaIdNav >= 0) cargarRutaGuia();
    }

    // ─── Ruta guía (Uber-like) ────────────────────────────────────────────────

    private void cargarRutaGuia() {
        apiService.obtenerRuta(rutaIdNav).enqueue(new Callback<ApiResponse<RouteDetail>>() {
            @Override
            public void onResponse(Call<ApiResponse<RouteDetail>> call,
                                   Response<ApiResponse<RouteDetail>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null) {
                    List<GpsPointData> puntos = response.body().getData().getPuntosGps();
                    if (puntos != null && !puntos.isEmpty()) {
                        runOnUiThread(() -> dibujarGuia(puntos));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<RouteDetail>> call, Throwable t) { }
        });
    }

    private void dibujarGuia(List<GpsPointData> puntos) {
        if (googleMap == null) return;

        // Convertir a LatLng
        List<LatLng> puntosLL = new ArrayList<>();
        LatLngBounds.Builder bb = new LatLngBounds.Builder();
        for (GpsPointData pt : puntos) {
            LatLng ll = new LatLng(pt.getLatitud(), pt.getLongitud());
            puntosLL.add(ll);
            bb.include(ll);
        }

        // Marcador inicio (verde)
        googleMap.addMarker(new MarkerOptions()
                .position(puntosLL.get(0))
                .title("Inicio")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        // Checkpoints intermedios (naranja) cada ~25%
        int segmento = Math.max(1, puntosLL.size() / 4);
        for (int i = segmento; i < puntosLL.size() - 1; i += segmento) {
            googleMap.addMarker(new MarkerOptions()
                    .position(puntosLL.get(i))
                    .title("Checkpoint")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }

        // Marcador meta (rojo)
        googleMap.addMarker(new MarkerOptions()
                .position(puntosLL.get(puntosLL.size() - 1))
                .title("Meta")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Centrar cámara en la ruta mientras el usuario no empieza a moverse
        if (!hasCentered) {
            try {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bb.build(), 80));
            } catch (Exception ignored) { }
        }

        // Dibujar línea recta de fallback (se reemplaza si la Directions API responde)
        guidePolyline = googleMap.addPolyline(new PolylineOptions()
                .addAll(puntosLL)
                .color(Color.parseColor("#664FC3F7"))
                .width(8f)
                .geodesic(true));

        // Pedir ruta por calles a Directions API
        obtenerRutaConCalles(puntos);
    }

    private void obtenerRutaConCalles(List<GpsPointData> puntos) {
        if (puntos.size() < 2) return;

        List<GpsPointData> muestra = downsample(puntos, 10);
        GpsPointData origen  = muestra.get(0);
        GpsPointData destino = muestra.get(muestra.size() - 1);

        StringBuilder url = new StringBuilder(
                "https://maps.googleapis.com/maps/api/directions/json");
        url.append("?origin=").append(
                String.format(Locale.US, "%.6f,%.6f",
                        origen.getLatitud(), origen.getLongitud()));
        url.append("&destination=").append(
                String.format(Locale.US, "%.6f,%.6f",
                        destino.getLatitud(), destino.getLongitud()));

        if (muestra.size() > 2) {
            url.append("&waypoints=");
            for (int i = 1; i < muestra.size() - 1; i++) {
                if (i > 1) url.append("|");
                url.append(String.format(Locale.US, "%.6f,%.6f",
                        muestra.get(i).getLatitud(), muestra.get(i).getLongitud()));
            }
        }
        url.append("&key=").append(MAPS_API_KEY);

        new OkHttpClient().newCall(
                new Request.Builder().url(url.toString()).build()
        ).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call,
                                   @NonNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) return;
                List<LatLng> pts = parseDirectionsPolyline(response.body().string());
                if (!pts.isEmpty()) {
                    runOnUiThread(() -> {
                        if (guidePolyline != null) guidePolyline.remove();
                        if (googleMap == null) return;
                        guidePolyline = googleMap.addPolyline(new PolylineOptions()
                                .addAll(pts)
                                .color(Color.parseColor("#994FC3F7"))
                                .width(14f)
                                .geodesic(false));
                    });
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                // la línea de fallback (recta) sigue visible
            }
        });
    }

    private List<LatLng> parseDirectionsPolyline(String json) {
        try {
            JSONArray routes = new JSONObject(json).getJSONArray("routes");
            if (routes.length() == 0) return new ArrayList<>();
            String encoded = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points");
            return decodePolyline(encoded);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> pts = new ArrayList<>();
        int idx = 0, len = encoded.length(), lat = 0, lng = 0;
        while (idx < len) {
            int b, shift = 0, res = 0;
            do { b = encoded.charAt(idx++) - 63; res |= (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            lat += ((res & 1) != 0 ? ~(res >> 1) : (res >> 1));
            shift = 0; res = 0;
            do { b = encoded.charAt(idx++) - 63; res |= (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            lng += ((res & 1) != 0 ? ~(res >> 1) : (res >> 1));
            pts.add(new LatLng(lat / 1E5, lng / 1E5));
        }
        return pts;
    }

    private List<GpsPointData> downsample(List<GpsPointData> pts, int max) {
        if (pts.size() <= max) return new ArrayList<>(pts);
        List<GpsPointData> out = new ArrayList<>();
        float step = (float)(pts.size() - 1) / (max - 1);
        for (int i = 0; i < max; i++) out.add(pts.get(Math.round(i * step)));
        return out;
    }

    // ─── Permisos y arranque ──────────────────────────────────────────────────

    private void solicitarPermisoUbicacion() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            iniciarSesion();
        } else {
            locationPermLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void iniciarSesion() {
        String titulo     = getIntent().getStringExtra(EXTRA_TITULO);
        String tipo       = getIntent().getStringExtra(EXTRA_TIPO);
        String dificultad = getIntent().getStringExtra(EXTRA_DIFICULTAD);
        String privacidad = getIntent().getStringExtra(EXTRA_PRIVACIDAD);

        if (titulo == null)     titulo     = "Actividad";
        if (tipo == null)       tipo       = "carrera";
        if (dificultad == null) dificultad = "3";
        if (privacidad == null) privacidad = "publico";

        IniciarActividadRequest req =
                new IniciarActividadRequest(titulo, tipo, dificultad, privacidad);

        int rutaId = getIntent().getIntExtra(EXTRA_RUTA_ID, -1);
        if (rutaId >= 0) req.setRutaId(rutaId);

        apiService.iniciarActividad(req).enqueue(new Callback<ApiResponse<ActivityData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ActivityData>> call,
                                   Response<ApiResponse<ActivityData>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess() && response.body().getData() != null) {
                    actividadId = response.body().getData().getId();
                    isStarted   = true;
                    arrancarTracking();
                } else {
                    String errorMsg = "No se pudo iniciar la actividad";
                    try {
                        if (response.errorBody() != null) {
                            org.json.JSONObject err =
                                    new org.json.JSONObject(response.errorBody().string());
                            if (err.has("message")) errorMsg = err.getString("message");
                        }
                    } catch (Exception ignored) { }
                    Toast.makeText(TrackingActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ActivityData>> call, Throwable t) {
                Toast.makeText(TrackingActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void arrancarTracking() {
        enableMyLocation();
        iniciarTimer();
        iniciarGps();
        programarEnvioLote();
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (googleMap == null) return;
        googleMap.setMyLocationEnabled(true);
    }

    // ─── Timer ────────────────────────────────────────────────────────────────

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedSeconds++;
            actualizarTimerUI();
            timerHandler.postDelayed(this, 1000);
        }
    };

    private void iniciarTimer() {
        timerHandler.post(timerRunnable);
    }

    private void detenerTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void actualizarTimerUI() {
        long h = elapsedSeconds / 3600;
        long m = (elapsedSeconds % 3600) / 60;
        long s = elapsedSeconds % 60;
        if (h > 0) {
            tvTimer.setText(String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s));
        } else {
            tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", m, s));
        }
    }

    // ─── GPS ──────────────────────────────────────────────────────────────────

    @SuppressLint("MissingPermission")
    private void iniciarGps() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                if (isPaused) return;
                for (Location loc : result.getLocations()) {
                    procesarPunto(loc);
                }
            }
        };

        LocationRequest req = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setMinUpdateDistanceMeters(5)
                .build();

        fusedClient.requestLocationUpdates(req, locationCallback, Looper.getMainLooper());
    }

    private void detenerGps() {
        if (locationCallback != null) {
            fusedClient.removeLocationUpdates(locationCallback);
        }
    }

    private void procesarPunto(Location loc) {
        LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
        routePoints.add(ll);

        if (routePolyline != null) routePolyline.setPoints(routePoints);

        if (googleMap != null) {
            float bearing = (lastLocation != null) ? lastLocation.bearingTo(loc) : 0f;

            // Marcador de posición del usuario (flecha azul que rota)
            if (userMarker == null) {
                userMarker = googleMap.addMarker(new MarkerOptions()
                        .position(ll)
                        .anchor(0.5f, 0.5f)
                        .flat(true)
                        .rotation(bearing)
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_AZURE))
                        .zIndex(10f));
            } else {
                userMarker.setPosition(ll);
                userMarker.setRotation(bearing);
            }

            // Cámara de navegación con tilt y bearing
            CameraPosition navCam = new CameraPosition.Builder()
                    .target(ll)
                    .zoom(hasCentered ? 17.5f : 16f)
                    .tilt(hasCentered ? 50f : 0f)
                    .bearing(bearing)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(navCam));
            hasCentered = true;
        }

        // Distance calculation
        if (lastLocation != null) {
            float dist = lastLocation.distanceTo(loc);
            totalDistanceM += dist;

            // Elevation gain
            if (loc.hasAltitude() && lastLocation.hasAltitude()) {
                double gain = loc.getAltitude() - lastLocation.getAltitude();
                if (gain > 0) totalAltGain += gain;
            }
        }
        lastLocation = loc;

        actualizarMetricasUI();

        GpsPointSend pt = new GpsPointSend(
                loc.getLatitude(),
                loc.getLongitude(),
                loc.hasAltitude() ? loc.getAltitude() : 0,
                loc.hasSpeed()    ? loc.getSpeed()    : 0,
                loc.getAccuracy(),
                sequenceCounter++,
                isoNow()
        );
        pendingPoints.add(pt);

        if (pendingPoints.size() >= GPS_BATCH_SIZE) {
            enviarLote();
        }
    }

    private void actualizarMetricasUI() {
        double km = totalDistanceM / 1000.0;
        tvDistancia.setText(String.format(Locale.getDefault(), "%.2f", km));

        // Pace in min/km
        if (km > 0.05 && elapsedSeconds > 0) {
            double minPerKm = (elapsedSeconds / 60.0) / km;
            int minInt = (int) minPerKm;
            int secInt = (int) ((minPerKm - minInt) * 60);
            tvRitmo.setText(String.format(Locale.getDefault(), "%d:%02d", minInt, secInt));
        }

        tvDesnivel.setText(String.format(Locale.getDefault(), "+%d", (int) totalAltGain));
    }

    // ─── Lote GPS ─────────────────────────────────────────────────────────────

    private final Runnable batchRunnable = new Runnable() {
        @Override
        public void run() {
            enviarLote();
            batchHandler.postDelayed(this, GPS_BATCH_INTERVAL);
        }
    };

    private void programarEnvioLote() {
        batchHandler.postDelayed(batchRunnable, GPS_BATCH_INTERVAL);
    }

    private void enviarLote() {
        if (pendingPoints.isEmpty() || actividadId < 0) return;
        List<GpsPointSend> lote = new ArrayList<>(pendingPoints);
        pendingPoints.clear();

        apiService.enviarGps(actividadId, new GpsBatch(lote))
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call,
                                           Response<ApiResponse<Object>> response) { }
                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        // Re-add failed points so they are resent next time
                        pendingPoints.addAll(0, lote);
                    }
                });
    }

    // ─── Pausar / Reanudar ────────────────────────────────────────────────────

    private void pausar() {
        isPaused = true;
        detenerTimer();
        detenerGps();
        btnPause.setText("Reanudar");
        tvStatusLabel.setText("PAUSADA");
        vStatusDot.setBackgroundTintList(
                ContextCompat.getColorStateList(this, R.color.accent));

        if (actividadId >= 0) {
            apiService.pausarActividad(actividadId)
                    .enqueue(new Callback<ApiResponse<Object>>() {
                        @Override public void onResponse(Call<ApiResponse<Object>> c,
                                                         Response<ApiResponse<Object>> r) { }
                        @Override public void onFailure(Call<ApiResponse<Object>> c, Throwable t) { }
                    });
        }
    }

    private void reanudar() {
        isPaused = false;
        iniciarTimer();
        iniciarGps();
        btnPause.setText(getString(R.string.btn_pause));
        tvStatusLabel.setText(getString(R.string.tracking_status_running));
        vStatusDot.setBackgroundTintList(
                ContextCompat.getColorStateList(this, R.color.primary));

        if (actividadId >= 0) {
            apiService.reanudarActividad(actividadId)
                    .enqueue(new Callback<ApiResponse<Object>>() {
                        @Override public void onResponse(Call<ApiResponse<Object>> c,
                                                         Response<ApiResponse<Object>> r) { }
                        @Override public void onFailure(Call<ApiResponse<Object>> c, Throwable t) { }
                    });
        }
    }

    // ─── Finalizar / Descartar ────────────────────────────────────────────────

    private void confirmarFinalizar() {
        new AlertDialog.Builder(this, R.style.Theme_Trace_Dialog)
                .setTitle("Finalizar actividad")
                .setMessage("¿Deseas terminar y guardar esta actividad?")
                .setPositiveButton("Finalizar", (d, w) -> finalizar())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarDescartar() {
        new AlertDialog.Builder(this, R.style.Theme_Trace_Dialog)
                .setTitle("Descartar actividad")
                .setMessage("¿Seguro que quieres descartar? Se perderán todos los datos.")
                .setPositiveButton("Descartar", (d, w) -> descartar())
                .setNegativeButton("Seguir", null)
                .show();
    }

    private void finalizar() {
        detenerTimer();
        detenerGps();
        batchHandler.removeCallbacks(batchRunnable);
        enviarLote();

        if (actividadId < 0) { finish(); return; }

        apiService.finalizarActividad(actividadId)
                .enqueue(new Callback<ApiResponse<FinalizarData>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<FinalizarData>> call,
                                           Response<ApiResponse<FinalizarData>> response) {
                        FinalizarData data = null;
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            data = response.body().getData();
                        }
                        Intent intent = new Intent(TrackingActivity.this, ResumenActivity.class);
                        if (data != null) {
                            intent.putExtra(ResumenActivity.EXTRA_FINALIZAR, data);
                        }
                        intent.putStringArrayListExtra(ResumenActivity.EXTRA_TIPO,
                                new java.util.ArrayList<>(List.of(
                                        getIntent().getStringExtra(EXTRA_TITULO),
                                        getIntent().getStringExtra(EXTRA_TIPO))));
                        intent.putExtra(ResumenActivity.EXTRA_ELAPSED, elapsedSeconds);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<FinalizarData>> call, Throwable t) {
                        Toast.makeText(TrackingActivity.this,
                                "Error al finalizar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void descartar() {
        detenerTimer();
        detenerGps();
        batchHandler.removeCallbacks(batchRunnable);

        if (actividadId >= 0) {
            apiService.descartarActividad(actividadId)
                    .enqueue(new Callback<ApiResponse<Object>>() {
                        @Override public void onResponse(Call<ApiResponse<Object>> c,
                                                         Response<ApiResponse<Object>> r) { }
                        @Override public void onFailure(Call<ApiResponse<Object>> c, Throwable t) { }
                    });
        }
        finish();
    }

    // ─── Ciclo de vida ────────────────────────────────────────────────────────

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerTimer();
        detenerGps();
        batchHandler.removeCallbacks(batchRunnable);
    }

    @Override
    public void onBackPressed() {
        confirmarDescartar();
    }

    // ─── Util ─────────────────────────────────────────────────────────────────

    private String isoNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}
