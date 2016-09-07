package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.MyConverter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class ShowTourDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView startLocation,endLocation,startDate,startTime,tourDriver,rankDriver;
    Button mapBtn,joinBtn;
    private UserLocalStore userLocalStore;
    private Passenger loggedInPassenger;
    private Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tour_details);

        setTitle(getString(R.string.title_activity_tour_details));

        startLocation = (TextView) findViewById(R.id.tour_details_start_location_text);
        endLocation = (TextView) findViewById(R.id.tour_details_end_location_text);
        startDate = (TextView) findViewById(R.id.tour_details_start_date_text);
        startTime = (TextView) findViewById(R.id.tour_details_start_time_text);
        tourDriver = (TextView) findViewById(R.id.tour_details_driver_name_text);
        rankDriver = (TextView) findViewById(R.id.tour_details_driver_rank);

        mapBtn = (Button) findViewById(R.id.tour_details_show_on_map_btn);
        joinBtn = (Button) findViewById(R.id.tour_details_attend_btn);


//        Bundle savedTourBundle = getIntent().getExtras();
//        final Tour tour = savedTourBundle.getParcelable("tour");

        UserLocalStore userLocalStore = new UserLocalStore(this);
        tour = userLocalStore.getTour();

        startLocation.setText(tour.getStartLocation());
        endLocation.setText(tour.getDestinationLocation());

        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.ENGLISH);

        String Date = dateFormat.format(tour.getStartDate().getTime());
        String Time = timeFormat.format(tour.getStartDate().getTime());

        startDate.setText(Date);
        startTime.setText(Time);

        tourDriver.setText(tour.getDriverUsername());
        rankDriver.setText(String.valueOf(tour.getRank()));

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(ShowTourDetailsActivity.this,tourMap.class);
                mapIntent.putExtra("tourid",tour.getId());
                startActivity(mapIntent);
            }
        });

        joinBtn.setOnClickListener(this);

        this.userLocalStore = new UserLocalStore(this);
        this.loggedInPassenger = this.userLocalStore.getPassenger();
        this.tour = this.userLocalStore.getTour();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tour_details_attend_btn:
                ServerRequest serverRequest = new ServerRequest(this);
                if (serverRequest.addPassengerToTour(this.loggedInPassenger.getId(), this.tour.getId()) == null)
                {
                    Toast.makeText(this,"Error on attending to tour",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"U are successfully joined on ride",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
