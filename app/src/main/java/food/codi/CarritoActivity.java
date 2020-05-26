package food.codi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import java.util.ArrayList;
import food.codi.adapter.CarritoAdapter;
import food.codi.entity.Carrito;
import food.codi.publico.Funciones;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class CarritoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static RecyclerView rvCarrito;
    PrefUtil prefUtil;
    ArrayList<Carrito> carrito;
    public static CarritoAdapter carritoAdapter;
    public static TextView tvMensaje;
    public Button btnRealizarPedido;
    public static Double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        rvCarrito = (RecyclerView) findViewById(R.id.rvCarrito);
        tvMensaje = (TextView) findViewById(R.id.tvMensaje);
        btnRealizarPedido = (Button) findViewById(R.id.btnRealizarPedido);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
        prefUtil = new PrefUtil(this);
        rvCarrito.setHasFixedSize(true);
        rvCarrito.setLayoutManager(new LinearLayoutManager(this));
        tvMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                cargarCarrito();
                Toast.makeText(CarritoActivity.this, "Producto eliminado del carrito satisfactoriamente.",
                        Toast.LENGTH_LONG).show();
            }
        });
        cargarCarrito();
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoActivity.this, DetalleActivity.class);
                intent.putExtra("id_pedido", prefUtil.getStringValue("id_pedido"));
                intent.putExtra("total", String.format("%.2f", total));
                startActivity(intent);
                finish();
            }
        });
    }

    public void cargarCarrito() {
        Log.i("cargarCarrito", "CarritoActivity");
        carrito = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_carrito.php?dni_cliente="
                        + prefUtil.getStringValue("dni_cliente"));
                Log.i("cargarCarrito", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                total = 0.0;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    carrito.add(new Carrito(jsonArray.getJSONObject(i).getString("id_detalle_pedido"),
                                            jsonArray.getJSONObject(i).getString("id_producto"),
                                            jsonArray.getJSONObject(i).getString("nombre"),
                                            jsonArray.getJSONObject(i).getDouble("precio"),
                                            jsonArray.getJSONObject(i).getString("proveedor"),
                                            jsonArray.getJSONObject(i).getString("imagen")));
                                    prefUtil.saveGenericValue("id_pedido",
                                            jsonArray.getJSONObject(i).getString("id_pedido"));
                                    total += jsonArray.getJSONObject(i).getDouble("precio");
                                }
                                btnRealizarPedido.setText("Realizar pedido (S/ " + String.format("%.2f", total) + ")");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        carritoAdapter = new CarritoAdapter(CarritoActivity.this, carrito);
                        rvCarrito.setAdapter(carritoAdapter);
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
                intent = new Intent(CarritoActivity.this, CategoriaActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navCerrar:
                prefUtil.clearAll();
                intent = new Intent(CarritoActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navNotificaciones:
                intent = new Intent(CarritoActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPedidos:
                intent = new Intent(CarritoActivity.this, PedidosActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPerfil:
                intent = new Intent(CarritoActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPreguntas:
                intent = new Intent(CarritoActivity.this, PreguntasActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPromociones:
                intent = new Intent(CarritoActivity.this, PromocionesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
