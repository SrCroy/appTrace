package com.example.apptrace.network;

import com.example.apptrace.model.auth.ApiResponse;
import com.example.apptrace.model.auth.LoginData;
import com.example.apptrace.model.auth.LoginRequest;
import com.example.apptrace.model.auth.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<ApiResponse<LoginData>> login(@Body LoginRequest request);
    @POST("register")
    Call<ApiResponse<Object>> register(@Body RegisterRequest request);
}