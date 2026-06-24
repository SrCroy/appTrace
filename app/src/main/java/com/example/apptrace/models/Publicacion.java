package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

public class Publicacion {
    @SerializedName("id")
    private int idPublicacion;

    @SerializedName("contenido")
    private String contenido;

    @SerializedName("privacidad")
    private String privacidad;

    // Relaciones
    @SerializedName("usuario_id")
    private int usuarioId;

    @SerializedName("actividad_id")
    private Integer actividadId;

    @SerializedName("ruta_id")
    private Integer rutaId;

    @SerializedName("creado_en")
    private String creadoEn;

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(String privacidad) {
        this.privacidad = privacidad;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getActividadId() {
        return actividadId;
    }

    public void setActividadId(Integer actividadId) {
        this.actividadId = actividadId;
    }

    public Integer getRutaId() {
        return rutaId;
    }

    public void setRutaId(Integer rutaId) {
        this.rutaId = rutaId;
    }

    public String getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(String creadoEn) {
        this.creadoEn = creadoEn;
    }
}
