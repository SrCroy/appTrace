package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Publicacion {
    @SerializedName("id")
    private int id;

    @SerializedName("contenido")
    private String contenido;

    @SerializedName("privacidad")
    private String privacidad;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("total_reacciones")
    private int totalReacciones;

    @SerializedName("total_comentarios")
    private int totalComentarios;

    @SerializedName("ya_reacciono")
    private boolean yaReacciono;

    @SerializedName("usuario")
    private UsuarioAnidado usuario;

    @SerializedName("actividad")
    private ActividadAnidada actividad;

    @SerializedName("ruta")
    private RutaAnidada ruta;

    @SerializedName("comentarios")
    private List<Comentario> comentarios;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getPrivacidad() { return privacidad; }
    public void setPrivacidad(String privacidad) { this.privacidad = privacidad; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getTotalReacciones() { return totalReacciones; }
    public void setTotalReacciones(int totalReacciones) { this.totalReacciones = totalReacciones; }

    public int getTotalComentarios() { return totalComentarios; }
    public void setTotalComentarios(int totalComentarios) { this.totalComentarios = totalComentarios; }

    public boolean isYaReacciono() { return yaReacciono; }
    public void setYaReacciono(boolean yaReacciono) { this.yaReacciono = yaReacciono; }

    public UsuarioAnidado getUsuario() { return usuario; }
    public void setUsuario(UsuarioAnidado usuario) { this.usuario = usuario; }

    public ActividadAnidada getActividad() { return actividad; }
    public void setActividad(ActividadAnidada actividad) { this.actividad = actividad; }

    public RutaAnidada getRuta() { return ruta; }
    public void setRuta(RutaAnidada ruta) { this.ruta = ruta; }

    public List<Comentario> getComentarios() { return comentarios; }
    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }

    public static class UsuarioAnidado {
        @SerializedName("id")
        private int id;
        @SerializedName("username")
        private String username;
        @SerializedName("nombre")
        private String nombre;
        @SerializedName("apellido")
        private String apellido;
        @SerializedName("avatar")
        private String avatar;

        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getAvatar() { return avatar; }
    }

    public static class ActividadAnidada {
        @SerializedName("id")
        private int id;
        @SerializedName("titulo")
        private String titulo;
        @SerializedName("tipo_deporte")
        private String tipoDeporte;
        @SerializedName("distancia_km")
        private double distanciaKm;
        @SerializedName("duracion_seg")
        private double duracionSeg;
        @SerializedName("calorias")
        private double calorias;

        public int getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getTipoDeporte() { return tipoDeporte; }
        public double getDistanciaKm() { return distanciaKm; }
        public double getDuracionSeg() { return duracionSeg; }
        public double getCalorias() { return calorias; }
    }

    public static class RutaAnidada {
        @SerializedName("id")
        private int id;
        @SerializedName("nombre")
        private String nombre;
        @SerializedName("distancia_km")
        private double distanciaKm;
        @SerializedName("dificultad")
        private String dificultad;

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public double getDistanciaKm() { return distanciaKm; }
        public String getDificultad() { return dificultad; }
    }
}
