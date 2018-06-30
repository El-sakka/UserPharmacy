package www.sydlinaonline.com.userpharmacy;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationActivity extends AppCompatActivity {
    private static final String TAG = "SetLocationActivity";
    private static final float CAMERA_ZOOM = 15f;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String LOCATION_CLASS = "location_class";

    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private LatLng mLatLng;

    GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String LATITUDE_KEY ="latitude";
    private static final String LANGITUDE_KEY ="longitude";
    private static final String PHRMACY_KEY ="phrmacy";

    private String phramcyName;


    //fab
    FloatingActionButton checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Intent intent = getIntent();
        phramcyName = intent.getStringExtra(PHRMACY_KEY);
        String lat = intent.getStringExtra(LATITUDE_KEY);
        String lng = intent.getStringExtra(LANGITUDE_KEY);

        mLatLng = new LatLng(Double.valueOf(lat),Double.valueOf(lng));

        if (googlePlayServiceAvaliable()) {
            Toast.makeText(this, "Perfect", Toast.LENGTH_SHORT).show();
            getLocationPermission();
        } else {
            // no Google maps layout
        }


    }

    private void redirectToPhrmacy(){

    }


    /*
     * initalize our map and display in into our fragment layout
     * */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);

               MarkerOptions options = new MarkerOptions();
               options.position(mLatLng).title(phramcyName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

               mGoogleMap.addMarker(options);

            }
        });
    }

    /*
     * getting device Current Location
     * */

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    CAMERA_ZOOM);
                        } else {
                            Log.d(TAG, "onComplete: current  location is null");
                            Toast.makeText(LocationActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to : lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting Location Permsissions ");
        String [] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION )
                    == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    /*
     * go to specific Location
     * */
    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);

        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, CAMERA_ZOOM);
        mGoogleMap.moveCamera(update);
    }
    public boolean googlePlayServiceAvaliable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
