package food.vespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import java.util.ArrayList;
import food.vespro.adapter.TiendaAdapter;
import food.vespro.entity.Tienda;
import food.vespro.publico.Funciones;

public class TiendaActivity extends AppCompatActivity {
    public static String id_categoria;
    private ArrayList<Tienda> tiendas;
    private RecyclerView rvTienda;
    private TiendaAdapter tiendaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);
        FloatingActionButton fab = findViewById(R.id.fab);
        rvTienda = (RecyclerView) findViewById(R.id.rvTienda);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TiendaActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });
        rvTienda.setHasFixedSize(true);
        rvTienda.setLayoutManager(new LinearLayoutManager(this));
        id_categoria = getIntent().getStringExtra("id_categoria");
        cargarDatos();
    }

    public void cargarDatos() {
        Log.i("cargarDatos", "TiendaActivity");
        tiendas = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_tiendas.php?id_categoria="
                        + id_categoria);
                Log.i("cargarDatos", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    tiendas.add(new Tienda(jsonArray.getJSONObject(i).getLong("id_proveedor"),
                                            jsonArray.getJSONObject(i).getString("proveedor"),
                                            jsonArray.getJSONObject(i).getString("imagen")));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        tiendaAdapter = new TiendaAdapter(TiendaActivity.this, tiendas);
                        rvTienda.setAdapter(tiendaAdapter);
                    }
                });
            }
        };
        tr.start();
    }
}
