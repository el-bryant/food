package food.codi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;

import food.codi.CarritoActivity;
import food.codi.R;
import food.codi.entity.Carrito;
import food.codi.entity.Producto;
import food.codi.publico.AppController;
import food.codi.publico.Funciones;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Carrito> mCarrito;
    private ImageLoader imgLoader = AppController.getInstance().getImageLoader();
    public static int contador;

    public CarritoAdapter(Context context, ArrayList<Carrito> carrito) {
        mContext = context;
        mCarrito = carrito;
    }

    @NonNull
    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_carrito, parent, false);
        return new CarritoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CarritoAdapter.ViewHolder holder, int position) {
        Carrito carrito = mCarrito.get(position);
        final String id_detalle_pedido = carrito.getId_detalle_pedido();
        final String id_producto = carrito.getId_producto();
        String nombre = carrito.getNombre();
        Double precio = carrito.getPrecio();
        String proveedor = carrito.getProveedor();
        String imagen = carrito.getImagen();
        holder.nivImagenProducto.setImageUrl(imagen, imgLoader);
        holder.tvIdProducto.setText(id_producto);
        holder.tvNombreProducto.setText(nombre);
        holder.tvPrecioProducto.setText("S/ " + String.format("%.2f", precio));
        holder.tvProveedorProducto.setText(proveedor);
        holder.ivEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("eliminarCarrito", id_detalle_pedido);
                eliminarCarrito(id_detalle_pedido);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCarrito.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView nivImagenProducto;
        public TextView tvIdProducto, tvNombreProducto, tvPrecioProducto, tvProveedorProducto;
        public CardView cvCarrito;
        public ImageView ivEliminar;

        public ViewHolder(@NonNull View v) {
            super(v);
            nivImagenProducto = (NetworkImageView) v.findViewById(R.id.nivImagenProducto);
            tvIdProducto = (TextView) v.findViewById(R.id.tvIdProducto);
            tvNombreProducto = (TextView) v.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = (TextView) v.findViewById(R.id.tvPrecioProducto);
            tvProveedorProducto = (TextView) v.findViewById(R.id.tvProveedorProducto);
            cvCarrito = (CardView) v.findViewById(R.id.cvCarrito);
            ivEliminar = (ImageView) v.findViewById(R.id.ivEliminar);
        }
    }

    public void eliminarCarrito(final String id_detalle_pedido) {
        Log.i("eliminarCarrito", "CarritoAdapter");
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String result = Funciones.primero("https://vespro.io/food/wsApp/eliminar_carrito.php?" +
                        "&id_detalle_pedido=" + id_detalle_pedido);
                Log.i("eliminarCarrito", result);
                int r = Funciones.segundo(result);
                if (r > 0) {
                    contador = Integer.parseInt(CarritoActivity.tvMensaje.getText().toString()) + 1;
                }
            }
        };
        tr.start();
        CarritoActivity.tvMensaje.setText(String.valueOf(contador));
        Log.i("eliminarCarrito", "Eliminado satisfactoriamente");
    }
}
