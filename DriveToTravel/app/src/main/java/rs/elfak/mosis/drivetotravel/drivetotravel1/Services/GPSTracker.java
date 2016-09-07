package rs.elfak.mosis.drivetotravel.drivetotravel1.Services;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Activities.tourMap;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LocListener;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.SendDeviceLocationDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by Alexa on 8/31/2016.
 */
public class GPSTracker extends Service implements LocationListener {

    private Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    boolean locationProviderIsWorking = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    private String responseLocations;
    private int userId;

    private boolean startSettings=false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 5 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000*5; //5s

    // Declaring a Location Manager
    protected LocationManager locationManager;

    // Broadcast receiver for location update
    LocalBroadcastManager broadcaster;

    static final public String GPS_RESULT = "GPSTRACKER_REQUEST_PROCESSED";
    static final public String GPS_MESSAGE = "GPSTRACKER_GPS_MSG";
    static final public String FRIEND_MESSAGE = "GPS_TRACKER_FRIEND_MESSAGE";

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    int mId=0;

    public void initTracker(int userID)
    {
        this.userId = userID;
    }

    public GPSTracker()
    {

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.mContext = this;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent!=null) {
            Bundle extras = intent.getExtras();
            this.userId = extras.getInt("userid");
        }

        buildNotifications();

        Log.d("[SERVIS]", "Servis je pokrenut");

        if (!this.locationProviderIsWorking)
        {
            this.initLocationProvider();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUsingGPS();
    }


    /**
     * Check if location provider (Network or GPS) is activated and get lcoation;
     *
     * @return
     */
    public boolean initLocationProvider()
    {
        Log.d("[SERVIS]", "Inicijalizacija location provajdera");

        try {

            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            //No permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //ActivityCompat.requestPermissions(this,new String[]{"Location"},0);
                Toast.makeText(this,"Please enable location permission!",Toast.LENGTH_SHORT).show();
                return false;
            }

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(this, "Not found any location provider, please turn on GPS", Toast.LENGTH_SHORT).show();
                canGetLocation=false;
                return false;
            }

            this.canGetLocation = true;

             // First try get location from network Provider
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                Log.d("[GPS Service]", "Network ");

