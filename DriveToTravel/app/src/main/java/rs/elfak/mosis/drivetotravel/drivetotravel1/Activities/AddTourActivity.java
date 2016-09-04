package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.MyConverter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class AddTourActivity extends AppCompatActivity implements
        View.OnClickListener,
        View.OnTouchListener
{
    private EditText startDate;
    private EditText startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);

        Button btnAddTour = (Button) findViewById(R.id.btnAddTour);
        btnAddTour.setOnClickListener(this);

        Button btnShowRoute = (Button) findViewById(R.id.btnShowRoute);
        btnShowRoute.setOnClickListener(this);

        this.startDate = (EditText)findViewById(R.id.etStartDate);
        this.startDate.setOnTouchListener(this);
        this.startTime = (EditText)findViewById(R.id.etStartTime);
        this.startTime.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnAddTour:

                Tour tour                       = this.AddTourFromInput();
//
//                EditText etStartLocation        = (EditText) findViewById(R.id.etAddTourBeginLocation);
//                EditText etDestinationLocation  = (EditText) findViewById(R.id.etAddTourDestinationLocation);
//                EditText etStartDate            = (EditText) findViewById(R.id.etStartDate);
//                EditText etStartTime            = (EditText) findViewById(R.id.etStartTime);
//
//                String msg                      = etStartLocation.getText() + "; "
//                                                    + etDestinationLocation.getText() + "; "
//                                                    + etStartDate.getText() + "; "
//                                                    + etStartTime.getText();
//
//                tour.setStartLocation(etStartLocation.getText().toString());
//                tour.setDestinationLocation(etDestinationLocation.getText().toString());
//
//                Date startDate = MyConverter._String2Date(etStartDate.getText().toString());
//                if (startDate == null)
//                {
//                    Toast.makeText(this, "Date error", Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                if (tour != null)
                {
                    ServerRequest serverRequest = new ServerRequest(this);
                    tour = serverRequest.storeTour(tour);

                    if (tour == null)
                    {
                        Toast.makeText(this, "Tour = null", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "Tour id=" +  String.valueOf(tour.getId()) + " is created", Toast.LENGTH_SHORT).show();
                    }
                }

                finish();
                break;
        }
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

                DatePickerDialog dpd = new DatePickerDialog(this,
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
                TimePickerDialog tpd = new TimePickerDialog(this,
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



    private Tour AddTourFromInput ()
    {
        EditText etStartLoc     = (EditText) findViewById(R.id.etAddTourBeginLocation);
        EditText etDestLoc      = (EditText) findViewById(R.id.etAddTourDestinationLocation);
        EditText etStartTime    = (EditText) findViewById(R.id.etStartTime);
        EditText etStartDate    = (EditText) findViewById(R.id.etStartDate);

        String startLoc         = etStartLoc.getText().toString();
        String destLoc          = etDestLoc.getText().toString();
        String startTimeStr     = etStartTime.getText().toString();
        String startDateStr     = etStartDate.getText().toString();

        Tour retValue           = new Tour();
        UserLocalStore store    = new UserLocalStore(this);

        retValue.setStartLocation(startLoc);
        retValue.setDestinationLocation(destLoc);

        Date startDate = MyConverter._String2Date(startDateStr + " " + startTimeStr);
        if (startDate == null)
        {
            Toast.makeText(this, "Date error", Toast.LENGTH_SHORT).show();
            return null;
        }

        retValue.setStartDateAndTime(startDate);
        retValue.setDriver(store.getDriver().getId());

        return retValue;
    }
}
