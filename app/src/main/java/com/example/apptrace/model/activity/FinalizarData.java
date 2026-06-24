package com.example.apptrace.model.activity;

import java.io.Serializable;

public class FinalizarData implements Serializable {
    private double distancia_km;
    private int duracion_seg;
    private double calorias;
    private double desnivel;
    private double ritmo_promedio;
    private double velocidad_promedio_kmh;

    public double getDistanciaKm()          { return distancia_km; }
    public int getDuracionSeg()             { return duracion_seg; }
    public double getCalorias()             { return calorias; }
    public double getDesnivel()             { return desnivel; }
    public double getRitmoPromedio()        { return ritmo_promedio; }
    public double getVelocidadPromedioKmh() { return velocidad_promedio_kmh; }
}
