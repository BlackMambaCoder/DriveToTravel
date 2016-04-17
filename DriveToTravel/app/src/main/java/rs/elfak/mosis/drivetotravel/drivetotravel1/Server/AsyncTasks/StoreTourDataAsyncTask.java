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
public class StoreTourDataAsyncTask extends AsyncTask<Void, Void, Void>
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
    protected Void doInBackground(Void... params) {

        try
        {

            URL url = new URL(ServerStaticAttributes._serverAddress
                    + ServerStaticAttributes._serverPath
                    + ServerStaticAttributes._CREATE_TOUR_SCRIPT);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(ServerStaticAttributes._readTimeOut);
            httpURLConnection.setConnectTimeout(ServerStaticAttributes._connectTimeOut);
            httpURLConnection.setRequestMethod(ServerStaticAttributes._requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            JSONObject data = new JSONObject();

            data.put(TourStaticAttributes._DESTINATIONLOCATION, this.tour.getDestinationLocation());
            data.put(TourStaticAttributes._STARTLOCATION, this.tour.getStartLocation());
            data.put(TourStaticAttributes._STARTDATE_AND_TIME, this.tour.getStartDate());
            data.put(TourStaticAttributes._TOUR_DRIVER, this.tour.getTourDriver());
            data.put(TourStaticAttributes._PASSENGERS, this.tour.getPassengers());

            OutputStream outputStream = httpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);//, "UTF-8");

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(data.toString());
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
            Log.e("*****BREAK_POINT*****", "\nTourDataAsync: \n" + e.getMessage() + "\n======================");
            this.tour = null;
        }
        catch (IOException e)
        {
            Log.e("*****BREAK_POINT*****", "\nTourDataAsync: \n" + e.getMessage() + "\n======================");
            this.tour = null;
        }
        catch (JSONException e)
        {
            Log.e("*****BREAK_POINT*****", "\nTourDataAsync: \n" + e.getMessage() + "\n======================");
            this.tour = null;
        }
        catch (Exception e)
        {
            Log.e("*****BREAK_POINT*****", "\nTourDataAsync: \n" + e.getMessage() + "\n======================");
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
