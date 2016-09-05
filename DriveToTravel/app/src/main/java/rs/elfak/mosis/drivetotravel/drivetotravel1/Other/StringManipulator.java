package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEO on 23.3.2016..
 */
public class StringManipulator {

    public static String inputStreamToString(InputStream inputStream)
    {
        String line = "";
        StringBuilder total = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while ((line = bufferedReader.readLine()) != null)
            {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            total = null;
        }

        return total != null ? total.toString() : null;
    }

    public static List<String> jsonArrayToStringList (String inputString)
    {
        List<String> returnArray = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(inputString);
            JSONArray jsonArray = jsonObject.getJSONArray("passengers");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                returnArray.add(jsonArray.getString(i));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return returnArray;
    }

    public static JSONArray stringToJSONArray (String inputString)
    {
        JSONObject object;
        JSONArray array;

        try
        {
            object = new JSONObject(inputString);
            array = object.getJSONArray("locations");
        }
        catch (JSONException e)
        {
            array = null;
        }

        return  array;
    }

    public static JSONObject stringToJSONObject (String inputStringParam) throws JSONException
    {
        return new JSONObject(inputStringParam);
    }
}
