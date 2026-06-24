package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "actividades",
        foreignKeys = {
                @ForeignKey(
                        entity = RutasEntity.class,
                        parentColumns = "id",
                        childColumns = "ruta_id",
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "id_usuario",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("ruta_id"),
                @Index("id_usuario")
        }
)
public class ActividadesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titulo;
    private String tipo_deporte;
    private int dificultad;
    private String privacidad;
    private Double distancia_km;
    private Double duracion_seg;
    private Double duracion_pausa_segundos;
    private Double calorias;
    private Double desnivel_positivo_m;
    private Double desnivel_negativo_m;
    private Double ritmo_promedio;
    private Double ritmo_maximo;
    private Double velocidad_promedio_kmh;
    private Double velocidad_maxima_kmh;
    private Double inicio_lat;
    private Double inicio_lng;
    private Double final_lat;
    private Double final_lng;
    private String nombre_lugar;
    private String estado;
    private String iniciada_en;
    private String finalizada_en;
    private Integer ruta_id;
    private int id_usuario;
    private String created_at;
    private String updated_at;
    public ActividadesEntity(String titulo, String tipo_deporte, int dificultad,
                             String privacidad, String estado, int id_usuario) {
        this.titulo = titulo;
        this.tipo_deporte = tipo_deporte;
        this.dificultad = dificultad;
        this.privacidad = privacidad;
        this.estado = estado;
        this.id_usuario = id_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo_deporte() {
        return tipo_deporte;
    }

    public void setTipo_deporte(String tipo_deporte) {
        this.tipo_deporte = tipo_deporte;
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

    public Double getDistancia_km() {
        return distancia_km;
    }

    public void setDistancia_km(Double distancia_km) {
        this.distancia_km = distancia_km;
    }

    public Double getDuracion_seg() {
        return duracion_seg;
    }

    public void setDuracion_seg(Double duracion_seg) {
        this.duracion_seg = duracion_seg;
    }

    public Double getDuracion_pausa_segundos() {
        return duracion_pausa_segundos;
    }

    public void setDuracion_pausa_segundos(Double duracion_pausa_segundos) {
        this.duracion_pausa_segundos = duracion_pausa_segundos;
    }

    public Double getCalorias() {
        return calorias;
    }

    public void setCalorias(Double calorias) {
        this.calorias = calorias;
    }

    public Double getDesnivel_positivo_m() {
        return desnivel_positivo_m;
    }

    public void setDesnivel_positivo_m(Double desnivel_positivo_m) {
        this.desnivel_positivo_m = desnivel_positivo_m;
    }

    public Double getDesnivel_negativo_m() {
        return desnivel_negativo_m;
    }

    public void setDesnivel_negativo_m(Double desnivel_negativo_m) {
        this.desnivel_negativo_m = desnivel_negativo_m;
    }

    public Double getRitmo_promedio() {
        return ritmo_promedio;
    }

    public void setRitmo_promedio(Double ritmo_promedio) {
        this.ritmo_promedio = ritmo_promedio;
    }

    public Double getRitmo_maximo() {
        return ritmo_maximo;
    }

    public void setRitmo_maximo(Double ritmo_maximo) {
        this.ritmo_maximo = ritmo_maximo;
    }

    public Double getVelocidad_promedio_kmh() {
        return velocidad_promedio_kmh;
    }

    public void setVelocidad_promedio_kmh(Double velocidad_promedio_kmh) {
        this.velocidad_promedio_kmh = velocidad_promedio_kmh;
    }

    public Double getVelocidad_maxima_kmh() {
        return velocidad_maxima_kmh;
    }

    public void setVelocidad_maxima_kmh(Double velocidad_maxima_kmh) {
        this.velocidad_maxima_kmh = velocidad_maxima_kmh;
    }

    public Double getInicio_lat() {
        return inicio_lat;
    }

    public void setInicio_lat(Double inicio_lat) {
        this.inicio_lat = inicio_lat;
    }

    public Double getInicio_lng() {
        return inicio_lng;
    }

    public void setInicio_lng(Double inicio_lng) {
        this.inicio_lng = inicio_lng;
    }

    public Double getFinal_lat() {
        return final_lat;
    }

    public void setFinal_lat(Double final_lat) {
        this.final_lat = final_lat;
    }

    public Double getFinal_lng() {
        return final_lng;
    }

    public void setFinal_lng(Double final_lng) {
        this.final_lng = final_lng;
    }

    public String getNombre_lugar() {
        return nombre_lugar;
    }

    public void setNombre_lugar(String nombre_lugar) {
        this.nombre_lugar = nombre_lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIniciada_en() {
        return iniciada_en;
    }

    public void setIniciada_en(String iniciada_en) {
        this.iniciada_en = iniciada_en;
    }

    public String getFinalizada_en() {
        return finalizada_en;
    }

    public void setFinalizada_en(String finalizada_en) {
        this.finalizada_en = finalizada_en;
    }

    public Integer getRuta_id() {
        return ruta_id;
    }

    public void setRuta_id(Integer ruta_id) {
        this.ruta_id = ruta_id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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
