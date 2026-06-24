package com.example.apptrace.network;

import com.example.apptrace.model.activity.ActivityData;
import com.example.apptrace.model.activity.ActivityDetail;
import com.example.apptrace.model.activity.FinalizarData;
import com.example.apptrace.model.activity.GpsBatch;
import com.example.apptrace.model.activity.IniciarActividadRequest;
import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.auth.LoginData;
import com.example.apptrace.model.auth.LoginRequest;
import com.example.apptrace.model.profile.ChangePasswordData;
import com.example.apptrace.model.profile.EditProfileRequest;
import com.example.apptrace.model.profile.ProfileData;
import com.example.apptrace.model.route.EditRouteRequest;
import com.example.apptrace.model.route.RouteData;
import com.example.apptrace.model.route.RouteDetail;

import java.util.List;
import com.example.apptrace.models.Comentario;
import com.example.apptrace.models.Grupo;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.models.Reaccion;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
    Call<List<Publicacion>> getFeedPrincipal();

    // Listar las comunidades sugeridas y unidas
    @GET("grupos")
    Call<List<Grupo>> getGrupos();

    // Enviar un comentario en una publicación
    @POST("comentarios")
    Call<Comentario> crearComentario(@Body Comentario comentario);

    // Obtener el muro interno de una comunidad específica
    @GET("grupos/{id}/publicaciones")
    Call<List<Publicacion>> getPublicacionesGrupo(@Path("id") int grupoId);

    // Dar me gusta a una publicación o ruta compartida
    @POST("reacciones")
    Call<Reaccion> crearReaccion(@Body Reaccion reaccion);

    // ── Actividades (Módulo 3) ────────────────────────────────────────────────

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

    // ── Rutas (Módulo 4) ──────────────────────────────────────────────────────

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
    Call<ApiResponse<RouteData>> editarRuta(@Path("id") int rutaId,
                                             @Body EditRouteRequest request);

    @DELETE("rutas/{id}")
    Call<ApiResponse<Object>> eliminarRuta(@Path("id") int rutaId);
}
