package com.example.apptrace.model.route;

public class EditRouteRequest {
    private String nombre;
    private String descripcion;
    private int    dificultad;
    private String tipo_deporte;
    private String privacidad;

    public EditRouteRequest(String nombre, String descripcion,
                            int dificultad, String tipoDeporte, String privacidad) {
        this.nombre       = nombre;
        this.descripcion  = descripcion;
        this.dificultad   = dificultad;
        this.tipo_deporte = tipoDeporte;
        this.privacidad   = privacidad;
    }
}
