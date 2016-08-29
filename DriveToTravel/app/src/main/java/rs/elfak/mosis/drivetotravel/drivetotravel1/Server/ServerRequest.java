package rs.elfak.mosis.drivetotravel.drivetotravel1.Server;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.AddFriendAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchDriverDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchTourDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FriendWithAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.LoginUserAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.StoreUserDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.UpdateTourRankAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

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

    public ServerRequest()
    {
        this.context = null;
        this.classDriver = null;
        this.userLocalStore = null;
    }

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

//    private void storeDriver ()
//    {
//        if (this.classDriver != null)
//        {
//            try
//            {
//                StoreDriverDataAsyncTask storeDriverDataAsyncTask = new StoreDriverDataAsyncTask(this.classDriver, this.context);
//                storeDriverDataAsyncTask.execute().get();
//                this.classDriver = storeDriverDataAsyncTask.getDriver();
//            }
//            catch (InterruptedException e)
//            {
//                Log.e("*****BREAK_POINT*****", "ServerRequest storeDriver: " + e.getMessage());
//                this.classDriver = null;
//            }
//            catch (ExecutionException e)
//            {
//                Log.e("*****BREAK_POINT*****", "ServerRequest storeDriver: " + e.getMessage());
//                this.classDriver = null;
//            }
//        }
//    }

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

//        this.classDriver = driverP;
//        this.storeDriver();
//
//        if (this.classDriver != null)
//        {
//            returnValue = true; //this.storeUserToLocalStore();
//        }
//
//        else
//        {
//            Log.e("/connectToServer", "Driver couldn't be stored");
//        }

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

//    public void searchTour(String startLocationParam, String endLocationParam, String timeParam, String dateParam)
//    {
//        JSONArray JSONDataArray = new JSONArray();
//
//        JSONDataArray.put(startLocationParam);
//        JSONDataArray.put(endLocationParam);
//        JSONDataArray.put(timeParam);
//        JSONDataArray.put(dateParam);
//
//        String dataArrayForPostRequest = JSONDataArray.toString();
//
//        FetchTourDataAsyncTask fetchTourDataAsyncTask = new FetchTourDataAsyncTask(this.context);
//        fetchTourDataAsyncTask.execute(dataArrayForPostRequest);
//    }

    public List<Tour> searchTour(String... params)
    {
        List<Tour> tours;
        String successMessage;
        JSONArray JSONDataArray         = new JSONArray();

        for (String param : params)
        {
            JSONDataArray.put(param);
        }

        String dataArrayForPostRequest  = JSONDataArray.toString();

        FetchTourDataAsyncTask fetchTourDataAsyncTask
                                        = new FetchTourDataAsyncTask(this.context);

        try
        {
            fetchTourDataAsyncTask.execute(dataArrayForPostRequest).get();
            tours                           = fetchTourDataAsyncTask.getTours();
        }
        catch (InterruptedException e)
        {
            successMessage = "ServerRequestSearchTour: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }
        catch (ExecutionException e)
        {
            successMessage = "ServerRequestSearchTour: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }

        return tours;
    }

    public boolean friendWith(String user1, String user2)
    {
        boolean retValue;
        JSONObject requestData = new JSONObject();

        FriendWithAsyncTask task = new FriendWithAsyncTask();
        try
        {

            requestData.put("firstuser", user1);
            requestData.put("seconduser", user2);

            task.execute(requestData.toString()).get();

            retValue = task.getResponse();
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequestFriendWith: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = false;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequestFriendWith: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = false;
        }
        catch (JSONException e)
        {
            String successMessage = "ServerRequestFriendWith: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = false;
        }

        return retValue;
    }

    public String addFriend(String userFriendParam)
    {
        JSONObject userFriend = new JSONObject();
        String successMessage;
        String retValue;

        AddFriendAsyncTask task = new AddFriendAsyncTask();

        try {
            userFriend.put("username", userFriendParam);
            task.execute(userFriend.toString()).get();
            if (task.getResponse())
            {
                retValue = userFriendParam;
            }
            else
            {
                retValue = null;
            }
        }
        catch (InterruptedException e)
        {
            successMessage = "ServerRequestAddFriend: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }
        catch (ExecutionException e)
        {
            successMessage = "ServerRequestAddFriend: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }
        catch (JSONException e)
        {
            successMessage = "ServerRequestAddFriend: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }

        return retValue;
    }

    public String removeFriend(String userFriendParam)
    {
        JSONObject userFriend = new JSONObject();
        String successMessage;
        String retValue;

        AddFriendAsyncTask task = new AddFriendAsyncTask();

        try {
            userFriend.put("username", userFriendParam);
            task.execute(userFriend.toString()).get();
            if (task.getResponse())
            {
                retValue = userFriendParam;
            }
            else
            {
                retValue = null;
            }
        }
        catch (InterruptedException e)
        {
            successMessage = "ServerRequestRemoveFriend: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }
        catch (ExecutionException e)
        {
            successMessage = "ServerRequestRemoveFriend: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }
        catch (JSONException e)
        {
            successMessage = "ServerRequestRemoveFriend: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }

        return retValue;
    }

    public List<Double> updateTourRank(double rankParam)
    {
        String successMessage;

        JSONObject requestData = new JSONObject();
        List<Double> responseData;

        UpdateTourRankAsyncTask task = new UpdateTourRankAsyncTask();

        try
        {
            requestData.put("rank", rankParam);

            task.execute(requestData.toString()).get();

            responseData = task.getResponseData();
        }
        catch (JSONException e)
        {
            successMessage = "ServerRequestUpdateTourRank: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            responseData = null;
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            successMessage = "ServerRequestUpdateTourRank: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            responseData = null;
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            successMessage = "ServerRequestUpdateTourRank: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            responseData = null;
            e.printStackTrace();
        }

        return responseData;
    }

    public Driver storeUser(Driver userObjectParam)
    {
        JSONObject user;
        StoreUserDataAsyncTask task = new StoreUserDataAsyncTask();

        try
        {
            user = userObjectParam.toJSONObject();
            task.execute(user.toString()).get();
            user = task.getStoredUser();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            user = null;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            user = null;
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
            user = null;
        }

        return Driver.getDriverFromJSONObject(user);
    }

    public Passenger storeUser(Passenger userObjectParam)
    {
        JSONObject user;
        StoreUserDataAsyncTask task = new StoreUserDataAsyncTask();

        try
        {
            user = userObjectParam.toJSONObject();
            task.execute(user.toString()).get();
            user = task.getStoredUser();
        }
        catch (JSONException e)
        {
            String successMessage = "ServerRequest::storeUser : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            user = null;
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequest::storeUser : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            user = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequest::storeUser : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            user = null;
        }

        return Passenger.getUserFromJSONObject(user);
    }

    public JSONObject loginUser(String username, String password)
    {
        JSONObject data = new JSONObject();
        LoginUserAsyncTask task = new LoginUserAsyncTask();
        JSONObject retValue;

        try
        {
            data.put(UserStaticAttributes._username, username);
            data.put(UserStaticAttributes._password, password);

            task.execute(data.toString()).get();

            retValue = task.getResponseData();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            retValue = null;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            retValue = null;
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
            retValue = null;
        }

        return retValue;
    }

}
