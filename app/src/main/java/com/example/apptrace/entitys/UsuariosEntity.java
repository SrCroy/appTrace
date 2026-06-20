package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UsuariosEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private String email_verified_at;
    private String password;
    private String avatar;
    private String rol;
    private boolean esta_baneado;
    private String estado;
    private String biografia;
    private Double peso_kg;
    private Double altura_cm;
    private String fecha_nacimiento;
    private String created_at;
    private String updated_at;

    public UsuariosEntity(String username, String nombre, String apellido, String email,
                          String password, String avatar, String rol, boolean esta_baneado,
                          String estado) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.rol = rol;
        this.esta_baneado = esta_baneado;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEsta_baneado() {
        return esta_baneado;
    }

    public void setEsta_baneado(boolean esta_baneado) {
        this.esta_baneado = esta_baneado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Double getPeso_kg() {
        return peso_kg;
    }

    public void setPeso_kg(Double peso_kg) {
        this.peso_kg = peso_kg;
    }

    public Double getAltura_cm() {
        return altura_cm;
    }

    public void setAltura_cm(Double altura_cm) {
        this.altura_cm = altura_cm;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
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
