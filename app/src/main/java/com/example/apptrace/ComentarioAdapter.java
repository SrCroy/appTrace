package com.example.apptrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apptrace.models.Comentario;
import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {

    private List<Comentario> listaComentarios;

    public ComentarioAdapter(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        Comentario c = listaComentarios.get(position);
        String username = (c.getUsuario() != null) ? c.getUsuario().getUsername() : "Usuario";
        holder.tvAutor.setText(username);
        holder.tvCuerpo.setText(c.getCuerpo());
        holder.tvIniciales.setText(username.isEmpty() ? "U" : username.substring(0, 1).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return listaComentarios != null ? listaComentarios.size() : 0;
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvIniciales, tvAutor, tvCuerpo;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIniciales = itemView.findViewById(R.id.tv_avatar_iniciales_comentario);
            tvAutor = itemView.findViewById(R.id.tv_autor_comentario);
            tvCuerpo = itemView.findViewById(R.id.tv_cuerpo_comentario);
        }
    }
}