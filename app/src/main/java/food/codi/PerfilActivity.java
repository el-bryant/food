package food.codi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import food.codi.publico.Funciones;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class PerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    PrefUtil prefUtil;
    ImageView ivMenu;
    LinearLayout ivCerrar;
    TextView tvDniCliente, tvNombreCliente, tvApellidoCliente, tvCelularCliente, tvCorreoCliente, tvNombreNav;
    NavigationView navView;
    private String nombre_nav = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        tvDniCliente = (TextView) findViewById(R.id.tvDniCliente);
        tvNombreCliente = (TextView) findViewById(R.id.tvNombreCliente);
        tvApellidoCliente = (TextView) findViewById(R.id.tvApellidoCliente);
        tvCelularCliente = (TextView) findViewById(R.id.tvCelularCliente);
        tvCorreoCliente = (TextView) findViewById(R.id.tvCorreoCliente);
        navView = (NavigationView) findViewById(R.id.nav_view);
        tvNombreNav = (TextView) navView.getHeaderView(0).findViewById(R.id.tvNombreNav);
        ivCerrar = (LinearLayout) findViewById(R.id.ivCerrar);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        prefUtil = new PrefUtil(this);
        char[] caracteres_nav = (prefUtil.getStringValue("nombre").substring(0,
                prefUtil.getStringValue("nombre").indexOf(" ")).toLowerCase()).toCharArray();
        caracteres_nav[0] = Character.toUpperCase(caracteres_nav[0]);
        for (int i = 0; i < prefUtil.getStringValue("nombre").substring(0,
                prefUtil.getStringValue("nombre").indexOf(" ")).length(); i ++) {
            if (caracteres_nav[i] == ' ') {
                caracteres_nav[i + 1] = Character.toUpperCase(caracteres_nav[i + 1]);
            }
            nombre_nav = nombre_nav + caracteres_nav[i];
        }
        tvNombreNav.setText("¡Hola, " + nombre_nav + "!");
        cargarDatos();
        ivCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefUtil.clearAll();
                Intent intent = new Intent(PerfilActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void cargarDatos() {
        Log.i("cargarDatos", "PerfilActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/acceder.php?dni_cliente=" +
                        prefUtil.getStringValue("dni_cliente"));
                Log.i("cargarDatos", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    tvDniCliente.setText(jsonArray.getJSONObject(0).getString("dni_cliente"));
                                    char[] caracteres_nombres = (jsonArray.getJSONObject(0).getString("nombres")
                                            .toLowerCase()).toCharArray();
                                    caracteres_nombres[0] = Character.toUpperCase(caracteres_nombres[0]);
                                    for (int i = 0; i < jsonArray.getJSONObject(0).getString("nombres")
                                            .toLowerCase().length(); i ++) {
                                        if (caracteres_nombres[i] == ' ') {
                                            caracteres_nombres[i + 1] = Character.toUpperCase(caracteres_nombres[i + 1]);
                                        }
                                        tvNombreCliente.setText("" + tvNombreCliente.getText() + caracteres_nombres[i]);
                                    }
                                    char[] caracteres_apellidos = (jsonArray.getJSONObject(0).getString("apellidos")
                                            .toLowerCase()).toCharArray();
                                    caracteres_apellidos[0] = Character.toUpperCase(caracteres_apellidos[0]);
                                    for (int i = 0; i < jsonArray.getJSONObject(0).getString("apellidos")
                                            .toLowerCase().length(); i ++) {
                                        if (caracteres_apellidos[i] == ' ') {
                                            caracteres_apellidos[i + 1] = Character.toUpperCase(caracteres_apellidos[i + 1]);
                                        }
                                        tvApellidoCliente.setText("" + tvApellidoCliente.getText() + caracteres_apellidos[i]);
                                    }
                                    tvCelularCliente.setText(jsonArray.getJSONObject(0).getString("telefono"));
                                    tvCorreoCliente.setText(jsonArray.getJSONObject(0).getString("correo"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
        tr.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent;
        switch (id) {
            case R.id.navCategorias:
                intent = new Intent(PerfilActivity.this, CategoriaActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ivCerrar:
                prefUtil.clearAll();
                intent = new Intent(PerfilActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navNotificaciones:
                intent = new Intent(PerfilActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPedidos:
                intent = new Intent(PerfilActivity.this, PedidosActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPerfil:
                intent = new Intent(PerfilActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPreguntas:
                intent = new Intent(PerfilActivity.this, PreguntasActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPromociones:
                intent = new Intent(PerfilActivity.this, PromocionesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
