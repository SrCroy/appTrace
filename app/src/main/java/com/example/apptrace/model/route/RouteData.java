package com.example.apptrace.model.route;

import java.io.Serializable;

public class RouteData implements Serializable {
    private int id;
    private int user_id;
    private String nombre;
    private String descripcion;
    private Double distancia_km;
    private String dificultad;
    private String tipo;
    private String miniatura;
    private String created_at;

    public int getId()              { return id; }
    public String getNombre()       { return nombre; }
    public String getDescripcion()  { return descripcion; }
    public Double getDistanciaKm()  { return distancia_km; }
    public String getDificultad()   { return dificultad; }
    public String getTipo()         { return tipo; }
    public String getMiniatura()    { return miniatura; }
    public String getCreatedAt()    { return created_at; }
}
