package com.example.apptrace;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.apptrace.model.activity.FinalizarData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResumenActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_FINALIZAR = "finalizar_data";
    public static final String EXTRA_TIPO      = "tipo_list";
    public static final String EXTRA_ELAPSED   = "elapsed_seconds";
    public static final String EXTRA_POINTS    = "route_points";

    private TextView tvTitle, tvType, tvDate;
    private TextView tvDistancia, tvTiempo, tvRitmoProm, tvVelProm, tvDesnivel, tvCalorias;

    private FinalizarData finalizarData;
    private long elapsedSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        tvTitle     = findViewById(R.id.tv_activity_title);
        tvType      = findViewById(R.id.tv_activity_type);
        tvDate      = findViewById(R.id.tv_activity_date);
        tvDistancia = findViewById(R.id.tv_res_distancia);
        tvTiempo    = findViewById(R.id.tv_res_tiempo);
        tvRitmoProm = findViewById(R.id.tv_res_ritmo_prom);
        tvVelProm   = findViewById(R.id.tv_res_vel_prom);
        tvDesnivel  = findViewById(R.id.tv_res_desnivel);
        tvCalorias  = findViewById(R.id.tv_res_calorias);

        finalizarData  = (FinalizarData) getIntent().getSerializableExtra(EXTRA_FINALIZAR);
        elapsedSeconds = getIntent().getLongExtra(EXTRA_ELAPSED, 0);

        ArrayList<String> tipoList = getIntent().getStringArrayListExtra(EXTRA_TIPO);
        String titulo = tipoList != null && tipoList.size() > 0 ? tipoList.get(0) : "Actividad";
        String tipo   = tipoList != null && tipoList.size() > 1 ? tipoList.get(1) : "";

        tvTitle.setText(titulo != null ? titulo : "Actividad");
        tvType.setText(tipoLabel(tipo));
        tvDate.setText(new SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault())
                .format(new Date()));

        poblarMetricas();

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_share_activity).setOnClickListener(v ->
                Toast.makeText(this, "Compartir — próximamente", Toast.LENGTH_SHORT).show());

        // Mapa
        SupportMapFragment mapFrag = SupportMapFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_map_resumen, mapFrag);
        ft.commit();
        mapFrag.getMapAsync(this);
    }

    private void poblarMetricas() {
        if (finalizarData != null) {
            tvDistancia.setText(String.format(Locale.getDefault(),
                    "%.2f km", finalizarData.getDistanciaKm()));
            tvTiempo.setText(formatTiempo(finalizarData.getDuracionSeg()));
            tvRitmoProm.setText(formatRitmo(finalizarData.getRitmoPromedio()));
            tvVelProm.setText(String.format(Locale.getDefault(),
                    "%.1f km/h", finalizarData.getVelocidadPromedioKmh()));
            tvDesnivel.setText(String.format(Locale.getDefault(),
                    "+%.0f m", finalizarData.getDesnivel()));
            tvCalorias.setText(String.format(Locale.getDefault(),
                    "%.0f kcal", finalizarData.getCalorias()));
        } else {
            // Fallback with local timer
            tvTiempo.setText(formatTiempo((int) elapsedSeconds));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark));
        } catch (Exception ignored) { }

        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        // Draw route if GPS points were passed
        ArrayList<Double> lats = (ArrayList<Double>)
                getIntent().getSerializableExtra("lats");
        ArrayList<Double> lngs = (ArrayList<Double>)
                getIntent().getSerializableExtra("lngs");

        if (lats != null && lngs != null && lats.size() > 1) {
            PolylineOptions opts = new PolylineOptions()
                    .color(Color.parseColor("#39FF14"))
                    .width(8f)
                    .geodesic(true);
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (int i = 0; i < lats.size(); i++) {
                LatLng ll = new LatLng(lats.get(i), lngs.get(i));
                opts.add(ll);
                boundsBuilder.include(ll);
            }
            googleMap.addPolyline(opts);
            LatLngBounds bounds = boundsBuilder.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
        }
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    private String formatTiempo(int totalSeg) {
        int h = totalSeg / 3600;
        int m = (totalSeg % 3600) / 60;
        int s = totalSeg % 60;
        if (h > 0) return String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s);
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }

    private String formatRitmo(double ritmoSeg) {
        if (ritmoSeg <= 0) return "--:--";
        int minPorKm = (int) (ritmoSeg / 60);
        int secPorKm = (int) (ritmoSeg % 60);
        return String.format(Locale.getDefault(), "%d:%02d /km", minPorKm, secPorKm);
    }

    private String tipoLabel(String tipo) {
        if (tipo == null) return "";
        switch (tipo) {
            case "correr":    return "Carrera";
            case "ciclismo":  return "Ciclismo";
            case "senderismo":return "Senderismo";
            case "natacion":  return "Natación";
            default:          return tipo;
        }
    }
}
