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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import food.codi.publico.AppController;
import food.codi.publico.Funciones;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class DescripcionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static String id_producto = "", nombre_producto = "", precio_producto = "", nombre_proveedor = "", imagen_producto = "",
            id_pedido;
    private String nombre_nav = "";
    public TextView tvIdProducto, tvNombreProducto, tvDescripcionProducto, tvNombreProveedor, tvNombreNav;
    public NetworkImageView nivImagenProducto;
    public Button btnEnviarCarrito;
    private ImageLoader imgLoader = AppController.getInstance().getImageLoader();
    PrefUtil prefUtil;
    ImageView ivMenu;
    LinearLayout ivCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        tvIdProducto = (TextView) findViewById(R.id.tvIdProducto);
        tvNombreProducto = (TextView) findViewById(R.id.tvNombreProducto);
        tvDescripcionProducto = (TextView) findViewById(R.id.tvDescripcionProducto);
        tvNombreProveedor = (TextView) findViewById(R.id.tvNombreTienda);
        btnEnviarCarrito = (Button) findViewById(R.id.btnEnviarCarrito);
        nivImagenProducto = (NetworkImageView) findViewById(R.id.nivImagenProducto);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
        tvNombreNav = (TextView) nav.getHeaderView(0).findViewById(R.id.tvNombreNav);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivCerrar = (LinearLayout) findViewById(R.id.ivCerrar);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        prefUtil = new PrefUtil(this);
        id_producto = getIntent().getStringExtra("id_producto");
        nombre_producto = getIntent().getStringExtra("nombre_producto");
        precio_producto = getIntent().getStringExtra("precio_producto");
        nombre_proveedor = getIntent().getStringExtra("nombre_proveedor");
        imagen_producto = getIntent().getStringExtra("imagen_producto");
        tvIdProducto.setText(id_producto);
        tvNombreProducto.setText(nombre_producto);
        btnEnviarCarrito.setText("Agregar a carrito (S/ " + precio_producto + ")");
        tvNombreProveedor.setText("Tienda: " + nombre_proveedor);
        nivImagenProducto.setImageUrl(imagen_producto, imgLoader);
        cargarDescripcion();
        btnEnviarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtener_id_pedido(tvIdProducto.getText().toString());
            }
        });
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
        ivCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefUtil.clearAll();
                Intent intent = new Intent(DescripcionActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void cargarDescripcion() {
        Log.i("cargarDescripcion", "DescripcionActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_descripcion.php?id_producto="
                        + id_producto);
                Log.i("cargarDescripcion", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    tvDescripcionProducto.setText("Descripción del producto\n\n\n" + jsonArray.getJSONObject(0)
                                            .getString("descripcion"));
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

    public void iniciarPedido(final String id_producto) {
        Log.i("iniciarPedido", "DescripcionActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/iniciar_pedido.php?dni_cliente="
                        + prefUtil.getStringValue("dni_cliente"));
                Log.i("iniciarPedido", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            prefUtil.saveGenericValue("carrito", "OCUPADO");
                            obtener_id_pedido(id_producto);
                        }
                    }
                });
            }
        };
        tr.start();
    }

    public void obtener_id_pedido(final String id_producto) {
        Log.i("obtener_id_pedido", "DescripcionActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_id_pedido.php?dni_cliente="
                        + prefUtil.getStringValue("dni_cliente"));
                Log.i("obtener_id_pedido", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    id_pedido = jsonArray.getJSONObject(0).getString("id_pedido");
                                    prefUtil.saveGenericValue("id_pedido", id_pedido);
                                    agregarProducto(id_producto, id_pedido);
                                } else {
                                    iniciarPedido(id_producto);
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

    public void agregarProducto(final String id_producto, final String id_pedido) {
        Log.i("agregarProducto", "DescripcionActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/agregar_producto.php?id_producto="
                        + id_producto + "&id_pedido=" + id_pedido);
                Log.i("agregarProducto", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            Toast.makeText(DescripcionActivity.this, "Producto agregado a carrito",
                                    Toast.LENGTH_SHORT).show();
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
                intent = new Intent(DescripcionActivity.this, CategoriaActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ivCerrar:
                prefUtil.clearAll();
                intent = new Intent(DescripcionActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navNotificaciones:
                intent = new Intent(DescripcionActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPedidos:
                intent = new Intent(DescripcionActivity.this, PedidosActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPerfil:
                intent = new Intent(DescripcionActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPreguntas:
                intent = new Intent(DescripcionActivity.this, PreguntasActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPromociones:
                intent = new Intent(DescripcionActivity.this, PromocionesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
