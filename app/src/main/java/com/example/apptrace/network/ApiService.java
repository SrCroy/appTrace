package com.example.apptrace.network;

import com.example.apptrace.models.Comentario;
import com.example.apptrace.models.Grupo;
import com.example.apptrace.models.Publicacion;
import com.example.apptrace.models.Reaccion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Obtener el muro de noticias global
    @GET("publicaciones")
    Call<List<Publicacion>> getFeedPrincipal(@Header("Authorization") String token);

    // Listar las comunidades sugeridas y unidas
    @GET("grupos")
    Call<List<Grupo>> getGrupos(@Header("Authorization") String token);

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

    // Enviar un comentario en una publicación
    @POST("comentarios")
    Call<Comentario> crearComentario(
            @Header("Authorization") String token,
            @Body Comentario comentario
    );
}
