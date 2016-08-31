package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 14.4.2016..
 */
public class FetchTourDataAsyncTask extends AsyncTask<String, Void, Void>
{
    public static int _fetchType = -1;
    public static String successMessage = "OK";

    private List<Tour> tours;
    private ProgressDialog progressDialog;

    public FetchTourDataAsyncTask(Context context)
    {
        this.tours = null;
        this.progressDialog = new ProgressDialog(context);
    }

    public List<Tour> getTours ()
    {
        return this.tours;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog.setMessage("Fetching tours...please wait");
        this.progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {

        String postValue = params[0];

//        Resources res = Resources.getSystem();

//        String routeUrl = res.getString(R.string.servers_url) + res.getString(R.string.fetch_tour_data);
        String routeUrl = ServerStaticAttributes.SERVER_ROOT_URL +
                            ServerStaticAttributes.SEARCH_TOURS_URL;

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

                if (responseData.contains("ERROR") || responseData.contains("error")) {
                    throw new Exception(responseData);
                }

                else if (responseData.contains("Exception"))
                {
                    throw new Exception(responseData);
                }

                else
                {
                    this.tours = Tour.getToursFromJsonArray(responseData);
                }
//                JsonReader reader = new JsonReader(new )
//
//                this.tour = new Tour();
//
//                JsonReader reader;
//
//                reader.beginArray();
//                reader.hasNext()
//
//                this.tour.setName((String)obj.get(UserStaticAttributes._name));
//                this.tour.setSurname((String) obj.get(UserStaticAttributes._surname));
//                this.tour.setUsername((String)obj.get(UserStaticAttributes._tourname));
//                this.tour.setPassword((String)obj.get(UserStaticAttributes._password));
//                this.tour.setPhoneNumber((String)obj.get(UserStaticAttributes._phoneNumber));
//                this.tour.seteMail((String)obj.get(UserStaticAttributes._eMail));
//                this.tour.setCarModel((String)obj.get(UserStaticAttributes._carModel));


                //String getString = driverStringObject;
            }

            else
            {
                throw new Exception("Not valid connection. Response code: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            successMessage = "FetchTourDataAsyncTask: ParseException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.tours = null;
        }
        catch (ProtocolException e)
        {
            successMessage = "FetchTourDataAsyncTask: ProtocolException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.tours = null;
        }
        catch (JSONException e)
        {
            successMessage = "FetchTourDataAsyncTask: JSONException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.tours = null;
        }
        catch (IOException e)
        {
            successMessage = "FetchTourDataAsyncTask: IOException - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.tours = null;
        }
        catch (Exception e)
        {
            successMessage = "FetchTourDataAsyncTask: Exception - " + e.getMessage();
            Log.e("*****BREAK_POINT*****", successMessage);
            this.tours = null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (this.progressDialog.isShowing())
            this.progressDialog.dismiss();
    }
}
