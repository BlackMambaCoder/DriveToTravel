package rs.elfak.mosis.drivetotravel.drivetotravel1.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

/**
 * Created by Alexa on 8/24/2016.
 */
public class BTServerThread extends Thread {
    //---------------------------------
    // Bluetooth promenljive

    private final BluetoothServerSocket mmServerSocket;
    private final BluetoothAdapter mBluetoothAdapter;

    private String NAME="Drive2Travel";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //---------------------------------

    // --------------------------------
    // Promenljive vezane za komunikaciju

    int bufferSize = 4086;
    int bytesRead = -1;
    byte[] buffer;

    InputStream inputStream;
    OutputStreamWriter outputStream;
    boolean COMMUNICATE;
    boolean WORK;

    // --------------------------------

    public BTServerThread(String deviceName)
    {
        BluetoothServerSocket tmp = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        NAME = deviceName;
        buffer = new byte[bufferSize];
        COMMUNICATE=true;
        WORK=true;

        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            Log.d("[SERVER]", "Started listening...");
        } catch (IOException e)
        {
            Log.d("[SERVER]", "Error: "+e.getMessage());
        }

        mmServerSocket = tmp;
    }

    public void run()
    {
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

                //1) Pravljenje stream-ova
                try {
                    inputStream = socket.getInputStream();
                    outputStream = new OutputStreamWriter(socket.getOutputStream());

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

                            while ((bytesRead == bufferSize) && (buffer[bufferSize - 1] != 0)) {
                                receivedMsg = receivedMsg + new String(buffer, 0, bytesRead);
                                bytesRead = inputStream.read(buffer);
                            }

                            receivedMsg = receivedMsg + new String(buffer, 0, bytesRead);
                            sb.append(receivedMsg);

                            //Obrada procitane poruke
                            Log.d("[COMMUNICATION]", "Server received: "+receivedMsg);

                            //Slanje podataka klijentu
                            String sendMsg = "Close";

                            try {
                                outputStream.write(sendMsg);
                                outputStream.flush();
                            }
                            catch (IOException e)
                            {
                                //Greska u slanju poruke
                                Log.d("[COMMUNICATION]", "Error: " + e.getMessage());
                                COMMUNICATE=false;
                            }
                        }

                    }
                    catch (IOException e) {
                        //Klijent se diskonektovao
                        Log.d("[COMMUNICATION]", "User "+user.getName()+" disconnected...");
                        COMMUNICATE = false;
                    }
                }

                //3)Zatvaranje konekcije

                try {
                    Log.d("[SERVER]", "Stopping listening...");
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    Log.d("[COMMUNICATION]", "Error: " + e.getMessage());
                }


            }
        }

        try {
            Log.d("[SERVER]", "Server stopping..");
            inputStream.close();
            outputStream.close();
            mmServerSocket.close();
            join(1000);
        } catch (IOException e) {
            Log.d("[SERVER]", "Error: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cancel()
    {

    }

    public void finishWork()
    {
        WORK=false;
        COMMUNICATE=false;
    }
}
