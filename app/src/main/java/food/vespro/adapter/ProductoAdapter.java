package food.vespro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import food.vespro.ProductoActivity;
import food.vespro.R;
import food.vespro.entity.Categoria;
import food.vespro.entity.Producto;
import food.vespro.publico.AppController;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Producto> mProductos;
    private ImageLoader imgLoader = AppController.getInstance().getImageLoader();

    public ProductoAdapter(Context context, ArrayList<Producto> productos) {
        mContext = context;
        mProductos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Producto productos = mProductos.get(position);
        String id_producto = productos.getId_producto();
        String nombre = productos.getNombre();
        Double precio = productos.getPrecio();
        String proveedor = productos.getProveedor();
        String imagen = productos.getImagen();
        holder.nivImagenProducto.setImageUrl(imagen, imgLoader);
        holder.tvIdProducto.setText(id_producto);
        holder.tvNombreProducto.setText(nombre);
        holder.tvPrecioProducto.setText("S/ " + String.format("%.2f", precio));
        holder.tvProveedorProducto.setText(proveedor);
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
}
