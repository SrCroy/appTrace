package com.example.apptrace.network;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.auth.LoginData;
import com.example.apptrace.model.auth.LoginRequest;
import com.example.apptrace.model.profile.ChangePasswordData;
import com.example.apptrace.model.profile.EditProfileRequest;
import com.example.apptrace.model.profile.ProfileData;
import com.example.apptrace.models.Comentario;
import com.example.apptrace.models.Grupo;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.models.Reaccion;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    // Obtener el muro de noticias global
    @GET("publicaciones")
    Call<List<Publicacion>> getFeedPrincipal(@Header("Authorization") String token);

    // Listar las comunidades sugeridas y unidas
    @GET("grupos")
    Call<List<Grupo>> getGrupos(@Header("Authorization") String token);

    // Enviar un comentario en una publicación
    @POST("comentarios")
    Call<Comentario> crearComentario(
            @Header("Authorization") String token,
            @Body Comentario comentario
    );

    // Obtener el muro interno de una comunidad específica
    @GET("grupos/{id}/publicaciones")
    Call<List<Publicacion>> getPublicacionesGrupo(
            @Header("Authorization") String token,
            @Path("id") int grupoId
    );

    // Dar me gusta a una publicación o ruta compartida
    @POST("reacciones")
    Call<Reaccion> crearReaccion(
            @Header("Authorization") String token,
            @Body Reaccion reaccion
    );
}
