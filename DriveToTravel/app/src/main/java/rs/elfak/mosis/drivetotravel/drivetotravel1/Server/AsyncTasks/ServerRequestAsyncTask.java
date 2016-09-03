package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.content.Context;
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
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;

/**
 * Created by Damjan on 9/3/2016.
 */
public class ServerRequestAsyncTask extends AsyncTask<String, Void, Void> {
    private String responseData;
    private Context context;

    public ServerRequestAsyncTask(Context context, String progressDialogTextParam)
    {
        this.context = context;
    }

    public String getResponseData()
    {
        return this.responseData;
    }
    @Override
    protected Void doInBackground(String... params)
    {
        String route = params[0];
        String postValue = params[1];

        String routeUrl = ServerStaticAttributes._SERVER_ROOT_URL
                            + route;

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
                throw new Exception("Dismissed connection: " + responseCode);
            }
        }
        catch (ProtocolException e)
        {
            String successMessage = "ServerRequestAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseData = null;
        }
        catch (MalformedURLException e)
        {
            String successMessage = "ServerRequestAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseData = null;
        }
        catch (IOException e)
        {
            String successMessage = "ServerRequestAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseData = null;
        }
        catch (Exception e)
        {
            String successMessage = "ServerRequestAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseData = null;
        }
        return null;
    }
}
