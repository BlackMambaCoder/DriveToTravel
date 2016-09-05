package rs.elfak.mosis.drivetotravel.drivetotravel1.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Alexa on 8/24/2016.
 */
public class BTClientThread extends Thread {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:15:FF:F2:19:5F";

    private BluetoothAdapter BA;
    private BluetoothSocket BS;
    private BluetoothDevice BD;

    private String deviceName,deviceAddress;
    private StringBuilder sb;

    private InputStream mmInStream;
    private OutputStream mmOutStream;


    public boolean Work=true;

    public BTClientThread(String serverDeviceName, String serverDeviceAddress)
    {
        deviceName = serverDeviceName;
        deviceAddress = serverDeviceAddress;

        BA = BluetoothAdapter.getDefaultAdapter();
    }

    public void run()
    {
        Log.d("[KLIJENT]","Starting client thread...");

        BD = BA.getRemoteDevice(deviceAddress);

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

        byte[] buffer = new byte[256];
        int bytes;

        Work=true;

        while(Work)
        {
            if(BS.isConnected()) {

                try {

                    //Sending Hello
                    writeMessage("Hello");

                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    if(bytes>0)
                    {
                        String receivedData = new String(buffer);
                        Log.d("[KLIJENT]","Received msg:  "+receivedData);

                        if(receivedData.contains("Close"))
                        {
                            Log.d("[KLIJENT]","Disconnecting");
                            finishWork();
                            break;
                        }
                    }

                    // messageHandler.obtainMessage(1, bytes, -1, buffer).sendToTarget();

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

        //Finish thread
        try {
            join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    private void writeMessage(String message) {
        Log.d("[KLIJENT]", "Sending: " + message);

        byte[] msgBuffer = message.getBytes();

        try {
            mmOutStream.write(msgBuffer);
        } catch (IOException e) {
            Log.d("[KLIJENT]", "Error: " + e.getMessage());
        }
    }
}
