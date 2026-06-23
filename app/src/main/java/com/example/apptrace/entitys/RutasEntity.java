package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "rutas",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = @Index("usuario_id")
)
public class RutasEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String descripcion;
    private Double distancia_km;
    private Double desnivel_positivo_m;
    private int dificultad;
    private String privacidad;
    private String tipo_deporte;
    private int veces_usada;
    private String miniatura;
    private int usuario_id;
    private String created_at;
    private String updated_at;
    public RutasEntity(String nombre, int dificultad, String privacidad,
                       String tipo_deporte, int veces_usada, int usuario_id) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.privacidad = privacidad;
        this.tipo_deporte = tipo_deporte;
        this.veces_usada = veces_usada;
        this.usuario_id = usuario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Double getDistancia_km() {
        return distancia_km;
    }

    public void setDistancia_km(Double distancia_km) {
        this.distancia_km = distancia_km;
    }

    public Double getDesnivel_positivo_m() {
        return desnivel_positivo_m;
    }

    public void setDesnivel_positivo_m(Double desnivel_positivo_m) {
        this.desnivel_positivo_m = desnivel_positivo_m;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public String getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(String privacidad) {
        this.privacidad = privacidad;
    }

    public String getTipo_deporte() {
        return tipo_deporte;
    }

    public void setTipo_deporte(String tipo_deporte) {
        this.tipo_deporte = tipo_deporte;
    }

    public int getVeces_usada() {
        return veces_usada;
    }

    public void setVeces_usada(int veces_usada) {
        this.veces_usada = veces_usada;
    }

    public String getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(String miniatura) {
        this.miniatura = miniatura;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
