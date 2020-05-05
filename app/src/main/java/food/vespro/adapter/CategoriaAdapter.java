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
import food.vespro.entity.Categoria;
import food.vespro.publico.AppController;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Categoria> mCategorias;
    private ImageLoader imgLoader = AppController.getInstance().getImageLoader();

    public CategoriaAdapter(Context context, ArrayList<Categoria> categorias) {
        mContext = context;
        mCategorias = categorias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_categoria, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Categoria categoria = mCategorias.get(position);
        String nombreCategoria = categoria.getNombre();
        String idCategoria = String.valueOf(categoria.getId_categoria());
        String imgCategoria = categoria.getImagen();
        holder.tvIdCategoria.setText(idCategoria);
        holder.tvNombreCategoria.setText(nombreCategoria);
        holder.ivIconoCategoria.setImageUrl(imgCategoria, imgLoader);
        holder.cvCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.cvCategoria.getContext(), ProductoActivity.class);
                intent.putExtra("id_categoria", holder.tvIdCategoria.getText().toString());
                holder.cvCategoria.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategorias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView ivIconoCategoria;
        public TextView tvNombreCategoria, tvIdCategoria;
        private CardView cvCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIconoCategoria = (NetworkImageView) itemView.findViewById(R.id.nivIconoCategoria);
            tvNombreCategoria = (TextView) itemView.findViewById(R.id.tvNombreCategoria);
            tvIdCategoria = (TextView) itemView.findViewById(R.id.tvIdCategoria);
            cvCategoria = (CardView) itemView.findViewById(R.id.cvCategoria);
        }
    }
}
