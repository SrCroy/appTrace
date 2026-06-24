package com.example.apptrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptrace.model.route.RouteData;

import java.util.List;
import java.util.Locale;

public class RutaAdapter extends RecyclerView.Adapter<RutaAdapter.ViewHolder> {

    public interface OnRutaClick { void onClick(RouteData ruta); }

    private final List<RouteData> items;
    private final OnRutaClick     listener;

    public RutaAdapter(List<RouteData> items, OnRutaClick listener) {
        this.items    = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ruta, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        RouteData ruta = items.get(pos);

        h.tvNombre.setText(ruta.getNombre());
        h.tvDificultad.setText(dificultadLabel(ruta.getDificultad()));
        h.tvDificultad.setTextColor(ContextCompat.getColor(
                h.itemView.getContext(), dificultadColor(ruta.getDificultad())));

        String distStr = "";
        if (ruta.getDistanciaKm() != null) {
            distStr = String.format(Locale.getDefault(), "%.1f km", ruta.getDistanciaKm());
        }
        h.tvStats.setText(distStr);
        h.tvScore.setText("");

        // Overlay con tipo de deporte y distancia
        h.tvOverlayTipo.setText(tipoLabel(ruta.getTipo()));
        h.tvOverlayDist.setText(distStr);

        if (ruta.getMiniatura() != null && !ruta.getMiniatura().isEmpty()) {
            h.vPlaceholder.setVisibility(View.GONE);
            Glide.with(h.itemView).load(ruta.getMiniatura()).into(h.ivPreview);
        } else {
            h.vPlaceholder.setVisibility(View.VISIBLE);
            h.ivPreview.setImageDrawable(null);
        }

        h.itemView.setOnClickListener(v -> listener.onClick(ruta));
    }

    @Override
    public int getItemCount() { return items.size(); }

    private String tipoLabel(String tipo) {
        if (tipo == null) return "Ruta";
        switch (tipo) {
            case "carrera":    return "Carrera";
            case "caminata":   return "Caminata";
            case "ciclismo":   return "Ciclismo";
            case "natacion":   return "Natación";
            case "senderismo": return "Senderismo";
            case "montanismo": return "Montañismo";
            case "otro":       return "Otro";
            default:           return tipo;
        }
    }

    private String dificultadLabel(String d) {
        if (d == null) return "";
        switch (d) {
            case "1": return "Muy fácil";
            case "2": return "Fácil";
            case "3": return "Moderado";
            case "4": return "Difícil";
            case "5": return "Muy difícil";
            case "6": return "Extremo";
            default:  return d;
        }
    }

    private int dificultadColor(String d) {
        if (d == null) return R.color.text_secondary;
        switch (d) {
            case "1": case "2": return R.color.diff_easy;
            case "3":           return R.color.diff_medium;
            case "4": case "5":
            case "6":           return R.color.diff_hard;
            default:            return R.color.text_secondary;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPreview;
        View      vPlaceholder;
        TextView  tvNombre, tvDificultad, tvStats, tvScore, tvOverlayTipo, tvOverlayDist;

        ViewHolder(View v) {
            super(v);
            ivPreview      = v.findViewById(R.id.iv_route_preview);
            vPlaceholder   = v.findViewById(R.id.v_route_placeholder);
            tvNombre       = v.findViewById(R.id.tv_route_name);
            tvDificultad   = v.findViewById(R.id.tv_route_difficulty);
            tvStats        = v.findViewById(R.id.tv_route_stats);
            tvScore        = v.findViewById(R.id.tv_route_score);
            tvOverlayTipo  = v.findViewById(R.id.tv_overlay_tipo);
            tvOverlayDist  = v.findViewById(R.id.tv_overlay_dist);
        }
    }
}
