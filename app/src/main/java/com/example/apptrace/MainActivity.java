package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptrace.session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabTrack;
    private ImageView ivNotifications, ivSearch;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = SessionManager.getInstance(this);

        ivNotifications = findViewById(R.id.iv_notifications);
        ivSearch        = findViewById(R.id.iv_search);
        fabTrack        = findViewById(R.id.fab_track_activity);

        LinearLayout llBottomNav = findViewById(R.id.ll_bottom_nav);

        // Tab perfil — long press → logout
        llBottomNav.getChildAt(4).setOnLongClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        });

        // Tab perfil → PerfilActivity
        llBottomNav.getChildAt(4).setOnClickListener(v ->
                startActivity(new Intent(this, PerfilActivity.class)));

        // Tab rutas → RutasActivity
        llBottomNav.getChildAt(1).setOnClickListener(v ->
                startActivity(new Intent(this, RutasActivity.class)));

        // Tab grupos
        llBottomNav.getChildAt(3).setOnClickListener(v ->
                Toast.makeText(this, "Grupos — próximamente", Toast.LENGTH_SHORT).show());

        ivNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Notificaciones — próximamente", Toast.LENGTH_SHORT).show());

        ivSearch.setOnClickListener(v ->
                Toast.makeText(this, "Búsqueda — próximamente", Toast.LENGTH_SHORT).show());

        // FAB → historial de actividades
        fabTrack.setOnClickListener(v ->
                startActivity(new Intent(this, ActividadesActivity.class)));
    }
}
