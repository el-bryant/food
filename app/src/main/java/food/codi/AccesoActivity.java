package food.codi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import food.codi.publico.Funciones;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class AccesoActivity extends AppCompatActivity {
    TextView tvRegistro;
    Button btnAcceder;
    PrefUtil prefUtil;
    AutoCompleteTextView actvDni;
    public static String nombreUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        tvRegistro = (TextView) findViewById(R.id.tvRegistro);
        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        actvDni = (AutoCompleteTextView) findViewById(R.id.actvDni);
        prefUtil = new PrefUtil(this);
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceder(actvDni.getText().toString());
            }
        });
    }

    public void registrar() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
        finish();
    }

    public void acceder(final String dni_cliente) {
        Log.i("acceder", "AccesoActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/acceder.php?dni_cliente=" +
                        dni_cliente);
                Log.i("acceder", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                if (jsonArray.length() > 0) {
                                    nombreUsuario = jsonArray.getJSONObject(0).getString("nombres");
                                    prefUtil.saveGenericValue("dni_cliente", dni_cliente);
                                    prefUtil.saveGenericValue("nombre", nombreUsuario);
                                    prefUtil.saveGenericValue(PrefUtil.LOGIN_STATUS, "1");
                                    Intent intent = new Intent(AccesoActivity.this, CategoriaActivity.class);
                                    intent.putExtra("nombre", nombreUsuario);
                                    startActivity(intent);
                                    Toast.makeText(AccesoActivity.this, "Bienvenido de nuevo",
                                            Toast.LENGTH_SHORT).show();
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
        SharedPreferences sharedPreferences = getSharedPreferences("datosUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("nomre", nombreUsuario);
        editor.apply();
    }
}
