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

import java.util.ArrayList;

import food.vespro.ProductoActivity;
import food.vespro.R;
import food.vespro.entity.Tienda;
import food.vespro.publico.AppController;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Tienda> mTiendas;
    private ImageLoader imgLoader = AppController.getInstance().getImageLoader();

    public TiendaAdapter(Context context, ArrayList<Tienda> tiendas) {
        mContext = context;
        mTiendas = tiendas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_tienda, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Tienda tienda = mTiendas.get(position);
        String nombreTienda = tienda.getNombre();
        final String idTienda = String.valueOf(tienda.getId_tienda());
        String imgTienda = tienda.getImagen();
        holder.tvNombreTienda.setText(nombreTienda);
        holder.tvIdTienda.setText(idTienda);
        holder.nivIconoTienda.setImageUrl(imgTienda, imgLoader);
        holder.cvTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.cvTienda.getContext(), ProductoActivity.class);
                intent.putExtra("id_tienda", idTienda);
                holder.cvTienda.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTiendas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView nivIconoTienda;
        public TextView tvNombreTienda, tvIdTienda;
        private CardView cvTienda;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nivIconoTienda = (NetworkImageView) itemView.findViewById(R.id.nivIconoTienda);
            tvNombreTienda = (TextView) itemView.findViewById(R.id.tvNombreTienda);
            tvIdTienda = (TextView) itemView.findViewById(R.id.tvIdTienda);
            cvTienda = (CardView) itemView.findViewById(R.id.cvTienda);
        }
    }
}
