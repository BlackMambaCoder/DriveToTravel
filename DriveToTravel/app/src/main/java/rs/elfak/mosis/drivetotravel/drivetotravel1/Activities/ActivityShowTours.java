package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

public class ActivityShowTours extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<String> upcomingTourNameList;
    private List<Tour> upcomingTourList;
    private ListView tourListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_show_tours);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.upcomingTourList = new ArrayList<>();
        this.upcomingTourNameList = new ArrayList<>();
        
        this.tourListView = (ListView)findViewById(R.id.lvActivityShowTours);
        this.tourListView.setOnItemClickListener(this);

        this.getTours();

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,this.upcomingTourNameList);

        this.tourListView.setAdapter(itemsAdapter);
    }

    private void getTours ()
    {
//        this.upcomingTourList = TourServerRequest.getTours(this);

        for (Tour currentTour :
                this.upcomingTourList) {
            this.upcomingTourNameList.add(
                    currentTour.getStartLocation() + " - " + currentTour.getDestinationLocation());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Tour selectedTour = this.upcomingTourList.get(position);

        Intent intent = new Intent(ActivityShowTours.this, ShowTourDetailsActivity.class);
        intent.putExtra("tour", selectedTour);

        startActivity(intent);
        
    }
}
