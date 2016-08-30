package rs.elfak.mosis.drivetotravel.drivetotravel1.Bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

/**
 * Created by Alexa on 8/25/2016.
 */
public class BTClientAsyncTask extends AsyncTask<Void,String,String>
{
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:15:FF:F2:19:5F";

    private BluetoothAdapter BA;
    private BluetoothSocket BS;
    private BluetoothDevice BD;

    private String deviceName,deviceAddress;


    private InputStream mmInStream;
    private OutputStream mmOutStream;

    private ProgressDialog progressDialog;

    private boolean Work=true;

    private Resources res;

    private StringBuilder sb;

    int bufferSize = 4086;
    int bytesRead = -1;
    byte[] buffer = new byte[bufferSize];

    public String exitMessage;

    private AlertDialog.Builder builder;
    private UserLocalStore userLocalStore;


    public BTClientAsyncTask(Context context,String deviceName, String deviceAddress) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Friendship request");

        builder = new AlertDialog.Builder(context);
        userLocalStore = new UserLocalStore(context);

        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.res = Resources.getSystem();
        BA = BluetoothAdapter.getDefaultAdapter();

        sb = new StringBuilder();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.progressDialog.setMessage("Connecting...");
        this.progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {

    //Komunikacija
        Log.d("[KLIJENT]","Starting client task...");

        BD = BA.getRemoteDevice(deviceAddress);
        Work=true;

        try {
            BS = createBluetoothSocket(BD);
        }
        catch (IOException e) {
            Log.d("[KLIJENT]","Error: "+e.toString());
        }

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = BS.getInputStream();
            tmpOut = BS.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        try {
            BS.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!BS.isConnected())
        {
            Log.d("[KLIJENT]","Not connected");
            exitMessage="Can't connect to driver";
            finishWork();
        }
        else
        {
            publishProgress("Connected ... Sending request");
            Log.d("[KLIJENT]","Connected");

        }

        while(Work)
        {
            if(BS.isConnected()) {

                try {

                    //Send my ID to server side
                    String myID = userLocalStore.getPassenger().getId(); //Get ID of Passenger or Driver
                    if(!myID.isEmpty())
                    {
                        this.writeMessage(myID);
                    }
                    else
                    {
                        this.writeMessage("-1");
                    }

                    bytesRead = mmInStream.read(buffer);

                    if (bytesRead != -1) {
                        //Citanje podataka u string result
                        String receivedMsg = "";

                        while ((bytesRead == bufferSize) && (buffer[bufferSize - 1] != 0)) {
                            receivedMsg = receivedMsg + new String(buffer, 0, bytesRead);
                            bytesRead = mmInStream.read(buffer);
                        }

                        receivedMsg = receivedMsg + new String(buffer, 0, bytesRead);
                        sb.append(receivedMsg);


                        Log.d("[KLIJENT]", "Received msg:  " + receivedMsg);

                        if (receivedMsg.contains("Finish")) {
                            Log.d("[KLIJENT]", "Disconnecting");
                            exitMessage="You are now friends";
                            finishWork();
                            break;
                        }
                    }

                } catch (IOException e) {
                    break;
                }

            }
            else
            {
                Log.d("[KLIJENT]","Bluetooth not connected!");
                Work=false;
            }
        }

        return exitMessage;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        this.progressDialog.setMessage(values[0]);
        this.progressDialog.show();
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);

        if (this.progressDialog.isShowing())
            this.progressDialog.dismiss();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(exitMessage).setTitle("Friendship request").setPositiveButton("OK",null);

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e("[KLIJENT]","Error: "+e.getMessage());
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public void writeMessage(String message) {
        Log.d("[KLIJENT]", "Sending: " + message);

        byte[] msgBuffer = message.getBytes();

        try {
            mmOutStream.write(msgBuffer,0,message.length());
        } catch (IOException e) {
            Log.d("[KLIJENT]", "Error: " + e.getMessage());
        }
    }

    public void finishWork()
    {
        Work=false;

        try {
            BS.close();
        } catch (IOException e) {
            Log.d("[KLIJENT]","Can't disconnect from BT, BS.close");
        }
    }
}
