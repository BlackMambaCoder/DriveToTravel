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
 * Created by LEO on 20.8.2016..
 */
public class AddFriendAsyncTask extends AsyncTask<String, Void, Void> {
    private String successMessage;
    private boolean response;

    public boolean getResponse()
    {
        return this.response;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        String postValue = params[0];

        Resources res = Resources.getSystem();

        String routeUrl = res.getString(R.string.servers_url) + res.getString(R.string.add_friend);

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
            successMessage = "AddFriendAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.response = false;
        }
        catch (ProtocolException e)
        {
            successMessage = "AddFriendAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.response = false;
        }
        catch (IOException e)
        {
            successMessage = "AddFriendAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.response = false;
        }
        catch (Exception e)
        {
            successMessage = "AddFriendAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            e.printStackTrace();
            this.response = false;
        }

        return null;
    }
}
