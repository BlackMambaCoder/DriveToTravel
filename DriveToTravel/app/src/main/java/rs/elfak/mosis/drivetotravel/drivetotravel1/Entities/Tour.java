package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.MyConverter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.TourStaticAttributes;

/**
 * Created by LEO on 4.4.2016..
 */
public class Tour implements Parcelable
{
    private int id;
    private String startLocation;
    private String destinationLocation;
    private Date startDateAndTime;
    private String tourDriverUsername;
    private List<String> passengers;
    private double rank;


    public Tour ()
    {
        this.id                         =    -1;
        this.startLocation              =    "";
        this.destinationLocation        =    "";
        this.startDateAndTime           =  null;
        this.tourDriverUsername         =    "";
        this.passengers                 =  new ArrayList<>();
        this.rank                       =  -1.0;
    }

    public Tour (
            String startArg,
            String destArg,
            String startDateArg,
            String startTimeArg,
            String tourDriverUsername,
            int tourID
    )
    {
        this.startLocation              = startArg;
        this.destinationLocation        = destArg;

        Date date                       = new Date();
        DateFormat dateFormat           = new SimpleDateFormat("d-M-yyyy-hh:mm", Locale.ENGLISH);

        try
        {
            date                            = dateFormat.parse(startDateArg + "-" + startTimeArg);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        this.startDateAndTime           = date;

        this.tourDriverUsername         = "";
        this.passengers                 = new ArrayList<>();
        this.rank                       = -1.0;
        this.id                         = tourID;
    }

    public JSONObject toJSONObject()
    {
        try
        {
            JSONObject retValue = new JSONObject();

            retValue.put(TourStaticAttributes._ID, this.id);
            retValue.put(TourStaticAttributes._STARTLOCATION, this.startLocation);
            retValue.put(TourStaticAttributes._DESTINATIONLOCATION, this.destinationLocation);
            retValue.put(TourStaticAttributes._STARTDATE_AND_TIME, this.startDateAndTime);
            retValue.put(TourStaticAttributes._TOUR_DRIVER, this.tourDriverUsername);
            retValue.put(TourStaticAttributes._RANK, this.tourDriverUsername);

            JSONArray passengers = new JSONArray();

            for (String passengersUsername :
                    this.passengers) {
                passengers.put(passengersUsername);
            }

            retValue.put(TourStaticAttributes._PASSENGERS, passengers);

            return retValue;
        }
        catch (JSONException e)
        {
            Log.e("error", e.getMessage());
            return null;
        }
    }

    // === GETTER === //
    public int getId()
    {
        return this.id;
    }

    public String getStartLocation()
    {
        return this.startLocation;
    }

    public String getDestinationLocation()
    {
        return this.destinationLocation;
    }

    public Date getStartDate()
    {
        return this.startDateAndTime;
    }

    public String getTourDriver()
    {
        return this.tourDriverUsername;
    }

    public List<String> getPassengers()
    {
        return this.passengers;
    }

    public String getPassenger(int position)
    {
        return this.passengers.get(position);
    }

    public double getRank()
    {
        return this.rank;
    }

    // === SETTER === //
    public void setId(int parameter)
    {
        this.id = parameter;
    }

    public void setStartLocation(String parameter)
    {
        this.startLocation = parameter;
    }

    public void setDestinationLocation(String parameter)
    {
        this.destinationLocation = parameter;
    }

    public void setStartDateAndTime (Date parameter)
    {
        this.startDateAndTime = parameter;
    }

    public void setPassengers(List<String> parameter)
    {
        this.passengers = parameter;
    }

    public void addPassenger(String passenger)
    {
        this.passengers.add(passenger);
    }

    public void setDriver(String driverUsername)
    {
        this.tourDriverUsername = driverUsername;
    }

    public Double setRank(double rankParam, boolean update)
    {
        if (!update) {
            this.rank = rankParam;
            return null;
        }

        ServerRequest request = new ServerRequest();
        List<Double> ranks = request.updateTourRank(rankParam,this.id);

        if (ranks != null) {
            this.rank = ranks.get(0);
            return ranks.get(1);
        }

        return null;
    }

    // === STATIC METHODS === //

    public static ArrayList<Tour> getToursFromJsonArray(String jsonString)
    {
        ArrayList<Tour> tourArrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                Tour tour = new Tour();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                tour.setStartLocation(jsonObject.getString(TourStaticAttributes._STARTLOCATION));
                tour.setDestinationLocation(jsonObject.getString(TourStaticAttributes._DESTINATIONLOCATION));

                String dateFromJson = jsonObject.getString(TourStaticAttributes._STARTDATE_AND_TIME);
                Date date = MyConverter._String2Date(dateFromJson);
                tour.setStartDateAndTime(date);

                tour.setDriver(jsonObject.getString(TourStaticAttributes._TOUR_DRIVER));

                List<String> passengerList = StringManipulator.jsonArrayToStringList(
                        jsonObject.getString(TourStaticAttributes._PASSENGERS)
                );

                tour.setPassengers(passengerList);

                tourArrayList.add(tour);
            }
        }
        catch (JSONException ex)
        {
            Log.e("*****BREAK_POINT*****", "Tour getToursFromJSONArray: " + ex.getMessage());
            tourArrayList = null;
        }
//        catch (ParseException e)
//        {
//            e.printStackTrace();
//            tourArrayList = null;
//        }

        return tourArrayList;
    }

    public static Tour getTourFromJSONObject(JSONObject tourJsonObject)
    {
        if (tourJsonObject == null)
        {
            return null;
        }

        Tour retValue = new Tour();

        try {
            retValue.setId(tourJsonObject.getInt(TourStaticAttributes._ID));
            retValue.setStartLocation(tourJsonObject.getString(TourStaticAttributes._STARTLOCATION));
            retValue.setDestinationLocation(tourJsonObject.getString(TourStaticAttributes._DESTINATIONLOCATION));
            String dateString = tourJsonObject.getString(TourStaticAttributes._STARTDATE_AND_TIME);

            Date date = MyConverter._String2Date(dateString);

            if (date == null)
            {
                return null;
            }

            retValue.setStartDateAndTime(date);
            retValue.setDriver(tourJsonObject.getString(TourStaticAttributes._TOUR_DRIVER));
            retValue.setRank(tourJsonObject.getDouble(TourStaticAttributes._RANK), false);

            JSONArray passengers = tourJsonObject.getJSONArray(TourStaticAttributes._PASSENGERS);

            for (int i = 0; i < passengers.length(); i++)
            {
                retValue.passengers.add(passengers.getString(i));
            }

        }
        catch (JSONException e)
        {
            String successMessage = "Tour::getTourFromJSONObject : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }

        return retValue;
    }

    // === Methods for parsing (PARCELABLE) === //

    public Tour (Parcel in)
    {
        String[] data = new String[5];

        in.readStringArray(data);
        this.startLocation = data[0];
        this.destinationLocation = data[1];
        this.startDateAndTime = MyConverter._String2Date(data[2]);
        this.tourDriverUsername = data[3];
        this.passengers = MyConverter._String2StringList(data[4]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        String passangerString="/";

        if(this.passengers!=null) {
           passangerString  = MyConverter._StringList2String(this.passengers);
        }

        String[] data={this.startLocation,this.destinationLocation,this.startDateAndTime.toString(),this.tourDriverUsername,passangerString};

        dest.writeStringArray(data);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Tour createFromParcel(Parcel in)
        {
            return new Tour(in);
        }

        public Tour[] newArray(int size)
        {
            return new Tour[size];
        }
    };
}
