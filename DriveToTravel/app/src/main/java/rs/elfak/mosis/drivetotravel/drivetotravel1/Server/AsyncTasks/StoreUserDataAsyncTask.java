package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
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

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.GetDriverCallback;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */
public class StoreUserDataAsyncTask extends AsyncTask<String, Void, Void>
{
    private String successMessage;
    private ProgressDialog progressDialog;
    private String responseUser;

    public StoreUserDataAsyncTask(){}

    public StoreUserDataAsyncTask(Driver user, Context context)//, ProgressDialog progressDialog)
    {
        Log.w("*****BREAK_POINT*****", "ServerRequest storeDriverAsyncTask constructor");
//        this.user = user;
        this.progressDialog = new ProgressDialog(context);
    }

//    public Driver getDriver()
//    {
//        return this.user;
//    }

    public JSONObject getStoredUser()
    {
        JSONObject user = null;

        if (this.responseUser != null)
        {
            try
            {

                if (this.responseUser.equals("user exists"))
                {
                    user = new JSONObject();
                    user.put(UserStaticAttributes.USER_EXISTS, true);

                    return user;
                }

                user = new JSONObject(this.responseUser);

                user.put(UserStaticAttributes.USER_EXISTS, false);
            }
            catch (JSONException e)
            {
                user = null;
            }
        }

        return user;
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        this.progressDialog.setMessage("Wait...Register");
//        this.progressDialog.show();
//    }

    @Override
    protected Void doInBackground(String... params) {

        String postValue            = params[0];
        String routeUrl             = ServerStaticAttributes._SERVER_ROOT_URL
                                        + ServerStaticAttributes._REGISTER_URL;

        try
        {
            URL url                     = new URL(routeUrl);

            HttpURLConnection httpURLConnection
                                        = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(ServerStaticAttributes._readTimeOut);
            httpURLConnection.setConnectTimeout(ServerStaticAttributes._connectTimeOut);
            httpURLConnection.setRequestMethod(ServerStaticAttributes._requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream   = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter
                                        = new OutputStreamWriter(outputStream);

            BufferedWriter bufferedWriter
                                        = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(postValue);
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();

            int responseCode            = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                this.responseUser           = StringManipulator.inputStreamToString(httpURLConnection.getInputStream());
            }

            else if (responseCode == HttpURLConnection.HTTP_CONFLICT)
            {
                this.responseUser           = "user exists";
            }

            else
            {
                throw new Exception("Not valid request. Response code: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            successMessage = "AddFriendAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseUser = null;
        }
        catch (ProtocolException e)
        {
            successMessage = "AddFriendAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseUser = null;
        }
        catch (IOException e)
        {
            successMessage = "AddFriendAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseUser = null;
        }
        catch (Exception e)
        {
            successMessage = "AddFriendAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseUser = null;
        }

        return null;
    }

//    @Override
//    protected void onPostExecute(Void aVoid)
//    {
//        super.onPostExecute(aVoid);
//
//        if (this.progressDialog.isShowing())
//            this.progressDialog.dismiss();
//    }
}
