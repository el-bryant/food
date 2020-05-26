package food.codi.publico;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
    private static final String NAME_REFERENCE = "version_code";
    private Context context;

    public PrefUtil(Context context) {
        this.context = context;
    }

    public void saveGenericValue(String campo, String valor) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(campo, valor);
        editor.apply();
    }

    public void clearAll() {
        SharedPreferences prefs = context.getSharedPreferences(NAME_REFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public String getStringValue(String campo) {
        SharedPreferences prefs = context.getSharedPreferences(NAME_REFERENCE, Context.MODE_PRIVATE);
        return prefs.getString(campo, "");
    }
}
