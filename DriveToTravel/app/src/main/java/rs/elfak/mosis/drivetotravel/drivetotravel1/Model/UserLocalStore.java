package rs.elfak.mosis.drivetotravel.drivetotravel1.Model;

import android.content.Context;
import android.content.SharedPreferences;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
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

    public String getTypeOfLoggedUser()
    {
        return this.userLocalSharedPrefferences.getString(UserStaticAttributes._userType,"");
    }

    public void clearUserData()
    {
        this.sharedPreferencesEditor.clear();
        this.sharedPreferencesEditor.commit();
    }
}
