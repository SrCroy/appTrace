package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.apptrace.FeedFragment;
import com.example.apptrace.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabTrack;
    private ImageView ivNotifications, ivSearch;
    private BottomNavigationView bottomNavigationView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = SessionManager.getInstance(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabTrack = findViewById(R.id.fab_track_activity);
        ivNotifications = findViewById(R.id.iv_notifications);
        ivSearch = findViewById(R.id.iv_search);

        // --- Navegación ---
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_feed) {
                cargarFragmento(new FeedFragment());
                return true;
            } else if (itemId == R.id.menu_profile) {
                // Si quieres abrir una Actividad, hazlo aquí:
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                return true;
            }
            return false;
        });

        // --- FAB ---
        fabTrack.setOnClickListener(v ->
                startActivity(new Intent(this, ActividadesActivity.class)));

        // --- Carga inicial ---
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.menu_feed);
        }
    }

    private void cargarFragmento(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}