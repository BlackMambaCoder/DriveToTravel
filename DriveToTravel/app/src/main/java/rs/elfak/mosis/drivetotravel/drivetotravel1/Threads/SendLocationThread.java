package rs.elfak.mosis.drivetotravel.drivetotravel1.Threads;

import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Activities.PassangerMainActivity;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LocListener;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.SendDeviceLocationDataAsyncTask;

/**
 * Created by LEO on 14.8.2016..
 */
public class SendLocationThread extends Thread
{
    private String responseLocations;
    private int userId;

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
}
