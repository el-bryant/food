package food.codi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import food.codi.config.Config;
import food.codi.publico.Funciones;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class DetalleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Button btnDireccionEntrega, btnPlataformaPagos;
//    public static EditText actvNumeroTarjeta, actvNombre, actvMes, actvAnio, actvCvv;
    public static TextView tvDireccionEntrega, tvNombreNav;
    RadioButton rbtEntrega, rbtTarjeta, rbtTienda;
    PrefUtil prefUtil;
//    LinearLayout llayTarjeta;
    Date fecha;
    public static Double latitud = 0.0, longitud = 0.0;
    private static final int PAYPAL_REQUEST_CODE = 7777;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    String amount = "";
    ImageView ivMenu;
    LinearLayout ivCerrar;
    private String nombre_nav = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //start paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnDireccionEntrega = (Button) findViewById(R.id.btnDireccionEntrega);
        tvDireccionEntrega = (TextView) findViewById(R.id.tvDireccionEntrega);
        rbtEntrega = (RadioButton) findViewById(R.id.rbtEntrega);
        rbtTarjeta = (RadioButton) findViewById(R.id.rbtTarjeta);
        rbtTienda = (RadioButton) findViewById(R.id.rbtTienda);
//        actvNumeroTarjeta = (EditText) findViewById(R.id.actvNumeroTarjeta);
//        actvNombre = (EditText) findViewById(R.id.actvNombre);
//        actvMes = (EditText) findViewById(R.id.actvMes);
//        actvAnio = (EditText) findViewById(R.id.actvAnio);
//        actvCvv = (EditText) findViewById(R.id.actvCvv);
        btnPlataformaPagos = (Button) findViewById(R.id.btnPlataformaPagos);
//        llayTarjeta = (LinearLayout) findViewById(R.id.llayTarjeta);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
        tvNombreNav = (TextView) nav.getHeaderView(0).findViewById(R.id.tvNombreNav);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivCerrar = (LinearLayout) findViewById(R.id.ivCerrar);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        prefUtil = new PrefUtil(this);
        btnPlataformaPagos.setText("Realizar pago (S/ " + getIntent().getStringExtra("total") + ")");
        btnDireccionEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDireccionEntrega.setEnabled(true);
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
//                    llayTarjeta.setVisibility(View.GONE);
//                    actvNumeroTarjeta.setEnabled(true);
//                    actvNombre.setEnabled(true);
//                    actvMes.setEnabled(true);
//                    actvAnio.setEnabled(true);
//                    actvCvv.setEnabled(true);
                    rbtEntrega.setChecked(false);
                    rbtTienda.setChecked(false);
//                    actvNumeroTarjeta.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(actvNumeroTarjeta, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        rbtEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbtEntrega.isChecked()) {
//                    llayTarjeta.setVisibility(View.GONE);
//                    actvNumeroTarjeta.setEnabled(false);
//                    actvNombre.setEnabled(false);
//                    actvMes.setEnabled(false);
//                    actvAnio.setEnabled(false);
//                    actvCvv.setEnabled(false);
                    rbtTarjeta.setChecked(false);
                    rbtTienda.setChecked(false);
                }
            }
        });
        rbtTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbtTienda.isChecked()) {
//                    llayTarjeta.setVisibility(View.GONE);
//                    actvNumeroTarjeta.setEnabled(false);
//                    actvNombre.setEnabled(false);
//                    actvMes.setEnabled(false);
//                    actvAnio.setEnabled(false);
//                    actvCvv.setEnabled(false);
                    rbtTarjeta.setChecked(false);
                    rbtEntrega.setChecked(false);
                }
            }
        });
