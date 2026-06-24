package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.adapters.RutaAdapter;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.route.RouteData;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RutasActivity extends AppCompatActivity {

    private RecyclerView rvRoutes;
    private ApiService apiService;
    private RutaAdapter adapter;
    private final List<RouteData> rutas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        rvRoutes = findViewById(R.id.rv_routes);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RutaAdapter(rutas, ruta -> {
            Intent i = new Intent(this, DetalleRutaActivity.class);
            i.putExtra(DetalleRutaActivity.EXTRA_RUTA_ID, ruta.getId());
            i.putExtra(DetalleRutaActivity.EXTRA_RUTA, ruta);
            startActivity(i);
        });
        rvRoutes.setAdapter(adapter);

        // FAB → historial de actividades
        findViewById(R.id.fab_track_activity).setOnClickListener(v ->
                startActivity(new Intent(this, ActividadesActivity.class)));

        // Botón agregar ruta
        findViewById(R.id.fl_add_route).setOnClickListener(v ->
                startActivity(new Intent(this, CrearRutaActivity.class)));

        // Bottom nav tabs
        LinearLayout llNav = findViewById(R.id.ll_bottom_nav);
        llNav.getChildAt(0).setOnClickListener(v -> finish());       // Feed
        llNav.getChildAt(3).setOnClickListener(v ->
                Toast.makeText(this, "Grupos — próximamente", Toast.LENGTH_SHORT).show());
        llNav.getChildAt(4).setOnClickListener(v -> {
            startActivity(new Intent(this, PerfilActivity.class));
        });

        cargarRutas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarRutas();
    }

    private void cargarRutas() {
        apiService.listarRutas().enqueue(new Callback<ApiResponse<List<RouteData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RouteData>>> call,
                                   Response<ApiResponse<List<RouteData>>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null) {
                    rutas.clear();
                    rutas.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<RouteData>>> call, Throwable t) {
                Toast.makeText(RutasActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
