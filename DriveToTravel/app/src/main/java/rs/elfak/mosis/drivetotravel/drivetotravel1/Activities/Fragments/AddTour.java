package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.MyConverter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.TourServerRequest;

/**
 * Created by LEO on 1.4.2016..
 */
public class AddTour extends Fragment implements View.OnClickListener, View.OnTouchListener{

    private View myView;
    public static boolean dateSet = false;
    private EditText startDate;
    private EditText startTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        if (savedInstanceState == null)
//        {
//            savedInstanceState = new Bundle();
//        }

        super.onCreate(savedInstanceState);

        if (dateSet)
        {
            Bundle bundle = this.getArguments();

            int year = bundle.getInt("year");

            dateSet = false;
        }
//        savedInstanceState.putInt("year", -1);
//        savedInstanceState.putInt("month", -1);
//        savedInstanceState.putInt("day", -1);
//
//        this.setArguments(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.myView = inflater.inflate(R.layout.add_tour_fragment, container, false);

        Button btnAddTour = (Button) this.myView.findViewById(R.id.btnAddTour);
        btnAddTour.setOnClickListener(this);

//        Button btnDatePicker = (Button) this.myView.findViewById(R.id.btnSetStartdate);
//        btnDatePicker.setOnClickListener(this);
//
//        Button btnTimePicker = (Button) this.myView.findViewById(R.id.btnStartTime);
//        btnTimePicker.setOnClickListener(this);

        Button btnShowRoute = (Button) this.myView.findViewById(R.id.btnShowRoute);
        btnShowRoute.setOnClickListener(this);

//        if (dateSet)
//        {
//            this.getDateFromPicker();
//            dateSet = false;
//        }

        this.startDate = (EditText) this.myView.findViewById(R.id.etStartDate);
        this.startDate.setOnTouchListener(this);
        this.startTime = (EditText) this.myView.findViewById(R.id.etStartTime);
        this.startTime.setOnTouchListener(this);

        return this.myView;
    }

    private void getDateFromPicker()
    {
        Bundle bundle = this.getArguments();

        int year = bundle.getInt("year");
        int month = bundle.getInt("month");
        int day = bundle.getInt("day");
    }

    @Override
    public void onClick(View v)
    {
        FragmentManager fragmentManager = getFragmentManager();



        switch (v.getId())
        {
            case R.id.btnAddTour:


                Tour tour = this.AddTourFromInput();
                EditText startLocation = (EditText) this.myView.findViewById(R.id.etAddTourBeginLocation);
                EditText destinationLocation = (EditText) this.myView.findViewById(R.id.etAddTourDestinationLocation);
                EditText etStartDate = (EditText) this.myView.findViewById(R.id.etStartDate);
                EditText etStartTime = (EditText) this.myView.findViewById(R.id.etStartTime);
                tour.setStartLocation(startLocation.getText().toString());
                tour.setDestinationLocation(destinationLocation.getText().toString());

                Date startDate = MyConverter._String2Date(
                        etStartDate.getText().toString() +
                        "+" +
                        etStartTime.getText().toString());

                if (startDate == null)
                {
                    // error
                }

                else
                {
                    tour.setStartDateAndTime(startDate);
                    if (!TourServerRequest.AddTour(tour, this.myView.getContext()))
                    {
                        Toast.makeText(this.myView.getContext(),
                                "Couldn't add tour to DB",
                                Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        Toast.makeText(this.myView.getContext(),
                                "Tour added to DB",
                                Toast.LENGTH_LONG).show();
                    }
                }

                /**
                 * Add tour to db
                 */
                //Toast.makeText(this.myView.getContext(), "add tour", Toast.LENGTH_SHORT).show();

                break;

//            case R.id.btnStartTime:
//
//                Toast.makeText(this.myView.getContext(), "start time", Toast.LENGTH_SHORT).show();
//                break;

            case R.id.btnShowRoute:

                Toast.makeText(this.myView.getContext(), "Show route", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private Tour AddTourFromInput ()
    {
        EditText etStartLoc = (EditText) this.myView.findViewById(R.id.etAddTourBeginLocation);
        EditText etDestLoc = (EditText) this.myView.findViewById(R.id.etAddTourDestinationLocation);
        EditText etStartTime = (EditText) this.myView.findViewById(R.id.etStartTime);
        EditText etStartDate = (EditText) this.myView.findViewById(R.id.etStartDate);

        String startLoc = etStartLoc.getText().toString();
        String destLoc = etDestLoc.getText().toString();
        String startTime = etStartTime.getText().toString();
        String startDate = etStartDate.getText().toString();

        return new Tour(startLoc, destLoc, startDate, startTime,1234);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId())
        {
            case R.id.etStartDate:

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(this.myView.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                startDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

                break;

            case R.id.etStartTime:

                final Calendar calendar = Calendar.getInstance();
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(this.myView.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                    int minute) {
                                // Display Selected time in textbox
                                startTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                tpd.show();

                break;
        }

        return true;
    }
}
