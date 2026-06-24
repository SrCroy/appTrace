package com.example.apptrace.model.auth;

public class UserDto {
    private int id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private String avatar;
    private String rol;

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getAvatar() { return avatar; }
    public String getRol() { return rol; }
}