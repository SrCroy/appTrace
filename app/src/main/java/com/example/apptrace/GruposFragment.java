package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.models.Grupo;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GruposFragment extends Fragment {

    private RecyclerView rvGrupos;
    private GrupoAdapter adapter;
    private List<Grupo> listaGrupos = new ArrayList<>();
    private TextInputEditText etBusqueda;
    private ExtendedFloatingActionButton fabCreateGroup;
    private ApiService apiService;

    public GruposFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Asume que renombraste "activity_grupos.xml" a "fragment_grupos.xml"
        return inflater.inflate(R.layout.fragment_grupos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Enlazar vistas
        rvGrupos = view.findViewById(R.id.rv_groups);
        etBusqueda = view.findViewById(R.id.et_search_groups);
        fabCreateGroup = view.findViewById(R.id.fab_create_group);

        // 2. Configurar RecyclerView
        rvGrupos.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new GrupoAdapter(listaGrupos, grupoId -> {
            // Puente hacia el muro interno de la comunidad (Mantenemos Activity para el detalle)
            Intent intent = new Intent(requireContext(), DetalleGrupoActivity.class);
            intent.putExtra("GRUPO_ID", grupoId);
            startActivity(intent);
        });

        rvGrupos.setAdapter(adapter);

        // 3. Inicializar API
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // 4. Lógica de la barra de búsqueda en tiempo real
        if (etBusqueda != null) {
            etBusqueda.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filtrarGrupos(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // 5. Botón de crear grupo
        if (fabCreateGroup != null) {
            fabCreateGroup.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), CrearGrupoActivity.class);
                startActivity(intent);
            });
        }

        // 6. Cargar datos del servidor
        cargarGrupos();
    }

    private void cargarGrupos() {
        apiService.getGrupos().enqueue(new Callback<List<Grupo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Grupo>> call, @NonNull Response<List<Grupo>> response) {
                if (!isAdded()) return; // Protección vital en Fragments

                if (response.isSuccessful() && response.body() != null) {
                    listaGrupos.clear();
                    listaGrupos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Error al cargar comunidades", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Grupo>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrarGrupos(String texto) {
        List<Grupo> listaFiltrada = new ArrayList<>();
        for (Grupo g : listaGrupos) {
            // Asumiendo que getNombre() existe en tu modelo Grupo
            if (g.getNombre() != null && g.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(g);
            }
        }
        adapter.setFiltrarLista(listaFiltrada);
    }
}