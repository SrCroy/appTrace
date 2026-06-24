package com.example.apptrace.model.logro;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MisLogrosResponse {

    private boolean success;
    private List<LogroData> data;
    private Resumen resumen;

    public boolean isSuccess()     { return success; }
    public List<LogroData> getData() { return data; }
    public Resumen getResumen()    { return resumen; }

    public static class Resumen {
        @SerializedName("total_disponibles")
        private int totalDisponibles;

        @SerializedName("total_obtenidos")
        private int totalObtenidos;

        @SerializedName("porcentaje")
        private double porcentaje;

        public int getTotalDisponibles()  { return totalDisponibles; }
        public int getTotalObtenidos()    { return totalObtenidos; }
        public double getPorcentaje()     { return porcentaje; }
    }
}
