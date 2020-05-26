package food.codi;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONObject;
import food.codi.publico.Funciones;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class RegistroActivity extends AppCompatActivity {
    AutoCompleteTextView actvDni, actvApellidos, actvNombres, actvTelefono, actvCorreo;
    Button btnRegistro;
    PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        actvDni = (AutoCompleteTextView) findViewById(R.id.actvDni);
        actvApellidos = (AutoCompleteTextView) findViewById(R.id.actvApellidos);
        actvNombres = (AutoCompleteTextView) findViewById(R.id.actvNombres);
        actvTelefono = (AutoCompleteTextView) findViewById(R.id.actvTelefono);
        actvCorreo = (AutoCompleteTextView) findViewById(R.id.actvCorreo);
        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        prefUtil = new PrefUtil(this);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
        actvDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (actvDni.getText().length() == 8) {
                    actvDni.setEnabled(false);
                    actvApellidos.setEnabled(false);
                    actvNombres.setEnabled(false);
                    actvTelefono.setEnabled(false);
                    actvCorreo.setEnabled(false);
                    comprobardatos(actvDni.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void registrar() {
        Log.i("registrar", "RegistroActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("http://vespro.io/food/wsApp/registro.php?dni_cliente=" +
                        actvDni.getText().toString() + "&apellidos=" + actvApellidos.getText().toString() + "&nombres=" +
                        actvNombres.getText().toString() + "&telefono=" + actvTelefono.getText().toString() + "&correo=" +
                        actvCorreo.getText().toString());
                Log.i("registrar", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            prefUtil.saveGenericValue("dni_cliente", actvDni.getText().toString());
                            Toast.makeText(RegistroActivity.this, "Gracias por su registro",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistroActivity.this, CategoriaActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    public void comprobardatos(final String dni) {
        Log.i("comprobardatos", "RegistroActivity");
        final Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://flota.vespro.io/dni?documento=" + dni);
                Log.i("comprobardatos", result);
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (!result.equals(" No se encontró el DNI o falló conexion con JNE")) {
                            int r = Funciones.segundo(result);
                            if (r > 0) {
                                try {
                                    JSONObject jsonArray = new JSONObject(result);
                                    String nombres = "", apellido_paterno = "", apellido_materno = "";
                                    if (jsonArray.length() > 0) {
                                        nombres = jsonArray.getString("nombres");
                                        apellido_paterno = jsonArray.getString("apellidoPaterno");
                                        apellido_materno = jsonArray.getString("apellidoMaterno");
                                        if (!nombres.equals("") && !apellido_paterno.equals("") && !apellido_materno.equals("")) {
                                            actvNombres.setText(nombres);
                                            actvApellidos.setText(apellido_paterno + " " + apellido_materno);
                                            actvDni.setEnabled(true);
                                            actvNombres.setEnabled(false);
                                            actvApellidos.setEnabled(false);
                                            actvTelefono.setEnabled(true);
                                            actvCorreo.setEnabled(true);
                                        } else {
                                            actvNombres.setText("");
                                            actvApellidos.setText("");
                                            actvDni.setEnabled(true);
                                            actvNombres.setEnabled(true);
                                            actvApellidos.setEnabled(true);
                                            actvTelefono.setEnabled(true);
                                            actvCorreo.setEnabled(true);
                                            Toast.makeText(RegistroActivity.this,
                                                    "No se pudo cargar los datos, por favor registre manualmente",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            actvApellidos.setText("");
                            actvNombres.setText("");
                            actvApellidos.setEnabled(true);
                            actvNombres.setEnabled(true);
                            Toast.makeText(RegistroActivity.this,
                                    "No se pudo encontrar los datos, por favor registre manualmente",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }
}
