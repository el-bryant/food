package food.codi.publico;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * By: El Bryant
 */

public class Funciones {

    public static String primero(String direccion) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();
            result = new StringBuilder();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((linea = reader.readLine()) != null) {
                    result.append(linea);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static int segundo(String response) {
        int res = 0;
        if (response != null) {
            res = 1;
        }
        return res;
    }
}
