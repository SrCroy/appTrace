package com.example.apptrace.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.R;
import com.example.apptrace.model.logro.LogroData;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogroAdapter extends RecyclerView.Adapter<LogroAdapter.ViewHolder> {

    private final List<LogroData> items;
    private final boolean modoObtenidos;

    public LogroAdapter(List<LogroData> items, boolean modoObtenidos) {
        this.items = items;
        this.modoObtenidos = modoObtenidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logro_global, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        LogroData logro = items.get(pos);

        h.tvTitle.setText(logro.getNombre());
        h.tvDesc.setText(logro.getDescripcion());

        if (modoObtenidos) {
            h.llProgress.setVisibility(View.GONE);
            h.tvDate.setVisibility(View.VISIBLE);
            h.tvDate.setText(formatFechaObtenido(logro.getObtenidoEn()));

            int gold = ContextCompat.getColor(h.itemView.getContext(), R.color.warning);
            int bg   = ContextCompat.getColor(h.itemView.getContext(), R.color.background);
            h.flIcon.setBackgroundTintList(ColorStateList.valueOf(gold));
            h.ivIcon.setImageTintList(ColorStateList.valueOf(bg));
        } else {
            h.llProgress.setVisibility(View.VISIBLE);
            h.tvDate.setVisibility(View.GONE);
            h.tvStatus.setText("En progreso");
            h.tvProgressText.setText(formatMeta(logro));
            h.pbProgress.setVisibility(View.GONE);

            int surface  = ContextCompat.getColor(h.itemView.getContext(), R.color.surface_2);
            int disabled = ContextCompat.getColor(h.itemView.getContext(), R.color.text_disabled);
            h.flIcon.setBackgroundTintList(ColorStateList.valueOf(surface));
            h.ivIcon.setImageTintList(ColorStateList.valueOf(disabled));
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    private String formatFechaObtenido(String iso) {
        if (iso == null || iso.isEmpty()) return "Obtenido";
        try {
            SimpleDateFormat in  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            SimpleDateFormat out = new SimpleDateFormat("d MMM yyyy", new Locale("es", "ES"));
            Date d = in.parse(iso);
            if (d == null) {
                in = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                d  = in.parse(iso);
            }
            return "Obtenido el " + (d != null ? out.format(d) : iso.substring(0, 10));
        } catch (ParseException e) {
            return "Obtenido el " + iso.substring(0, Math.min(10, iso.length()));
        }
    }

    private String formatMeta(LogroData logro) {
        int valor = (int) logro.getValorDisparador();
        if (logro.getTipoDisparador() == null) return "Meta: " + valor;
        switch (logro.getTipoDisparador()) {
            case "distancia_total":
            case "distancia_unica":    return "Meta: " + valor + " km";
            case "conteo_actividades": return "Meta: " + valor + (valor == 1 ? " actividad" : " actividades");
            case "dias_racha":         return "Meta: " + valor + " días seguidos";
            case "desnivel_total":     return "Meta: " + valor + " m desnivel";
            default:                   return "Meta: " + valor;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flIcon;
        ImageView ivIcon;
        TextView tvTitle, tvDesc, tvDate, tvStatus, tvProgressText;
        LinearLayout llProgress;
        LinearProgressIndicator pbProgress;

        ViewHolder(View v) {
            super(v);
            flIcon         = v.findViewById(R.id.fl_achievement_icon);
            ivIcon         = v.findViewById(R.id.iv_achievement_icon);
            tvTitle        = v.findViewById(R.id.tv_achievement_title);
            tvDesc         = v.findViewById(R.id.tv_achievement_desc);
            tvDate         = v.findViewById(R.id.tv_achievement_date);
            llProgress     = v.findViewById(R.id.ll_progress_container);
            tvStatus       = v.findViewById(R.id.tv_achievement_status);
            tvProgressText = v.findViewById(R.id.tv_achievement_progress_text);
            pbProgress     = v.findViewById(R.id.pb_achievement_progress);
        }
    }
}
