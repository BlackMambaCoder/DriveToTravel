package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;

/**
 * Created by LEO on 13.8.2016..
 */
public class SendDeviceLocationDataAsyncTask extends AsyncTask<String, Void, Void>
{
    public static String successMessage = "OK";

    private String JSONUsersLocationArray = null;

    public String getJSONUsersLocationArray()
    {
        return this.JSONUsersLocationArray;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        String postValue        = params[0];
        String successMessage;
        String routeUrl         = ServerStaticAttributes._SERVER_ROOT_URL +
                ServerStaticAttributes.UPDATE_USER_LOCATION;
        try
        {
            URL url                 = new URL(routeUrl);

            HttpURLConnection httpURLConnection
                                    = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(ServerStaticAttributes._readTimeOut);
            httpURLConnection.setConnectTimeout(ServerStaticAttributes._connectTimeOut);
            httpURLConnection.setRequestMethod(ServerStaticAttributes._requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);//, "UTF-8");

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(postValue);
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

        }
        catch (MalformedURLException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
        }
        catch (ProtocolException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
        }
        catch (IOException e)
        {
            successMessage = "SendDeviceLocationDataAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
        }
        return null;
    }
}
