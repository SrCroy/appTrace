package com.example.apptrace.network;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.auth.LoginData;
import com.example.apptrace.model.auth.LoginRequest;
import com.example.apptrace.model.profile.ChangePasswordData;
import com.example.apptrace.model.profile.EditProfileRequest;
import com.example.apptrace.model.profile.ProfileData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {

    // ── Auth ──────────────────────────────────────────────────────────────────

    @POST("login")
    Call<ApiResponse<LoginData>> login(@Body LoginRequest request);

    @Multipart
    @POST("register")
    Call<ApiResponse<Object>> register(
            @Part("username") RequestBody username,
            @Part("nombre")   RequestBody nombre,
            @Part("apellido") RequestBody apellido,
            @Part("email")    RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part avatar
    );

    // ── Perfil ────────────────────────────────────────────────────────────────

    @GET("perfil")
    Call<ApiResponse<ProfileData>> miPerfil();

    @PUT("perfil/editar")
    Call<ApiResponse<ProfileData>> editarPerfil(@Body EditProfileRequest request);

    @POST("perfil/cambiar-contrase%C3%B1a")
    Call<ApiResponse<Object>> cambiarContrasena(@Body ChangePasswordData request);

    @Multipart
    @POST("avatar/update")
    Call<ApiResponse<Object>> actualizarAvatar(@Part MultipartBody.Part avatar);
}