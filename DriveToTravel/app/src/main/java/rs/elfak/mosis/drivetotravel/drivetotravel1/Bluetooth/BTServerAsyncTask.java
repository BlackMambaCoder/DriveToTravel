package rs.elfak.mosis.drivetotravel.drivetotravel1.Bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

/**
 * Created by Alexa on 8/25/2016.
 */
public class BTServerAsyncTask extends AsyncTask<Void,String,String>
{
    private BluetoothServerSocket mmServerSocket;
    private BluetoothAdapter mBluetoothAdapter;

    private String NAME="Drive2Travel";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    int bufferSize = 4086;
    int bytesRead = -1;
    byte[] buffer;

    InputStream inputStream;
    OutputStream outputStream;
    boolean COMMUNICATE;
    boolean WORK;

    private Resources res;
    private ProgressDialog progressDialog;

    private  AlertDialog.Builder builder;
    private  UserLocalStore userLocalStore;

    public BTServerAsyncTask(Context context, String deviceName)
    {
        BluetoothServerSocket tmp = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        NAME = deviceName;
        buffer = new byte[bufferSize];
        COMMUNICATE=true;
        WORK=true;

        builder = new AlertDialog.Builder(context);

        this.res = Resources.getSystem();
        progressDialog = new ProgressDialog(context);

        progressDialog.setTitle("Friendship request");

        userLocalStore = new UserLocalStore(context);

        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            Log.d("[SERVER]", "Started listening...");
        } catch (IOException e)
        {
            Log.d("[SERVER]", "Error: "+e.getMessage());
        }

        mmServerSocket = tmp;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.setMessage("Waiting for connection...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (this.progressDialog.isShowing())
            this.progressDialog.dismiss();


        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Finish").setTitle("Friendship request").setPositiveButton("OK",null);

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
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
    protected String doInBackground(Void... params) {
        BluetoothSocket socket = null;

        //Osluskuj konekcije
        while (WORK) {

            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.d("[SERVER]", "Error: "+e.getMessage());
                break;
            }
            // Ako je konekcija uspostavljena (Ovako radi samo 1 klijent sto je nama i potrebno, za N klijenata ubaciti kod u thread)
            if (socket != null) {

                //Komunikacija
                BluetoothDevice user = socket.getRemoteDevice();
                COMMUNICATE=true;

                Log.d("[SERVER]", "User connected "+user.getName());
                publishProgress("Connected device: "+user.getName());

                //1) Pravljenje stream-ova
                try {
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                } catch (IOException e) {
                    Log.d("[SERVER]", "Error: "+e.getMessage());
                    break;
                }

                //2)Citanje stream-ova
                while(COMMUNICATE)
                {
                    final StringBuilder sb = new StringBuilder();

                    try {
                        bytesRead = inputStream.read(buffer);

                        //Ako nesto ima da se procita
                        if (bytesRead != -1)
                        {
                            //Citanje podataka u string result
                            String receivedMsg= "";

                            while ((bytesRead == bufferSize) && (buffer[bufferSize - 1] != 0) && (buffer[bufferSize - 1] != '?')) {
                                receivedMsg = receivedMsg + new String(buffer, 0, bytesRead);
                                bytesRead = inputStream.read(buffer);
                            }

                            receivedMsg = receivedMsg + new String(buffer, 0, bytesRead);
                            sb.append(receivedMsg);

                            //Obrada procitane poruke
                            Log.d("[SERVER]", "Received: "+receivedMsg);

                            String myID = userLocalStore.getDriver().getId(); //Get ID of Passenger or Driver

                            if(myID.isEmpty())
                            {
                                myID="-1";
                            }

                            Log.d("[SERVER]","Is friends: "+myID+" and "+receivedMsg);

                            //Slanje podataka klijentu
                            String sendMsg = "Finish";

                            writeMessage(sendMsg);

                            publishProgress("Finish ");

                            //Exit
                            finishWork();
                        }

                    }
                    catch (IOException e) {
                        //Klijent se diskonektovao
                        Log.d("[SERVER]", "User "+user.getName()+" disconnected...");
                        finishWork();
                    }
                }


            }
        }

        try {
            Log.d("[SERVER]", "Server stopping..");
            mmServerSocket.close();
        } catch (IOException e) {
            Log.d("[SERVER]", "Error: " + e.getMessage());
        }

        return null;
    }

    private void writeMessage(String message) {
        Log.d("[SERVER]", "Sending: " + message);

        byte[] msgBuffer = message.getBytes();

        try {
            outputStream.write(msgBuffer);
            //outputStream.flush();
        }
        catch (IOException e)
        {
            //Greska u slanju poruke
            Log.d("[SERVER]", "Error: " + e.getMessage());
        }
    }

    private void finishWork()
    {
        try {
            Log.d("[SERVER]", "Stopping listening...");
            COMMUNICATE=false;
            WORK=false;
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Log.d("[SERVER]", "Error: " + e.getMessage());
        }
    }


}
