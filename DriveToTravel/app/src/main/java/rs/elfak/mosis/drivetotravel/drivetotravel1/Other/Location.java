package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LEO on 15.8.2016..
 */
public class Location {
    private double longitude;
    private double latitude;
    private double altitude;
    private double speed;

    public Location (double lon, double lat, double alt, double speed)
    {
        this.longitude                      =   lon;
        this.latitude                       =   lat;
        this.altitude                       =   alt;
        this.speed                          = speed;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public double getAltitude()
    {
        return this.altitude;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    public static ArrayList<Location> getLocationsFromJSONArray (JSONArray array)
    {
        ArrayList<Location> retValue = new ArrayList<>();

        for (int i = 0; i < array.length(); i++)
        {
            JSONObject obj = null;
            try {
                obj = array.getJSONObject(i);

                double lon          = obj.getDouble("longitude");
                double lat          = obj.getDouble("latitude");
                double alt          = obj.getDouble("altitude");
                double speed        = obj.getDouble("speed");

                Location loc        = new Location(
                        lon,
                        lat,
                        alt,
                        speed
                );

                retValue.add(loc);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return retValue;
    }
}
