package com.example.apptrace.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Publicacion {
    @NonNull
    @SerializedName("id_publicacion")
    private int idPublicacion;

    @SerializedName("contenido")
    private String contenido;

    @SerializedName("privacidad")
    private String privacidad;

    @SerializedName("usuario_id")
    private int usuarioId;

    @SerializedName("actividad_id")
    private Integer actividadId;

    @SerializedName("ruta_id")
    private Integer rutaId;

    @SerializedName("creado_en")
    private String creadoEn;

    @SerializedName("username")
    private String userName; // Nombre del usuario que publica

    @SerializedName("likes_count")
    private int likesCount; // Conteo de reacciones desde el servidor

    @SerializedName("comentarios_count")
    private int comentariosCount; // Conteo de comentarios

    @SerializedName("actividad")
    private ActividadAnidada actividad; // Datos de la actividad si existe

    public int getIdPublicacion() { return idPublicacion; }
    public void setIdPublicacion(int idPublicacion) { this.idPublicacion = idPublicacion; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getPrivacidad() { return privacidad; }
    public void setPrivacidad(String privacidad) { this.privacidad = privacidad; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public Integer getActividadId() { return actividadId; }
    public void setActividadId(Integer actividadId) { this.actividadId = actividadId; }

    public Integer getRutaId() { return rutaId; }
    public void setRutaId(Integer rutaId) { this.rutaId = rutaId; }

    public String getCreadoEn() { return creadoEn; }
    public void setCreadoEn(String creadoEn) { this.creadoEn = creadoEn; }

    public String getUserName() { return userName; }
    public int getLikesCount() { return likesCount; }
    public int getComentariosCount() { return comentariosCount; }
    public ActividadAnidada getActividad() { return actividad; }

    // Clase interna para mapear la actividad asociada
    public static class ActividadAnidada {
        @SerializedName("tipo_deporte")
        private String tipoDeporte;

        @SerializedName("distancia_km")
        private double distanciaKm;

        @SerializedName("duracion_seg")
        private double duracionSeg;

        @SerializedName("ritmo_promedio")
        private double ritmoPromedio;

        public String getTipoDeporte() { return tipoDeporte; }
        public double getDistanciaKm() { return distanciaKm; }
        public double getDuracionSeg() { return duracionSeg; }
        public double getRitmoPromedio() { return ritmoPromedio; }
    }
}