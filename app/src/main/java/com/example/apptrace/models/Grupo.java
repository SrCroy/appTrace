package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

public class Grupo {
    @SerializedName("idGrupo")
    private int idGrupo;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("privacidad")
    private String privacidad;

    @SerializedName("total_miembros")
    private int totalMiembros;

    @SerializedName("propietario_id")
    private int propietarioId;

    @SerializedName("creado_en")
    private String creadoEn;

    @SerializedName("actualizado_en")
    private String actualizadoEn;

    public Grupo() {
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(String privacidad) {
        this.privacidad = privacidad;
    }

    public int getTotalMiembros() {
        return totalMiembros;
    }

    public void setTotalMiembros(int totalMiembros) {
        this.totalMiembros = totalMiembros;
    }

    public int getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(int propietarioId) {
        this.propietarioId = propietarioId;
    }

    public String getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(String creadoEn) {
        this.creadoEn = creadoEn;
    }

    public String getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(String actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }
}
