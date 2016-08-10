package rs.elfak.mosis.drivetotravel.drivetotravel1.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */

public class Driver extends User
{
    private String carModel = "";

//    private static Driver instance = null;
//
//    protected Driver() {}
//
//    public static Driver getInstance()
//    {
//        if (instance == null)
//        {
//            instance = new Driver();
//        }
//
//        return instance;
//    }
//
//    public static void setInstanceNull()
//    {
//        if (instance != null)
//        {
//            instance = null;
//        }
//    }

    // === GETTER === //

    public Driver()
    {

    }

    public Driver (User user)
    {
        this.id = user.id;
        this.name = user.name;
        this.surname = user.surname;
        this.username = user.username;
        this.password = user.password;
        this.eMail = user.eMail;
        this.phoneNumber = user.phoneNumber;
    }

    public String getCarModel()
    {
        return this.carModel;
    }

    // === SETTER === //

    public void setCarModel (String carModel)
    {
        this.carModel = carModel;
    }

//    public static Driver getDriverFromJSONString (String jsonString)
//    {
//        Driver returnValue = new Driver();
//        try
//        {
//            JSONObject jsonObject = new JSONObject(jsonString);
//
//            returnValue.setId(
//                    jsonObject.getLong("_id"));
//            returnValue.setName(
//                    jsonObject.getString(UserStaticAttributes._name));
//            returnValue.setSurname(
//                    jsonObject.getString(UserStaticAttributes._surname));
//            returnValue.setUsername(
//                    jsonObject.getString(UserStaticAttributes._username));
//            returnValue.setName(
//                    jsonObject.getString(UserStaticAttributes._name));
//            returnValue.setName(
//                    jsonObject.getString(UserStaticAttributes._name));
//            returnValue.setName(
//                    jsonObject.getString(UserStaticAttributes._name));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
