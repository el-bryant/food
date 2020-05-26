package food.codi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.json.JSONArray;
import food.codi.publico.AppController;
import food.codi.publico.Funciones;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class DescripcionActivity extends AppCompatActivity {
    public static String id_producto = "", nombre_producto = "", precio_producto = "", nombre_proveedor = "", imagen_producto = "",
            id_pedido;
    public TextView tvIdProducto, tvNombreProducto, tvDescripcionProducto, tvNombreProveedor;
    public NetworkImageView nivImagenProducto;
    public Button btnEnviarCarrito;
    private ImageLoader imgLoader = AppController.getInstance().getImageLoader();
    PrefUtil prefUtil;
    public static int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);
        tvIdProducto = (TextView) findViewById(R.id.tvIdProducto);
        tvNombreProducto = (TextView) findViewById(R.id.tvNombreProducto);
        tvDescripcionProducto = (TextView) findViewById(R.id.tvDescripcionProducto);
        tvNombreProveedor = (TextView) findViewById(R.id.tvNombreTienda);
        btnEnviarCarrito = (Button) findViewById(R.id.btnEnviarCarrito);
        nivImagenProducto = (NetworkImageView) findViewById(R.id.nivImagenProducto);
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
}
