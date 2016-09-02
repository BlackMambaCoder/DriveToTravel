package rs.elfak.mosis.drivetotravel.drivetotravel1.Server;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchDriversToursAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchTourDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FriendWithAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.LoginUserAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.StoreTourDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.StoreUserDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.UpdateTourRankAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.TourStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */
public class ServerRequest {
    /**
     * STATIC VARIABLES
     */
    public static int SEARCH_FOR_DRIVER = 35;
    public static int SEARCH_FOR_LOCATION = 36;

    private UserLocalStore userLocalStore;
    private Driver classDriver;
    private Context context;

    public ServerRequest() {
        this.context = null;
        this.classDriver = null;
        this.userLocalStore = null;
    }

    public ServerRequest(Context context) {
        this.context = context;
        this.userLocalStore = new UserLocalStore(this.context);
        this.classDriver = null;
    }

    public Driver getDriver() {
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

//    public void fetchDriverByUsername (String username)
//    {
//        try
//        {
//            FetchDriverDataAsyncTask fetchDriverDataAsyncTask = new FetchDriverDataAsyncTask(this.context);
//            fetchDriverDataAsyncTask.execute(username).get();
//            this.classDriver = fetchDriverDataAsyncTask.getUser();
//        }
//        catch (InterruptedException e)
//        {
//            Log.e("*****BREAK_POINT*****", "ServerRequest fetDriverByUsername: " + e.getMessage());
//            e.printStackTrace();
//            this.classDriver = null;
//        }
//        catch (ExecutionException e)
//        {
//            Log.e("*****BREAK_POINT*****", "ServerRequest fetDriverByUsername: " + e.getMessage());
//            e.printStackTrace();
//            this.classDriver = null;
//        }
//    }

    public boolean connectToServer(Driver driverP) {
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

//    public boolean connectToServer(String username, String password)
//    {
//        boolean retValue = true;
//
//        this.fetchDriverByUsername(username);
//
//        if (this.classDriver != null && !password.equals(this.classDriver.getPassword()))
//        {
//            retValue = this.storeUserToLocalStore();
//        }
//
//        return retValue;
//    }

    private boolean storeUserToLocalStore() {
        boolean retValue = false;

        if (!this.userLocalStore.getUserLoggedIn()) {
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

    private List<Tour> searchTour(JSONObject jsonObject) throws ExecutionException, InterruptedException {
        FetchTourDataAsyncTask fetchTourDataAsyncTask
                = new FetchTourDataAsyncTask(this.context);

        fetchTourDataAsyncTask.execute(jsonObject.toString()).get();

        return fetchTourDataAsyncTask.getTours();
    }

    public List<Tour> searchTour(int searchCriteria, String... searchAttribute) {
        List<Tour> tours;
        JSONObject jsonObject = new JSONObject();

        try {
            if (searchCriteria == ServerRequest.SEARCH_FOR_DRIVER) {
                jsonObject.put("username", searchAttribute[0]);
            } else if (searchCriteria == ServerRequest.SEARCH_FOR_LOCATION) {
                jsonObject.put("startlocation", searchAttribute[0]);
                jsonObject.put("destlocation", searchAttribute[1]);
            }

            tours = this.searchTour(jsonObject);
        } catch (JSONException e) {
            String successMessage = "ServerRequestSearchTour: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequestSearchTour: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequestSearchTour: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }

        return tours;
    }

    public List<Tour> searchTour(Date searchAttribute) {
        List<Tour> tours;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", searchAttribute);

            tours = this.searchTour(jsonObject);
        } catch (JSONException e) {
            String successMessage = "ServerRequestSearchTourForDate: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequestSearchTourForDate: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequestSearchTourForDate: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            tours = null;
        }

        return tours;
    }

    public boolean friendWith(String user1, String user2) {
        boolean retValue;
        JSONObject requestData = new JSONObject();

        FriendWithAsyncTask task = new FriendWithAsyncTask();
        try {

            requestData.put("firstuser", user1);
            requestData.put("seconduser", user2);

            task.execute(requestData.toString()).get();

            retValue = task.getResponse();
        } catch (InterruptedException e) {
            String successMessage = "ServerRequestFriendWith: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = false;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequestFriendWith: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = false;
        } catch (JSONException e) {
            String successMessage = "ServerRequestFriendWith: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = false;
        }

        return retValue;
    }

    public String addFriend(String userFriendParam) {
        JSONObject userFriend = new JSONObject();
        String successMessage;
        String retValue;

        AddFriendAsyncTask task = new AddFriendAsyncTask();

        try {
            userFriend.put("username", userFriendParam);
            task.execute(userFriend.toString()).get();
            if (task.getResponse()) {
                retValue = userFriendParam;
            } else {
                retValue = null;
            }
        } catch (InterruptedException e) {
            successMessage = "ServerRequestAddFriend: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        } catch (ExecutionException e) {
            successMessage = "ServerRequestAddFriend: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        } catch (JSONException e) {
            successMessage = "ServerRequestAddFriend: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }

        return retValue;
    }

    public String removeFriend(String userFriendParam) {
        JSONObject userFriend = new JSONObject();
        String successMessage;
        String retValue;

        AddFriendAsyncTask task = new AddFriendAsyncTask();

        try {
            userFriend.put("username", userFriendParam);
            task.execute(userFriend.toString()).get();
            if (task.getResponse()) {
                retValue = userFriendParam;
            } else {
                retValue = null;
            }
        } catch (InterruptedException e) {
            successMessage = "ServerRequestRemoveFriend: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        } catch (ExecutionException e) {
            successMessage = "ServerRequestRemoveFriend: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        } catch (JSONException e) {
            successMessage = "ServerRequestRemoveFriend: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            retValue = null;
        }

        return retValue;
    }

    public List<Double> updateTourRank(double rankParam, int tourid) {
        String successMessage;

        JSONObject requestData = new JSONObject();
        List<Double> responseData;

        UpdateTourRankAsyncTask task = new UpdateTourRankAsyncTask();

        try {
            requestData.put("rank", rankParam);
            requestData.put("tourid", tourid);

            task.execute(requestData.toString()).get();

            responseData = task.getResponseData();
        } catch (JSONException e) {
            successMessage = "ServerRequestUpdateTourRank: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            responseData = null;
            e.printStackTrace();
        } catch (InterruptedException e) {
            successMessage = "ServerRequestUpdateTourRank: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            responseData = null;
            e.printStackTrace();
        } catch (ExecutionException e) {
            successMessage = "ServerRequestUpdateTourRank: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            responseData = null;
            e.printStackTrace();
        }

        return responseData;
    }

    public Driver storeUser(Driver userObjectParam) {
        JSONObject user;
        StoreUserDataAsyncTask task = new StoreUserDataAsyncTask();

        try {
            user = userObjectParam.toJSONObject();
            task.execute(user.toString()).get();
            user = task.getStoredUser();

            if (user.getBoolean(UserStaticAttributes.USER_EXISTS)) {
                Toast.makeText(
                        this.context,
                        "Username exists",
                        Toast.LENGTH_LONG
                ).show();
                return null;
            }
        } catch (JSONException e) {
            String successMessage = "ServerRequest::storeUser : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequest::storeUser : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequest::storeUser : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        }

        return Driver.getDriverFromJSONObject(user);
    }

    public Passenger storeUser(Passenger userObjectParam) {
        JSONObject user;
        StoreUserDataAsyncTask task = new StoreUserDataAsyncTask();

        try {
            user = userObjectParam.toJSONObject();
            task.execute(user.toString()).get();
            user = task.getStoredUser();

            if (user.getBoolean(UserStaticAttributes.USER_EXISTS)) {
                Toast.makeText(
                        this.context,
                        "Username exists",
                        Toast.LENGTH_LONG
                ).show();
                return null;
            }
        } catch (JSONException e) {
            String successMessage = "ServerRequest::storeUser : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequest::storeUser : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequest::storeUser : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            user = null;
        }

        return Passenger.getUserFromJSONObject(user);
    }

    public JSONObject loginUser(String username, String password) {
        JSONObject data = new JSONObject();
        LoginUserAsyncTask task = new LoginUserAsyncTask();
        JSONObject retValue = null;

        try {
            data.put(UserStaticAttributes._username, username);
            data.put(UserStaticAttributes._password, password);

            task.execute(data.toString()).get();

            retValue = task.getResponseData();

            if (retValue == null)
                return null;

            if (retValue.getBoolean(
                    String.valueOf(ServerStaticAttributes.USER_NAME_ERROR)
            )) {
                Toast.makeText(
                        this.context,
                        "Incorrect username",
                        Toast.LENGTH_LONG
                ).show();

                return null;
            }

            if (retValue.getBoolean(
                    String.valueOf(ServerStaticAttributes.PASSWORD_ERROR)
            )) {
                Toast.makeText(
                        this.context,
                        "Incorrect password",
                        Toast.LENGTH_LONG
                ).show();

                return null;
            }

            return retValue;
        } catch (JSONException e) {
            String successMessage = "ServerRequest::loginUser : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequest::loginUser : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequest::loginUser : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }

    public Tour storeTour(Tour tourParam)
    {
        JSONObject tourJsonObj = tourParam.toJSONObject();

        StoreTourDataAsyncTask task = new StoreTourDataAsyncTask();

        try
        {
            task.execute(tourJsonObj.toString()).get();

            tourJsonObj = task.getTour();
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequest::storeTour : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequest::storeTour : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }

        return Tour.getTourFromJSONObject(tourJsonObj);
    }

    public Tour[] getDriverTours(int driverIdParam)
    {
        JSONObject postValue = new JSONObject();
        try
        {
            postValue.put(UserStaticAttributes._id, driverIdParam);
            FetchDriversToursAsyncTask task = new FetchDriversToursAsyncTask(null);
            task.execute(postValue.toString()).get();
            String response = task.getResponse();

            if (response == null) {
                Toast.makeText(this.context, "response from tours is null", Toast.LENGTH_SHORT).show();
                return null;
            }
            List<Tour> tours = Tour.getToursFromJsonArray(response);
            return Tour.getArrayFromList(tours);
        }
        catch (JSONException e)
        {
            String successMessage = "ServerRequest::getDriverTours : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequest::getDriverTours : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequest::getDriverTours : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }
}
