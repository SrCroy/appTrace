package com.example.apptrace.models;

import com.google.gson.annotations.SerializedName;

public class ToggleReaccionResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("reaccionado")
    private boolean reaccionado;
    @SerializedName("total_reacciones")
    private int totalReacciones;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public boolean isReaccionado() { return reaccionado; }
    public int getTotalReacciones() { return totalReacciones; }
}
