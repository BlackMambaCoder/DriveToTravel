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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.MyConverter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class AddTourActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView startDate,startTime;
    private ImageButton dateBtn,timeBtn,addTour;

    public static int REQUEST_CODE_NEW_DRIVE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);

        Button btnAddTour = (Button) findViewById(R.id.btnAddTour);
        btnAddTour.setOnClickListener(this);

        this.startDate = (TextView) findViewById(R.id.start_date_label);
        this.startTime = (TextView) findViewById(R.id.start_time_label);

        this.dateBtn = (ImageButton) findViewById(R.id.btn_select_date);
        this.timeBtn = (ImageButton) findViewById(R.id.btn_select_time);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnAddTour:

                if(checkInputData())
                {
                    Tour tour = this.AddTourFromInput();

                    if (tour != null) {
                        ServerRequest serverRequest = new ServerRequest(this);
                        tour = serverRequest.storeTour(tour);

                        if (tour == null) {
                            Toast.makeText(this, "Tour is not sucessfully created", Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(this, "Tour id=" +  String.valueOf(tour.getId()) + " is created", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "Tour is sucessfully created", Toast.LENGTH_SHORT).show();
                        }
                    }

                    UserLocalStore tourLocalStore = new UserLocalStore(this);
                    tourLocalStore.storeTour(tour);

                    setResult(REQUEST_CODE_NEW_DRIVE);
                    finish();
                }

                break;
        }
    }


    private Tour AddTourFromInput ()
    {
        EditText etStartLoc     = (EditText) findViewById(R.id.etAddTourBeginLocation);
        EditText etDestLoc      = (EditText) findViewById(R.id.etAddTourDestinationLocation);
        TextView etStartTime    = (TextView) findViewById(R.id.start_time_label);
        TextView etStartDate    = (TextView) findViewById(R.id.start_date_label);

        String startLoc         = etStartLoc.getText().toString();
        String destLoc          = etDestLoc.getText().toString();
        String startTimeStr     = etStartTime.getText().toString();
        String startDateStr     = etStartDate.getText().toString();

        Tour retValue           = new Tour();
        UserLocalStore store    = new UserLocalStore(this);

        retValue.setStartLocation(startLoc);
        retValue.setDestinationLocation(destLoc);

//        Date startDate = MyConverter._String2Date(startDateStr + " " + startTimeStr);

        String[] dateStr = startDateStr.split("-");
        String[] timeStr = startTimeStr.split(":");

        Date startDate = new Date(
                Integer.valueOf(dateStr[2]) - 1900,
                Integer.valueOf(dateStr[1]),
                Integer.valueOf(dateStr[0]),
                Integer.valueOf(timeStr[0]),
                Integer.valueOf(timeStr[1])
        );

        if (startDate == null)
        {
            Toast.makeText(this, "Date error", Toast.LENGTH_SHORT).show();
            return null;
        }

        retValue.setStartDateAndTime(startDate);
        retValue.setDriver(store.getDriver().getId());

        return retValue;
    }

    private void selectDate()
    {
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
    }

    private void selectTime()
    {
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
    }

    private boolean checkInputData()
    {
        EditText etStartLoc     = (EditText) findViewById(R.id.etAddTourBeginLocation);
        EditText etDestLoc      = (EditText) findViewById(R.id.etAddTourDestinationLocation);
        TextView etStartTime    = (TextView) findViewById(R.id.start_time_label);
        TextView etStartDate    = (TextView) findViewById(R.id.start_date_label);

        String startLoc         = etStartLoc.getText().toString();
        String destLoc          = etDestLoc.getText().toString();
        String startTimeStr     = "";
        String startDateStr     = "";

        startTimeStr = etStartTime.getText().toString();
        startDateStr = etStartDate.getText().toString();


        if(startLoc.isEmpty())
        {
            Toast.makeText(this,"Please enter start loaction",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(destLoc.isEmpty())
        {
            Toast.makeText(this,"Please enter destination",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(startTimeStr.isEmpty())
        {
            Toast.makeText(this,"Please enter start time",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(startDateStr.isEmpty())
        {
            Toast.makeText(this,"Please enter start date",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
