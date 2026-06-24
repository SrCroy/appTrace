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

import com.example.apptrace.models.Comentario;
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

    // Patrón recomendado para pasar argumentos a un Fragmento
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
        apiService.obtenerComentarios(publicacionId).enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaComentarios.clear();
                    listaComentarios.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Hacer scroll automático al final si hay comentarios
                    if (!listaComentarios.isEmpty()) {
                        rvComentarios.scrollToPosition(listaComentarios.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarComentario() {
        String texto = etNuevoComentario.getText().toString().trim();
        if (texto.isEmpty()) return;

        // Limpiar el campo inmediatamente para que la UI se sienta ágil
        etNuevoComentario.setText("");

        Comentario nuevo = new Comentario();
        nuevo.setCuerpo(texto);
        nuevo.setComentableId(publicacionId);
        nuevo.setComentableTipo("App\\Models\\Publicacion");

        apiService.crearComentario(nuevo).enqueue(new Callback<Comentario>() {
            @Override
            public void onResponse(Call<Comentario> call, Response<Comentario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Agregamos el comentario devuelto a la lista y recargamos
                    listaComentarios.add(response.body());
                    adapter.notifyItemInserted(listaComentarios.size() - 1);
                    rvComentarios.scrollToPosition(listaComentarios.size() - 1);
                } else {
                    Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comentario> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}