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
 * Created by Damjan on 9/2/2016.
 */
public class FetchDriversToursAsyncTask extends AsyncTask<String, Void, Void> {
    private Context context;
    private String responseTours;

    public FetchDriversToursAsyncTask(Context contextParam)
    {
        this.context = contextParam;
        this.responseTours = null;
    }

    public String getResponse()
    {
        return this.responseTours;
    }
    @Override
    protected Void doInBackground(String... params) {
        String routeUrl = ServerStaticAttributes._SERVER_ROOT_URL
                + ServerStaticAttributes.FETCH_DRIVERS_TOURS;
        String postValue = params[0];

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
                this.responseTours = StringManipulator.inputStreamToString(httpURLConnection.getInputStream());
            }

            else
            {
                throw new Exception("Dismissed connection: " + responseCode);
            }
        }
        catch (ProtocolException e)
        {
            String successMessage = "FetchDriversToursAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTours = null;
        }
        catch (MalformedURLException e)
        {
            String successMessage = "FetchDriversToursAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTours = null;
        }
        catch (IOException e)
        {
            String successMessage = "FetchDriversToursAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTours = null;
        }
        catch (Exception e)
        {
            String successMessage = "FetchDriversToursAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTours = null;
        }
        return null;
    }
}
