package com.example.apptrace.model.profile;

public class ChangePasswordData {
    private String password_actual;
    private String password_nueva;
    private String password_nueva_confirmation;

    public ChangePasswordData(String actual, String nueva) {
        this.password_actual              = actual;
        this.password_nueva               = nueva;
        this.password_nueva_confirmation  = nueva;
    }
}
