package com.example.apptrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apptrace.models.Grupo;
import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder> {

    private List<Grupo> listaGrupos;
    private OnGrupoClickListener listener;

    public interface OnGrupoClickListener {
        void onGrupoClick(int grupoId);
    }

    public GrupoAdapter(List<Grupo> listaGrupos, OnGrupoClickListener listener) {
        this.listaGrupos = listaGrupos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grupo, parent, false);
        return new GrupoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoViewHolder holder, int position) {
        Grupo grupo = listaGrupos.get(position);

        holder.tvNombre.setText(grupo.getNombre());
        holder.tvMiembros.setText(grupo.getTotalMiembros() + " miembros");

        holder.tvDeporte.setText("Multideporte"); // Dato por defecto si no viene de la API
        holder.tvActividades.setText("-- actividades"); // Dato por defecto

        holder.tvUltimaActividad.setText("Privacidad: " + grupo.getPrivacidad());
        holder.tvUltimaActividad.setText("Activo recientemente");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGrupoClick(grupo.getIdGrupo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaGrupos != null ? listaGrupos.size() : 0;
    }

    public void setFiltrarLista(List<Grupo> listaFiltrada) {
        this.listaGrupos = listaFiltrada;
        notifyDataSetChanged();
    }

    public static class GrupoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDeporte, tvUltimaActividad, tvMiembros, tvActividades;
        ImageView ivIconoDeporte;

        public GrupoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_group_name);
            tvDeporte = itemView.findViewById(R.id.tv_group_sport);
            tvUltimaActividad = itemView.findViewById(R.id.tv_group_last_active);
            tvMiembros = itemView.findViewById(R.id.tv_group_members);
            tvActividades = itemView.findViewById(R.id.tv_group_activities);
            ivIconoDeporte = itemView.findViewById(R.id.iv_group_sport_icon);
        }
    }
}