package food.codi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import food.codi.publico.PrefUtil;

/**
 * By: El Bryant
 */

public class Splash extends AppCompatActivity {
    ProgressBar pbSplash;
    Handler handler;
    PrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pbSplash = (ProgressBar) findViewById(R.id.pbSplash);
        prefUtil = new PrefUtil(this);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefUtil.getStringValue(PrefUtil.LOGIN_STATUS).equals("1")) {
                    Intent intent = new Intent(Splash.this, CategoriaActivity.class);
                    startActivity(intent);
                    intent.putExtra("nombre", prefUtil.getStringValue("noombre"));
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, AccesoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}
