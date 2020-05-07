package food.vespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import java.util.ArrayList;
import food.vespro.adapter.ProductoAdapter;
import food.vespro.entity.Producto;
import food.vespro.publico.Funciones;

/**
 * By: El Bryant
 */

public class ProductoActivity extends AppCompatActivity {
    public static String id_categoria;
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
        id_categoria = getIntent().getStringExtra("id_categoria");
        cargarProductos();
        tvMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(ProductoActivity.this, "Producto agregado al carrito", Toast.LENGTH_LONG).show();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void cargarProductos() {
        Log.i("cargarProductos", "ProductoActivity");
        productos = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_productos.php?id_categoria="
                        + id_categoria);
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
