package rs.elfak.mosis.drivetotravel.drivetotravel1.Threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutionException;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Activities.PassangerMainActivity;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Handlers.ReceiveLocationHandler;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LocListener;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.SendDeviceLocationDataAsyncTask;

/**
 * Created by LEO on 14.8.2016..
 */
public class SendLocationThread extends Thread
{
    private String responseLocations;
    public static boolean runThread = true;

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
        try {
            task.execute(
                    sendLocData[0],
                    sendLocData[1],
                    sendLocData[2],
                    sendLocData[3]
            ).get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        this.responseLocations = task.getJSONUsersLocationArray();
    }
}
