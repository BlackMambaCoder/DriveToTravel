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

/**
 * Created by LEO on 6.4.2016..
 */
public class StoreTourDataAsyncTask extends AsyncTask<String, Void, Void>
{
    private Tour tour;
    private ProgressDialog progressDialog;

    public StoreTourDataAsyncTask(Tour tourArg, Context contextArg)
    {
        this.tour = tourArg;
        this.progressDialog = new ProgressDialog(contextArg);
    }

    public Tour getTour()
    {
        return this.tour;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog.setMessage("Wait...Adding tour");
        this.progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {

        String postValue = params[0];
        String routeUrl = ServerStaticAttributes.SERVER_ROOT_URL +
                            ServerStaticAttributes.STORE_TOUR;

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
                String checkFeedback = StringManipulator.inputStreamToString(httpURLConnection.getInputStream());

                if (!checkFeedback.equals("OK"))
                {
                    throw new Exception(checkFeedback);
                }
            }

            else
            {
                throw new Exception("Dismissed connection: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            Log.e("*****BREAK_POINT*****", "TourDataAsync: " + e.getMessage());
            this.tour = null;
        }
        catch (IOException e)
        {
            Log.e("*****BREAK_POINT*****", "TourDataAsync: " + e.getMessage());
            this.tour = null;
        }
        catch (JSONException e)
        {
            Log.e("*****BREAK_POINT*****", "TourDataAsync: " + e.getMessage());
            this.tour = null;
        }
        catch (Exception e)
        {
            Log.e("*****BREAK_POINT*****", "TourDataAsync: " + e.getMessage());
            this.tour = null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);

        if (this.progressDialog.isShowing())
            this.progressDialog.dismiss();
    }
}
