package com.example.apptrace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.models.Comentario;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComentariosBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_PUBLICACION_ID = "publicacion_id";

    private int publicacionId;
    private RecyclerView rvComentarios;
    private EditText etNuevoComentario;
    private ImageButton btnEnviar;
    private ComentarioAdapter adapter;
    private List<Comentario> listaComentarios = new ArrayList<>();
    private ApiService apiService;

    public static ComentariosBottomSheet newInstance(int publicacionId) {
        ComentariosBottomSheet fragment = new ComentariosBottomSheet();
        Bundle args = new Bundle();
        args.putInt(ARG_PUBLICACION_ID, publicacionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            publicacionId = getArguments().getInt(ARG_PUBLICACION_ID);
        }
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottom_sheet_comentarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvComentarios = view.findViewById(R.id.rv_comentarios);
        etNuevoComentario = view.findViewById(R.id.et_nuevo_comentario);
        btnEnviar = view.findViewById(R.id.btn_enviar_comentario);

        rvComentarios.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ComentarioAdapter(listaComentarios);
        rvComentarios.setAdapter(adapter);

        cargarComentarios();

        btnEnviar.setOnClickListener(v -> enviarComentario());
    }

    private void cargarComentarios() {
        // GET /publicaciones/{id} devuelve la publicación con sus comentarios embebidos
        apiService.obtenerPublicacion(publicacionId).enqueue(new Callback<ApiResponse<Publicacion>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Publicacion>> call, @NonNull Response<ApiResponse<Publicacion>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Comentario> comentarios = response.body().getData().getComentarios();
                    listaComentarios.clear();
                    if (comentarios != null) {
                        listaComentarios.addAll(comentarios);
                    }
                    adapter.notifyDataSetChanged();
                    if (!listaComentarios.isEmpty()) {
                        rvComentarios.scrollToPosition(listaComentarios.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Publicacion>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarComentario() {
        String texto = etNuevoComentario.getText().toString().trim();
        if (texto.isEmpty()) return;

        etNuevoComentario.setText("");

        Comentario nuevo = new Comentario();
        nuevo.setCuerpo(texto);

        apiService.crearComentario(publicacionId, nuevo).enqueue(new Callback<ApiResponse<Comentario>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Comentario>> call, @NonNull Response<ApiResponse<Comentario>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    listaComentarios.add(response.body().getData());
                    adapter.notifyItemInserted(listaComentarios.size() - 1);
                    rvComentarios.scrollToPosition(listaComentarios.size() - 1);
                } else {
                    Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Comentario>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
