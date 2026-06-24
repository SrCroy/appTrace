package com.example.apptrace;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.adapters.LogroAdapter;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.logro.LogroData;
import com.example.apptrace.model.logro.MisLogrosResponse;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogrosActivity extends AppCompatActivity {

    private TextView tvTotalObtained, tvTotalProgress;
    private RecyclerView rvObtained, rvProgress;
    private View sectionObtenidos, sectionEnProgreso;

    private ApiService apiService;
    private List<LogroData> obtenidos = new ArrayList<>();
    private MisLogrosResponse.Resumen resumen;
    private boolean misLogrosCargado = false;
    private boolean catalogoCargado = false;
    private List<LogroData> todosLogros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        ImageView ivBack = findViewById(R.id.iv_back);
        tvTotalObtained  = findViewById(R.id.tv_total_obtained);
        tvTotalProgress  = findViewById(R.id.tv_total_progress);
        rvObtained       = findViewById(R.id.rv_achievements_obtained);
        rvProgress       = findViewById(R.id.rv_achievements_progress);

        rvObtained.setLayoutManager(new LinearLayoutManager(this));
        rvObtained.setNestedScrollingEnabled(false);
        rvProgress.setLayoutManager(new LinearLayoutManager(this));
        rvProgress.setNestedScrollingEnabled(false);

        ivBack.setOnClickListener(v -> finish());

        apiService = RetrofitClient.getClient().create(ApiService.class);

        cargarMisLogros();
        cargarCatalogo();
    }

    private void cargarMisLogros() {
        apiService.misLogros().enqueue(new Callback<MisLogrosResponse>() {
            @Override
            public void onResponse(Call<MisLogrosResponse> call, Response<MisLogrosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    obtenidos = response.body().getData() != null ? response.body().getData() : new ArrayList<>();
                    resumen   = response.body().getResumen();
                }
                misLogrosCargado = true;
                mostrarSiListo();
            }

            @Override
            public void onFailure(Call<MisLogrosResponse> call, Throwable t) {
                misLogrosCargado = true;
                mostrarSiListo();
                Toast.makeText(LogrosActivity.this, "Error al cargar logros", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarCatalogo() {
        apiService.catalogoLogros().enqueue(new Callback<ApiResponse<List<LogroData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LogroData>>> call,
                                   Response<ApiResponse<List<LogroData>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    todosLogros = response.body().getData() != null ? response.body().getData() : new ArrayList<>();
                }
                catalogoCargado = true;
                mostrarSiListo();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LogroData>>> call, Throwable t) {
                catalogoCargado = true;
                mostrarSiListo();
            }
        });
    }

    private void mostrarSiListo() {
        if (!misLogrosCargado || !catalogoCargado) return;

        // Resumen card
        int totalObtenidos = resumen != null ? resumen.getTotalObtenidos() : obtenidos.size();
        int enProgresoCount = resumen != null
                ? resumen.getTotalDisponibles() - totalObtenidos
                : Math.max(0, todosLogros.size() - obtenidos.size());
        double pct = resumen != null ? resumen.getPorcentaje() : 0;

        tvTotalObtained.setText(totalObtenidos + (totalObtenidos == 1 ? " logro obtenido" : " logros obtenidos"));
        tvTotalProgress.setText(String.format(Locale.getDefault(), "%.0f%% del catálogo · %d en progreso", pct, enProgresoCount));

        // Lista OBTENIDOS
        rvObtained.setAdapter(new LogroAdapter(obtenidos, true));

        // Lista EN PROGRESO: logros del catálogo que NO están en obtenidos
        Set<Integer> obtenidosIds = new HashSet<>();
        for (LogroData l : obtenidos) obtenidosIds.add(l.getId());

        List<LogroData> enProgreso = new ArrayList<>();
        for (LogroData l : todosLogros) {
            if (!obtenidosIds.contains(l.getId())) enProgreso.add(l);
        }
        rvProgress.setAdapter(new LogroAdapter(enProgreso, false));
    }
}
