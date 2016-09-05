package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
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
    private boolean lockTaxiPosition = false;
    private String uid;
    private UserLocalStore userLocalStore;
    private Passenger userPassanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        userLocalStore = new UserLocalStore(this);
        userPassanger = userLocalStore.getPassenger();

        mGPS = new GPSTracker(this, userLocalStore.getPassenger().getId());

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

                    if (lockTaxiPosition) {
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Please enable location permission!",Toast.LENGTH_SHORT).show();
            finish();
        }

        if (mGPS.canGetLocation())
        {
            myLocation = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
        }

        //Check location
        if(myLocation != null)
        {
            mMap.setMyLocationEnabled(true);

            String accUsername = userPassanger.getUsername();
            String firstName = userPassanger.getName();
            String lastName = userPassanger.getSurname();
            String phone = userPassanger.getPhoneNumber();
            String userType;
            Bitmap profile_img = userPassanger.getProfileImage();

            if(userPassanger.getUserType() == User.USER_TYPE_PASSENGER) {
                userType = "Passenger";
            }
            else
            {
                userType = "Driver";
            }

            String data = firstName+" "+lastName+","+phone+","+userType;

            myPositionMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title(accUsername).snippet(data));
            myPositionMarker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcon(profile_img,140,162)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));
        }

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
                Toast.makeText(tourMap.this,"Tracking driver: "+lockTaxiPosition,Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap resizeMapIcon(int drawableIcon, int width, int height)
    {
        Bitmap imageBitmap = BitmapFactory.decodeResource(this.getResources(),drawableIcon);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private Bitmap resizeMapIcon(Bitmap image,int width, int height){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        return resizedBitmap;
    }

}
