package com.example.apptrace;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrazarRutaActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String RESULT_LATS = "lats";
    public static final String RESULT_LNGS = "lngs";
    public static final String RESULT_DIST = "distancia_km";

    private GoogleMap       googleMap;
    private Polyline        polyline;
    private final List<LatLng> puntos  = new ArrayList<>();
    private final List<Marker> markers = new ArrayList<>();

    private TextView tvPuntCount, tvDistancia;
    private EditText etBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trazar_ruta);

        tvPuntCount = findViewById(R.id.tv_point_count);
        tvDistancia = findViewById(R.id.tv_trazar_distancia);
        etBuscar    = findViewById(R.id.et_trazar_buscar);

        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_trazar);
        if (mapFrag != null) mapFrag.getMapAsync(this);

        findViewById(R.id.fl_trazar_back).setOnClickListener(v -> finish());

        // Búsqueda de calles / lugares
        findViewById(R.id.btn_trazar_buscar).setOnClickListener(v -> buscarLugar());
        etBuscar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { buscarLugar(); return true; }
            return false;
        });

        findViewById(R.id.btn_trazar_deshacer).setOnClickListener(v -> deshacerUltimo());

        findViewById(R.id.btn_trazar_limpiar).setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this, R.style.Theme_Trace_Dialog)
                    .setTitle("Limpiar ruta")
                    .setMessage("¿Eliminar todos los puntos?")
                    .setPositiveButton("Limpiar", (d, w) -> limpiarTodo())
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        findViewById(R.id.btn_trazar_listo).setOnClickListener(v -> confirmarListo());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;

        try {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark));
        } catch (Exception ignored) { }

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        // Zoom a El Salvador por defecto si no hay ubicación
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(13.7942, -88.8965), 8f));

        // Cada tap en el mapa agrega un punto
        googleMap.setOnMapClickListener(this::agregarPunto);
    }

    // ─── Búsqueda de calles ───────────────────────────────────────────────────

    private void buscarLugar() {
        String query = etBuscar.getText().toString().trim();
        if (query.isEmpty()) return;

        // Cerrar teclado
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(etBuscar.getWindowToken(), 0);

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> resultados = geocoder.getFromLocationName(query, 1);
            if (resultados != null && !resultados.isEmpty()) {
                Address addr = resultados.get(0);
                LatLng ll = new LatLng(addr.getLatitude(), addr.getLongitude());
                googleMap.animateCamera(
                        com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(ll, 16f));
                Toast.makeText(this, addr.getAddressLine(0), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lugar no encontrado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al buscar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // ─── Manejo de puntos ─────────────────────────────────────────────────────

    private void agregarPunto(LatLng ll) {
        puntos.add(ll);

        // Marcador: inicio = verde, resto = azul
        MarkerOptions markerOpts = new MarkerOptions()
                .position(ll)
                .title("Punto " + puntos.size());

        if (puntos.size() == 1) {
            markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            markerOpts.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }

        markers.add(googleMap.addMarker(markerOpts));

        // Actualizar/redibujar polilínea
        redibujarPolilinea();
        actualizarUI();
    }

    private void deshacerUltimo() {
        if (puntos.isEmpty()) return;
        puntos.remove(puntos.size() - 1);

        Marker ultimo = markers.remove(markers.size() - 1);
        if (ultimo != null) ultimo.remove();

        // El primer marcador recupera color verde si hay puntos
        if (!markers.isEmpty() && markers.get(0) != null && puntos.size() == 1) {
            // ya es verde desde cuando se creó, ok
        }

        redibujarPolilinea();
        actualizarUI();
    }

    private void limpiarTodo() {
        for (Marker m : markers) if (m != null) m.remove();
        markers.clear();
        puntos.clear();
        if (polyline != null) polyline.remove();
        polyline = null;
        actualizarUI();
    }

    private void redibujarPolilinea() {
        if (polyline != null) polyline.remove();
        if (puntos.size() < 2) { polyline = null; return; }

        polyline = googleMap.addPolyline(new PolylineOptions()
                .addAll(puntos)
                .color(Color.parseColor("#39FF14"))
                .width(8f)
                .geodesic(true));
    }

    private void actualizarUI() {
        tvPuntCount.setText(puntos.size() + " punto" + (puntos.size() == 1 ? "" : "s"));

        double km = calcularDistanciaKm();
        tvDistancia.setText(String.format(Locale.getDefault(), "%.2f km", km));
    }

    // ─── Calcular distancia total ──────────────────────────────────────────────

    private double calcularDistanciaKm() {
        if (puntos.size() < 2) return 0;
        float[] result = new float[1];
        double total = 0;
        for (int i = 0; i < puntos.size() - 1; i++) {
            LatLng a = puntos.get(i);
            LatLng b = puntos.get(i + 1);
            Location.distanceBetween(a.latitude, a.longitude,
                    b.latitude, b.longitude, result);
            total += result[0];
        }
        return total / 1000.0;
    }

    // ─── Confirmar y devolver ─────────────────────────────────────────────────

    private void confirmarListo() {
        if (puntos.size() < 2) {
            Toast.makeText(this,
                    "Agrega al menos 2 puntos para trazar una ruta",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Double> lats = new ArrayList<>();
        ArrayList<Double> lngs = new ArrayList<>();
        for (LatLng ll : puntos) {
            lats.add(ll.latitude);
            lngs.add(ll.longitude);
        }

        Intent result = new Intent();
        result.putExtra(RESULT_LATS, lats);
        result.putExtra(RESULT_LNGS, lngs);
        result.putExtra(RESULT_DIST, calcularDistanciaKm());
        setResult(RESULT_OK, result);
        finish();
    }
}
