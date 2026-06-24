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

    private LinearLayout llFeed, llRoutes, llGroups, llProfile;
    private FloatingActionButton fabTrack;
    private ImageView ivNotifications, ivSearch;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = SessionManager.getInstance(this);

        llFeed          = findViewById(R.id.ll_bottom_nav).findViewWithTag(null);
        ivNotifications = findViewById(R.id.iv_notifications);
        ivSearch        = findViewById(R.id.iv_search);
        fabTrack        = findViewById(R.id.fab_track_activity);

        // Logout al mantener presionado el ícono de perfil
        LinearLayout llBottomNav = findViewById(R.id.ll_bottom_nav);

        // Tab perfil es el 4to hijo del bottom nav (índice 3, saltando el View vacío del FAB)
        llBottomNav.getChildAt(4).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sessionManager.logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }
        });

        // Tab perfil click normal → abre PerfilActivity
        llBottomNav.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
            }
        });

        llBottomNav.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Rutas — próximamente", Toast.LENGTH_SHORT).show();
            }
        });

        llBottomNav.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Grupos — próximamente", Toast.LENGTH_SHORT).show();
            }
        });

        ivNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Notificaciones — próximamente", Toast.LENGTH_SHORT).show();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Búsqueda — próximamente", Toast.LENGTH_SHORT).show();
            }
        });

        fabTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Tracking — próximamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}