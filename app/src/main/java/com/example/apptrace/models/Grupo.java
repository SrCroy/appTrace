package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

public class Grupo {
    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("privacidad")
    private String privacidad;

    @SerializedName("total_miembros")
    private int totalMiembros;

    @SerializedName("es_miembro")
    private boolean esMiembro;

    @SerializedName("propietario")
    private PropietarioAnidado propietario;

    @SerializedName("created_at")
    private String createdAt;

    public Grupo() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getPrivacidad() { return privacidad; }
    public void setPrivacidad(String privacidad) { this.privacidad = privacidad; }

    public int getTotalMiembros() { return totalMiembros; }
    public void setTotalMiembros(int totalMiembros) { this.totalMiembros = totalMiembros; }

    public boolean isEsMiembro() { return esMiembro; }
    public void setEsMiembro(boolean esMiembro) { this.esMiembro = esMiembro; }

    public PropietarioAnidado getPropietario() { return propietario; }
    public void setPropietario(PropietarioAnidado propietario) { this.propietario = propietario; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public static class PropietarioAnidado {
        @SerializedName("id")
        private int id;
        @SerializedName("username")
        private String username;
        @SerializedName("avatar")
        private String avatar;

        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getAvatar() { return avatar; }
    }
}
