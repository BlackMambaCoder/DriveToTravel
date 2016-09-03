package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.CustomInfoWindowAdapter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Services.GPSTracker;

public class tourMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker mGPS;
    private BroadcastReceiver receiver;
    private Marker myPositionMarker;
    private LatLng myLocation;
    private boolean mapIsReady = false;
    private boolean lockTaxiPosition=false;
    private String uid;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        userLocalStore = new UserLocalStore(this);

        mGPS = new GPSTracker(this,userLocalStore.getPassenger().getId());

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String s = intent.getStringExtra("GPSTRACKER_GPS_MSG");

                Log.d("[MAP]", "Location update: " + s);

                //Update UI
                String[] data = s.split(",");

                LatLng myPos;

                if (mapIsReady) {
                    myLocation = new LatLng(Double.valueOf(data[0]), Double.valueOf(data[1]));
                    myPositionMarker.setPosition(myLocation);

                    if(lockTaxiPosition) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    }
                }
            }
        };
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
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

        if (mGPS.canGetLocation()) {
            myLocation = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
        } else {
            myLocation = new LatLng(0, 0);
            finish();
        }

        //mMap.setMyLocationEnabled(true);

        String accUsername = userLocalStore.getPassenger().getUsername();
        String firstName = userLocalStore.getPassenger().getName();
        String lastName = userLocalStore.getPassenger().getSurname();

        String phone = userLocalStore.getPassenger().getPhoneNumber();
        String userType = "Passenger";

        String data = firstName+","+lastName+","+phone+","+userType;

        myPositionMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title(accUsername).snippet(data));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

        mapIsReady=true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("GPSTRACKER_REQUEST_PROCESSED")
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_map_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.map_taxi_lock_btn:
                lockTaxiPosition = !lockTaxiPosition;
                Toast.makeText(tourMap.this,"Tracking taxi: "+lockTaxiPosition,Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
