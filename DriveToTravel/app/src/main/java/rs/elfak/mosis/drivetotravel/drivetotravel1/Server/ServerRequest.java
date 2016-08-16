package rs.elfak.mosis.drivetotravel.drivetotravel1.Server;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchDriverDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchTourDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.StoreDriverDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */
public class ServerRequest
{
    /**
     *  STATIC VARIABLES
     */

//    public static int DISTANCE_SEARCH           = 345;
//    public static int LOCATION_TIME_SEARCH      = 346;

    private UserLocalStore userLocalStore;
    private Driver classDriver;
    private Context context;

    public ServerRequest (Context context)
    {
        this.context = context;
        this.userLocalStore = new UserLocalStore(this.context);
        this.classDriver = null;
    }

    public Driver getDriver()
    {
        return this.classDriver;
    }

    private void storeDriver ()
    {
        if (this.classDriver != null)
        {
            try
            {
                StoreDriverDataAsyncTask storeDriverDataAsyncTask = new StoreDriverDataAsyncTask(this.classDriver, this.context);
                storeDriverDataAsyncTask.execute().get();
                this.classDriver = storeDriverDataAsyncTask.getDriver();
            }
            catch (InterruptedException e)
            {
                Log.e("*****BREAK_POINT*****", "ServerRequest storeDriver: " + e.getMessage());
                this.classDriver = null;
            }
            catch (ExecutionException e)
            {
                Log.e("*****BREAK_POINT*****", "ServerRequest storeDriver: " + e.getMessage());
                this.classDriver = null;
            }
        }
    }

    public void fetchDriverByUsername (String username)
    {
        try
        {
            FetchDriverDataAsyncTask fetchDriverDataAsyncTask = new FetchDriverDataAsyncTask(this.context);
            fetchDriverDataAsyncTask.execute(username).get();
            this.classDriver = fetchDriverDataAsyncTask.getUser();
        }
        catch (InterruptedException e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest fetDriverByUsername: " + e.getMessage());
            e.printStackTrace();
            this.classDriver = null;
        }
        catch (ExecutionException e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest fetDriverByUsername: " + e.getMessage());
            e.printStackTrace();
            this.classDriver = null;
        }
    }

    public boolean connectToServer(Driver driverP)
    {
        boolean returnValue = false;

        this.classDriver = driverP;
        this.storeDriver();

        if (this.classDriver != null)
        {
            returnValue = true; //this.storeUserToLocalStore();
        }

        else
        {
            Log.e("/connectToServer", "Driver couldn't be stored");
        }

        return returnValue;
    }

    public boolean connectToServer(String username, String password)
    {
        boolean retValue = true;

        this.fetchDriverByUsername(username);

        if (this.classDriver != null && !password.equals(this.classDriver.getPassword()))
        {
            retValue = this.storeUserToLocalStore();
        }

        return retValue;
    }

    private boolean storeUserToLocalStore()
    {
        boolean retValue = false;

        if (!this.userLocalStore.getUserLoggedIn())
        {
            this.userLocalStore.storeUser(this.classDriver);
            this.userLocalStore.setUserLoggedIn(true);
            retValue = true;
        }

        return retValue;
    }

    public void searchTour(String startLocationParam, String endLocationParam, String timeParam, String dateParam)
    {
        JSONArray JSONDataArray = new JSONArray();

        JSONDataArray.put(startLocationParam);
        JSONDataArray.put(endLocationParam);
        JSONDataArray.put(timeParam);
        JSONDataArray.put(dateParam);

        String dataArrayForPostRequest = JSONDataArray.toString();

        FetchTourDataAsyncTask fetchTourDataAsyncTask = new FetchTourDataAsyncTask(this.context);
        fetchTourDataAsyncTask.execute(dataArrayForPostRequest);
    }

    public void searchTour(String distanceParam)
    {
        JSONArray JSONDataArray = new JSONArray();

        JSONDataArray.put(distanceParam);

        String dataArrayForPostRequest = JSONDataArray.toString();

        FetchTourDataAsyncTask fetchTourDataAsyncTask = new FetchTourDataAsyncTask(this.context);
        fetchTourDataAsyncTask.execute(dataArrayForPostRequest);
    }

    public void searchTour(String... params)
    {

    }

    public void storeUser(User userObjectParam)
    {

    }

    public void getUser(String... params)
    {
        JSONArray JSONDataArrayFromInput = new JSONArray();

        for(String param : params)
        {
            JSONDataArrayFromInput.put(param);
        }

        String dataArrayForPostRequest = JSONDataArrayFromInput.toString();


    }
}
