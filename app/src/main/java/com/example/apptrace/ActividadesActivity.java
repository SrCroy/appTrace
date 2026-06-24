package com.example.apptrace;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.model.activity.ActivityData;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadesActivity extends AppCompatActivity {

    private RecyclerView        rvActividades;
    private LinearLayout        llEmpty;
    private ApiService          apiService;
    private ActividadAdapter    adapter;
    private final List<ActivityData> actividades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        rvActividades = findViewById(R.id.rv_actividades);
        llEmpty       = findViewById(R.id.ll_empty);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        adapter = new ActividadAdapter(actividades);
        rvActividades.setLayoutManager(new LinearLayoutManager(this));
        rvActividades.setAdapter(adapter);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        cargarActividades();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarActividades();
    }

    private void cargarActividades() {
        apiService.listarActividades().enqueue(new Callback<ApiResponse<List<ActivityData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ActivityData>>> call,
                                   Response<ApiResponse<List<ActivityData>>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null) {
                    actividades.clear();
                    actividades.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }
                llEmpty.setVisibility(actividades.isEmpty() ? View.VISIBLE : View.GONE);
                rvActividades.setVisibility(actividades.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ActivityData>>> call, Throwable t) {
                Toast.makeText(ActividadesActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
