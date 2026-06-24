package com.example.apptrace.model.activity;

public class GpsPointSend {
    private double latitud;
    private double longitud;
    private double altitud_m;
    private double velocidad_ms;
    private double precision_m;
    private int secuencia;
    private String registrado_en;

    public GpsPointSend(double latitud, double longitud, double altitud_m,
                        double velocidad_ms, double precision_m,
                        int secuencia, String registrado_en) {
        this.latitud       = latitud;
        this.longitud      = longitud;
        this.altitud_m     = altitud_m;
        this.velocidad_ms  = velocidad_ms;
        this.precision_m   = precision_m;
        this.secuencia     = secuencia;
        this.registrado_en = registrado_en;
    }

    public double getLatitud()  { return latitud; }
    public double getLongitud() { return longitud; }
}