//        actvNumeroTarjeta.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                actvNumeroTarjeta.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        if (keyCode != KeyEvent.KEYCODE_DEL) {
//                            if (actvNumeroTarjeta.getText().length() == 4) {
//                                actvNumeroTarjeta.setText(actvNumeroTarjeta.getText().toString() + " ");
//                                actvNumeroTarjeta.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        actvNumeroTarjeta.setSelection(actvNumeroTarjeta.getText().length());
//                                    }
//                                });
//                            } else if (actvNumeroTarjeta.getText().length() == 9) {
//                                actvNumeroTarjeta.setText(actvNumeroTarjeta.getText().toString() + " ");
//                                actvNumeroTarjeta.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        actvNumeroTarjeta.setSelection(actvNumeroTarjeta.getText().length());
//                                    }
//                                });
//                            } else if (actvNumeroTarjeta.getText().length() == 14) {
//                                actvNumeroTarjeta.setText(actvNumeroTarjeta.getText().toString() + " ");
//                                actvNumeroTarjeta.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        actvNumeroTarjeta.setSelection(actvNumeroTarjeta.getText().length());
//                                    }
//                                });
//                            }
//                        }
//                        return false;
//                    }
//                });
//            }
//        });
        char[] caracteres_nav = (prefUtil.getStringValue("nombre").substring(0,
                prefUtil.getStringValue("nombre").indexOf(" ")).toLowerCase()).toCharArray();
        caracteres_nav[0] = Character.toUpperCase(caracteres_nav[0]);
        for (int i = 0; i < prefUtil.getStringValue("nombre").substring(0,
                prefUtil.getStringValue("nombre").indexOf(" ")).length(); i ++) {
            if (caracteres_nav[i] == ' ') {
                caracteres_nav[i + 1] = Character.toUpperCase(caracteres_nav[i + 1]);
            }
            nombre_nav = nombre_nav + caracteres_nav[i];
        }
        tvNombreNav.setText("¡Hola, " + nombre_nav + "!");
        ivCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefUtil.clearAll();
                Intent intent = new Intent(DetalleActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void pagar() {
        if (rbtEntrega.isChecked()) {
            pagoEntrega();
        } else if (rbtTarjeta.isChecked()) {
            processPayment();
        } else if (rbtTienda.isChecked()) {
            pagoTienda();
        }
//        if (!verificarCompletado()) {
//            Toast.makeText(this, "Todo conforme", Toast.LENGTH_SHORT).show();
//        }
    }

//    private boolean verificarCompletado() {
//        boolean falta = false;
//        if (actvNumeroTarjeta.getText().toString().trim().isEmpty()) {
//            actvNumeroTarjeta.setError("Este campo es obligatorio");
//            falta = true;
//        } else if (actvNumeroTarjeta.getText().length() < 19) {
//            actvNumeroTarjeta.setError("Número de tarjeta incorrecto");
//            falta = true;
//        }
//        if (actvNombre.getText().toString().trim().isEmpty()) {
//            actvNombre.setError("Este campo es obligatorio");
//            falta = true;
//        }
//        if (actvMes.getText().toString().trim().isEmpty()) {
//            actvMes.setError("Este campo es obligatorio");
//            falta = true;
//        } else if (actvMes.getText().length() < 2) {
//            actvMes.setError("Mes incorrecto");
//            falta = true;
//        }
//        if (actvAnio.getText().toString().trim().isEmpty()) {
//            actvAnio.setError("Este campo es obligatorio");
//            falta = true;
//        } else if (actvAnio.getText().length() < 2) {
//            actvAnio.setError("Año incorrecto");
//            falta = true;
//        }
//        if (actvCvv.getText().toString().trim().isEmpty()) {
//            actvCvv.setError("Este campo esobligatorio");
//            falta = true;
//        } else if (actvCvv.getText().length() != 3) {
//            actvCvv.setError("CVV incorrecto");
//            falta = true;
//        }
//        return falta;
//    }

    public void pagoEntrega() {
        fecha = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        Log.i("pagoEntrega", "DetalleActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/pago_entrega.php?id_pedido="
                        + prefUtil.getStringValue("id_pedido") + "&fecha=" + dateFormat.format(fecha) + "&hora="
                        + hourFormat.format(fecha) + "&estado=2&direccion=" + tvDireccionEntrega.getText().toString()
                        + "&latitud=" + String.format("%.10f", latitud) + "&longitud=" + String.format("%.10f", longitud) +
                        "&total=" + btnPlataformaPagos.getText().toString().substring(18, btnPlataformaPagos.getText().length()
                        - 1));
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
        fecha = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        Log.i("pagoTienda", "DetalleActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/pago_entrega.php?id_pedido="
                        + prefUtil.getStringValue("id_pedido") + "&fecha=" + dateFormat.format(fecha) + "&hora="
                        + hourFormat.format(fecha) + "&estado=3&direccion=" + tvDireccionEntrega.getText().toString()
                        + "&latitud=" + latitud + "&longitud=" + longitud + "&total="
                        + btnPlataformaPagos.getText().toString().substring(18, btnPlataformaPagos.getText().length() - 1));
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

    public void pagoTarjeta() {
        fecha = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        Log.i("pagoTarjeta", "DetalleActivity");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/pago_entrega.php?id_pedido="
                        + prefUtil.getStringValue("id_pedido") + "&fecha=" + dateFormat.format(fecha) + "&hora="
                        + hourFormat.format(fecha) + "&estado=4&direccion=" + tvDireccionEntrega.getText().toString()
                        + "&latitud=" + latitud + "&longitud=" + longitud + "&total="
                        + btnPlataformaPagos.getText().toString().substring(18, btnPlataformaPagos.getText().length() - 1));
                Log.i("pagoTarjeta", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = Funciones.segundo(result);
                        if (r > 0) {
                            Toast.makeText(DetalleActivity.this, "Pago realizado con éxito",
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

    private void processPayment() {
        amount = btnPlataformaPagos.getText().toString().substring(18, btnPlataformaPagos.getText().length() - 1);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD",
                "Compras por App Food Delivery para Android", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("Payment Details", paymentDetails)
                                .putExtra("Amount",amount));
                        pagoTarjeta();
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent;
        switch (id) {
            case R.id.navCategorias:
                intent = new Intent(DetalleActivity.this, CategoriaActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ivCerrar:
                prefUtil.clearAll();
                intent = new Intent(DetalleActivity.this, AccesoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navNotificaciones:
                intent = new Intent(DetalleActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPedidos:
                intent = new Intent(DetalleActivity.this, PedidosActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPerfil:
                intent = new Intent(DetalleActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPreguntas:
                intent = new Intent(DetalleActivity.this, PreguntasActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navPromociones:
                intent = new Intent(DetalleActivity.this, PromocionesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
