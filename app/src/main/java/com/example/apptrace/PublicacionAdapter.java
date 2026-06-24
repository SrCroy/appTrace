package com.example.apptrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apptrace.R;
import com.example.apptrace.models.Publicacion;
import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {

    private List<Publicacion> listaPublicaciones;

    public PublicacionAdapter(List<Publicacion> listaPublicaciones) {
        this.listaPublicaciones = listaPublicaciones;
    }

    @NonNull
    @Override
    public PublicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publicacion, parent, false);
        return new PublicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionViewHolder holder, int position) {
        Publicacion p = listaPublicaciones.get(position);

        holder.tvUserName.setText(p.getUserName() != null ? p.getUserName() : "Usuario " + p.getUsuarioId());
        holder.tvTime.setText(p.getCreadoEn());
        holder.tvContenidoPost.setText(p.getContenido());

        // Contadores de interacciones sociales
        holder.tvLikeCount.setText(String.valueOf(p.getLikesCount()));
        holder.tvCommentCount.setText(String.valueOf(p.getComentariosCount()));

        // Verificar si la publicación incluye datos deportivos
        if (p.getActividad() != null) {
            // Habilitamos el contenedor de estadísticas si existe actividad
            holder.itemView.findViewById(R.id.ll_stats).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.tv_activity_type).setVisibility(View.VISIBLE);

            Publicacion.ActividadAnidada act = p.getActividad();
            holder.tvActivityType.setText(act.getTipoDeporte());
            holder.tvStatDistance.setText(String.format("%.2f km", act.getDistanciaKm()));

            // Convertir segundos a formato mm:ss o hh:mm:ss si es necesario
            int minutos = (int) (act.getDuracionSeg() / 60);
            int segundos = (int) (act.getDuracionSeg() % 60);
            holder.tvStatTime.setText(String.format("%02d:%02d", minutos, segundos));

            holder.tvStatPace.setText(String.format("%.2f /km", act.getRitmoPromedio()));
        } else {
            // Si es solo un mensaje de texto, ocultamos los campos de métricas deportivas
            holder.itemView.findViewById(R.id.ll_stats).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.tv_activity_type).setVisibility(View.GONE);
            holder.ivMapPreview.setVisibility(View.GONE); // Oculta el recuadro del mapa
        }
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones != null ? listaPublicaciones.size() : 0;
    }

    public static class PublicacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvTime, tvActivityType, tvContenidoPost, tvStatDistance, tvStatTime, tvStatPace, tvLikeCount, tvCommentCount;
        ImageView ivMapPreview;

        public PublicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvActivityType = itemView.findViewById(R.id.tv_activity_type);
            tvContenidoPost = itemView.findViewById(R.id.tv_contenido_post);
            tvStatDistance = itemView.findViewById(R.id.tv_stat_distance);
            tvStatTime = itemView.findViewById(R.id.tv_stat_time);
            tvStatPace = itemView.findViewById(R.id.tv_stat_pace);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            ivMapPreview = itemView.findViewById(R.id.iv_map_preview);
        }
    }
}