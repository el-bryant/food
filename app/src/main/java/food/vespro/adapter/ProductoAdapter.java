package food.vespro.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.json.JSONArray;
import java.util.ArrayList;
import food.vespro.ProductoActivity;
import food.vespro.R;
import food.vespro.entity.Producto;
import food.vespro.publico.AppController;
import food.vespro.publico.Funciones;
import food.vespro.publico.PrefUtil;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Producto> mProductos;
    private ImageLoader imgLoader = AppController.getInstance().getImageLoader();
    private PrefUtil prefUtil;
    private static String carrito, id_pedido;
    public static int contador;

    public ProductoAdapter(Context context, ArrayList<Producto> productos) {
        mContext = context;
        mProductos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_producto, parent, false);
        prefUtil = new PrefUtil(mContext);
        Log.i("carrito", prefUtil.getStringValue("carrito"));
        if (prefUtil.getStringValue("carrito").equals("")) {
            carrito = "VACIO";
        } else {
            carrito = "OCUPADO";
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Producto productos = mProductos.get(position);
        final String id_producto = productos.getId_producto();
        String nombre = productos.getNombre();
        Double precio = productos.getPrecio();
        String proveedor = productos.getProveedor();
        String imagen = productos.getImagen();
        holder.nivImagenProducto.setImageUrl(imagen, imgLoader);
        holder.tvIdProducto.setText(id_producto);
        holder.tvNombreProducto.setText(nombre);
        holder.tvPrecioProducto.setText("S/ " + String.format("%.2f", precio));
        holder.tvProveedorProducto.setText(proveedor);
        holder.cvProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrito.equals("VACIO")) {
                    prefUtil.saveGenericValue("carrito", "OCUPADO");
                    iniciarPedido(id_producto);
                } else {
                    obtener_id_pedido(id_producto);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView nivImagenProducto;
        public TextView tvIdProducto, tvNombreProducto, tvPrecioProducto, tvProveedorProducto;
        private CardView cvProducto;

        public ViewHolder(@NonNull View v) {
            super(v);
            nivImagenProducto = (NetworkImageView) v.findViewById(R.id.nivImagenProducto);
            tvIdProducto = (TextView) v.findViewById(R.id.tvIdProducto);
            tvNombreProducto = (TextView) v.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = (TextView) v.findViewById(R.id.tvPrecioProducto);
            tvProveedorProducto = (TextView) v.findViewById(R.id.tvProveedorProducto);
            cvProducto = (CardView) v.findViewById(R.id.cvProducto);
        }
    }

    public void iniciarPedido(final String id_producto) {
        Log.i("iniciarPedido", "ProductoAdapter");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/iniciar_pedido.php?dni_cliente="
                        + prefUtil.getStringValue("dni_cliente"));
                Log.i("iniciarPedido", result);
                int r = Funciones.segundo(result);
                if (r > 0) {
                    prefUtil.saveGenericValue("carrito", "OCUPADO");
                    obtener_id_pedido(id_producto);
                }
            }
        };
        tr.start();
    }

    public void obtener_id_pedido(final String id_producto) {
        Log.i("obtener_id_pedido", "ProductoAdapter");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/obtener_id_pedido.php?dni_cliente="
                        + prefUtil.getStringValue("dni_cliente"));
                Log.i("obtener_id_pedido", result);
                int r = Funciones.segundo(result);
                if (r > 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        if (jsonArray.length() > 0) {
                            id_pedido = jsonArray.getJSONObject(0).getString("id_pedido");
                            prefUtil.saveGenericValue("id_pedido", id_pedido);
                            agregarProducto(id_producto, id_pedido);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        tr.start();
    }

    public void agregarProducto(final String id_producto, final String id_pedido) {
        Log.i("agregarProducto", "ProductoAdapter");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/agregar_producto.php?id_producto="
                        + id_producto + "&id_pedido=" + id_pedido);
                Log.i("iniciarPedido", result);
                int r = Funciones.segundo(result);
                if (r > 0) {
                    contador = Integer.parseInt(ProductoActivity.tvMensaje.getText().toString()) + 1;
                }
            }
        };
        tr.start();
        ProductoActivity.tvMensaje.setText(String.valueOf(contador));
        Log.i("agregarProducto", "Agregado satisfactoriamente");
    }
}
