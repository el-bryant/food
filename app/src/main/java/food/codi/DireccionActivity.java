package food.codi;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import java.util.Locale;

/**
 * By: El Bryant
 */

public class DireccionActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {
    public static Double latitud = 0.0, longitud = 0.0;
    public static Marker markerMiUbicacion;
    TextView tvDireccion;
    public Geocoder geocoder;
    Button btnAceptar;
    private boolean MOVER_CAMARA = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        tvDireccion = (TextView) findViewById(R.id.tvDireccion);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        geocoder = new Geocoder(this, Locale.getDefault());
        mapFragment.getMapAsync(this);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvDireccion.getText().toString().equals("Cargando...")) {
                    DetalleActivity.tvDireccionEntrega.setText(tvDireccion.getText().toString());
                    DetalleActivity.latitud = latitud;
                    DetalleActivity.longitud = longitud;
                    DetalleActivity.btnPlataformaPagos.setEnabled(true);
                    finish();
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        MarkerOptions markerOptions = new MarkerOptions();
        if (latitud != 0.0 && longitud != 0.0) {
            LatLng miPosicion = new LatLng(latitud, longitud);
            markerOptions.position(miPosicion);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador));
            if (markerMiUbicacion == null) {
                markerMiUbicacion = mMap.addMarker(markerOptions);
            } else {
                markerMiUbicacion.setPosition(miPosicion);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .zoom(14)
                    .target(markerMiUbicacion.getPosition())
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            new DireccionOrigenGeocoder().execute(markerMiUbicacion.getPosition().latitude,
                    markerMiUbicacion.getPosition().longitude);
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
            }
            MarkerOptions markerOptions = new MarkerOptions();
            if (latitud != 0.0 && longitud != 0.0) {
                LatLng miPosicion = new LatLng(latitud, longitud);
                markerOptions.position(miPosicion);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador));
                if (markerMiUbicacion == null) {
                    markerMiUbicacion = mMap.addMarker(markerOptions);
                } else {
                    markerMiUbicacion.setPosition(miPosicion);
                }
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .zoom(14)
                        .target(markerMiUbicacion.getPosition())
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                new DireccionOrigenGeocoder().execute(markerMiUbicacion.getPosition().latitude,
                        markerMiUbicacion.getPosition().longitude);
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    public void onCameraIdle() {
        if (MOVER_CAMARA) {
            if (markerMiUbicacion == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng miPosicion = new LatLng(latitud, longitud);
                markerOptions.position(miPosicion);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador));
                markerMiUbicacion = mMap.addMarker(markerOptions);
            } else {
                markerMiUbicacion.setPosition(mMap.getCameraPosition().target);
            }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(locListener);
            new DireccionOrigenGeocoder().execute(markerMiUbicacion.getPosition().latitude,
                    markerMiUbicacion.getPosition().longitude);
        }
    }

    @Override
    public void onCameraMove() {
        MOVER_CAMARA = true;
        if (markerMiUbicacion == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng miPosicion = new LatLng(latitud, longitud);
            markerOptions.position(miPosicion);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador));
            markerMiUbicacion = mMap.addMarker(markerOptions);
        } else {
            markerMiUbicacion.setPosition(mMap.getCameraPosition().target);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locListener);
    }

    @SuppressLint("StaticFieldLeak")
    private class DireccionOrigenGeocoder extends AsyncTask<Double, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(Double... geoPoint) {
            String direccion = "";
            Double mLatitud = geoPoint[0];
            Double mLongitud = geoPoint[1];
            String mDireccion = "";
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(mLatitud, mLongitud, 3);
            } catch (Exception e) {
                e.printStackTrace();
                mDireccion = "Cargando...";
            }
            if (addresses != null) {
                if (addresses.size() > 0) {
                    for (int i = 0; i < addresses.size(); i++) {
                        if (addresses.get(i).getAddressLine(0) != null) {
                            mDireccion = addresses.get(i).getAddressLine(0);
                        }
                        if (!mDireccion.equals("")) {
                            break;
                        }
                    }
                }
            }
            direccion = mDireccion;
            return direccion;
        }
        protected void onPostExecute(String result) {
            tvDireccion.setText(result);
        }
    }
}
