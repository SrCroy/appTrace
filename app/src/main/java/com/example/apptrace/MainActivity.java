package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.apptrace.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llFeed, llRoutes, llGroups, llProfile;
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

        llFeed          = findViewById(R.id.ll_bottom_nav).findViewWithTag(null);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabTrack = findViewById(R.id.fab_track_activity);
        ivNotifications = findViewById(R.id.iv_notifications);
        ivSearch = findViewById(R.id.iv_search);


        // Logout al mantener presionado el ícono de perfil
        LinearLayout llBottomNav = findViewById(R.id.ll_bottom_nav);
        if (bottomNavigationView.getMenu().findItem(R.id.menu_placeholder) != null) {
            bottomNavigationView.getMenu().findItem(R.id.menu_placeholder).setEnabled(false);
        }

        // Tab perfil es el 4to hijo del bottom nav (índice 3, saltando el View vacío del FAB)
        llBottomNav.getChildAt(4).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sessionManager.logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_feed) {
                cargarFragmento(new FeedFragment());
                return true;
            } else if (itemId == R.id.menu_routes) {

                cargarFragmento(new RutasFragment());
                return true;
            } else if (itemId == R.id.menu_groups) {

                Toast.makeText(this, "Grupos — próximamente", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_profile) {
                cargarFragmento(new PerfilFragment());
                return true;
            }
            return false;
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
        fabTrack.setOnClickListener(v ->
                startActivity(new Intent(this, ActividadesActivity.class)));

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

        if (savedInstanceState == null) {

            bottomNavigationView.setSelectedItemId(R.id.menu_feed);
        }




        private void cargarFragmento(Fragment fragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}


