package rs.elfak.mosis.drivetotravel.drivetotravel1.Threads;

import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Activities.PassangerMainActivity;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LocListener;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.SendDeviceLocationDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;

/**
 * Created by LEO on 14.8.2016..
 */
public class SendLocationThread extends Thread
{
    private String responseLocations;
    private int userId;
    private String JSONUsersLocationArray = null;

    public static boolean runThread = true;

    public SendLocationThread (int userIdParam)
    {
        this.userId                     = userIdParam;
    }

    @Override
    public void run()
    {
        while (runThread)
        {
            this.sendLocationToServer();

            synchronized (this)
            {
                try
                {
                    wait(500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    break;
                }
            }

            Message message = new Message();
            message.obj = this.responseLocations;

            // if  responseLocations is empty array, jump out of loop

            PassangerMainActivity.publicHandler.sendMessage(message);
        }
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

    private void sendLocationToServer()
    {
        double[] sendLocData                = this.getDeviceLocation();
        SendDeviceLocationDataAsyncTask task
                                            = new SendDeviceLocationDataAsyncTask();
        try
        {
            task.execute(this.locationToJSONObject(sendLocData).toString()).get();
            this.responseLocations              = task.getJSONUsersLocationArray();
        }
        catch (InterruptedException e)
        {
            String successMessage = "SendLocationThread::sendLocationToServer: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseLocations              = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "SendLocationThread::sendLocationToServer: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseLocations              = null;
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

    private void sendLocationServerRequest(String...params)
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
//        return null;
    }
}
