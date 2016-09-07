package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.DriverManager;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.CustomListAdapter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class DriversMainActivity extends AppCompatActivity implements View.OnClickListener, Comparator<Tour> {

    private ListView lvTours;
    private Tour[] toursArray;
    private List<Tour> toursList;
    private CustomListAdapter listAdapter;
    private Driver user;

    private boolean sortAlphabetic = true;
    private int sortFlag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drivers_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Home");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_tour_button);
        fab.setOnClickListener(this);

        this.getLoggedInUser();
        this.prepareView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.create_tour_button:
                Intent intent = new Intent(this, AddTourActivity.class);
                startActivityForResult(intent, AddTourActivity.REQUEST_CODE_NEW_DRIVE);
                Toast.makeText(this, "create tour", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private void getDriversTours()
    {
        ServerRequest serverRequest = new ServerRequest(this);
        this.toursList = serverRequest.getDriverTours(this.user.getId(), true);

        if (toursList == null)
        {
            this.toursArray = null;
            return;
        }

        this.toursArray = Tour.getArrayFromList(this.toursList);
    }

    private void prepareView()
    {
        this.lvTours = (ListView)findViewById(R.id.tourListDriver);

        this.getDriversTours();

        this.fillTourListView();
    }

    private void getLoggedInUser()
    {
        UserLocalStore userLocalStore = new UserLocalStore(this);
        this.user = userLocalStore.getDriver();
        Toast.makeText(this, this.user.getUsername(), Toast.LENGTH_LONG).show();
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
            case R.id.passanger_main_menu_friendship:
                Intent intent_fr = new Intent(this, friendshipActivity.class);
                startActivity(intent_fr);
                break;

            case R.id.passanger_main_menu_sort:
                sortTours();
                break;

            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AddTourActivity.REQUEST_CODE_NEW_DRIVE)
        {
            Tour newTour = new UserLocalStore(this).getTour();
            this.toursList.add(newTour);

            this.toursArray = Tour.getArrayFromList(this.toursList);

            this.fillTourListView();
        }
    }

    private void fillTourListView()
    {
        if (this.toursArray == null)
        {
            return;
        }

        this.listAdapter = new CustomListAdapter(DriversMainActivity.this, this.toursArray);
        this.lvTours.setAdapter(this.listAdapter);
    }

    private void sortTours()
    {
        this.sortFlag = 1;

        if (this.sortAlphabetic)
        {
            this.sortFlag = -1;
        }

        this.sortAlphabetic = !this.sortAlphabetic;
        Collections.sort(this.toursList, this);

        this.toursArray = Tour.getArrayFromList(this.toursList);
        this.fillTourListView();
    }

    @Override
    public int compare(Tour lhs, Tour rhs) {
        return lhs.getStartLocation().compareTo(rhs.getStartLocation()) < 0 ? sortFlag : (-1) * sortFlag;
    }
}
