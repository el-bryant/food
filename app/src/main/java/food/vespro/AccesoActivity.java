package food.vespro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import food.vespro.publico.Funciones;
import food.vespro.publico.PrefUtil;

public class AccesoActivity extends AppCompatActivity {
    TextView tvRegistro;
    Button btnAcceder;
    PrefUtil prefUtil;
    AutoCompleteTextView actvDni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        tvRegistro = (TextView) findViewById(R.id.tvRegistro);
        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        actvDni = (AutoCompleteTextView) findViewById(R.id.actvDni);
        prefUtil = new PrefUtil(this);
        if (prefUtil.getStringValue("dni_cliente").equals("")) {
            Log.i("dni_cliente", prefUtil.getStringValue("dni_cliente"));
            Intent intent = new Intent(AccesoActivity.this, CategoriaActivity.class);
//            startActivity(intent);
//            finish();
        }
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
                                    prefUtil.saveGenericValue("dni_cliente", dni_cliente);
                                    Intent intent = new Intent(AccesoActivity.this, CategoriaActivity.class);
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
    }
}
