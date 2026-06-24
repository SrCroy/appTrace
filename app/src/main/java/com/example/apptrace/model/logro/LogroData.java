package com.example.apptrace.model.logro;

import com.google.gson.annotations.SerializedName;

public class LogroData {

    private int id;
    private String clave;
    private String nombre;
    private String descripcion;
    private String icono;

    @SerializedName("tipo_disparador")
    private String tipoDisparador;

    @SerializedName("valor_disparador")
    private double valorDisparador;

    @SerializedName("tipo_deporte")
    private String tipoDeporte;

    private boolean obtenido;

    @SerializedName("obtenido_en")
    private String obtenidoEn;

    public int getId()                 { return id; }
    public String getClave()           { return clave; }
    public String getNombre()          { return nombre; }
    public String getDescripcion()     { return descripcion; }
    public String getIcono()           { return icono; }
    public String getTipoDisparador()  { return tipoDisparador; }
    public double getValorDisparador() { return valorDisparador; }
    public String getTipoDeporte()     { return tipoDeporte; }
    public boolean isObtenido()        { return obtenido; }
    public String getObtenidoEn()      { return obtenidoEn; }
}
