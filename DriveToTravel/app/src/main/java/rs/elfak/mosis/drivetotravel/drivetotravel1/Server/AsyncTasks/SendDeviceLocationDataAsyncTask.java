package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Exception.HttpConnectionException;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;

/**
 * Created by LEO on 13.8.2016..
 */
public class SendDeviceLocationDataAsyncTask extends AsyncTask<Double, Void, Void>
{
    public static String successMessage = "OK";

    private String JSONUsersLocationArray = null;

    public String getJSONUsersLocationArray()
    {
        return this.JSONUsersLocationArray;
    }

    @Override
    protected Void doInBackground(Double... params)
    {
        double lat                          = params[0];
        double lon                          = params[1];
        double alt                          = params[2];
        double speed                        = params[3];

        try
        {
            URL url = new URL("ipAddress");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(ServerStaticAttributes._readTimeOut);
            httpURLConnection.setConnectTimeout(ServerStaticAttributes._connectTimeOut);
            httpURLConnection.setRequestMethod(ServerStaticAttributes._requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            JSONArray sendData = new JSONArray();

            sendData.put(lat);
            sendData.put(lon);
            sendData.put(alt);
            sendData.put(speed);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);//, "UTF-8");

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(sendData.toString());
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                this.JSONUsersLocationArray = StringManipulator.inputStreamToString(
                        httpURLConnection.getInputStream()
                );
            }
            else
            {
                throw new HttpConnectionException("Dismissed connection: " + responseCode);
            }

        }
        catch (MalformedURLException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
        }
        catch (ProtocolException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
        }
        catch (HttpConnectionException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: HttpConnectionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
        }
        return null;
    }
}
