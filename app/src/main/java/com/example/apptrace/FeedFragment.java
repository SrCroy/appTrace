package com.example.apptrace;

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

import com.example.apptrace.R;
import com.example.apptrace.PublicacionAdapter;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    private RecyclerView rvFeed;
    private PublicacionAdapter adapter;
    private List<Publicacion> listaPublicaciones = new ArrayList<>();
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el diseño general del fragmento del feed
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        rvFeed = view.findViewById(R.id.rv_feed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PublicacionAdapter(listaPublicaciones);
        rvFeed.setAdapter(adapter);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        cargarFeedDesdeServidor();

        return view;
    }

    private void cargarFeedDesdeServidor() {
        Call<List<Publicacion>> call = apiService.getFeedPrincipal();

        call.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(@NonNull Call<List<Publicacion>> call, @NonNull Response<List<Publicacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Limpiamos e inyectamos los datos de Railway
                    listaPublicaciones.clear();
                    listaPublicaciones.addAll(response.body());

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No se pudo cargar el feed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Publicacion>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}