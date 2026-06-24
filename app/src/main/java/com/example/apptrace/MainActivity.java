package com.example.apptrace;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
    private LinearLayout tabFeed, tabRutas, tabGrupos, tabPerfil;

    private FloatingActionButton fabTrack;
    private ImageView ivNotifications, ivSearch;
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

        tabFeed = findViewById(R.id.tab_feed);
        tabRutas = findViewById(R.id.tab_rutas);
        tabGrupos = findViewById(R.id.tab_grupos);
        tabPerfil = findViewById(R.id.tab_perfil);

        tabFeed.setOnClickListener(v -> {
            cargarFragmento(new FeedFragment());
            actualizarColoresMenu(tabFeed);
        });
        tabRutas.setOnClickListener(v -> {
            cargarFragmento(new RutasFragment());
            actualizarColoresMenu(tabRutas);
        });
        tabGrupos.setOnClickListener(v -> {
            cargarFragmento(new GruposFragment());
            actualizarColoresMenu(tabGrupos);
        });
        tabPerfil.setOnClickListener(v -> {
            cargarFragmento(new PerfilFragment());
            actualizarColoresMenu(tabPerfil);
        });

        // Estado inicial por defecto
        if (savedInstanceState == null) {
            cargarFragmento(new FeedFragment());
            actualizarColoresMenu(tabFeed);
        }
    }

    private void cargarFragmento(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Lógica visual para encender el tab tocado y apagar los demás
    private void actualizarColoresMenu(LinearLayout tabSeleccionado) {
        restaurarColor(tabFeed);
        restaurarColor(tabRutas);
        restaurarColor(tabGrupos);
        restaurarColor(tabPerfil);

        ImageView icono = (ImageView) tabSeleccionado.getChildAt(0);
        TextView texto = (TextView) tabSeleccionado.getChildAt(1);

        icono.setColorFilter(ContextCompat.getColor(this, R.color.primary));
        texto.setTextColor(ContextCompat.getColor(this, R.color.primary));
    }

    // Lógica visual para poner un tab en estado inactivo
    private void restaurarColor(LinearLayout tab) {
        ImageView icono = (ImageView) tab.getChildAt(0);
        TextView texto = (TextView) tab.getChildAt(1);

        icono.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
        texto.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));

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
