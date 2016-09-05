package rs.elfak.mosis.drivetotravel.drivetotravel1.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.util.List;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.MyConverter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.TourStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */
public class UserLocalStore {

    public static String SHAREDPREFERENCE_NAME = "userDetails";
    private SharedPreferences userLocalSharedPrefferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    public UserLocalStore (Context context)
    {
        this.userLocalSharedPrefferences = context.getSharedPreferences(SHAREDPREFERENCE_NAME, 0);
        this.sharedPreferencesEditor = this.userLocalSharedPrefferences.edit();
    }

    public void storeUser (Driver user)
    {
        this.sharedPreferencesEditor.putInt     (UserStaticAttributes._id,          user.getId());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._name,        user.getName());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._surname,     user.getSurname());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._username,    user.getUsername());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._password,    user.getPassword());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._eMail,       user.geteMail());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._phoneNumber, user.getPhoneNumber());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._carModel,    user.getCarModel());
        this.sharedPreferencesEditor.putInt     (UserStaticAttributes._userType,    User.USER_TYPE_DRIVER);
        this.sharedPreferencesEditor.putString  (UserStaticAttributes.PROFILE_IMAGE,user.getProfileImageString());

        this.sharedPreferencesEditor.commit();
    }

    public void storeUser (Passenger user)
    {
        this.sharedPreferencesEditor.putInt     (UserStaticAttributes._id,          user.getId());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._name,        user.getName());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._surname,     user.getSurname());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._username,    user.getUsername());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._password,    user.getPassword());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._eMail,       user.geteMail());
        this.sharedPreferencesEditor.putString  (UserStaticAttributes._phoneNumber, user.getPhoneNumber());
        this.sharedPreferencesEditor.putInt     (UserStaticAttributes._userType,    User.USER_TYPE_PASSENGER);
        this.sharedPreferencesEditor.putString  (UserStaticAttributes.PROFILE_IMAGE,user.getProfileImageString());

        this.sharedPreferencesEditor.commit();
    }

    public Driver getDriver ()
    {
        Driver user = new Driver();

        user.setId(this.userLocalSharedPrefferences.getInt(UserStaticAttributes._id, -1));
        user.setName(this.userLocalSharedPrefferences.getString(UserStaticAttributes._name, ""));
        user.setSurname(this.userLocalSharedPrefferences.getString(UserStaticAttributes._surname, ""));
        user.setUsername(this.userLocalSharedPrefferences.getString(UserStaticAttributes._username, ""));
        user.setPassword(this.userLocalSharedPrefferences.getString(UserStaticAttributes._password, ""));
        user.seteMail(this.userLocalSharedPrefferences.getString(UserStaticAttributes._eMail, ""));
        user.setPhoneNumber(this.userLocalSharedPrefferences.getString(UserStaticAttributes._phoneNumber, ""));
        user.setCarModel(this.userLocalSharedPrefferences.getString(UserStaticAttributes._carModel, ""));
        String img = this.userLocalSharedPrefferences.getString(UserStaticAttributes.PROFILE_IMAGE, null);

        if (img != null)
        {
            user.setProfileImage(User.stringToBitmap(img));
        }

        return user;
    }

    public Passenger getPassenger()
    {
        Passenger user = new Passenger();

        user.setId(this.userLocalSharedPrefferences.getInt(UserStaticAttributes._id, -1));
        user.setName(this.userLocalSharedPrefferences.getString(UserStaticAttributes._name, ""));
        user.setSurname(this.userLocalSharedPrefferences.getString(UserStaticAttributes._surname, ""));
        user.setUsername(this.userLocalSharedPrefferences.getString(UserStaticAttributes._username, ""));
        user.setPassword(this.userLocalSharedPrefferences.getString(UserStaticAttributes._password, ""));
        user.seteMail(this.userLocalSharedPrefferences.getString(UserStaticAttributes._eMail, ""));
        user.setPhoneNumber(this.userLocalSharedPrefferences.getString(UserStaticAttributes._phoneNumber, ""));
        String img = this.userLocalSharedPrefferences.getString(UserStaticAttributes.PROFILE_IMAGE, null);

        if (img != null)
        {
            user.setProfileImage(User.stringToBitmap(img));
        }

        return user;
    }

    public void setUserLoggedIn(boolean loggedIn)
    {
        this.sharedPreferencesEditor.putBoolean(UserStaticAttributes._loggedIn, loggedIn);
        this.sharedPreferencesEditor.commit();
    }

    public boolean getUserLoggedIn()
    {
//        boolean retValue = false;
//
//        if (this.userLocalSharedPrefferences.getBoolean(UserStaticAttributes._loggedIn, true))
//        {
//            retValue = true;
//        }
//
//        return retValue;

        return this.userLocalSharedPrefferences.getBoolean(UserStaticAttributes._loggedIn, true);
    }

    public int getTypeOfLoggedUser()
    {
        return this.userLocalSharedPrefferences.getInt(UserStaticAttributes._userType,-1);
    }

    public void clearUserData()
    {
        this.sharedPreferencesEditor.clear();
        this.sharedPreferencesEditor.commit();
    }

    public void storeTour(Tour tour)
    {
        this.sharedPreferencesEditor.putInt(TourStaticAttributes._ID, tour.getId());
        this.sharedPreferencesEditor.putString(TourStaticAttributes._STARTLOCATION, tour.getStartLocation());
        this.sharedPreferencesEditor.putString(TourStaticAttributes._DESTINATIONLOCATION, tour.getDestinationLocation());

//        String date = MyConverter._Date2String(tour.getStartDate());
        this.sharedPreferencesEditor.putString(TourStaticAttributes._STARTDATE_AND_TIME, tour.getStartDate().toString());
        this.sharedPreferencesEditor.putInt(TourStaticAttributes._TOUR_DRIVER, tour.getTourDriver());

        String passengers = MyConverter._StringList2String(tour.getPassengers());

        this.sharedPreferencesEditor.putString(TourStaticAttributes._PASSENGERS, passengers);
        this.sharedPreferencesEditor.putString(TourStaticAttributes._RANK, Double.toString(tour.getRank()));
        this.sharedPreferencesEditor.putString(TourStaticAttributes.DRIVER_USER_NAME, tour.getDriverUsername());
        this.sharedPreferencesEditor.putString(TourStaticAttributes.DRIVER_RANK, Double.toString(tour.getDriverRank()));

        this.sharedPreferencesEditor.commit();
    }

    public Tour getTour()
    {
        Tour tour = new Tour();

        tour.setId(this.userLocalSharedPrefferences.getInt(TourStaticAttributes._ID, -1));
        tour.setStartLocation(this.userLocalSharedPrefferences.getString(TourStaticAttributes._STARTLOCATION, ""));
        tour.setDestinationLocation(this.userLocalSharedPrefferences.getString(TourStaticAttributes._DESTINATIONLOCATION, ""));
        Date date = MyConverter._ComplexString2Date(
                this.userLocalSharedPrefferences.getString(TourStaticAttributes._STARTDATE_AND_TIME, "")
        );
        tour.setStartDateAndTime(date);
        tour.setDriver(this.userLocalSharedPrefferences.getInt(TourStaticAttributes._TOUR_DRIVER, -1));

        List<String> passengers =
                MyConverter._String2StringList(
                        this.userLocalSharedPrefferences.getString(TourStaticAttributes._PASSENGERS, "")
                );
        tour.setPassengers(passengers);

        String rankStr = this.userLocalSharedPrefferences.getString(TourStaticAttributes._RANK, Double.toString(-1.0));
        tour.setRank(Double.valueOf(rankStr), false);

        tour.setDriverUsername(this.userLocalSharedPrefferences.getString(TourStaticAttributes.DRIVER_USER_NAME, ""));
        String driverRank = this.userLocalSharedPrefferences.getString(TourStaticAttributes.DRIVER_RANK, Double.toString(-1.0));
        tour.setDriverRank(Double.valueOf(driverRank));

        return tour;
    }
}
