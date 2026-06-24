package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.auth.LoginData;
import com.example.apptrace.model.auth.LoginRequest;
import com.example.apptrace.network.ApiService;
import com.example.apptrace.network.RetrofitClient;
import com.example.apptrace.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegisterLink;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentarLogin();
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });
    }

    private void intentarLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCargando(true);
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<ApiResponse<LoginData>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginData>> call,
                                   Response<ApiResponse<LoginData>> response) {
                mostrarCargando(false);

                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()) {
                    LoginData data = response.body().getData();
                    SessionManager.getInstance(LoginActivity.this).saveSession(data);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            obtenerMensajeError(response),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginData>> call, Throwable t) {
                mostrarCargando(false);
                Toast.makeText(LoginActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String obtenerMensajeError(Response<ApiResponse<LoginData>> response) {
        try {
            if (response.errorBody() != null) {
                ApiResponse<?> error = new Gson().fromJson(
                        response.errorBody().string(), ApiResponse.class);
                if (error != null && error.getMessage() != null) {
                    return error.getMessage();
                }
            }
        } catch (Exception e) {
            // respuesta no es JSON (ej. HTML de error del servidor)
        }
        return "Credenciales inválidas";
    }

    private void mostrarCargando(boolean cargando) {
        btnLogin.setEnabled(!cargando);
        btnLogin.setText(cargando ? "Ingresando..." : "Ingresar");
    }
}