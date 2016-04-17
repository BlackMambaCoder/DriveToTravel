package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

public class ActivityTourDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_tour_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle savedTourBundle = getIntent().getExtras();
        Tour savedTour = (Tour)savedTourBundle.getParcelable("tour");

        TextView tvStartLocationDetail = (TextView)findViewById(R.id.tvTourDetailStartLocation);
        tvStartLocationDetail.setText(savedTour.getStartLocation());

        TextView tvDestLocationDetail = (TextView)findViewById(R.id.tvTourDetailDestLocation);
        tvDestLocationDetail.setText(savedTour.getDestinationLocation());

        TextView tvStartDateTimeDetail = (TextView)findViewById(R.id.tvTourDetailStartDateTime);
        tvStartDateTimeDetail.setText(savedTour.getStartDate().toString());
    }

}
