package com.example.apptrace.model.activity;

public class IniciarActividadRequest {
    private String titulo;
    private String tipo_deporte;
    private String dificultad;
    private String privacidad;
    private Integer ruta_id;

    public IniciarActividadRequest(String titulo, String tipo_deporte,
                                   String dificultad, String privacidad) {
        this.titulo       = titulo;
        this.tipo_deporte = tipo_deporte;
        this.dificultad   = dificultad;
        this.privacidad   = privacidad;
    }

    public void setRutaId(int rutaId) { this.ruta_id = rutaId; }
}
