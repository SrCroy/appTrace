package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class RutasFragment extends Fragment {

    private RecyclerView rvRoutes;
    private ApiService apiService;
    private RutaAdapter adapter;
    private final List<RouteData> rutas = new ArrayList<>();

    public RutasFragment() {
        // Constructor público vacío requerido por Android
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_rutas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Usamos 'view.findViewById' y 'requireContext()' para el LayoutManager
        rvRoutes = view.findViewById(R.id.rv_routes);
        rvRoutes.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RutaAdapter(rutas, ruta -> {
            Intent i = new Intent(requireContext(), DetalleRutaActivity.class);
            i.putExtra(DetalleRutaActivity.EXTRA_RUTA_ID, ruta.getId());
            i.putExtra(DetalleRutaActivity.EXTRA_RUTA, ruta);
            startActivity(i);
        });
        rvRoutes.setAdapter(adapter);


        View btnAddRoute = view.findViewById(R.id.fl_add_route);
        if (btnAddRoute != null) {
            btnAddRoute.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), CrearRutaActivity.class)));
        }



        cargarRutas();
    }

    @Override
    public void onResume() {
        super.onResume();

        cargarRutas();
    }



    private void cargarRutas() {
        apiService.listarRutas().enqueue(new Callback<ApiResponse<List<RouteData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<RouteData>>> call,
                                   Response<ApiResponse<List<RouteData>>> response) {

                if (!isAdded()) return;

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
                if (!isAdded()) return;
                Toast.makeText(requireContext(),
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}