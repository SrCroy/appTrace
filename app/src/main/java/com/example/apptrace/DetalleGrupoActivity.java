package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.models.Grupo;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleGrupoActivity extends AppCompatActivity {

    private int grupoId;
    private ApiService apiService;

    // Vistas de la cabecera
    private FrameLayout flBack, flShare;
    private TextView tvGroupName, tvGroupSport, tvGroupDescription;
    private MaterialButton btnJoinGroup;
    private TabLayout tlGroupTabs;

    // Vistas del Feed
    private RecyclerView rvGroupFeed;
    private PublicacionAdapter adapter;
    private List<Publicacion> listaPublicaciones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_grupo);

        grupoId = getIntent().getIntExtra("GRUPO_ID", -1);
        if (grupoId == -1) {
            Toast.makeText(this, "Error al cargar la comunidad", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = RetrofitClient.getClient().create(ApiService.class);

        inicializarVistas();
        configurarRecyclerView();
        configurarPestanas();

        // Cargar los datos desde el servidor
        cargarDetallesDelGrupo();
        cargarPublicacionesDelGrupo();
    }

    private void inicializarVistas() {
        flBack = findViewById(R.id.fl_back);
        flShare = findViewById(R.id.fl_share_group);
        tvGroupName = findViewById(R.id.tv_group_name);
        tvGroupSport = findViewById(R.id.tv_group_sport);
        tvGroupDescription = findViewById(R.id.tv_group_description);
        btnJoinGroup = findViewById(R.id.btn_join_group);
        tlGroupTabs = findViewById(R.id.tl_group_tabs);
        rvGroupFeed = findViewById(R.id.rv_group_feed);

        // Funcionalidad de la barra superior
        flBack.setOnClickListener(v -> finish());
        flShare.setOnClickListener(v -> Toast.makeText(this, "Compartir grupo...", Toast.LENGTH_SHORT).show());

        btnJoinGroup.setOnClickListener(v -> unirseAlGrupo());
    }

    private void configurarRecyclerView() {
        // Al estar dentro de un ScrollView, es vital que el RecyclerView no intente hacer scroll por su cuenta
        rvGroupFeed.setLayoutManager(new LinearLayoutManager(this));
        rvGroupFeed.setNestedScrollingEnabled(false);

        adapter = new PublicacionAdapter(listaPublicaciones, new PublicacionAdapter.OnPublicacionClickListener() {
            @Override
            public void onUsuarioClick(int usuarioId) {
                Toast.makeText(DetalleGrupoActivity.this, "Ver perfil: " + usuarioId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRutaClick(int rutaId) {
                Intent intent = new Intent(DetalleGrupoActivity.this, DetalleRutaActivity.class);
                intent.putExtra("RUTA_ID", rutaId);
                startActivity(intent);
            }

            @Override
            public void onComentarioClick(int publicacionId) {
                ComentariosBottomSheet bottomSheet = ComentariosBottomSheet.newInstance(publicacionId);
                bottomSheet.show(getSupportFragmentManager(), "ComentariosBottomSheet");
            }
        });

        rvGroupFeed.setAdapter(adapter);
    }

    private void configurarPestanas() {
        tlGroupTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int posicion = tab.getPosition();
                switch (posicion) {
                    case 0: // Feed
                        rvGroupFeed.setVisibility(View.VISIBLE);
                        break;
                    case 1: // Retos
                        rvGroupFeed.setVisibility(View.GONE);
                        Toast.makeText(DetalleGrupoActivity.this, "Mostrar Retos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2: // Miembros
                        rvGroupFeed.setVisibility(View.GONE);
                        Toast.makeText(DetalleGrupoActivity.this, "Mostrar Miembros", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: // Logros
                        rvGroupFeed.setVisibility(View.GONE);
                        Toast.makeText(DetalleGrupoActivity.this, "Mostrar Logros", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void cargarDetallesDelGrupo() {
        apiService.obtenerGrupo(grupoId).enqueue(new Callback<ApiResponse<Grupo>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Grupo>> call, @NonNull Response<ApiResponse<Grupo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Grupo grupo = response.body().getData();
                    tvGroupName.setText(grupo.getNombre());
                    tvGroupDescription.setText(grupo.getDescripcion());
                    tvGroupSport.setText(grupo.getPrivacidad());
                    if (grupo.isEsMiembro()) {
                        btnJoinGroup.setText("Salir del grupo");
                        btnJoinGroup.setOnClickListener(v -> salirDelGrupo());
                    } else {
                        btnJoinGroup.setText("Unirse");
                        btnJoinGroup.setOnClickListener(v -> unirseAlGrupo());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Grupo>> call, @NonNull Throwable t) {
                Toast.makeText(DetalleGrupoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarPublicacionesDelGrupo() {
        apiService.getPublicacionesGrupo(grupoId).enqueue(new Callback<ApiResponse<List<Publicacion>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Publicacion>>> call, @NonNull Response<ApiResponse<List<Publicacion>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    listaPublicaciones.clear();
                    listaPublicaciones.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DetalleGrupoActivity.this, "No hay publicaciones aún", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Publicacion>>> call, @NonNull Throwable t) {
                Toast.makeText(DetalleGrupoActivity.this, "Error al cargar feed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unirseAlGrupo() {
        apiService.unirseGrupo(grupoId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(DetalleGrupoActivity.this, "Te has unido al grupo", Toast.LENGTH_SHORT).show();
                    btnJoinGroup.setText("Salir del grupo");
                    btnJoinGroup.setOnClickListener(v -> salirDelGrupo());
                    cargarPublicacionesDelGrupo();
                } else {
                    Toast.makeText(DetalleGrupoActivity.this, "No se pudo unir al grupo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                Toast.makeText(DetalleGrupoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salirDelGrupo() {
        apiService.salirGrupo(grupoId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(DetalleGrupoActivity.this, "Has salido del grupo", Toast.LENGTH_SHORT).show();
                    btnJoinGroup.setText("Unirse");
                    btnJoinGroup.setOnClickListener(v -> unirseAlGrupo());
                } else {
                    Toast.makeText(DetalleGrupoActivity.this, "No se pudo salir del grupo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                Toast.makeText(DetalleGrupoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}