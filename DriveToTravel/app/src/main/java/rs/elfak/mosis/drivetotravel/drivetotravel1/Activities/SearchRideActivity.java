package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rs.elfak.mosis.drivetotravel.drivetotravel1.R;



public class SearchRideActivity extends AppCompatActivity {

    public static int REQUEST_CODE = 2;

    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    ImageButton timeButton,dateButton;
    Button startSearch,dismissSearch;
    TextView timeText,dateText;
    EditText startEditText,stopEditText,distanceText;

    SimpleDateFormat dateFormatter;

    String startLocation,stopLocation,timeTravel,dateTravel,distanceTravel;

    boolean toogle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);
        setTitle(getString(R.string.title_activity_search_main));

        //Init
        timeButton = (ImageButton) findViewById(R.id.search_time_btn);
        dateButton = (ImageButton) findViewById(R.id.search_date_btn);
        timeText = (TextView) findViewById(R.id.search_time_text);
        dateText = (TextView) findViewById(R.id.search_date_text);
        startSearch = (Button) findViewById(R.id.search_accept_btn);
        dismissSearch = (Button) findViewById(R.id.search_dismiss_btn);

        startEditText = (EditText) findViewById(R.id.search_start_text);
        stopEditText = (EditText) findViewById(R.id.search_stop_text);
        distanceText = (EditText) findViewById(R.id.search_distance_edit_text);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        startLocation="";
        stopLocation="";
        timeTravel="";
        dateTravel="";
        distanceTravel="";
        toogle=false;


        //Search button
        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check data
                boolean notOk=false;

                if(toogle)
                {
                    distanceTravel = distanceText.getText().toString().replaceAll(" ","");

                    if(distanceTravel.isEmpty())
                    {
                        String errorMessage = getString(R.string.search_error_message);
                        Toast.makeText(SearchRideActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent();
                        intent.putExtra("Result", distanceTravel);
                        setResult(REQUEST_CODE, intent);
                        finish();
                    }
                }
                else {
                    startLocation = startEditText.getText().toString().replaceAll(" ", "");
                    stopLocation = stopEditText.getText().toString().replaceAll(" ", "");

                    if (startLocation.isEmpty()) {
                        notOk = true;
                    }

                    if (stopLocation.isEmpty()) {
                        notOk = true;
                    }

                    if (timeTravel.isEmpty()) {
                        notOk = true;
                    }

                    if (dateTravel.isEmpty()) {
                        notOk = true;
                    }


                    //If everything is ok
                    if (!notOk) {
                        String message = startLocation + "," + stopLocation + "," + timeTravel + "," + dateTravel;
                        Intent intent = new Intent();
                        intent.putExtra("Result", message);
                        setResult(REQUEST_CODE, intent);
                        finish();
                    } else {
                        String errorMessage = getString(R.string.search_error_message);
                        Toast.makeText(SearchRideActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Dismiss button
        dismissSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                Intent intent=new Intent();
                intent.putExtra("Result",message);
                setResult(REQUEST_CODE,intent);
                finish();
            }
        });


        //Time/Date pickers

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                String datumText = getString(R.string.date_label_activity_search_main);
                dateTravel = dateFormatter.format(newDate.getTime());

                dateText.setText(datumText+" "+dateTravel);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                String timeLabel = getString(R.string.time_label_activity_search_main);
                timeTravel = selectedHour+":"+selectedMinute;
               timeText.setText(timeLabel+" "+timeTravel);
            }
        },hour,minute,true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_search_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search_distance_toggle:
                //Promena enable-a
                toogle=!toogle;
                distanceText.setEnabled(toogle);
                timeButton.setEnabled(!toogle);
                dateButton.setEnabled(!toogle);
                startEditText.setEnabled(!toogle);
                stopEditText.setEnabled(!toogle);

                if(toogle)
                {
                    String searchByDistance = getString(R.string.search_by_distance);
                    Toast.makeText(SearchRideActivity.this,searchByDistance,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String searchByData = getString(R.string.search_by_data);
                    Toast.makeText(SearchRideActivity.this,searchByData,Toast.LENGTH_SHORT).show();
                }


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

