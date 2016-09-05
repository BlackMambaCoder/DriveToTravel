package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;

/**
 * Created by LEO on 11.4.2016..
 */
public class MyConverter
{
    public static String DATE_COMPLEX_FORMAT = "E MMM dd HH:mm:ss Z yyyy";

    public static Date _String2Date(String dateArg)
    {
        /**
         * dateArg = "2-8-2016 17:35"
         * dateArg = "Fri Sep 02 21:00:56 CEST 2016"
         *
         * format = "E M d H:m:s z y"
         */
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("d-M-y H:m", Locale.ENGLISH);

        Date retValue = new Date();

        try
        {
            retValue = simpleDateFormat.parse(dateArg);
        }
        catch (ParseException e)
        {
            Log.e("Error _String2Date: ", e.getMessage());
            retValue = null;
        }

        return retValue;
    }

    public static Date _ComplexString2Date(String dateArg)
    {
        /**
         * dateArg = "2-8-2016 17:35"
         * dateArg = "Fri Sep 02 21:00:56 CEST 2016"
         *
         * format = "E M d H:m:s z y"
         */
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
//        SimpleDateFormat simpleDateFormat =
//                new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        Date retValue ;//= new Date();

        try
        {
            retValue = simpleDateFormat.parse(dateArg);
        }
        catch (ParseException e)
        {
            Log.e("Error _String2Date: ", e.getMessage());
            retValue = null;
        }

        return retValue;
    }

    public static String _Date2String(Date dateArg)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dateArg.getDay());
        stringBuilder.append("-");
        stringBuilder.append(dateArg.getMonth());
        stringBuilder.append("-");
        stringBuilder.append(dateArg.getYear());
        stringBuilder.append(" ");
        stringBuilder.append(dateArg.getHours());
        stringBuilder.append(":");
        stringBuilder.append(dateArg.getMinutes());

        return stringBuilder.toString();
    }

    public static String _StringList2String (List<String> stringListArg)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element :
                stringListArg) {
            stringBuilder.append(element + ",");
        }

        if (stringBuilder.length() > 0)
        {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    public static List<String> _String2StringList(String stringArg)
    {
        String[] stringsForList = stringArg.split(",");
        List<String> retValue = new ArrayList<>();

        for (String element :
                stringsForList) {
            retValue.add(element);
        }

        return retValue;
    }

    public static List<Double> JSONString2DoubleValueList(String stringArg)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(stringArg);
            List<Double> retValue = new ArrayList<>();

            double tourRank = jsonObject.getDouble("tourrank");
            double driverRank = jsonObject.getDouble("driverrank");

            retValue.add(tourRank);
            retValue.add(driverRank);

            return retValue;
        }
        catch (JSONException e)
        {
            String successMessage = "MyConverter::JSONString2DoubleValueList: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }
}
