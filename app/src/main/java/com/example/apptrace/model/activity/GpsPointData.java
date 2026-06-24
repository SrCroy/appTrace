package com.example.apptrace.model.activity;

import java.io.Serializable;

public class GpsPointData implements Serializable {
    private int id;
    private double latitud;
    private double longitud;
    private double altitud_m;
    private double velocidad_ms;
    private int secuencia;

    public double getLatitud()  { return latitud; }
    public double getLongitud() { return longitud; }
    public double getAltitudM() { return altitud_m; }
    public int getSecuencia()   { return secuencia; }
}
