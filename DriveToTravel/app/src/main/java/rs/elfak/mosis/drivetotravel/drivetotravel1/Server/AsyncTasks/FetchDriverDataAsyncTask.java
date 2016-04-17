package rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.ServerStaticAttributes;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

/**
 * Created by LEO on 23.3.2016..
 */
public class FetchDriverDataAsyncTask extends AsyncTask <String, Void, Void>
{
    private Driver user;
    private ProgressDialog progressDialog;

    public FetchDriverDataAsyncTask(Context context/*GetDriverCallback callback, ProgressDialog progressDialog*/)
    {
        Log.w("*****BREAK_POINT*****", "ServerRequest FetchDriberDataAsyncTask constructor");
        this.user = null;

        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog.setMessage("Wait...Login");
        this.progressDialog.show();
    }

    public Driver getUser ()
    {
        return this.user;
    }

    @Override
    protected Void doInBackground(String... params) {

        String username = params[0];

        try
        {
            URL url = new URL(ServerStaticAttributes._serverAddress
                    + ServerStaticAttributes._serverPath
                    + ServerStaticAttributes._fetchDriverByUsername);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(ServerStaticAttributes._readTimeOut);
            httpURLConnection.setConnectTimeout(ServerStaticAttributes._connectTimeOut);
            httpURLConnection.setRequestMethod(ServerStaticAttributes._requestMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            JSONObject data = new JSONObject();
            data.put(UserStaticAttributes._username, username);

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
                String driverStringObject = StringManipulator.inputStreamToString(httpURLConnection.getInputStream());
                /**
                 * test substring in 'driverStringObject'
                 */

                if (driverStringObject.equals("ERROR"))
                {
                    throw new Exception("Such driver doesn't exists");
                }
                else if (driverStringObject.contains("Exception"))
                {
                    throw new Exception(driverStringObject);
                }

                else
                {
                    //JSONObject
                    org.json.simple.JSONObject obj = (org.json.simple.JSONObject) new JSONParser().parse(driverStringObject);

                    JSONObject mongoDBIdJsonObject = new JSONObject(driverStringObject);
                    String _$id = mongoDBIdJsonObject.getString(UserStaticAttributes._id);

                    JSONObject driverIdJsonObject = new JSONObject(_$id);

                    this.user = new Driver();

                    this.user.setId(driverIdJsonObject.getString("$id"));
                    this.user.setName((String) obj.get(UserStaticAttributes._name));
                    this.user.setSurname((String) obj.get(UserStaticAttributes._surname));
                    this.user.setUsername((String) obj.get(UserStaticAttributes._username));
                    this.user.setPassword((String) obj.get(UserStaticAttributes._password));
                    this.user.setPhoneNumber((String) obj.get(UserStaticAttributes._phoneNumber));
                    this.user.seteMail((String) obj.get(UserStaticAttributes._eMail));
                    this.user.setCarModel((String) obj.get(UserStaticAttributes._carModel));
                }


                //String getString = driverStringObject;
            }

            else
            {
                throw new Exception("Not valid connection. Response code: " + responseCode);
            }
        }
        catch (MalformedURLException e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest FetchDriverDataAsyncTask doInBackGround: " + e.getMessage());
            this.user = null;
        }
        catch (ProtocolException e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest FetchDriverDataAsyncTask doInBackGround: " + e.getMessage());
            this.user = null;
        }
        catch (JSONException e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest FetchDriverDataAsyncTask doInBackGround: " + e.getMessage());
            this.user = null;
        }
        catch (IOException e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest FetchDriverDataAsyncTask doInBackGround: " + e.getMessage());
            this.user = null;
        }
        catch (ParseException e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest FetchDriverDataAsyncTask doInBackGround: " + e.getMessage());
            this.user = null;
        }
        catch (Exception e)
        {
            Log.e("*****BREAK_POINT*****", "ServerRequest FetchDriverDataAsyncTask doInBackGround: " + e.getMessage());
            this.user = null;
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
