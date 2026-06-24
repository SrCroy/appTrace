package com.example.apptrace.model.auth;

public class RegisterRequest {
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private String password;

    public RegisterRequest(String username, String nombre, String apellido,
                           String email, String password) {
        this.username = username;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.email    = email;
        this.password = password;
    }
}