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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private boolean firstLocation=false;

    private Marker[] friendMarkers;
    private LatLng[] friendLocations;
    private  boolean showFriends=true;

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

        mGPS = new GPSTracker();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

//                String s = intent.getStringExtra("GPSTRACKER_GPS_MSG");
                String friends = intent.getStringExtra(GPSTracker.FRIEND_MESSAGE);

//                Log.d("[MAP]", "Location update: " + s);

                //Update UI
//                String[] data = s.split(",");
                JSONArray userLocations;

//                try
//                {
//                    userLocations = new JSONArray(s);
//                }
//                catch (JSONException e)
//                {
//                    userLocations = null;
//                    e.printStackTrace();
//                }

                if (firstLocation)
                {

                }

                LatLng myPos;

                if (mapIsReady)
                {
//                    myLocation = new LatLng(Double.valueOf(data[0]), Double.valueOf(data[1]));
//                    myPositionMarker.setPosition(myLocation);

                    if(!firstLocation)
                    {
                        initFriends(friends);

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(myLocation)
                                .zoom(14)
                                .build();

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        firstLocation=true;
                    }
                    else
                    {
                        updateFriends(friends);
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


        try
        {
            initUserMarker(new LatLng(0,0));
            mapIsReady=true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            mapIsReady = false;
        }
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

            case R.id.map_show_friends_btn:

                showFriends=!showFriends;

                if(friendMarkers != null && friendMarkers.length>0) {
                    for (Marker m : friendMarkers) {
                        m.setVisible(showFriends);
                    }

                    Toast.makeText(this,"Show friends: "+showFriends,Toast.LENGTH_SHORT).show();
                }

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

    private void initUserMarker(LatLng loc) throws JSONException
    {
//        String accUsername = userPassanger.getUsername();
//        String firstName = userPassanger.getName();
//        String lastName = userPassanger.getSurname();
//        String phone = userPassanger.getPhoneNumber();
//        String userType;
        Bitmap profile_img = userPassanger.getProfileImage();

//        if(userPassanger.getUserType() == User.USER_TYPE_PASSENGER)
//        {
//            userType = "Passenger";
//        }
//        else
//        {
//            userType = "Driver";
//        }

//        String data = firstName+" "+lastName+","+phone+","+userType;

        JSONObject userPassengerJSONObject = userPassanger.toJSONObject();

        myLocation = new LatLng(loc.latitude,loc.longitude);
        myPositionMarker = mMap.addMarker(new MarkerOptions().position(loc).title(userPassanger.getUsername()).snippet(userPassengerJSONObject.toString()));
        myPositionMarker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcon(profile_img,140,180)));
    }

    private void initFriends(String userLocationsString)
    {
        JSONArray userLocations = null;
        try
        {
            userLocations = new JSONArray(userLocationsString);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            this.friendLocations = new LatLng[0];
            this.friendMarkers = new Marker[0];

            return;
        }

        this.friendLocations = new LatLng[userLocations.length() - 2];
        this.friendMarkers = new Marker[userLocations.length() - 2];

        for (int i = 0; i < userLocations.length() - 2; i++)
        {
            double lat = 0.0;
            double lng = 0.0;

            Marker marker;
            LatLng friendLoc;

            try
            {
                lat = userLocations.getJSONObject(i).getJSONObject("location").getDouble("latitude");
                lng = userLocations.getJSONObject(i).getJSONObject("location").getDouble("longitude");

                friendLoc = new LatLng(lat, lng);

                marker = mMap.addMarker(new MarkerOptions().position(friendLoc).title("").snippet(userLocations.getJSONObject(i).toString()));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                continue;
            }

            Bitmap bitmap;
            try
            {
                bitmap = User.stringToBitmap(userLocations.getJSONObject(i).getString("profileimage"));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                bitmap = resizeMapIcon(R.drawable.profile, 140, 180);
            }

            marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcon(bitmap, 140, 180)));

            this.friendMarkers[i] = marker;
            this.friendLocations[i] = friendLoc;
        }
    }

    private void updateFriends(String userLocationsString)
    {
        JSONArray userLocations = null;
        try
        {
            userLocations = new JSONArray(userLocationsString);
        }
        catch (JSONException e)
        {
            e.printStackTrace();

            this.friendLocations = new LatLng[0];
            this.friendMarkers = new Marker[0];

            return;
        }

        for (int i = 0; i < userLocations.length() - 2; i++)
        {
            double lat = 0.0;
            double lng = 0.0;

            try
            {
                lat = userLocations.getJSONObject(i).getJSONObject("location").getDouble("latitude");
                lng = userLocations.getJSONObject(i).getJSONObject("location").getDouble("longitude");

                friendLocations[i] = new LatLng(lat,lng);
                friendMarkers[i].setPosition(friendLocations[i]);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                continue;
            }
        }

        try
        {
            double myLat = userLocations.getDouble(userLocations.length() - 1);
            double myLng = userLocations.getDouble(userLocations.length() - 2);

            this.myLocation = new LatLng(myLat, myLng);
            this.myPositionMarker.setPosition(myLocation);

            double distance = GPSTracker.distanceBetween(myLocation,friendLocations[0]);
            Log.d("[DISTANCE]",String.valueOf(distance));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
