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
import food.vespro.adapter.CategoriaAdapter;
import food.vespro.entity.Categoria;
import food.vespro.publico.Funciones;

public class CategoriaActivity extends AppCompatActivity {
    private ArrayList<Categoria> categorias;
    private RecyclerView rvCategoria;
    private CategoriaAdapter categoriaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        rvCategoria = (RecyclerView) findViewById(R.id.rvCategoria);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoriaActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });
        rvCategoria.setHasFixedSize(true);
        rvCategoria.setLayoutManager(new LinearLayoutManager(this));
        cargarDatos();
    }

    public void cargarDatos() {
        Log.i("cargarDatos", "CategoriaActivity");
        categorias = new ArrayList<>();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_categorias.php");
                Log.i("cargarDatos", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    categorias.add(new Categoria(jsonArray.getJSONObject(i).getLong("id_categoria"),
                                            jsonArray.getJSONObject(i).getString("nombre"),
                                            jsonArray.getJSONObject(i).getString("imagen")));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        categoriaAdapter = new CategoriaAdapter(CategoriaActivity.this, categorias);
                        rvCategoria.setAdapter(categoriaAdapter);
                    }
                });
            }
        };
        tr.start();
    }
}
