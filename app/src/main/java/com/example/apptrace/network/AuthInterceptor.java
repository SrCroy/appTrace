package com.example.apptrace.network;

import com.example.apptrace.AppTraceApplication;
import com.example.apptrace.session.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        SessionManager sessionManager = SessionManager.getInstance(
                AppTraceApplication.getAppContext());
        String token = sessionManager.getToken();

        if (token == null) {
            return chain.proceed(original);
        }

        Request authorized = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(authorized);
    }
}