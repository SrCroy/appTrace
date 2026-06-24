package com.example.apptrace.model.profile;

import java.io.Serializable;

public class ProfileData implements Serializable {
    private int id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private String avatar;
    private String biografia;
    private Double peso_kg;
    private Double altura_cm;
    private String fecha_nacimiento;
    private String rol;
    private String estado;
    private String creado_en;

    public int getId()                  { return id; }
    public String getUsername()         { return username; }
    public String getNombre()           { return nombre; }
    public String getApellido()         { return apellido; }
    public String getEmail()            { return email; }
    public String getAvatar()           { return avatar; }
    public String getBiografia()        { return biografia; }
    public Double getPesoKg()           { return peso_kg; }
    public Double getAlturaCm()         { return altura_cm; }
    public String getFechaNacimiento()  { return fecha_nacimiento; }
    public String getRol()              { return rol; }
    public String getEstado()           { return estado; }
    public String getCreadoEn()         { return creado_en; }
}
