package com.example.apptrace.model.route;

import com.example.apptrace.model.activity.GpsPointData;

import java.util.List;

public class RouteDetail extends RouteData {
    private List<GpsPointData> puntos_gps;

    public List<GpsPointData> getPuntosGps() { return puntos_gps; }
}
