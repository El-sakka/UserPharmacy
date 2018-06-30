package www.sydlinaonline.com.userpharmacy.Maps;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.Model.PharmacyInfo;
import www.sydlinaonline.com.userpharmacy.R;
import www.sydlinaonline.com.userpharmacy.Reservation.ReservationActivity;

public class CutomMapLocation extends AppCompatActivity {


    private static final String TAG = "CutomMapLocation";
    private static final float CAMERA_ZOOM = 15f;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String LOCATION_CLASS = "location_class";

    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String LATITUDE_KEY ="latitude";
    private static final String LANGITUDE_KEY ="longitude";
    private static final String PHRMACY_KEY ="phrmacy";
    private static final String QUANTITY_KEY = "quantity";
    private final static String NAME_KEY="NAME";
    private static final String LIST_OBJECTS = "list";
    private static final String MEDICINE_KEY = "medicine";


    private String medicineName;
    private String medicineQuantity;
    private int quatity;

    ArrayList<PharmacyInfo> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutom_map_location);
        list = new ArrayList<>();
        Intent intent = getIntent();
        list = intent.getParcelableArrayListExtra(LIST_OBJECTS);
        quatity = intent.getIntExtra(QUANTITY_KEY,0);
        medicineName = intent.getStringExtra(MEDICINE_KEY);

        Log.d(TAG, "onCreate: Cutomeeeee: List : "+list.size());


        if (googlePlayServiceAvaliable()) {
            Toast.makeText(this, "Perfect", Toast.LENGTH_SHORT).show();
            getLocationPermission();
        } else {
            // no Google maps layout
        }

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_location);
        //mapFragment.getMapAsync(this);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(CutomMapLocation.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(CutomMapLocation.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);

                for(int i=0;i<list.size();i++){
                    PharmacyInfo pharmacyInfo = list.get(i);
                    String lat = pharmacyInfo.getPharmacyLat();
                    String lng = pharmacyInfo.getPharmacyLan();
                    String name = pharmacyInfo.getPharmacyName();
                    final String contact = pharmacyInfo.getPharmacyPhone();
                    Log.d(TAG, "onMapReady: lat: "+lat+" lng: "+lng);
                    LatLng latLng = new LatLng(Double.valueOf(lat),Double.valueOf(lng));
                    MarkerOptions options = new MarkerOptions();
                    options.position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mGoogleMap.addMarker(options);
                    mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            LinearLayout info = new LinearLayout(CutomMapLocation.this);
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(CutomMapLocation.this);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(CutomMapLocation.this);
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(contact);

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });
                }

                mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String phramcyName = marker.getTitle();
                        Intent intent = new Intent(CutomMapLocation.this, ReservationActivity.class);
                        intent.putExtra(PHRMACY_KEY,phramcyName);
                        intent.putExtra(QUANTITY_KEY,quatity);
                        intent.putExtra(MEDICINE_KEY,medicineName);
                        startActivity(intent);
                    }
                });

            }
        });
    }
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
                            Toast.makeText(CutomMapLocation.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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
