package food.vespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import food.vespro.adapter.CategoriaAdapter;
import food.vespro.entity.Categoria;

public class CategoriaActivity extends AppCompatActivity {
    private ArrayList<Categoria> categorias;
    private RecyclerView rvCategoria;
    private Categoria categoria;
    private RequestQueue requestQueue;
    private CategoriaAdapter categoriaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvCategoria = (RecyclerView) findViewById(R.id.rvCategoria);
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
                final String result = primero("https://vespro.io/food/wsApp/obtener_categorias.php");
                Log.i("cargarDatos", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = segundo(result);
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
