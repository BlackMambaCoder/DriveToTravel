package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

public class ActivityDriverMain extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener
    , DialogInterface.OnClickListener
{

    private ListView mainListView;
    private ArrayList<String> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activity_driver_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mainListView = (ListView)findViewById(R.id.lvActivityDriverMain);
        this.mainListView.setOnItemClickListener(this);

        this.menuItems = new ArrayList<String>();
        this.menuItems.add("Create Tour");
        this.menuItems.add("View tours");
        this.menuItems.add("View map");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,this.menuItems);
        this.mainListView.setAdapter(itemsAdapter);

        /**
         * Action Bar setting
         */

        Toolbar menuToolBar = (Toolbar)findViewById(R.id.toolbarDriverMainActivityMenu);
        setSupportActionBar(menuToolBar);

        //testMethod();
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent;

        switch (position)
        {
            case 0:
                intent = new Intent(this, AddTourActivity.class);
                startActivity(intent);
                break;

            case 1:
                intent = new Intent(this, ActivityShowTours.class);
                startActivity(intent);
                break;

            case 2:
                Toast.makeText(this,"2", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                Toast.makeText(this,"3", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void testMethod()
    {
        UserLocalStore userLocalStore = new UserLocalStore(this);

        Driver driver = userLocalStore.getDriver();

        String toastString = "";
        toastString += "Id: " + driver.getId() + "\n";
        toastString += "Name: " + driver.getName() + "\n";
        toastString += "Surname: " + driver.getSurname() + "\n";
        toastString += "Username: " + driver.getUsername() + "\n";
        toastString += "Password: " + driver.getPassword() + "\n";
        toastString += "eMail: " + driver.geteMail() + "\n";
        toastString += "Phone number: " + driver.getPhoneNumber() + "\n";
        toastString += "Car model: " + driver.getCarModel() + "\n";
        toastString += "Logged in: ";

        if (userLocalStore.getUserLoggedIn())
        {
            toastString += "true";
        }
        else
        {
            toastString += "false";
        }

        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean retValue = false;

        Intent intent;

        switch (item.getItemId())
        {
            case R.id.menu_driver_main_logout_action:

                UserLocalStore userLocalStore = new UserLocalStore(this);
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                retValue = true;

                break;

            default:
                retValue = super.onOptionsItemSelected(item);
        }

        return retValue;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();


    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Closing Activity");
        builder.setMessage("Are you sure you want to close? You will be logged out.");
        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("Cancel", this);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        switch (which)
        {
            case -1:
                UserLocalStore userLocalStore = new UserLocalStore(this);
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }
    }
}
