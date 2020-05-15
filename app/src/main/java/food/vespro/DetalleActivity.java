package food.vespro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import food.vespro.publico.Funciones;
import food.vespro.publico.PrefUtil;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class DetalleActivity extends AppCompatActivity {
    private static final int MY_SCAN_REQUEST_CODE = 100;
    Button btnDireccionEntrega, btnPlataformaPagos;
    public static EditText actvDireccionEntrega, actvNumeroTarjeta, actvNombre, actvMes, actvAnio, actvCvv;
    RadioButton rbtEntrega, rbtTarjeta, rbtTienda;
    PrefUtil prefUtil;
    LinearLayout llayTarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        btnDireccionEntrega = (Button) findViewById(R.id.btnDireccionEntrega);
        actvDireccionEntrega = (AutoCompleteTextView) findViewById(R.id.actvDireccionEntrega);
        rbtEntrega = (RadioButton) findViewById(R.id.rbtEntrega);
        rbtTarjeta = (RadioButton) findViewById(R.id.rbtTarjeta);
        rbtTienda = (RadioButton) findViewById(R.id.rbtTienda);
        actvNumeroTarjeta = (EditText) findViewById(R.id.actvNumeroTarjeta);
        actvNombre = (EditText) findViewById(R.id.actvNombre);
        actvMes = (EditText) findViewById(R.id.actvMes);
        actvAnio = (EditText) findViewById(R.id.actvAnio);
        actvCvv = (EditText) findViewById(R.id.actvCvv);
        btnPlataformaPagos = (Button) findViewById(R.id.btnPlataformaPagos);
        llayTarjeta = (LinearLayout) findViewById(R.id.llayTarjeta);
        prefUtil = new PrefUtil(this);
        btnPlataformaPagos.setText("Realizar pago (S/ " + getIntent().getStringExtra("total") + ")");
        btnDireccionEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actvDireccionEntrega.setEnabled(true);
                Intent intent = new Intent(DetalleActivity.this, DireccionActivity.class);
                startActivity(intent);
            }
        });
        btnPlataformaPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagar();
            }
        });
        rbtTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbtTarjeta.isChecked()) {
                    llayTarjeta.setVisibility(View.VISIBLE);
                    actvNumeroTarjeta.setEnabled(true);
                    actvNombre.setEnabled(true);
                    actvMes.setEnabled(true);
                    actvAnio.setEnabled(true);
                    actvCvv.setEnabled(true);
                    rbtEntrega.setChecked(false);
                    rbtTienda.setChecked(false);
                    actvNumeroTarjeta.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(actvNumeroTarjeta, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        rbtEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbtEntrega.isChecked()) {
                    llayTarjeta.setVisibility(View.GONE);
                    actvNumeroTarjeta.setEnabled(false);
                    actvNombre.setEnabled(false);
                    actvMes.setEnabled(false);
                    actvAnio.setEnabled(false);
                    actvCvv.setEnabled(false);
                    rbtTarjeta.setChecked(false);
                    rbtTienda.setChecked(false);
                }
            }
        });
        rbtTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbtTienda.isChecked()) {
                    llayTarjeta.setVisibility(View.GONE);
                    actvNumeroTarjeta.setEnabled(false);
                    actvNombre.setEnabled(false);
                    actvMes.setEnabled(false);
                    actvAnio.setEnabled(false);
                    actvCvv.setEnabled(false);
                    rbtTarjeta.setChecked(false);
                    rbtEntrega.setChecked(false);
                }
            }
        });
        actvNumeroTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                actvNumeroTarjeta.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode != KeyEvent.KEYCODE_DEL) {
                            if (actvNumeroTarjeta.getText().length() == 4) {
                                actvNumeroTarjeta.setText(actvNumeroTarjeta.getText().toString() + " ");
                                actvNumeroTarjeta.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        actvNumeroTarjeta.setSelection(actvNumeroTarjeta.getText().length());
                                    }
                                });
                            } else if (actvNumeroTarjeta.getText().length() == 9) {
                                actvNumeroTarjeta.setText(actvNumeroTarjeta.getText().toString() + " ");
                                actvNumeroTarjeta.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        actvNumeroTarjeta.setSelection(actvNumeroTarjeta.getText().length());
                                    }
                                });
                            } else if (actvNumeroTarjeta.getText().length() == 14) {
                                actvNumeroTarjeta.setText(actvNumeroTarjeta.getText().toString() + " ");
                                actvNumeroTarjeta.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        actvNumeroTarjeta.setSelection(actvNumeroTarjeta.getText().length());
                                    }
                                });
                            }
                        }
                        return false;
                    }
                });
            }
        });
    }

    public void pagar() {
        if (rbtEntrega.isChecked()) {
            pagoEntrega();
        } else if (rbtTarjeta.isChecked()) {
            onScanPress();
        } else if (rbtTienda.isChecked()) {
            pagoTienda();
        }
//        if (!verificarCompletado()) {
//            Toast.makeText(this, "Todo conforme", Toast.LENGTH_SHORT).show();
//        }
    }

    private boolean verificarCompletado() {
        boolean falta = false;
        if (actvNumeroTarjeta.getText().toString().trim().isEmpty()) {
            actvNumeroTarjeta.setError("Este campo es obligatorio");
            falta = true;
        } else if (actvNumeroTarjeta.getText().length() < 19) {
            actvNumeroTarjeta.setError("Número de tarjeta incorrecto");
            falta = true;
        }
        if (actvNombre.getText().toString().trim().isEmpty()) {
            actvNombre.setError("Este campo es obligatorio");
            falta = true;
        }
        if (actvMes.getText().toString().trim().isEmpty()) {
            actvMes.setError("Este campo es obligatorio");
            falta = true;
        } else if (actvMes.getText().length() < 2) {
            actvMes.setError("Mes incorrecto");
            falta = true;
        }
        if (actvAnio.getText().toString().trim().isEmpty()) {
            actvAnio.setError("Este campo es obligatorio");
            falta = true;
        } else if (actvAnio.getText().length() < 2) {
            actvAnio.setError("Año incorrecto");
            falta = true;
        }
        if (actvCvv.getText().toString().trim().isEmpty()) {
            actvCvv.setError("Este campo esobligatorio");
            falta = true;
        } else if (actvCvv.getText().length() != 3) {
            actvCvv.setError("CVV incorrecto");
            falta = true;
        }
        return falta;
    }

    public  void onScanPress() {
        Intent scanIntent = new Intent(this, CardIOActivity.class)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                DetalleActivity.actvNumeroTarjeta.setText("" + scanResult.getRedactedCardNumber());
                if (scanResult.isExpiryValid()) {
                    DetalleActivity.actvMes.setText("" + scanResult.expiryMonth);
                    DetalleActivity.actvAnio.setText(String.valueOf(scanResult.expiryYear).substring(2, 4));
                }
            }
        }
    }

    public void pagoEntrega() {
        Log.i("pagoEntrega", "DetalleActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/pago_entrega.php?id_pedido=" +
                        prefUtil.getStringValue("id_pedido") + "&estado=2");
                Log.i("pagoEntrega", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            Toast.makeText(DetalleActivity.this, "Pedido realizado con éxito",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DetalleActivity.this, CategoriaActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    public void pagoTienda() {
        Log.i("pagoTienda", "DetalleActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/pago_tienda.php?id_pedido=" +
                        prefUtil.getStringValue("id_pedido") + "&estado=3");
                Log.i("pagoTienda", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            Toast.makeText(DetalleActivity.this, "Pedido realizado satisfactoriamente",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DetalleActivity.this, CategoriaActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        tr.start();
    }
}
