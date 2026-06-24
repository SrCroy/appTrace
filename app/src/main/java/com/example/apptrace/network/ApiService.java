package com.example.apptrace.network;

import com.example.apptrace.model.activity.ActivityData;
import com.example.apptrace.model.activity.ActivityDetail;
import com.example.apptrace.model.activity.FinalizarData;
import com.example.apptrace.model.activity.GpsBatch;
import com.example.apptrace.model.activity.IniciarActividadRequest;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.auth.LoginData;
import com.example.apptrace.model.auth.LoginRequest;
import com.example.apptrace.model.logro.LogroData;
import com.example.apptrace.model.logro.MisLogrosResponse;
import com.example.apptrace.model.profile.ChangePasswordData;
import com.example.apptrace.model.profile.EditProfileRequest;
import com.example.apptrace.model.profile.ProfileData;
import com.example.apptrace.model.route.EditRouteRequest;
import com.example.apptrace.model.route.RouteData;
import com.example.apptrace.model.route.RouteDetail;
import com.example.apptrace.models.Comentario;
import com.example.apptrace.models.Grupo;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.models.ToggleReaccionResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // ── Logros (Módulo 5) ─────────────────────────────────────────────────────

    @GET("logros")
    Call<ApiResponse<List<LogroData>>> catalogoLogros();

    @GET("logros/mis-logros")
    Call<MisLogrosResponse> misLogros();

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

    @GET("usuarios/{id}")
    Call<ApiResponse<ProfileData>> obtenerPerfilUsuario(@Path("id") int usuarioId);

    @PUT("perfil/editar")
    Call<ApiResponse<ProfileData>> editarPerfil(@Body EditProfileRequest request);

    @POST("perfil/cambiar-contrase%C3%B1a")
    Call<ApiResponse<Object>> cambiarContrasena(@Body ChangePasswordData request);

    @Multipart
    @POST("avatar/update")
    Call<ApiResponse<Object>> actualizarAvatar(@Part MultipartBody.Part avatar);

    // ── Módulo 6: Publicaciones (Feed) ────────────────────────────────────────

    @GET("publicaciones")
    Call<ApiResponse<List<Publicacion>>> getFeedPrincipal();

    @GET("publicaciones/{id}")
    Call<ApiResponse<Publicacion>> obtenerPublicacion(@Path("id") int id);

    @POST("publicaciones")
    Call<ApiResponse<Publicacion>> crearPublicacion(@Body Publicacion nuevaPublicacion);

    @PUT("publicaciones/{id}")
    Call<ApiResponse<Publicacion>> editarPublicacion(@Path("id") int id, @Body Publicacion publicacion);

    @DELETE("publicaciones/{id}")
    Call<ApiResponse<Object>> eliminarPublicacion(@Path("id") int id);

    @POST("publicaciones/{id}/reaccion")
    Call<ToggleReaccionResponse> toggleReaccion(@Path("id") int publicacionId);

    @POST("publicaciones/{id}/comentario")
    Call<ApiResponse<Comentario>> crearComentario(@Path("id") int publicacionId, @Body Comentario comentario);

    // ── Módulo 7: Grupos ──────────────────────────────────────────────────────

    @GET("grupos")
    Call<ApiResponse<List<Grupo>>> getGrupos();

    @GET("grupos/{id}")
    Call<ApiResponse<Grupo>> obtenerGrupo(@Path("id") int grupoId);

    @POST("grupos")
    Call<ApiResponse<Grupo>> crearGrupo(@Body Grupo grupo);

    @POST("grupos/{id}/unirse")
    Call<ApiResponse<Object>> unirseGrupo(@Path("id") int grupoId);

    @POST("grupos/{id}/salir")
    Call<ApiResponse<Object>> salirGrupo(@Path("id") int grupoId);

    @GET("grupos/{id}/publicaciones")
    Call<ApiResponse<List<Publicacion>>> getPublicacionesGrupo(@Path("id") int grupoId);

    @POST("grupos/{id}/publicaciones")
    Call<ApiResponse<Publicacion>> publicarEnGrupo(@Path("id") int grupoId, @Body Publicacion publicacion);

    @POST("grupos/{grupoId}/publicaciones/{pubId}/reaccion")
    Call<ToggleReaccionResponse> toggleReaccionGrupo(@Path("grupoId") int grupoId, @Path("pubId") int pubId);

    @POST("grupos/{grupoId}/publicaciones/{pubId}/comentario")
    Call<ApiResponse<Comentario>> crearComentarioGrupo(@Path("grupoId") int grupoId, @Path("pubId") int pubId, @Body Comentario comentario);

    // ── Módulo 3: Actividades y GPS ───────────────────────────────────────────

    @POST("actividades/iniciar")
    Call<ApiResponse<ActivityData>> iniciarActividad(@Body IniciarActividadRequest request);

    @POST("actividades/{id}/gps")
    Call<ApiResponse<Object>> enviarGps(@Path("id") int actividadId, @Body GpsBatch batch);

    @POST("actividades/{id}/pausar")
    Call<ApiResponse<Object>> pausarActividad(@Path("id") int actividadId);

    @POST("actividades/{id}/reanudar")
    Call<ApiResponse<Object>> reanudarActividad(@Path("id") int actividadId);

    @POST("actividades/{id}/finalizar")
    Call<ApiResponse<FinalizarData>> finalizarActividad(@Path("id") int actividadId);

    @POST("actividades/{id}/descartar")
    Call<ApiResponse<Object>> descartarActividad(@Path("id") int actividadId);

    @GET("actividades")
    Call<ApiResponse<List<ActivityData>>> listarActividades();

    @GET("actividades/{id}")
    Call<ApiResponse<ActivityDetail>> obtenerActividad(@Path("id") int actividadId);

    @DELETE("actividades/{id}")
    Call<ApiResponse<Object>> eliminarActividad(@Path("id") int actividadId);

    // ── Módulo 4: Rutas ───────────────────────────────────────────────────────

    @GET("rutas")
    Call<ApiResponse<List<RouteData>>> listarRutas();

    @GET("rutas/{id}")
    Call<ApiResponse<RouteDetail>> obtenerRuta(@Path("id") int rutaId);

    @Multipart
    @POST("rutas")
    Call<ApiResponse<RouteData>> crearRuta(
            @Part("nombre")        RequestBody nombre,
            @Part("descripcion")   RequestBody descripcion,
            @Part("dificultad")    RequestBody dificultad,
            @Part("tipo_deporte")  RequestBody tipoDeporte,
            @Part("distancia_km")  RequestBody distanciaKm,
            @Part("privacidad")    RequestBody privacidad
    );

    @Multipart
    @POST("rutas")
    Call<ApiResponse<RouteData>> crearRutaConImagen(
            @Part("nombre")        RequestBody nombre,
            @Part("descripcion")   RequestBody descripcion,
            @Part("dificultad")    RequestBody dificultad,
            @Part("tipo_deporte")  RequestBody tipoDeporte,
            @Part("distancia_km")  RequestBody distanciaKm,
            @Part("privacidad")    RequestBody privacidad,
            @Part MultipartBody.Part miniatura
    );

    @POST("rutas/{id}/puntos-gps")
    Call<ApiResponse<Object>> agregarPuntosRuta(@Path("id") int rutaId, @Body GpsBatch puntos);

    @PUT("rutas/{id}")
    Call<ApiResponse<RouteData>> editarRuta(@Path("id") int rutaId, @Body EditRouteRequest request);

    @DELETE("rutas/{id}")
    Call<ApiResponse<Object>> eliminarRuta(@Path("id") int rutaId);
}
