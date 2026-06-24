package com.example.apptrace.model.activity;

import java.io.Serializable;

public class ActivityData implements Serializable {
    private int id;
    private int user_id;
    private String titulo;
    private String tipo_deporte;
    private String dificultad;
    private String privacidad;
    private String estado;
    private String started_at;
    private String finished_at;
    private Double distancia_km;
    private Integer duracion_seg;
    private Double calorias;
    private Double desnivel;
    private Double ritmo_promedio;
    private Double velocidad_promedio_kmh;

    public int getId()                         { return id; }
    public String getTitulo()                  { return titulo; }
    public String getTipoDeporte()             { return tipo_deporte; }
    public String getDificultad()              { return dificultad; }
    public String getEstado()                  { return estado; }
    public String getStartedAt()               { return started_at; }
    public String getFinishedAt()              { return finished_at; }
    public Double getDistanciaKm()             { return distancia_km; }
    public Integer getDuracionSeg()            { return duracion_seg; }
    public Double getCalorias()                { return calorias; }
    public Double getDesnivel()                { return desnivel; }
    public Double getRitmoPromedio()           { return ritmo_promedio; }
    public Double getVelocidadPromedioKmh()    { return velocidad_promedio_kmh; }
}
