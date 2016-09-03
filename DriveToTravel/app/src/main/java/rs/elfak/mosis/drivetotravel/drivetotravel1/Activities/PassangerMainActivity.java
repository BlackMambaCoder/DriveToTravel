package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import org.json.JSONArray;

import java.util.ArrayList;


import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.CustomListAdapter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.Location;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.StringManipulator;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Services.LocationUpdateService;

public class PassangerMainActivity extends AppCompatActivity {

    ListView listaVoznji;
    CustomListAdapter listAdapter;
    String start,stop;

    Tour[] tours;
    boolean toggleLocationNotification      = false;
    private String locations                = "";
    ArrayList<Location> locationsList       = null;

    public static Handler publicHandler            = null;

    private Passenger user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_main);
        setTitle("Home");

//        listaVoznji = (ListView)findViewById(R.id.listView);
//
//        tours = new  Tour[3];
//
//        tours[0] = new Tour("Nis","Beograd","08-12-2016","23:9",0, 0);
//        tours[1] = new Tour("Zajecar","Bor","08-12-2016","23:9",0, 1);
//        tours[2] = new Tour("Negotin","Kladovo","08-12-2016","23:9",0, 2);
//
//        listAdapter = new CustomListAdapter(PassangerMainActivity.this,tours);
//        listaVoznji.setAdapter(listAdapter);



        //Long klik na item
        /*
        listaVoznji.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                if (!start.isEmpty() && !stop.isEmpty()) {
                    Toast.makeText(PassangerMainActivity.this, "Podaci za pretragu: " + start + " - " + stop, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(PassangerMainActivity.this, "Nisu unešeni svi podaci!!!", Toast.LENGTH_SHORT).show();
                }


            public boolean onItemLongClick(AdapterView<?> adapterView, View v,
                                           int index, long arg3) {

                Tour item =(Tour) adapterView.getItemAtPosition(index);
                Toast.makeText(PassangerMainActivity.this, "Long click: " + item.getStartLocation()+" "+item.getDestinationLocation(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        */

        this.getLoggedInUser();
        this.prepareView();

        //Klik na item
        listaVoznji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Tour item =(Tour) adapterView.getItemAtPosition(i);
                // Toast.makeText(PassangerMainActivity.this, "Click: " + item.getStartLocation()+" "+item.getDestinationLocation(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PassangerMainActivity.this, ShowTourDetailsActivity.class);
                intent.putExtra("tour", item);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.passanger_main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.passanger_menu_search_btn:
                //Open search dialog
                //showSearchDialog();

                Intent intent = new Intent(this, SearchRideActivity.class);
                startActivityForResult(intent,SearchRideActivity.REQUEST_CODE);


                break;

            case R.id.passanger_main_menu_activate_location_notification:

                if (!this.toggleLocationNotification)
                {
                    this.toggleLocationNotification = true;

                    /**
                     *  User id
                     */
                    this.startLocationUpdateService(-1);
                    Toast.makeText(this, "Location update activated", Toast.LENGTH_LONG).show();
                }
                else
                {
                    this.toggleLocationNotification = false;
                    this.stopLocationUpdateService();
                    Toast.makeText(this, "Location update deactivated", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.passanger_main_menu_friendship:
                Intent intent_fr = new Intent(this, friendshipActivity.class);
                startActivity(intent_fr);
                break;

            default:
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SearchRideActivity.REQUEST_CODE)
        {
            String message=data.getStringExtra("Result");
            Toast.makeText(PassangerMainActivity.this,"Search: "+message,Toast.LENGTH_SHORT).show();
            //textView1.setText(message);

            /*TODO: Ovde se salje serveru zahtev, promenljiva message a server treba da vrati
              rezultat pretrage
             */
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        this.stopLocationUpdateService();
    }

    private void startLocationUpdateService(int userId)
    {
        Intent intent = new Intent(this, LocationUpdateService.class);
        intent.putExtra("userid", userId);
        startService(intent);
    }

    private void stopLocationUpdateService()
    {
        stopService(new Intent(this, LocationUpdateService.class));
    }

    private void getReceiveLocationMessages()
    {
        final Context context = this;
        publicHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                locations = (String)msg.obj;

                JSONArray arrayWithLocations = StringManipulator.stringToJSONArray(locations);
                locationsList = Location.getLocationsFromJSONArray(arrayWithLocations);

                // update UI
            }
        };
    }

    private void prepareView()
    {
        this.listaVoznji = (ListView)findViewById(R.id.listView);

        this.getAllTours();

        if (this.tours == null)
        {
            return;
        }

        this.listAdapter = new CustomListAdapter(PassangerMainActivity.this, this.tours);
        this.listaVoznji.setAdapter(this.listAdapter);
    }

    private void getLoggedInUser()
    {
        UserLocalStore userLocalStore = new UserLocalStore(this);
        this.user = userLocalStore.getPassenger();
        Toast.makeText(this, this.user.getUsername(), Toast.LENGTH_LONG).show();
    }

    private void getAllTours()
    {
        ServerRequest serverRequest = new ServerRequest(this);
        this.tours = serverRequest.getAllTours();
    }
}
