package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
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

        // 1. Enlazar las vistas correctas desde el XML
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabTrack = findViewById(R.id.fab_track_activity);
        ivNotifications = findViewById(R.id.iv_notifications);
        ivSearch = findViewById(R.id.iv_search);

        // Opcional: Asegurarnos por código de que el hueco central no haga nada si lo tocan por error
        if (bottomNavigationView.getMenu().findItem(R.id.menu_placeholder) != null) {
            bottomNavigationView.getMenu().findItem(R.id.menu_placeholder).setEnabled(false);
        }

        // 2. Configurar la navegación profesional (Esto reemplaza todos tus OnClickListeners manuales)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_feed) {
                cargarFragmento(new FeedFragment()); // Asegúrate de tener este Fragment creado
                return true;
            } else if (itemId == R.id.menu_routes) {
                // cargarFragmento(new RutasFragment()); // Descomenta cuando lo crees
                Toast.makeText(this, "Rutas — próximamente", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_groups) {
                // cargarFragmento(new GruposFragment()); // Descomenta cuando lo crees
                Toast.makeText(this, "Grupos — próximamente", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.menu_profile) {
                cargarFragmento(new PerfilFragment());
                return true;
            }
            return false;
        });

        // 3. Configurar clics independientes
        fabTrack.setOnClickListener(v ->
                startActivity(new Intent(this, ActividadesActivity.class)));

        ivNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Notificaciones — próximamente", Toast.LENGTH_SHORT).show());

        ivSearch.setOnClickListener(v ->
                Toast.makeText(this, "Búsqueda — próximamente", Toast.LENGTH_SHORT).show());

        // 4. Estado inicial por defecto al abrir la app
        if (savedInstanceState == null) {
            // Esto marca el icono como seleccionado Y carga el fragmento automáticamente
            bottomNavigationView.setSelectedItemId(R.id.menu_feed);
        }
    }

    // ─── Método central para cambiar pantallas ────────────────────────────────

    private void cargarFragmento(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}