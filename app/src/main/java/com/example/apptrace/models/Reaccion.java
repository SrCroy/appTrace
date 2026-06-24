package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

public class Reaccion {
    @SerializedName("idReaccion")
    private int idReaccion;

    @SerializedName("reaccionable_tipo")
    private String reaccionableTipo;

    @SerializedName("reaccionable_id")
    private int reaccionableId; // ID del registro que recibe el me gusta

    @SerializedName("usuario_id")
    private int usuarioId; // Usuario que reaccionó

    @SerializedName("creado_en")
    private String creadoEn; // Momento en que se dio el me gusta

    public Reaccion() {
    }

    public int getIdReaccion() {
        return idReaccion;
    }

    public void setIdReaccion(int idReaccion) {
        this.idReaccion = idReaccion;
    }

    public String getReaccionableTipo() {
        return reaccionableTipo;
    }

    public void setReaccionableTipo(String reaccionableTipo) {
        this.reaccionableTipo = reaccionableTipo;
    }

    public int getReaccionableId() {
        return reaccionableId;
    }

    public void setReaccionableId(int reaccionableId) {
        this.reaccionableId = reaccionableId;
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
}
