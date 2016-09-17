package mx.jgarcia.android.radargdl;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import mx.jgarcia.android.radargdl.RadarActivity;

public class RadarOverlay extends AsyncTask<String, Integer, BitmapDescriptor> {

    BitmapDescriptor bitmapDescriptor;
    GoogleMap googleMap;
    String url;
    String lastModified;
    String formattedDate;

    public RadarOverlay(GoogleMap googleMap, String url) {
        this.googleMap = googleMap;
        this.url = url;

    }

    @Override
    protected BitmapDescriptor doInBackground(String... url2) {
        URLConnection connection;

        try {
            connection = new URL(url).openConnection();
            //lastModified = connection.getHeaderField("Last-Modified");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
            lastModified = formatter.format(new Date(connection.getLastModified()));
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(Picasso.with(RadarActivity.activity).load(url).get());

            if (bitmapDescriptor == null) {
                Toast.makeText(RadarActivity.activity, "Pronostico No Disponible", Toast.LENGTH_SHORT).show();
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapDescriptor;
    }

    protected void onPostExecute(BitmapDescriptor icon) {

        try {
            LatLng gdl = new LatLng(20.66, -103.33);
            LatLngBounds gdlRain;

            gdlRain = new LatLngBounds(new LatLng(18.41, -105.78), new LatLng(22.93, -100.98));

            GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
                    .image(bitmapDescriptor)
                    .positionFromBounds(gdlRain);
            googleMap.setTrafficEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);

            googleMap.addGroundOverlay(groundOverlay);
            googleMap.setMinZoomPreference(11);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


            googleMap.moveCamera(CameraUpdateFactory.newLatLng(gdl));
            Toast.makeText(RadarActivity.activity, "Actualizacion:: " + lastModified, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}