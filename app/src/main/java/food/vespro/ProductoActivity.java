package food.vespro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProductoActivity extends AppCompatActivity {
    TextView tvIdCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        String id_categoria = getIntent().getStringExtra("id_categoria");
        tvIdCategoria = (TextView) findViewById(R.id.tvIdCategoria);
        tvIdCategoria.setText(id_categoria);
    }
}
