package com.example.apptrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptrace.model.activity.ActivityData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ViewHolder> {

    private final List<ActivityData> items;

    public ActividadAdapter(List<ActivityData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actividad, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ActivityData a = items.get(pos);

        h.tvTipo.setText(tipoLabel(a.getTipoDeporte()));
        h.tvTitulo.setText(a.getTitulo() != null ? a.getTitulo() : "Actividad");
        h.tvFecha.setText(formatFecha(a.getStartedAt()));

        if (a.getDistanciaKm() != null) {
            h.tvDistancia.setText(String.format(Locale.getDefault(), "%.2f km", a.getDistanciaKm()));
        } else {
            h.tvDistancia.setText("— km");
        }

        if (a.getDuracionSeg() != null) {
            h.tvDuracion.setText(formatDuracion(a.getDuracionSeg()));
        } else {
            h.tvDuracion.setText("—");
        }

        if (a.getCalorias() != null) {
            h.tvCalorias.setText(String.format(Locale.getDefault(), "%d kcal", a.getCalorias().intValue()));
        } else {
            h.tvCalorias.setText("— kcal");
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    private String tipoLabel(String tipo) {
        if (tipo == null) return "Actividad";
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

    private String formatFecha(String iso) {
        if (iso == null) return "";
        try {
            SimpleDateFormat in  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            SimpleDateFormat out = new SimpleDateFormat("d MMM yyyy", new Locale("es", "ES"));
            Date d = in.parse(iso);
            return d != null ? out.format(d) : iso;
        } catch (ParseException e) {
            return iso.substring(0, Math.min(10, iso.length()));
        }
    }

    private String formatDuracion(int seg) {
        int h = seg / 3600;
        int m = (seg % 3600) / 60;
        int s = seg % 60;
        if (h > 0) return String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s);
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo, tvTitulo, tvFecha, tvDistancia, tvDuracion, tvCalorias;

        ViewHolder(View v) {
            super(v);
            tvTipo      = v.findViewById(R.id.tv_act_tipo);
            tvTitulo    = v.findViewById(R.id.tv_act_titulo);
            tvFecha     = v.findViewById(R.id.tv_act_fecha);
            tvDistancia = v.findViewById(R.id.tv_act_distancia);
            tvDuracion  = v.findViewById(R.id.tv_act_duracion);
            tvCalorias  = v.findViewById(R.id.tv_act_calorias);
        }
    }
}
