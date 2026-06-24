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

import com.example.apptrace.model.auth.ApiResponse;
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        rvFeed = view.findViewById(R.id.rv_feed);
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PublicacionAdapter(listaPublicaciones, new PublicacionAdapter.OnPublicacionClickListener() {
            @Override
            public void onUsuarioClick(int usuarioId) {
                PerfilFragment perfilFragment = new PerfilFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("USUARIO_ID", usuarioId);
                perfilFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, perfilFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onRutaClick(int rutaId) {
                Intent intent = new Intent(requireContext(), DetalleRutaActivity.class);
                intent.putExtra("RUTA_ID", rutaId);
                startActivity(intent);
                Toast.makeText(getContext(), "Navegando a la ruta ID: " + rutaId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComentarioClick(int publicacionId) {
                // Puente a los Comentarios
                ComentariosBottomSheet bottomSheet = ComentariosBottomSheet.newInstance(publicacionId);
                bottomSheet.show(requireActivity().getSupportFragmentManager(), "ComentariosBottomSheet");
            }
        });

        rvFeed.setAdapter(adapter);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        cargarFeedDesdeServidor();

        return view;
    }

    private void cargarFeedDesdeServidor() {
        apiService.getFeedPrincipal().enqueue(new Callback<ApiResponse<List<Publicacion>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Publicacion>>> call, @NonNull Response<ApiResponse<List<Publicacion>>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    listaPublicaciones.clear();
                    listaPublicaciones.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No se pudo cargar el feed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Publicacion>>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}