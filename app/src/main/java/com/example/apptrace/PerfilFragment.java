package com.example.apptrace;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.profile.ProfileData;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.example.apptrace.session.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class PerfilFragment extends Fragment {

    private TextView tvAvatarInitials, tvFullName, tvUsername, tvLocation, tvBio;
    private ImageView ivAvatarProfile, ivEditProfileIcon;
    private MaterialButton btnEditProfile, btnLogout;

    private ApiService apiService;
    private ProfileData currentProfile;
    private ActivityResultLauncher<Intent> editLauncher;

    public PerfilFragment() {
        // Constructor público vacío requerido por los Fragments
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // El launcher DEBE ir aquí en onCreate cuando usamos Fragments
        editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        cargarPerfil();
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Asegúrate de que tu archivo XML se llame fragment_perfil.xml
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializamos las vistas desde 'view' (ya NO usamos 'this')
        tvAvatarInitials  = view.findViewById(R.id.tv_avatar_initials);
        ivAvatarProfile   = view.findViewById(R.id.iv_avatar_profile);
        tvFullName        = view.findViewById(R.id.tv_full_name);
        tvUsername        = view.findViewById(R.id.tv_username);
        tvLocation        = view.findViewById(R.id.tv_location);
        tvBio             = view.findViewById(R.id.tv_bio);
        btnEditProfile    = view.findViewById(R.id.btn_edit_profile);
        ivEditProfileIcon = view.findViewById(R.id.iv_edit_profile_icon);
        btnLogout         = view.findViewById(R.id.btn_logout);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        View.OnClickListener abrirEdicion = v -> {
            // Pasamos requireContext() porque estamos en un Fragment
            Intent intent = new Intent(requireContext(), EditarPerfilActivity.class);
            if (currentProfile != null) {
                intent.putExtra(EditarPerfilActivity.EXTRA_PROFILE, currentProfile);
            }
            editLauncher.launch(intent);
        };

        btnEditProfile.setOnClickListener(abrirEdicion);
        ivEditProfileIcon.setOnClickListener(abrirEdicion);
        btnLogout.setOnClickListener(v -> confirmarCierreSesion());

        // ¡ATENCIÓN! Aquí borramos toda la lógica del ll_bottom_nav que hacía explotar tu app

        cargarPerfil();
    }

    // ─── Carga ────────────────────────────────────────────────────────────────

    private void cargarPerfil() {
        apiService.miPerfil().enqueue(new Callback<ApiResponse<ProfileData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProfileData>> call,
                                   Response<ApiResponse<ProfileData>> response) {
                // Validación vital anti-crash
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {
                    currentProfile = response.body().getData();
                    mostrarPerfil(currentProfile);
                } else {
                    Toast.makeText(requireContext(),
                            "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProfileData>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(),
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarPerfil(ProfileData p) {
        tvFullName.setText(p.getNombre() + " " + p.getApellido());
        tvUsername.setText("@" + p.getUsername());
        tvLocation.setText(anioMiembro(p.getCreadoEn()));

        if (p.getBiografia() != null && !p.getBiografia().isEmpty()) {
            tvBio.setText(p.getBiografia());
            tvBio.setVisibility(View.VISIBLE);
        } else {
            tvBio.setVisibility(View.GONE);
        }

        String iniciales = iniciales(p.getNombre(), p.getApellido());
        tvAvatarInitials.setText(iniciales);

        // Control seguro de la URL del avatar
        String avatarUrl = null;
        if (p.getAvatar() != null && !p.getAvatar().isEmpty()) {
            avatarUrl = p.getAvatar() + "?t=" + System.currentTimeMillis();
        }

        Glide.with(requireContext())
                .load(avatarUrl)
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
        new AlertDialog.Builder(requireContext(), R.style.Theme_Trace_Dialog)
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que deseas cerrar sesión?")
                .setPositiveButton("Cerrar sesión", (d, w) -> {
                    SessionManager.getInstance(requireContext()).logout();
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish(); // Cierra el MainActivity que sostiene al Fragment
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