package com.example.apptrace;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.profile.ChangePasswordData;
import com.example.apptrace.model.profile.EditProfileRequest;
import com.example.apptrace.model.profile.ProfileData;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE = "extra_profile";

    private static final int MAX_PX_IMAGEN  = 1024;
    private static final int CALIDAD_JPEG   = 80;

    private ImageView ivClose, ivEditAvatar;
    private TextView tvSaveProfile, tvEditAvatarInitials;
    private TextInputEditText etNombre, etApellido, etBio, etPeso, etAltura, etFecha;
    private MaterialButton btnCambiarContrasena;

    private ApiService apiService;
    private ProfileData perfil;
    private String fechaSeleccionada = null;
    private Uri cameraUri = null;

    private ActivityResultLauncher<String>  cameraPermissionLauncher;
    private ActivityResultLauncher<Uri>     cameraLauncher;
    private ActivityResultLauncher<String>  galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        perfil = (ProfileData) getIntent().getSerializableExtra(EXTRA_PROFILE);

        ivClose               = findViewById(R.id.iv_close);
        tvSaveProfile         = findViewById(R.id.tv_save_profile);
        tvEditAvatarInitials  = findViewById(R.id.tv_edit_avatar_initials);
        ivEditAvatar          = findViewById(R.id.iv_edit_avatar);
        etNombre              = findViewById(R.id.et_edit_nombre);
        etApellido            = findViewById(R.id.et_edit_apellido);
        etBio                 = findViewById(R.id.et_edit_bio);
        etPeso                = findViewById(R.id.et_edit_peso);
        etAltura              = findViewById(R.id.et_edit_altura);
        etFecha               = findViewById(R.id.et_edit_fecha);
        btnCambiarContrasena  = findViewById(R.id.btn_cambiar_contrasena);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        registrarLaunchers();
        rellenarCampos();

        ivClose.setOnClickListener(v -> finish());
        tvSaveProfile.setOnClickListener(v -> guardarPerfil());
        etFecha.setOnClickListener(v -> mostrarDatePicker());
        btnCambiarContrasena.setOnClickListener(v -> mostrarDialogoCambiarContrasena());
        findViewById(R.id.cl_avatar_container).setOnClickListener(v -> mostrarSelectorAvatar());
    }

    // ─── Relleno inicial ──────────────────────────────────────────────────────

    private void rellenarCampos() {
        if (perfil == null) return;

        etNombre.setText(perfil.getNombre());
        etApellido.setText(perfil.getApellido());
        etBio.setText(perfil.getBiografia());
        if (perfil.getPesoKg() != null)
            etPeso.setText(String.valueOf(perfil.getPesoKg()));
        if (perfil.getAlturaCm() != null)
            etAltura.setText(String.valueOf(perfil.getAlturaCm()));
        if (perfil.getFechaNacimiento() != null) {
            fechaSeleccionada = perfil.getFechaNacimiento();
            etFecha.setText(perfil.getFechaNacimiento());
        }

        String iniciales = iniciales(perfil.getNombre(), perfil.getApellido());
        tvEditAvatarInitials.setText(iniciales);

        if (perfil.getAvatar() != null && !perfil.getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(perfil.getAvatar())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivEditAvatar);
            ivEditAvatar.setVisibility(View.VISIBLE);
            tvEditAvatarInitials.setVisibility(View.GONE);
        }
    }

    // ─── Guardar perfil ───────────────────────────────────────────────────────

    private void guardarPerfil() {
        String nombre   = texto(etNombre);
        String apellido = texto(etApellido);
        String bio      = texto(etBio);
        String pesoStr  = texto(etPeso);
        String altStr   = texto(etAltura);

        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Nombre y apellido son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Double peso   = pesoStr.isEmpty()   ? null : parseDouble(pesoStr);
        Double altura = altStr.isEmpty()     ? null : parseDouble(altStr);
        String fecha  = fechaSeleccionada;

        tvSaveProfile.setEnabled(false);
        tvSaveProfile.setText("Guardando...");

        EditProfileRequest request = new EditProfileRequest(nombre, apellido, bio,
                peso, altura, fecha);

        apiService.editarPerfil(request).enqueue(new Callback<ApiResponse<ProfileData>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProfileData>> call,
                                   Response<ApiResponse<ProfileData>> response) {
                tvSaveProfile.setEnabled(true);
                tvSaveProfile.setText("Guardar");

                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditarPerfilActivity.this,
                            "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProfileData>> call, Throwable t) {
                tvSaveProfile.setEnabled(true);
                tvSaveProfile.setText("Guardar");
                Toast.makeText(EditarPerfilActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ─── Cambiar contraseña ───────────────────────────────────────────────────

    private void mostrarDialogoCambiarContrasena() {
        // 1. "Inflamos" (cargamos) el diseño XML que acabas de crear
        View viewDialogo = getLayoutInflater().inflate(R.layout.dialog_cambiar_contrasena, null);

        // 2. Enlazamos los campos de texto usando la vista del diálogo
        EditText etActual = viewDialogo.findViewById(R.id.etContrasenaActual);
        EditText etNueva = viewDialogo.findViewById(R.id.etNuevaContrasena);
        EditText etConfirmar = viewDialogo.findViewById(R.id.etConfirmarContrasena);

        new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_Trace_Dialog)
                .setTitle("Cambiar contraseña")
                .setView(viewDialogo) // <--- Aquí le pasamos tu diseño
                .setPositiveButton("Cambiar", (d, w) -> {
                    String actual = etActual.getText().toString().trim();
                    String nueva = etNueva.getText().toString().trim();
                    String confirmar = etConfirmar.getText().toString().trim();

                    if (actual.isEmpty() || nueva.isEmpty()) {
                        Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!nueva.equals(confirmar)) {
                        Toast.makeText(this, "Las contraseñas nuevas no coinciden", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (nueva.length() < 6) {
                        Toast.makeText(this, "Mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    enviarCambioContrasena(actual, nueva);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void enviarCambioContrasena(String actual, String nueva) {
        apiService.cambiarContrasena(new ChangePasswordData(actual, nueva))
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call,
                                           Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            Toast.makeText(EditarPerfilActivity.this,
                                    "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            String msg = response.body() != null
                                    ? response.body().getMessage()
                                    : "Error al cambiar contraseña";
                            Toast.makeText(EditarPerfilActivity.this,
                                    msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        Toast.makeText(EditarPerfilActivity.this,
                                "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ─── Avatar ───────────────────────────────────────────────────────────────

    private void registrarLaunchers() {
        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) abrirCamara();
                    else Toast.makeText(this, "Permiso de cámara denegado",
                            Toast.LENGTH_SHORT).show();
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && cameraUri != null) subirAvatar(cameraUri);
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> { if (uri != null) subirAvatar(uri); }
        );
    }

    private void mostrarSelectorAvatar() {
        new AlertDialog.Builder(this)
                .setTitle("Cambiar foto de perfil")
                .setItems(new String[]{"Tomar foto", "Elegir de galería"}, (d, which) -> {
                    if (which == 0) solicitarCamara();
                    else galleryLauncher.launch("image/*");
                })
                .show();
    }

    private void solicitarCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void abrirCamara() {
        try {
            File f = File.createTempFile("avatar_", ".jpg", getCacheDir());
            cameraUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", f);
            cameraLauncher.launch(cameraUri);
        } catch (IOException e) {
            Toast.makeText(this, "No se pudo acceder a la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private void subirAvatar(Uri uri) {
        MultipartBody.Part part;
        try {
            part = crearPartAvatar(uri);
        } catch (IOException e) {
            Toast.makeText(this, "No se pudo procesar la imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.actualizarAvatar(part).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "Foto actualizada", Toast.LENGTH_SHORT).show();
                    ivEditAvatar.setImageURI(uri);
                    ivEditAvatar.setImageTintList(null);
                    ivEditAvatar.setVisibility(View.VISIBLE);
                    tvEditAvatarInitials.setVisibility(View.GONE);
                } else {
                    Toast.makeText(EditarPerfilActivity.this,
                            "Error al subir la foto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MultipartBody.Part crearPartAvatar(Uri uri) throws IOException {
        InputStream is = getContentResolver().openInputStream(uri);
        Bitmap original = BitmapFactory.decodeStream(is);
        is.close();

        int w = original.getWidth(), h = original.getHeight();
        Bitmap scaled = original;
        if (w > MAX_PX_IMAGEN || h > MAX_PX_IMAGEN) {
            float ratio = Math.min((float) MAX_PX_IMAGEN / w, (float) MAX_PX_IMAGEN / h);
            scaled = Bitmap.createScaledBitmap(original,
                    Math.round(w * ratio), Math.round(h * ratio), true);
            original.recycle();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, CALIDAD_JPEG, bos);
        scaled.recycle();

        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), bos.toByteArray());
        return MultipartBody.Part.createFormData("avatar", "avatar.jpg", body);
    }

    // ─── DatePicker ───────────────────────────────────────────────────────────

    private void mostrarDatePicker() {
        Calendar cal = Calendar.getInstance();
        if (fechaSeleccionada != null && fechaSeleccionada.length() >= 10) {
            try {
                String[] partes = fechaSeleccionada.split("-");
                cal.set(Integer.parseInt(partes[0]),
                        Integer.parseInt(partes[1]) - 1,
                        Integer.parseInt(partes[2]));
            } catch (Exception ignored) { }
        }

        DatePickerDialog dpd = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    fechaSeleccionada = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", year, month + 1, day);
                    etFecha.setText(fechaSeleccionada);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 86400000L);
        dpd.show();
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    private String texto(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private String iniciales(String nombre, String apellido) {
        StringBuilder sb = new StringBuilder();
        if (nombre != null && !nombre.isEmpty())   sb.append(nombre.charAt(0));
        if (apellido != null && !apellido.isEmpty()) sb.append(apellido.charAt(0));
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    private Double parseDouble(String s) {
        try { return Double.parseDouble(s.replace(",", ".")); }
        catch (NumberFormatException e) { return null; }
    }
}
