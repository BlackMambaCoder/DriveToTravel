package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.net.URL;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.TourStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 6.4.2016..
 */
public class StoreTourDataAsyncTask extends AsyncTask<String, Void, Void>
{
    private String responseTour;
    private ProgressDialog progressDialog;

    public StoreTourDataAsyncTask()
    {
        this.progressDialog = null;
    }

    public StoreTourDataAsyncTask(Context contextArg)
    {
        this.progressDialog = new ProgressDialog(contextArg);
    }

    public JSONObject getTour()
    {
        if (this.responseTour != null)
        {
            try
            {
                return new JSONObject(this.responseTour);
            }
            catch (JSONException e)
            {
                return null;
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
//        super.onPreExecute();
//        this.progressDialog.setMessage("Wait...Adding tour");
//        this.progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params)
    {
        String routeUrl = ServerStaticAttributes._SERVER_ROOT_URL
                + ServerStaticAttributes.CREATE_TOUR_URL;
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
                this.responseTour = StringManipulator.inputStreamToString(httpURLConnection.getInputStream());
            }

            else
            {
                throw new Exception("Dismissed connection: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            String successMessage = "StoreTourDataAsyncTask: MalformedURLException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTour = null;
        }
        catch (IOException e)
        {
            String successMessage = "StoreTourDataAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTour = null;
        }
        catch (JSONException e)
        {
            String successMessage = "StoreTourDataAsyncTask: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTour = null;
        }
        catch (Exception e)
        {
            String successMessage = "StoreTourDataAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.responseTour = null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
//
//        if (this.progressDialog.isShowing())
//            this.progressDialog.dismiss();
    }
}
