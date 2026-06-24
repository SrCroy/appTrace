package com.example.apptrace;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private static final int NIVEL_BATERIA_CRITICO = 5;
    private static final int NIVEL_BATERIA_BAJO    = 15;
    private static final int MAX_PX_IMAGEN         = 1024;
    private static final int CALIDAD_JPEG          = 80;

    private TextInputEditText etUsername, etNombre, etApellido,
            etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvBack;
    private ImageView ivAvatar;

    private Uri avatarUri  = null;
    private Uri cameraUri  = null;
    private ApiService apiService;

    private ActivityResultLauncher<String>  cameraPermissionLauncher;
    private ActivityResultLauncher<Uri>     cameraLauncher;
    private ActivityResultLauncher<String>  galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUsername        = findViewById(R.id.et_username);
        etNombre          = findViewById(R.id.et_nombre);
        etApellido        = findViewById(R.id.et_apellido);
        etEmail           = findViewById(R.id.et_email);
        etPassword        = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister       = findViewById(R.id.btn_register);
        tvBack            = findViewById(R.id.tv_back);
        ivAvatar          = findViewById(R.id.iv_avatar);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        registrarLaunchers();

        ivAvatar.setOnClickListener(v -> mostrarSelectorImagen());
        tvBack.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> intentarRegistro());
    }

    // ─── Launchers ────────────────────────────────────────────────────────────

    private void registrarLaunchers() {
        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        abrirCamara();
                    } else {
                        Toast.makeText(this,
                                "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && cameraUri != null) {
                        mostrarAvatarSeleccionado(cameraUri);
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        mostrarAvatarSeleccionado(uri);
                    }
                }
        );
    }

    // ─── Selección de imagen ──────────────────────────────────────────────────

    private void mostrarSelectorImagen() {
        new AlertDialog.Builder(this, R.style.Theme_Trace_Dialog)
                .setTitle("Foto de perfil")
                .setItems(new String[]{"Tomar foto", "Elegir de galería"}, (dialog, which) -> {
                    if (which == 0) {
                        solicitarCamara();
                    } else {
                        galleryLauncher.launch("image/*");
                    }
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
            File photoFile = File.createTempFile("avatar_", ".jpg", getCacheDir());
            cameraUri = FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", photoFile);
            cameraLauncher.launch(cameraUri);
        } catch (IOException e) {
            Toast.makeText(this, "No se pudo acceder a la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarAvatarSeleccionado(Uri uri) {
        avatarUri = uri;
        ivAvatar.setImageURI(uri);
        ivAvatar.setImageTintList(null);
        ivAvatar.setPadding(0, 0, 0, 0);
    }

    // ─── Registro ─────────────────────────────────────────────────────────────

    private void intentarRegistro() {
        String username        = texto(etUsername);
        String nombre          = texto(etNombre);
        String apellido        = texto(etApellido);
        String email           = texto(etEmail);
        String password        = texto(etPassword);
        String confirmPassword = texto(etConfirmPassword);

        if (username.isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        MultipartBody.Part avatarPart = null;
        if (avatarUri != null) {
            try {
                avatarPart = crearPartAvatar(avatarUri);
            } catch (IOException e) {
                Toast.makeText(this, "No se pudo procesar la imagen", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        verificarBateriaYRegistrar(username, nombre, apellido, email, password, avatarPart);
    }

    private void verificarBateriaYRegistrar(String username, String nombre, String apellido,
                                             String email, String password,
                                             MultipartBody.Part avatarPart) {
        int nivel    = nivelBateria();
        boolean cargando = estasCargando();

        if (nivel <= NIVEL_BATERIA_CRITICO && !cargando) {
            Toast.makeText(this,
                    "Batería crítica (" + nivel + "%). Conecta el cargador antes de continuar.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (nivel <= NIVEL_BATERIA_BAJO && !cargando) {
            new AlertDialog.Builder(this, R.style.Theme_Trace_Dialog)
                    .setTitle("Batería baja")
                    .setMessage("La batería está al " + nivel + "%. El proceso puede "
                            + "interrumpirse si se agota. ¿Deseas continuar?")
                    .setPositiveButton("Continuar", (d, w) ->
                            realizarRegistro(username, nombre, apellido, email, password, avatarPart))
                    .setNegativeButton("Cancelar", null)
                    .show();
            return;
        }

        realizarRegistro(username, nombre, apellido, email, password, avatarPart);
    }

    private void realizarRegistro(String username, String nombre, String apellido,
                                   String email, String password,
                                   MultipartBody.Part avatarPart) {
        mostrarCargando(true);

        apiService.register(
                textPart(username),
                textPart(nombre),
                textPart(apellido),
                textPart(email),
                textPart(password),
                avatarPart
        ).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {
                mostrarCargando(false);
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {
                    Toast.makeText(RegistroActivity.this,
                            "Cuenta creada, inicia sesión", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegistroActivity.this,
                            obtenerMensajeError(response), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                mostrarCargando(false);
                Toast.makeText(RegistroActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ─── Batería ──────────────────────────────────────────────────────────────

    private int nivelBateria() {
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int nivel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return nivel < 0 ? 100 : nivel;   // -1 si no disponible → asumir cargada
    }

    private boolean estasCargando() {
        Intent status = registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (status == null) return false;
        int plugged = status.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC
                || plugged == BatteryManager.BATTERY_PLUGGED_USB
                || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
    }

    // ─── Imagen ───────────────────────────────────────────────────────────────

    private MultipartBody.Part crearPartAvatar(Uri uri) throws IOException {
        byte[] bytes = comprimirImagen(uri);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        return MultipartBody.Part.createFormData("avatar", "avatar.jpg", body);
    }

    private byte[] comprimirImagen(Uri uri) throws IOException {
        InputStream is = getContentResolver().openInputStream(uri);
        Bitmap original = BitmapFactory.decodeStream(is);
        is.close();

        Bitmap scaled = original;
        int w = original.getWidth(), h = original.getHeight();
        if (w > MAX_PX_IMAGEN || h > MAX_PX_IMAGEN) {
            float ratio = Math.min((float) MAX_PX_IMAGEN / w, (float) MAX_PX_IMAGEN / h);
            scaled = Bitmap.createScaledBitmap(original,
                    Math.round(w * ratio), Math.round(h * ratio), true);
            original.recycle();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, CALIDAD_JPEG, bos);
        scaled.recycle();
        return bos.toByteArray();
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    private RequestBody textPart(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private String texto(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private String obtenerMensajeError(Response<ApiResponse<Object>> response) {
        try {
            if (response.errorBody() != null) {
                ApiResponse<?> error = new Gson().fromJson(
                        response.errorBody().string(), ApiResponse.class);
                if (error != null && error.getMessage() != null) return error.getMessage();
            }
        } catch (IOException e) {
            // cae al genérico
        }
        return "Error al registrar";
    }

    private void mostrarCargando(boolean cargando) {
        btnRegister.setEnabled(!cargando);
        btnRegister.setText(cargando ? "Registrando..." : "Crear cuenta");
    }
}
