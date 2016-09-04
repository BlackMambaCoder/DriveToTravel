package rs.elfak.mosis.drivetotravel.drivetotravel1.Server;

import android.content.Context;
import android.graphics.Bitmap;
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
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.MyConverter;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.AddFriendAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchDriversToursAsyncTask;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FriendWithAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.LoginUserAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.ServerRequestAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.StoreUserDataAsyncTask;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.UpdateTourRankAsyncTask;
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

    private List<Tour> searchTour(JSONObject jsonObject) throws ExecutionException, InterruptedException {
//        FetchTourDataAsyncTask fetchTourDataAsyncTask
//                = new FetchTourDataAsyncTask(this.context);
//
//        fetchTourDataAsyncTask.execute(jsonObject.toString()).get();
//
//        return fetchTourDataAsyncTask.getTours();

        String route = ServerStaticAttributes.SEARCH_TOUR_BY_LOCATION;
        String requestData = jsonObject.toString();
        String[] requestDataArray = {route, requestData};

        ServerRequestAsyncTask task = new ServerRequestAsyncTask(this.context, null);
        task.execute(requestDataArray).get();

        return Tour.getToursFromJsonArray(task.getResponseData());
    }

    public List<Tour> searchTour(int searchCriteria, String... searchAttribute) {
        List<Tour> tours;
        JSONObject jsonObject = new JSONObject();

        try
        {
            if (searchCriteria == ServerRequest.SEARCH_FOR_DRIVER)
            {
                jsonObject.put("username", searchAttribute[0]);
            }
            else if (searchCriteria == ServerRequest.SEARCH_FOR_LOCATION)
            {
                jsonObject.put("startlocation", searchAttribute[0]);
                jsonObject.put("destlocation", searchAttribute[1]);
            }

            tours = this.searchTour(jsonObject);
        }
        catch (JSONException e)
        {
            String successMessage = "ServerRequestSearchTour: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            tours = null;
        }
        catch (InterruptedException e)
        {
            String successMessage = "ServerRequestSearchTour: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            tours = null;
        }
        catch (ExecutionException e)
        {
            String successMessage = "ServerRequestSearchTour: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            tours = null;
        }

        return tours;
    }

    public List<Tour> searchTour(Date searchAttribute) {
        List<Tour> tours;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TourStaticAttributes._STARTDATE_AND_TIME, searchAttribute);

            tours = this.searchTour(jsonObject);
        } catch (JSONException e) {
            String successMessage = "ServerRequestSearchTourForDate: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            tours = null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequestSearchTourForDate: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            tours = null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequestSearchTourForDate: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            tours = null;
        }

        return tours;
    }

    public boolean friendWith(String user1, String user2) {
        boolean retValue;
        JSONObject requestData = new JSONObject();

//        FriendWithAsyncTask task = new FriendWithAsyncTask();
        try {
            requestData.put("firstuser", user1);
            requestData.put("seconduser", user2);

            String route = ServerStaticAttributes.USER_FRIEND_WITH;
            String requestDataString = requestData.toString();
            String[] requestDataArray = { route , requestDataString };

            ServerRequestAsyncTask task = new ServerRequestAsyncTask(this.context, null);
            task.execute(requestDataArray).get();

            return task.getResponseData() != null;

//            task.execute(requestData.toString()).get();

//            retValue = task.getResponse();
        } catch (InterruptedException e) {
            String successMessage = "ServerRequestFriendWith: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return false;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequestFriendWith: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return false;
        } catch (JSONException e) {
            String successMessage = "ServerRequestFriendWith: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return false;
        }
    }

    public String addFriend(String userFriendParam) {
        JSONObject userFriend = new JSONObject();
        String successMessage;
        String retValue;

//        AddFriendAsyncTask task = new AddFriendAsyncTask();

        try {
            userFriend.put("username", userFriendParam);

            String route = ServerStaticAttributes.USER_ADD_FRIEND;
            String requestData = userFriend.toString();
            String[] requestDataArray = { route , requestData };

            ServerRequestAsyncTask task = new ServerRequestAsyncTask(this.context, null);
            task.execute(requestDataArray).get();

            return task.getResponseData();
//            task.execute(userFriend.toString()).get();
//            if (task.getResponse()) {
//                retValue = userFriendParam;
//            } else {
//                retValue = null;
//            }
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

        return null;
    }

//    public String removeFriend(String userFriendParam) {
//        JSONObject userFriend = new JSONObject();
//        String successMessage;
//        String retValue;
//
//        AddFriendAsyncTask task = new AddFriendAsyncTask();
//
//        try {
//            userFriend.put("username", userFriendParam);
//            task.execute(userFriend.toString()).get();
//            if (task.getResponse()) {
//                retValue = userFriendParam;
//            } else {
//                retValue = null;
//            }
//        } catch (InterruptedException e) {
//            successMessage = "ServerRequestRemoveFriend: InterruptedException - " + e.getMessage();
//            Log.e("*****BREAK_POINT*****", successMessage);
//            e.printStackTrace();
//            retValue = null;
//        } catch (ExecutionException e) {
//            successMessage = "ServerRequestRemoveFriend: ExecutionException - " + e.getMessage();
//            Log.e("*****BREAK_POINT*****", successMessage);
//            e.printStackTrace();
//            retValue = null;
//        } catch (JSONException e) {
//            successMessage = "ServerRequestRemoveFriend: JSONException - " + e.getMessage();
//            Log.e("*****BREAK_POINT*****", successMessage);
//            e.printStackTrace();
//            retValue = null;
//        }
//
//        return retValue;
//    }

    public List<Double> updateTourRank(double rankParam, int tourid) {
        String successMessage;

        JSONObject requestData = new JSONObject();
//        List<Double> responseData;

//        UpdateTourRankAsyncTask task = new UpdateTourRankAsyncTask();
        ServerRequestAsyncTask serverRequest = new ServerRequestAsyncTask(this.context, null);


        try {
            requestData.put("rank", rankParam);
            requestData.put("tourid", tourid);

//            task.execute(requestData.toString()).get();
//            responseData = task.getResponseData();

            String postValue = requestData.toString();
            String route = ServerStaticAttributes.UPDATE_TOUR_RANK;
            String[] requestDataArray = {route, postValue};
            serverRequest.execute(requestDataArray).get();

            String responseDataStr = serverRequest.getResponseData();

            if (responseDataStr != null) {
                return MyConverter.JSONString2DoubleValueList(responseDataStr);
            }

            return null;
        } catch (JSONException e) {
            successMessage = "ServerRequestUpdateTourRank: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (InterruptedException e) {
            successMessage = "ServerRequestUpdateTourRank: InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (ExecutionException e) {
            successMessage = "ServerRequestUpdateTourRank: ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
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

    public Tour storeTour(Tour tourParam) {
        JSONObject tourJsonObj = tourParam.toJSONObject();

        ServerRequestAsyncTask serverRequest
                = new ServerRequestAsyncTask(
                this.context,
                null
        );

        try {
            String postValue = tourJsonObj.toString();
            String route = ServerStaticAttributes.CREATE_TOUR_URL;
            String[] requestDataArray = {route, postValue};

            serverRequest.execute(requestDataArray).get();
            String responseData = serverRequest.getResponseData();

            if (responseData != null) {
                tourJsonObj = new JSONObject(responseData);
                return Tour.getTourFromJSONObject(tourJsonObj);
            }

            return null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequest::storeTour : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequest::storeTour : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (JSONException e) {
            String successMessage = "ServerRequest::storeTour : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }

    public Tour[] getDriverTours(int driverIdParam) {
        JSONObject postValue = new JSONObject();
        try {
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
        } catch (JSONException e) {
            String successMessage = "ServerRequest::getDriverTours : JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequest::getDriverTours : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequest::getDriverTours : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }

    /**
     * Uses new AsyncTask: ServerRequest AsyncTask which can perform
     * server requests for all types of requests from this app.
     *
     * @return array all tours from database
     */
    public Tour[] getAllTours() {
        String postValue = "";
        String route = ServerStaticAttributes.FETCH_ALL_TOURS;

        String[] taskDataArray = {route, postValue};

        ServerRequestAsyncTask task = new ServerRequestAsyncTask(this.context, null);

        try {
            task.execute(taskDataArray).get();
            String response = task.getResponseData();

            if (response != null) {
                return Tour.getArrayFromList(
                        Tour.getToursFromJsonArray(response)
                );
            }

            return null;
        } catch (InterruptedException e) {
            String successMessage = "ServerRequest::getAllTours : InterruptedException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        } catch (ExecutionException e) {
            String successMessage = "ServerRequest::getAllTours : ExecutionException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            return null;
        }
    }

    public String uploadBitmapString(String bitmapParam, int driverIdParam) {
        try {
            String route = ServerStaticAttributes.UPLOAD_USER_IMAGE;
            JSONObject image = new JSONObject();

            image.put(UserStaticAttributes._id, driverIdParam);
            image.put(UserStaticAttributes.PROFILE_IMAGE, bitmapParam);

            String[] requestDataArray = {route, image.toString()};

            ServerRequestAsyncTask task = new ServerRequestAsyncTask(this.context, null);
            task.execute(requestDataArray).get();

            return task.getResponseData();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap uploadBitmap(Bitmap bitmapParam, int driverIdParam)
    {
        try
        {
            String route = ServerStaticAttributes.UPLOAD_USER_IMAGE;
            JSONObject image = new JSONObject();

            image.put(UserStaticAttributes._id, driverIdParam);
            image.put(UserStaticAttributes.PROFILE_IMAGE, bitmapParam);

            String[] requestDataArray = {route, image.toString()};

            ServerRequestAsyncTask task = new ServerRequestAsyncTask(this.context, null);
            task.execute(requestDataArray).get();

            JSONArray responseImage = new JSONArray(task.getResponseData());

            return User.stringToBitmap(responseImage.get(0).toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Passenger addPassengerToTour (int passengerIdParam, int tourIdParam)
    {

        try
        {
            JSONObject requestJSONObject = new JSONObject();
            requestJSONObject.put(TourStaticAttributes.PASSENGER_ID, passengerIdParam);
            requestJSONObject.put(TourStaticAttributes.TOUR_ID, tourIdParam);

            String route = ServerStaticAttributes.ADD_PASSENGER_TO_TOUR;
            String requestData = requestJSONObject.toString();
            String[] requestDataArray = { route , requestData };

            ServerRequestAsyncTask task = new ServerRequestAsyncTask(this.context, null);
            task.execute(requestDataArray).get();

            String responseData = task.getResponseData();

            if (responseData != null)
            {
                JSONObject responseJSONObject = new JSONObject(responseData);
                return Passenger.getUserFromJSONObject(responseJSONObject);
            }

            return null;
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
