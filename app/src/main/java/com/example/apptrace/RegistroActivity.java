package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.auth.RegisterRequest;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etNombre, etApellido,
            etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvBack;
    private ApiService apiService;

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

        apiService = RetrofitClient.getClient().create(ApiService.class);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentarRegistro();
            }
        });
    }

    private void intentarRegistro() {
        String username       = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String nombre         = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String apellido       = etApellido.getText() != null ? etApellido.getText().toString().trim() : "";
        String email          = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password       = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        if (username.isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCargando(true);

        RegisterRequest request = new RegisterRequest(username, nombre, apellido, email, password);

        apiService.register(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {
                mostrarCargando(false);

                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {
                    Toast.makeText(RegistroActivity.this,
                            "Cuenta creada, inicia sesión",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegistroActivity.this,
                            obtenerMensajeError(response),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                mostrarCargando(false);
                Toast.makeText(RegistroActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String obtenerMensajeError(Response<ApiResponse<Object>> response) {
        try {
            if (response.errorBody() != null) {
                ApiResponse<?> error = new Gson().fromJson(
                        response.errorBody().string(), ApiResponse.class);
                if (error != null && error.getMessage() != null) {
                    return error.getMessage();
                }
            }
        } catch (IOException e) {
            // cae al mensaje genérico
        }
        return "Error al registrar";
    }

    private void mostrarCargando(boolean cargando) {
        btnRegister.setEnabled(!cargando);
        btnRegister.setText(cargando ? "Registrando..." : "Crear cuenta");
    }
}