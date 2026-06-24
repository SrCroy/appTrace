package com.example.apptrace;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.logro.MisLogrosResponse;
import com.example.apptrace.model.profile.ProfileData;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.example.apptrace.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvAvatarInitials, tvFullName, tvUsername, tvLocation, tvBio;
    private ImageView ivAvatarProfile, ivEditProfileIcon;
    private MaterialButton btnEditProfile, btnLogout;
    private MaterialCardView cvLogrosPreview;
    private TextView tvLogrosCount, tvLogrosPct;

    private ApiService apiService;
    private ProfileData currentProfile;
    private ActivityResultLauncher<Intent> editLauncher;
    private boolean recargarAlVolver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvAvatarInitials  = findViewById(R.id.tv_avatar_initials);
        ivAvatarProfile   = findViewById(R.id.iv_avatar_profile);
        tvFullName        = findViewById(R.id.tv_full_name);
        tvUsername        = findViewById(R.id.tv_username);
        tvLocation        = findViewById(R.id.tv_location);
        tvBio             = findViewById(R.id.tv_bio);
        btnEditProfile    = findViewById(R.id.btn_edit_profile);
        ivEditProfileIcon = findViewById(R.id.iv_edit_profile_icon);
        btnLogout         = findViewById(R.id.btn_logout);
        cvLogrosPreview   = findViewById(R.id.cv_logros_preview);
        tvLogrosCount     = findViewById(R.id.tv_logros_count);
        tvLogrosPct       = findViewById(R.id.tv_logros_pct);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        recargarAlVolver = true;
                    }
                }
        );

        View.OnClickListener abrirEdicion = v -> {
            Intent intent = new Intent(this, EditarPerfilActivity.class);
            if (currentProfile != null)
                intent.putExtra(EditarPerfilActivity.EXTRA_PROFILE, currentProfile);
            editLauncher.launch(intent);
        };

        btnEditProfile.setOnClickListener(abrirEdicion);
        ivEditProfileIcon.setOnClickListener(abrirEdicion);
        btnLogout.setOnClickListener(v -> confirmarCierreSesion());
        cvLogrosPreview.setOnClickListener(v ->
                startActivity(new Intent(this, LogrosActivity.class)));

        // Tabs del bottom nav
        LinearLayout llBottomNav = findViewById(R.id.ll_bottom_nav);
        llBottomNav.getChildAt(0).setOnClickListener(v -> finish()); // Feed → volver
        llBottomNav.getChildAt(1).setOnClickListener(v ->
                Toast.makeText(this, "Rutas — próximamente", Toast.LENGTH_SHORT).show());
        llBottomNav.getChildAt(3).setOnClickListener(v ->
                Toast.makeText(this, "Grupos — próximamente", Toast.LENGTH_SHORT).show());

        cargarPerfil();
        cargarLogros();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recargarAlVolver) {
            recargarAlVolver = false;
            cargarPerfil();
        }
    }

    // ─── Carga ────────────────────────────────────────────────────────────────

    private void cargarLogros() {
        apiService.misLogros().enqueue(new Callback<MisLogrosResponse>() {
            @Override
            public void onResponse(Call<MisLogrosResponse> call, Response<MisLogrosResponse> response) {
                if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()) {
                    tvLogrosCount.setText("Ver logros");
                    return;
                }
                MisLogrosResponse r = response.body();
                MisLogrosResponse.Resumen res = r.getResumen();
                int obtenidos = res != null ? res.getTotalObtenidos()
                        : (r.getData() != null ? r.getData().size() : 0);
                double pct = res != null ? res.getPorcentaje() : 0;

                tvLogrosCount.setText(obtenidos + (obtenidos == 1 ? " logro obtenido" : " logros obtenidos"));
                tvLogrosPct.setText(String.format(Locale.getDefault(), "%.0f%% del catálogo completado", pct));
                tvLogrosPct.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MisLogrosResponse> call, Throwable t) {
                tvLogrosCount.setText("Ver logros");
            }
        });
    }

    private void cargarPerfil() {
        apiService.miPerfil().enqueue(new Callback<ApiResponse<ProfileData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProfileData>> call,
                                   Response<ApiResponse<ProfileData>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {
                    currentProfile = response.body().getData();
                    mostrarPerfil(currentProfile);
                } else {
                    Toast.makeText(PerfilActivity.this,
                            "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProfileData>> call, Throwable t) {
                Toast.makeText(PerfilActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarPerfil(ProfileData p) {
        tvFullName.setText(p.getNombre() + " " + p.getApellido());
        tvUsername.setText("@" + p.getUsername());

        // "Miembro desde YYYY" en el campo location
        tvLocation.setText(anioMiembro(p.getCreadoEn()));

        // Biografía
        if (p.getBiografia() != null && !p.getBiografia().isEmpty()) {
            tvBio.setText(p.getBiografia());
            tvBio.setVisibility(View.VISIBLE);
        } else {
            tvBio.setVisibility(View.GONE);
        }

        // Avatar
        String iniciales = iniciales(p.getNombre(), p.getApellido());
        tvAvatarInitials.setText(iniciales);

        Glide.with(this)
                .load(RetrofitClient.storageUrl(p.getAvatar()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        ivAvatarProfile.setVisibility(View.GONE);
                        tvAvatarInitials.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        ivAvatarProfile.setVisibility(View.VISIBLE);
                        tvAvatarInitials.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivAvatarProfile);
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    private String iniciales(String nombre, String apellido) {
        StringBuilder sb = new StringBuilder();
        if (nombre != null && !nombre.isEmpty())   sb.append(nombre.charAt(0));
        if (apellido != null && !apellido.isEmpty()) sb.append(apellido.charAt(0));
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    private void confirmarCierreSesion() {
        new AlertDialog.Builder(this, R.style.Theme_Trace_Dialog)
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que deseas cerrar sesión?")
                .setPositiveButton("Cerrar sesión", (d, w) -> {
                    SessionManager.getInstance(this).logout();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private String anioMiembro(String fechaIso) {
        if (fechaIso == null || fechaIso.isEmpty()) return "Miembro de Trace";
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                    Locale.getDefault()).parse(fechaIso);
            if (d == null) d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(fechaIso);
            String anio = new SimpleDateFormat("yyyy", Locale.getDefault()).format(d);
            return "Miembro desde " + anio;
        } catch (ParseException e) {
            return "Miembro de Trace";
        }
    }
}
