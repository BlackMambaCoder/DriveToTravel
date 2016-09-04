package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

public class ShowTourDetailsActivity extends AppCompatActivity {

    TextView startLocation,endLocation,startDate,startTime,tourDriver;
    Button mapBtn;

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

        mapBtn = (Button) findViewById(R.id.tour_details_show_on_map_btn);

        Bundle savedTourBundle = getIntent().getExtras();
        final Tour tour = savedTourBundle.getParcelable("tour");

        startLocation.setText(tour.getStartLocation());
        endLocation.setText(tour.getDestinationLocation());

        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.ENGLISH);

        String Date = dateFormat.format(tour.getStartDate().getTime());
        String Time = timeFormat.format(tour.getStartDate().getTime());

        startDate.setText(Date);
        startTime.setText(Time);

        tourDriver.setText(tour.getTourDriver());

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(ShowTourDetailsActivity.this,tourMap.class);
                startActivity(mapIntent);
            }
        });
    }
}
