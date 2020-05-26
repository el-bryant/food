package food.codi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import java.util.ArrayList;
import food.codi.adapter.ProductoAdapter;
import food.codi.entity.Producto;
import food.codi.publico.Funciones;

/**
 * By: El Bryant
 */

public class ProductoActivity extends AppCompatActivity {
    public static String idTienda;
    private RecyclerView rvProducto;
    private ArrayList<Producto> productos;
    private ProductoAdapter productoAdapter;
    public static TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        rvProducto = (RecyclerView) findViewById(R.id.rvProducto);
        tvMensaje = (TextView) findViewById(R.id.tvMensaje);
        rvProducto.setHasFixedSize(true);
        rvProducto.setLayoutManager(new LinearLayoutManager(this));
        idTienda = getIntent().getStringExtra("id_tienda");
        cargarProductos();
        tvMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(ProductoActivity.this, "Producto agregado al carrito", Toast.LENGTH_LONG).show();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductoActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void cargarProductos() {
        Log.i("cargarProductos", "ProductoActivity");
        productos = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_productos.php?id_tienda="
                        + idTienda);
                Log.i("cargarProductos", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    productos.add(new Producto(jsonArray.getJSONObject(i).getString("id_producto"),
                                            jsonArray.getJSONObject(i).getString("nombre"),
                                            jsonArray.getJSONObject(i).getDouble("precio"),
                                            jsonArray.getJSONObject(i).getString("proveedor"),
                                            jsonArray.getJSONObject(i).getString("imagen")));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        productoAdapter = new ProductoAdapter(ProductoActivity.this, productos);
                        rvProducto.setAdapter(productoAdapter);
                    }
                });
            }
        };
        tr.start();
    }
}
