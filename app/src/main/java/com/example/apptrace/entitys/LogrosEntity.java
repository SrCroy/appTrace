package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "logros")
public class LogrosEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String clave;
    private String nombre;
    private String descripcion;
    private String icono;
    private String tipo_disparador;
    private Double valor_disparador;
    private String tipo_deporte;
    private String creado_en;
    public LogrosEntity(String clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getTipo_disparador() {
        return tipo_disparador;
    }

    public void setTipo_disparador(String tipo_disparador) {
        this.tipo_disparador = tipo_disparador;
    }

    public Double getValor_disparador() {
        return valor_disparador;
    }

    public void setValor_disparador(Double valor_disparador) {
        this.valor_disparador = valor_disparador;
    }

    public String getTipo_deporte() {
        return tipo_deporte;
    }

    public void setTipo_deporte(String tipo_deporte) {
        this.tipo_deporte = tipo_deporte;
    }

    public String getCreado_en() {
        return creado_en;
    }

    public void setCreado_en(String creado_en) {
        this.creado_en = creado_en;
    }
}