                //Get last known location
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }

                Toast.makeText(this, "Location update activated", Toast.LENGTH_LONG).show();
                return true;
            }

            //Try to get location from GPS provider
            if (isGPSEnabled)
            {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                Log.d("[GPS Service]", "GPS Enabled");

                //Get last known location
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }

                this.locationProviderIsWorking = true;
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Location getLocation()
    {
            return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS() {

        if (locationManager != null)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //  ActivityCompat.requestPermissions(this,new String[]{"Location"},0);

                Toast.makeText(this,"Please grant location permission",Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Location update deactivated", Toast.LENGTH_LONG).show();
            Log.d("[SERVIS]","Stopiran GPS servis");
            locationManager.removeUpdates(GPSTracker.this);
            this.canGetLocation=false;
            locationProviderIsWorking=false;
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude()

    {
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
       /*
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                this.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
        */
    }


    /* LOCATION CHANGED */
    @Override
    public void onLocationChanged(Location location)
    {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        //Send broadcast message to other activities
        //String locationStr=String.valueOf(latitude)+","+String.valueOf(longitude);

//        sendLocationUpdate(locationStr);
        sendLocationToServer();
    }

    /* WHEN USER DISABLE INTERNET OR GPS */
    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this,"Location provider: "+provider+" disabled",Toast.LENGTH_SHORT).show();
    }

    /* WHEN USER ENABLE INTERNET OR GPS */

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this,"Location provider: "+provider+" enabled",Toast.LENGTH_SHORT).show();
    }

     /* WHEN USER DISABLE/ENABLE INTERNET OR GPS */

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /* SEND UPDATE VIA BROADCAST */

    private void sendLocationUpdate(String message) {

        Intent intent = new Intent(GPS_RESULT);

        Log.d("[GPS]","Location update");

        if(message != null)
            intent.putExtra(GPS_MESSAGE, message);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendFriendLocation(String message) {

        Intent intent = new Intent(GPS_RESULT);

        Log.d("[GPS]","Location update");

        if(message != null)
            intent.putExtra(FRIEND_MESSAGE, message);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private String[] getDeviceLocation()
    {
        String[] retValue = new String[4];

        retValue[0] = String.valueOf(longitude);
        retValue[1] = String.valueOf(latitude);
        retValue[2] = String.valueOf(0);
        retValue[3]  = String.valueOf(0);

        Log.d("[GPS]","Sending Latitude: "+retValue[0]+" Longitude: "+retValue[1]);

        return retValue;
    }

    /* SEND UPDATE TO SERVER */

    private void sendLocationToServer()
    {
        String[] sendLocData = this.getDeviceLocation();

        SendDeviceLocationDataAsyncTask task = new SendDeviceLocationDataAsyncTask();

        try
        {
            JSONObject requestDataJsonObject = this.locationToJSONObject(sendLocData);
            requestDataJsonObject.put("userid", this.userId);

            task.execute(requestDataJsonObject.toString()).get();
            this.responseLocations = task.getJSONUsersLocationArray();
            JSONArray jsonArray = new JSONArray();
            JSONArray newJsonArrayFriends = new JSONArray();

            if (responseLocations != null)
            {
                jsonArray = new JSONArray(this.responseLocations);

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject friend = jsonArray.getJSONObject(i);
                    JSONObject newFriend = friend.getJSONObject("meta_data");

                    newFriend.put(UserStaticAttributes._username, friend.get(UserStaticAttributes._username));
                    newFriend.put("location", friend.getJSONObject("location"));
                    newJsonArrayFriends.put(newFriend);
                }
            }

            newJsonArrayFriends.put(longitude);
            newJsonArrayFriends.put(latitude);

//            List<JSONObject> users = User.JSONArrayToJSONObject(this.responseLocations);

            if (this.responseLocations != null)
            {
                this.sendFriendLocation(newJsonArrayFriends.toString());
            }
        }
        catch (InterruptedException e)
        {
            String successMessage = "SendLocationThread::sendLocationToServer: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseLocations  = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "SendLocationThread::sendLocationToServer: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseLocations  = null;
        } catch (JSONException e)
        {
            String successMessage = "SendLocationThread::sendLocationToServer: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseLocations  = null;
        }
    }

    private JSONObject locationToJSONObject(String[] locationParam)
    {
        JSONObject retValue = new JSONObject();

        try
        {
            retValue.put("latitude", locationParam[0]);
            retValue.put("longitude", locationParam[1]);
            retValue.put("altitude", locationParam[2]);
            retValue.put("speed", locationParam[3]);
            retValue.put("userid", this.userId);
        }
        catch (JSONException e)
        {
            String successMessage = "SendLocationThread::locationToJSONObject: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }

        return retValue;
    }

    /* Distance in km */
    public static double distanceBetween(LatLng loc1,LatLng loc2)
    {
        double lat1 = loc1.latitude * Math.PI / 180.0;
        double lon1 = loc1.longitude * Math.PI / 180.0;
        double lat2 = loc2.latitude * Math.PI / 180.0;
        double lon2 = loc2.longitude * Math.PI / 180.0;

        double theta = lon1 - lon2;
        double dist = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(theta);
        dist = Math.acos(dist);
        dist = dist* 180.0 / Math.PI;
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private void sendInitUserLocation()
    {
        JSONArray newJsonArrayFriends = new JSONArray();

        try {
            newJsonArrayFriends.put(longitude);
            newJsonArrayFriends.put(latitude);
            this.sendFriendLocation(newJsonArrayFriends.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void buildNotifications()
    {
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Drive2Travle - Friend is nearby")
                        .setContentText("Your friend is nearby, check map");

        Intent resultIntent = new Intent(this, tourMap.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(tourMap.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void showNotification()
    {
      mNotificationManager.notify(mId, mBuilder.build());
    }

}