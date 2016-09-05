package rs.elfak.mosis.drivetotravel.drivetotravel1.Services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
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
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LocListener;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.SendDeviceLocationDataAsyncTask;

/**
 * Created by Alexa on 8/31/2016.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    private String responseLocations;
    private int userId;

    private boolean startSettings=false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000*10; //10s

    // Declaring a Location Manager
    protected LocationManager locationManager;

    // Broadcast receiver for location update
    LocalBroadcastManager broadcaster;

    static final public String GPS_RESULT = "GPSTRACKER_REQUEST_PROCESSED";
    static final public String GPS_MESSAGE = "GPSTRACKER_GPS_MSG";

    public GPSTracker(Context context,int userID)
        {
        this.mContext = context;
        this.userId = userID;
        broadcaster = LocalBroadcastManager.getInstance(mContext);
        initLocationProvider();
    }

    public boolean initLocationProvider()
    {
        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //No permission
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //ActivityCompat.requestPermissions(this,new String[]{"Location"},0);
                Toast.makeText(mContext,"Please enable location permission!",Toast.LENGTH_SHORT).show();
                return false;
            }

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(mContext, "Not found any location provider, please turn on GPS", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(mContext, "Location update activated", Toast.LENGTH_LONG).show();
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

        if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //  ActivityCompat.requestPermissions(this,new String[]{"Location"},0);

                Toast.makeText(mContext,"Please grant location permission",Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(mContext, "Location update deactivated", Toast.LENGTH_LONG).show();
            locationManager.removeUpdates(GPSTracker.this);
            this.canGetLocation=false;
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
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
    }


    /* LOCATION CHANGED */
    @Override
    public void onLocationChanged(Location location)
    {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        //Send broadcast message to other activities

        String locationStr=String.valueOf(latitude)+","+String.valueOf(longitude);
        sendLocationUpdate(locationStr);
        //sendLocationToServer();
    }

    /* WHEN USER DISABLE INTERNET OR GPS */
    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(mContext,"Location provider: "+provider+" disabled",Toast.LENGTH_SHORT).show();
    }

    /* WHEN USER ENABLE INTERNET OR GPS */

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(mContext,"Location provider: "+provider+" enabled",Toast.LENGTH_SHORT).show();
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

        Log.d("[GPS]","Location update: "+message);

        if(message != null)
            intent.putExtra(GPS_MESSAGE, message);

        broadcaster.sendBroadcast(intent);
    }

    private double[] getDeviceLocation()
    {
        double[] retValue = new double[4];

        retValue[0]                         = LocListener.getLat();
        retValue[1]                         = LocListener.getLon();
        retValue[2]                         = LocListener.getAlt();
        retValue[3]                         = LocListener.getSpeed();

        return retValue;
    }

    /* SEND UPDATE TO SERVER */

    private void sendLocationToServer()
    {
        double[] sendLocData = this.getDeviceLocation();
        SendDeviceLocationDataAsyncTask task
                = new SendDeviceLocationDataAsyncTask();
        try
        {
            task.execute(this.locationToJSONObject(sendLocData).toString()).get();
            this.responseLocations = task.getJSONUsersLocationArray();

            List<JSONObject> users = User.JSONArrayToJSONObject(this.responseLocations);
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
        }
    }

    private JSONObject locationToJSONObject(double[] locationParam)
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

}