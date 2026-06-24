package com.example.apptrace.model.activity;

import java.util.List;

public class GpsBatch {
    private List<GpsPointSend> puntos;

    public GpsBatch(List<GpsPointSend> puntos) {
        this.puntos = puntos;
    }
}
