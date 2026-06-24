package com.example.apptrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.models.ToggleReaccionResponse;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {

    private List<Publicacion> listaPublicaciones;
    private OnPublicacionClickListener listener;

    public interface OnPublicacionClickListener {
        void onUsuarioClick(int usuarioId);
        void onRutaClick(int rutaId);
        void onComentarioClick(int publicacionId);
    }

    public PublicacionAdapter(List<Publicacion> listaPublicaciones, OnPublicacionClickListener listener) {
        this.listaPublicaciones = listaPublicaciones;
        this.listener = listener;
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

        String username = (p.getUsuario() != null) ? p.getUsuario().getUsername() : "";
        holder.tvUserName.setText(username);
        holder.tvTime.setText(p.getCreatedAt());
        holder.tvContenidoPost.setText(p.getContenido());

        holder.tvLikeCount.setText(String.valueOf(p.getTotalReacciones()));
        holder.tvCommentCount.setText(String.valueOf(p.getTotalComentarios()));

        if (p.getActividad() != null) {
            holder.itemView.findViewById(R.id.ll_stats).setVisibility(View.VISIBLE);
            holder.itemView.findViewById(R.id.tv_activity_type).setVisibility(View.VISIBLE);
            holder.ivMapPreview.setVisibility(View.VISIBLE);

            Publicacion.ActividadAnidada act = p.getActividad();
            holder.tvActivityType.setText(act.getTipoDeporte());
            holder.tvStatDistance.setText(String.format("%.2f km", act.getDistanciaKm()));

            int minutos = (int) (act.getDuracionSeg() / 60);
            int segundos = (int) (act.getDuracionSeg() % 60);
            holder.tvStatTime.setText(String.format("%02d:%02d", minutos, segundos));
            holder.tvStatPace.setText("");
        } else {
            holder.itemView.findViewById(R.id.ll_stats).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.tv_activity_type).setVisibility(View.GONE);
            holder.ivMapPreview.setVisibility(View.GONE);
        }

        View.OnClickListener irAlPerfil = v -> {
            if (listener != null && p.getUsuario() != null) {
                listener.onUsuarioClick(p.getUsuario().getId());
            }
        };
        holder.flAvatar.setOnClickListener(irAlPerfil);
        holder.tvUserName.setOnClickListener(irAlPerfil);

        if (p.getRuta() != null) {
            holder.ivMapPreview.setOnClickListener(v -> {
                if (listener != null) listener.onRutaClick(p.getRuta().getId());
            });
        }

        holder.ivComment.setOnClickListener(v -> {
            if (listener != null) listener.onComentarioClick(p.getId());
        });

        holder.ivLike.setOnClickListener(v -> darLike(p, holder));
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones != null ? listaPublicaciones.size() : 0;
    }

    private void darLike(Publicacion publicacion, PublicacionViewHolder holder) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.toggleReaccion(publicacion.getId()).enqueue(new Callback<ToggleReaccionResponse>() {
            @Override
            public void onResponse(@NonNull Call<ToggleReaccionResponse> call, @NonNull Response<ToggleReaccionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ToggleReaccionResponse res = response.body();
                    publicacion.setTotalReacciones(res.getTotalReacciones());
                    publicacion.setYaReacciono(res.isReaccionado());
                    holder.tvLikeCount.setText(String.valueOf(res.getTotalReacciones()));
                    int color = res.isReaccionado()
                            ? holder.itemView.getContext().getResources().getColor(R.color.diff_hard)
                            : holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray);
                    holder.ivLike.setColorFilter(color);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ToggleReaccionResponse> call, @NonNull Throwable t) {}
        });
    }

    public static class PublicacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvTime, tvActivityType, tvContenidoPost, tvStatDistance, tvStatTime, tvStatPace, tvLikeCount, tvCommentCount;
        ImageView ivLike, ivMapPreview, ivComment;
        View flAvatar;

        public PublicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            flAvatar = itemView.findViewById(R.id.fl_avatar);
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
            ivComment = itemView.findViewById(R.id.iv_comment);
        }
    }
}
