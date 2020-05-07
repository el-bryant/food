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
import food.vespro.adapter.CarritoAdapter;
import food.vespro.entity.Carrito;
import food.vespro.publico.Funciones;
import food.vespro.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class CarritoActivity extends AppCompatActivity {
    public static RecyclerView rvCarrito;
    PrefUtil prefUtil;
    ArrayList<Carrito> carrito;
    public static CarritoAdapter carritoAdapter;
    public static TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        rvCarrito = (RecyclerView) findViewById(R.id.rvCarrito);
        tvMensaje = (TextView) findViewById(R.id.tvMensaje);
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
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    carrito.add(new Carrito(jsonArray.getJSONObject(i).getString("id_detalle_pedido"),
                                            jsonArray.getJSONObject(i).getString("id_producto"),
                                            jsonArray.getJSONObject(i).getString("nombre"),
                                            jsonArray.getJSONObject(i).getDouble("precio"),
                                            jsonArray.getJSONObject(i).getString("proveedor"),
                                            jsonArray.getJSONObject(i).getString("imagen")));
                                }
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
}
