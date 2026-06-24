package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

public class Comentario {
    @SerializedName("idComentario")
    private int idComentario;

    @SerializedName("comentable_tipo")
    private String comentableTipo;

    @SerializedName("comentable_id")
    private int comentableId; // ID de la publicación comentada

    @SerializedName("cuerpo")
    private String cuerpo; // Contenido del texto

    @SerializedName("estado")
    private String estado; // "activo" o "eliminado"

    @SerializedName("padre_id")
    private Integer padreId; // Nullable - ID del comentario al que responde

    @SerializedName("usuario_id")
    private int usuarioId; // Usuario que escribió el comentario

    @SerializedName("creado_en")
    private String creadoEn;

    @SerializedName("actualizado_en")
    private String actualizadoEn;

    public Comentario() {
    }

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public String getComentableTipo() {
        return comentableTipo;
    }

    public void setComentableTipo(String comentableTipo) {
        this.comentableTipo = comentableTipo;
    }

    public int getComentableId() {
        return comentableId;
    }

    public void setComentableId(int comentableId) {
        this.comentableId = comentableId;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPadreId() {
        return padreId;
    }

    public void setPadreId(Integer padreId) {
        this.padreId = padreId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
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
