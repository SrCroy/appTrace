package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "puntos_gps",
        foreignKeys = {
                @ForeignKey(
                        entity = ActividadesEntity.class,
                        parentColumns = "id",
                        childColumns = "actividad_id",
                        onDelete = ForeignKey.CASCADE  // Si se borra la actividad, se borran sus puntos GPS
                )
        },
        indices = @Index("actividad_id")
)
public class PuntosGpsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double latitud;
    private Double longitud;
    private Double altitud_m;
    private Double velocidad_ms;
    private Double precision_m;
    private int secuencia;
    private String registrado_en;
    private int actividad_id;

    public PuntosGpsEntity(int secuencia, String registrado_en, int actividad_id) {
        this.secuencia = secuencia;
        this.registrado_en = registrado_en;
        this.actividad_id = actividad_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getAltitud_m() {
        return altitud_m;
    }

    public void setAltitud_m(Double altitud_m) {
        this.altitud_m = altitud_m;
    }

    public Double getVelocidad_ms() {
        return velocidad_ms;
    }

    public void setVelocidad_ms(Double velocidad_ms) {
        this.velocidad_ms = velocidad_ms;
    }

    public Double getPrecision_m() {
        return precision_m;
    }

    public void setPrecision_m(Double precision_m) {
        this.precision_m = precision_m;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public String getRegistrado_en() {
        return registrado_en;
    }

    public void setRegistrado_en(String registrado_en) {
        this.registrado_en = registrado_en;
    }

    public int getActividad_id() {
        return actividad_id;
    }

    public void setActividad_id(int actividad_id) {
        this.actividad_id = actividad_id;
    }
}
