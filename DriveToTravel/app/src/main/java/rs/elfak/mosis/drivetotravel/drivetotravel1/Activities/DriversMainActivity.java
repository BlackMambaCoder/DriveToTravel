package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.CustomListAdapter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class DriversMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvTours;
    private Tour[] toursArray;
    private CustomListAdapter listAdapter;
    private Driver user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                startActivity(intent);
                Toast.makeText(this, "create tour", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private void getDriversTours()
    {
        ServerRequest serverRequest = new ServerRequest(this);
        this.toursArray = serverRequest.getDriverTours(this.user.getId());
    }

    private void prepareView()
    {
        this.lvTours = (ListView)findViewById(R.id.tourListDriver);

        this.getDriversTours();

        if (this.toursArray == null)
        {
            return;
        }

        this.listAdapter = new CustomListAdapter(DriversMainActivity.this, this.toursArray);
        this.lvTours.setAdapter(this.listAdapter);
    }

    private void getLoggedInUser()
    {
        UserLocalStore userLocalStore = new UserLocalStore(this);
        this.user = userLocalStore.getDriver();
        Toast.makeText(this, this.user.getUsername(), Toast.LENGTH_LONG).show();
    }
}
