package rs.elfak.mosis.drivetotravel.drivetotravel1.Server;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
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
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.TourStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */
public class ServerRequest
{
    /**
     *  STATIC VARIABLES
     */
    public static int SEARCH_FOR_DRIVER     = 35;
    public static int SEARCH_FOR_LOCATION   = 36;

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

    private List<Tour> searchTour(JSONObject jsonObject) throws ExecutionException, InterruptedException
    {
        FetchTourDataAsyncTask fetchTourDataAsyncTask
                                        = new FetchTourDataAsyncTask(this.context);

        fetchTourDataAsyncTask.execute(jsonObject.toString()).get();

        return fetchTourDataAsyncTask.getTours();
    }

    public List<Tour> searchTour(int searchCriteria, String... searchAttribute)
    {
        List<Tour> tours;
        JSONObject jsonObject = new JSONObject();

        try
        {
            if (searchCriteria == ServerRequest.SEARCH_FOR_DRIVER)
            {
                jsonObject.put(UserStaticAttributes._username, searchAttribute[0]);
            }
            else if (searchCriteria == ServerRequest.SEARCH_FOR_LOCATION)
            {
                jsonObject.put(TourStaticAttributes._STARTLOCATION, searchAttribute[0]);
                jsonObject.put(TourStaticAttributes._DESTINATIONLOCATION, searchAttribute[1]);
            }

            tours = this.searchTour(jsonObject);
        }
        catch (JSONException e)
        {
            String successMessage = "ServerRequestSearchTour: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequestSearchTour: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequestSearchTour: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }

        return tours;
    }

    public List<Tour> searchTour(Date searchAttribute)
    {
        List<Tour> tours;
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put(TourStaticAttributes._STARTDATE_AND_TIME, searchAttribute.toString());

            tours = this.searchTour(jsonObject);
        }
        catch (JSONException e)
        {
            String successMessage = "ServerRequestSearchTourForDate: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequestSearchTourForDate: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequestSearchTourForDate: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }

        return tours;
    }

    public boolean friendWith(String user1, String user2)
    {
        JSONObject requestData = new JSONObject();

        FriendWithAsyncTask task = new FriendWithAsyncTask();
        try
        {

            requestData.put(UserStaticAttributes.FIRST_USER, user1);
            requestData.put(UserStaticAttributes.SECOND_USER, user2);

            task.execute(requestData.toString()).get();

            return task.getResponse();
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequestFriendWith: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return false;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequestFriendWith: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return false;
        }
        catch (JSONException e)
        {
            String successMessage = "ServerRequestFriendWith: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return false;
        }
    }

    public String addFriend(String userFriendParam, String userNameParam)
    {
        JSONObject userFriend = new JSONObject();
        String successMessage;
        String retValue;

        AddFriendAsyncTask task = new AddFriendAsyncTask();

        try {
            userFriend.put(UserStaticAttributes.FRIENDS_USERNAME, userFriendParam);
            userFriend.put(UserStaticAttributes._username, userNameParam);
            task.execute(userFriend.toString()).get();

            if (task.getResponse())
            {
                return userFriendParam;
            }
            else
            {
                return null;
            }
        }
        catch (InterruptedException e)
        {
            successMessage = "ServerRequestAddFriend: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
        catch (ExecutionException e)
        {
            successMessage = "ServerRequestAddFriend: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
        catch (JSONException e)
        {
            successMessage = "ServerRequestAddFriend: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }

    public String removeFriend(String userFriendParam, String usernameParam)
    {
        JSONObject userFriend = new JSONObject();
        String successMessage;
        String retValue;

        AddFriendAsyncTask task = new AddFriendAsyncTask();

        try {
            userFriend.put(UserStaticAttributes.FRIENDS_USERNAME, userFriendParam);
            userFriend.put(UserStaticAttributes._username, usernameParam);
            task.execute(userFriend.toString()).get();

            if (task.getResponse())
            {
                return userFriendParam;
            }
            else
            {
                return null;
            }
        }
        catch (InterruptedException e)
        {
            successMessage = "ServerRequestRemoveFriend: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
        catch (ExecutionException e)
        {
            successMessage = "ServerRequestRemoveFriend: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
        catch (JSONException e)
        {
            successMessage = "ServerRequestRemoveFriend: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }

    public List<Double> updateTourRank(double rankParam,int tourid)
    {
        String successMessage;

        JSONObject requestData = new JSONObject();
        List<Double> responseData;

        UpdateTourRankAsyncTask task = new UpdateTourRankAsyncTask();

        try
        {
            requestData.put("rank", rankParam);
            requestData.put("tourid",tourid);

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
            String successMessage = "ServerRequest::storeUser : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequest::storeUser : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequest::storeUser : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        }

        if (user != null)
        {
            return Driver.getDriverFromJSONObject(user);
        }

        return null;
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
            user = null;
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequest::storeUser : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequest::storeUser : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        }

        if (user != null)
        {
            return Passenger.getUserFromJSONObject(user);
        }

        return null;
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

            return task.getResponseData();
        }
        catch (JSONException e)
        {
            return null;
        }
        catch (InterruptedException e)
        {
            return null;
        }
        catch (ExecutionException e)
        {
            return null;
        }
    }

}
