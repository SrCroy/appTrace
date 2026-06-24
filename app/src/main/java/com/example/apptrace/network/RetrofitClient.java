package com.example.apptrace.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL    = "https://apiresttrace-production.up.railway.app/api/";
    private static final String STORAGE_URL = BASE_URL.replace("/api/", "/storage/");

    /** Convierte rutas del servidor a URL completa. Si ya es URL absoluta la devuelve tal cual. */
    public static String storageUrl(String path) {
        if (path == null || path.isEmpty()) return null;
        if (path.startsWith("http://")) return path.replace("http://", "https://");
        if (path.startsWith("https://")) return path;
        return STORAGE_URL + path;
    }

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new AuthInterceptor())
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
