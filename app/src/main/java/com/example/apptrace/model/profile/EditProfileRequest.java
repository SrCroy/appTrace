package com.example.apptrace.model.profile;

public class EditProfileRequest {
    private String nombre;
    private String apellido;
    private String biografia;
    private Double peso_kg;
    private Double altura_cm;
    private String fecha_nacimiento;

    public EditProfileRequest(String nombre, String apellido, String biografia,
                              Double peso_kg, Double altura_cm, String fecha_nacimiento) {
        this.nombre           = nombre;
        this.apellido         = apellido;
        this.biografia        = biografia;
        this.peso_kg          = peso_kg;
        this.altura_cm        = altura_cm;
        this.fecha_nacimiento = fecha_nacimiento;
    }
}
