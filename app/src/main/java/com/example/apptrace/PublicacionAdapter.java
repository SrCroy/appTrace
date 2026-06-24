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
import com.example.apptrace.models.Reaccion;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // Lógica del Like
        holder.ivLike.setOnClickListener(v -> {
            darLike(p.getIdPublicacion(), holder);
        });
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones != null ? listaPublicaciones.size() : 0;
    }

    // Método para llamar a la API de reacciones
    private void darLike(int publicacionId, PublicacionViewHolder holder) {
        Reaccion reaccion = new Reaccion();
        reaccion.setReaccionableId(publicacionId);
        reaccion.setReaccionableTipo("App\\Models\\Publicacion");

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.crearReaccion(reaccion).enqueue(new Callback<Reaccion>() {
            @Override
            public void onResponse(Call<Reaccion> call, Response<Reaccion> response) {
                if (response.isSuccessful()) {
                    // Éxito: cambiamos visualmente el corazón
                    holder.ivLike.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.diff_hard));
                }
            }

            @Override
            public void onFailure(Call<Reaccion> call, Throwable t) {

            }
        });
    }

    public static class PublicacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvTime, tvActivityType, tvContenidoPost, tvStatDistance, tvStatTime, tvStatPace, tvLikeCount, tvCommentCount;
        ImageView ivLike, ivMapPreview;

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
            ivLike = itemView.findViewById(R.id.iv_like);
        }
    }
}