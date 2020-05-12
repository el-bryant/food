package food.vespro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleActivity extends AppCompatActivity {
    Button btnDireccionEntrega, btnPlataformaPagos;
    public static AutoCompleteTextView actvDireccionEntrega, actvNumeroTarjeta, actvNombre, actvMes, actvAnio, actvCvv;
    RadioButton rbtEntrega, rbtTarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        btnDireccionEntrega = (Button) findViewById(R.id.btnDireccionEntrega);
        actvDireccionEntrega = (AutoCompleteTextView) findViewById(R.id.actvDireccionEntrega);
        rbtEntrega = (RadioButton) findViewById(R.id.rbtEntrega);
        rbtTarjeta = (RadioButton) findViewById(R.id.rbtTarjeta);
        actvNumeroTarjeta = (AutoCompleteTextView) findViewById(R.id.actvNumeroTarjeta);
        actvNombre = (AutoCompleteTextView) findViewById(R.id.actvNombre);
        actvMes = (AutoCompleteTextView) findViewById(R.id.actvMes);
        actvAnio = (AutoCompleteTextView) findViewById(R.id.actvAnio);
        actvCvv = (AutoCompleteTextView) findViewById(R.id.actvCvv);
        btnPlataformaPagos = (Button) findViewById(R.id.btnPlataformaPagos);
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
                    actvNumeroTarjeta.setEnabled(true);
                    actvNombre.setEnabled(true);
                    actvMes.setEnabled(true);
                    actvAnio.setEnabled(true);
                    actvCvv.setEnabled(true);
                    rbtEntrega.setChecked(false);
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
                    actvNumeroTarjeta.setEnabled(false);
                    actvNombre.setEnabled(false);
                    actvMes.setEnabled(false);
                    actvAnio.setEnabled(false);
                    actvCvv.setEnabled(false);
                    rbtTarjeta.setChecked(false);
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
        if (!verificarCompletado()) {
            Toast.makeText(this, "Todo conforme", Toast.LENGTH_SHORT).show();
        }
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
}
