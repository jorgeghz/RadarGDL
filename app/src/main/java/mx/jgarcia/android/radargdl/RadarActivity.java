package mx.jgarcia.android.radargdl;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

public class RadarActivity extends AppCompatActivity implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private GoogleMap mMap;
    RadarOverlay radar;
    String url;
    public static Activity activity;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_radar);

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        AdView mAdView = (AdView) findViewById(R.id.adView);
       // AdRequest.Builder.addTestDevice("19512A916F2282E949EE5B8345AAF928");

        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("19512A916F2282E949EE5B8345AAF928").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        activity = this;
        res = getResources();
        url = res.getString(R.string.URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_refresh) {
           refreshOverlay(this.getCurrentFocus());
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng gdl = new LatLng(20.66, -103.33);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        Resources res = getResources();
        this.radar = new RadarOverlay(mMap, res.getString(R.string.URL));
        this.radar.execute();

    }

    public void refreshOverlay(View view) {
        radar = new RadarOverlay(this.mMap, this.url);
        radar.execute();
    }

}
