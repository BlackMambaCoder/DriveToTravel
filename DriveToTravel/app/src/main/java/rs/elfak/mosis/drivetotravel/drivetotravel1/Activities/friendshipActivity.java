package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

public class friendshipActivity extends AppCompatActivity {

    private BluetoothAdapter BA;
    private ArrayAdapter listAdapter;
    private ArrayList deviceList,addressList;
    private BroadcastReceiver newDeviceReceiver;
    private ListView lista;
    private Context context;
    private Set<BluetoothDevice> pairedDevices;

    private boolean zavrsena_pretraga=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship);
        setTitle(R.string.friendship_activity_title);

        //Promenljive

        context = this;
        lista = (ListView) findViewById(R.id.friendship_bt_list);

        //BT liste
        deviceList = new ArrayList();
        addressList = new ArrayList();

        //Ukljucivanje bluetooth-a
        BA = BluetoothAdapter.getDefaultAdapter();

        //Broadcast receiver za pretragu uredjaja
        newDeviceReceiver= new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                Log.d("BT","Action: "+action);

                //Kada pronadje novi uredjaj
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    deviceList.add(device.getName());
                    addressList.add(device.getAddress());
                }

                //Kada se zavrsi pretraga
                else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                {
                        Toast.makeText(getApplicationContext(),R.string.bluetooth_search_finish_message,Toast.LENGTH_SHORT).show();

                        //Add paired devices
                        pairedDevices = BA.getBondedDevices();

                        for(BluetoothDevice bt : pairedDevices) {
                        deviceList.add(bt.getName());
                        addressList.add(bt.getAddress());
                        }

                        if(deviceList.size()>0) {
                            //Prikaz vidljivih uredjaja
                            ShowAlertDialogWithListview(getString(R.string.bluetooth_nearby_title), deviceList);
                        }
                        else {
                            Toast.makeText(context,getString(R.string.bluetooth_no_devices),Toast.LENGTH_SHORT).show();
                        }
                        zavrsena_pretraga=true;
                }

                //Kada je pretraga zapoceta
                else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                {
                    Toast.makeText(getApplicationContext(),R.string.bluetooth_search_start_message,Toast.LENGTH_SHORT).show();
                    deviceList.clear();
                    addressList.clear();
                    zavrsena_pretraga=false;
                }

            }
        };

        //Prijava aplikacije da hvata broadcast bluetooth-a za akcije ACTION_FOUND,STARTED,FINISHED DISCOVERY :)
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(newDeviceReceiver, filter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_friendship_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.friendship_search_button:
                //Search bez interneta ;)
                break;


            case R.id.friendship_add_button:
                //Dodavanje prijatelja preko bluetooth-a
                checkBluetooth();
                BA.startDiscovery();

            default:
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowAlertDialogWithListview(String title, ArrayList<String> data)
    {

        List<String> mData = data;

        //Create sequence of items
        final CharSequence[] DataSequence = mData.toArray(new String[mData.size()]);

        //Build dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setItems(DataSequence, dialogItemClick);
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }


    //Click on button
    private DialogInterface.OnClickListener dialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            String selectedText = deviceList.get(which).toString();    //Selected item in listview
            Toast.makeText(friendshipActivity.this,"Selected device: "+selectedText,Toast.LENGTH_SHORT).show();

            //Provera da li je korisnik vozac ili putnik

            //TODO: Proveri putnik/vozac i pokreni odredjenu komponentu
            UserLocalStore userLocalStore = new UserLocalStore(context);

            String loggedUser = userLocalStore.getTypeOfLoggedUser();

            if(loggedUser.equals("Driver"))
            {
                //Start server
            }
            else if(loggedUser.equals("Passanger"))
            {
                //Start client
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Kraj pretrage ako je bila zapoceta!
        BA.cancelDiscovery();
        unregisterReceiver(newDeviceReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkBluetooth();
    }

    void checkBluetooth()
    {
        if(!BA.isEnabled())
        {
            Toast.makeText(context,R.string.bluetooth_start_message,Toast.LENGTH_SHORT).show();
            Intent turnOnBluetooth = new Intent(BA.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnBluetooth,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Ako korisnik ne dozvoli ukljucivanje bluetooth-a
        if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}

