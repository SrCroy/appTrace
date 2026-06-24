package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

public class Comentario {
    @SerializedName("id")
    private int id;

    @SerializedName("cuerpo")
    private String cuerpo;

    @SerializedName("padre_id")
    private Integer padreId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("usuario")
    private UsuarioAnidado usuario;

    public Comentario() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }

    public Integer getPadreId() { return padreId; }
    public void setPadreId(Integer padreId) { this.padreId = padreId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public UsuarioAnidado getUsuario() { return usuario; }
    public void setUsuario(UsuarioAnidado usuario) { this.usuario = usuario; }

    public static class UsuarioAnidado {
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
