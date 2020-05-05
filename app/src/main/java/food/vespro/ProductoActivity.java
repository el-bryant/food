package food.vespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import food.vespro.adapter.ProductoAdapter;
import food.vespro.entity.Producto;

/**
 * By: El Bryant
 */

public class ProductoActivity extends AppCompatActivity {
    public static String id_categoria;
    private RecyclerView rvProducto;
    private ArrayList<Producto> productos;
    private ProductoAdapter productoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        rvProducto = (RecyclerView) findViewById(R.id.rvProducto);
        rvProducto.setHasFixedSize(true);
        rvProducto.setLayoutManager(new LinearLayoutManager(this));
        id_categoria = getIntent().getStringExtra("id_categoria");
        Log.i("id_categoria", id_categoria);
        cargarProductos();
    }

    public void cargarProductos() {
        Log.i("cargarProductos", "ProductoActivity");
        productos = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = primero("https://vespro.io/food/wsApp/obtener_productos.php?id_categoria="
                        + id_categoria);
                Log.i("cargarProductos", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
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

    public String primero(String direccion) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();
            result = new StringBuilder();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((linea = reader.readLine()) != null) {
                    result.append(linea);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public int segundo(String response) {
        int res = 0;
        if (response != null) {
            res = 1;
        }
        return res;
    }
}
