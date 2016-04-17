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

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.GetDriverCallback;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */
public class StoreDriverDataAsyncTask extends AsyncTask<Void, Void, Void>
{
    private Driver user;
    private ProgressDialog progressDialog;

    public StoreDriverDataAsyncTask(Driver user, Context context)//, ProgressDialog progressDialog)
    {
        Log.w("*****BREAK_POINT*****", "ServerRequest storeDriverAsyncTask constructor");
        this.user = user;
        this.progressDialog = new ProgressDialog(context);
    }

    public Driver getDriver()
    {
        return this.user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog.setMessage("Wait...Register");
        this.progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try
        {
            URL url = new URL(ServerStaticAttributes._serverAddress
                    + ServerStaticAttributes._serverPath
                    + ServerStaticAttributes._createDriverScript);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(ServerStaticAttributes._readTimeOut);
            httpURLConnection.setConnectTimeout(ServerStaticAttributes._connectTimeOut);
            httpURLConnection.setRequestMethod(ServerStaticAttributes._requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            JSONObject data = new JSONObject();

            data.put(UserStaticAttributes._name, user.getName());
            data.put(UserStaticAttributes._surname, user.getSurname());
            data.put(UserStaticAttributes._username, user.getUsername());
            data.put(UserStaticAttributes._password, user.getPassword());
            data.put(UserStaticAttributes._phoneNumber, user.getPhoneNumber());
            data.put(UserStaticAttributes._eMail, user.geteMail());
            data.put(UserStaticAttributes._carModel, user.getCarModel());

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

                if (checkFeedback.contains("ERROR"))
                {
                    this.user = null;

                    throw new Exception(checkFeedback);
                }

                else
                {
                    this.user.setId(checkFeedback);
                }
            }

            else
            {
                throw new Exception("Dismissed connection: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            Log.w("*****BREAK_POINT*****", "ServerRequest storeDriverAsyncTask: " + e.getMessage());
            this.user = null;
            //Toast.makeText(context, "Error storeDriver: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.w("*****BREAK_POINT*****", "ServerRequest storeDriverAsyncTask: " + e.getMessage());
            this.user = null;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.w("*****BREAK_POINT*****", "ServerRequest storeDriverAsyncTask: " + e.getMessage());
            this.user = null;
        }
        catch (Exception e)
        {
            Log.w("*****BREAK_POINT*****", "ServerRequest storeDriverAsyncTask: " + e.getMessage());
            this.user = null;
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
