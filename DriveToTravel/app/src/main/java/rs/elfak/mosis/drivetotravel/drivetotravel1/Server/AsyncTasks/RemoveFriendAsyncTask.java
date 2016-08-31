package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by LEO on 29.8.2016..
 */
public class RemoveFriendAsyncTask extends AsyncTask<String, Void, Void> {

    private boolean response;
    private String successMessage;

    @Override
    protected Void doInBackground(String... params) {

         String postValue = params[0];

//        Resources res = Resources.getSystem();

//        String routeUrl = res.getString(R.string.servers_url) + res.getString(R.string.remove_friend);
        String routeUrl = ServerStaticAttributes.SERVER_ROOT_URL +
                            ServerStaticAttributes.REMOVE_FRIEND_URL;

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
                String responseData = StringManipulator.inputStreamToString(httpURLConnection.getInputStream());
                this.response = true;
            }

            else
            {
                throw new Exception("Not valid request. Response code: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            successMessage = "RemoveFriendAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.response = false;
        }
        catch (ProtocolException e)
        {
            successMessage = "RemoveFriendAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.response = false;
        }
        catch (IOException e)
        {
            successMessage = "RemoveFriendAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.response = false;
        }
        catch (Exception e)
        {
            successMessage = "RemoveFriendAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.response = false;
        }

        return null;
    }
}
