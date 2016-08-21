package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

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

import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;

/**
 * Created by LEO on 21.8.2016..
 */
public class LoginUserAsyncTask extends AsyncTask<String, Void, Void> {

    private String responseData;

    public JSONObject getResponseData()
    {
        JSONObject retValue;

        try
        {
            retValue = new JSONObject(this.responseData);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            retValue = null;
        }

        return retValue;
    }

    @Override
    protected Void doInBackground(String... params) {
        Resources res           = Resources.getSystem();
        String postValue        = params[0];
        String successMessage;
        String routeUrl         = res.getString(R.string.servers_url) + res.getString(R.string.login_user);

        try
        {
            URL url = new URL(routeUrl);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(ServerStaticAttributes._readTimeOut);
            httpURLConnection.setConnectTimeout(ServerStaticAttributes._connectTimeOut);
            httpURLConnection.setRequestMethod(ServerStaticAttributes._requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);//, "UTF-8");

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            // Sending JSONArray as string to server
            bufferedWriter.write(postValue);
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                this.responseData = StringManipulator.inputStreamToString(httpURLConnection.getInputStream());
            }

            else
            {
                throw new Exception("Not valid request. Response code: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            successMessage = "LoginUserAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseData = null;
        }
        catch (ProtocolException e)
        {
            successMessage = "LoginUserAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseData = null;
        }
        catch (IOException e)
        {
            successMessage = "LoginUserAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseData = null;
        }
        catch (Exception e)
        {
            successMessage = "LoginUserAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.responseData = null;
        }

        return null;
    }
}
