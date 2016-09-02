package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LanguageChange;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;
import rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings.UserStaticAttributes;

public class Login extends ActionBarActivity implements View.OnClickListener{

    private Button loginBtn;
    private TextView registerTV;
    private EditText usernameET;
    private EditText passwordET;

    private Button testUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.loginBtn = (Button)findViewById(R.id.loginBtn);
        this.loginBtn.setOnClickListener(this);

        this.registerTV = (TextView)findViewById(R.id.registerLabel);
        this.registerTV.setOnClickListener(this);

        this.usernameET = (EditText)findViewById(R.id.etLoginActivityUsername);
        this.passwordET = (EditText)findViewById(R.id.etLoginActivityPassword);

        this.testUserButton = (Button) findViewById(R.id.testUser_button);
        testUserButton.setOnClickListener(this);

        Button testDriverButton = (Button) findViewById(R.id.testDriver_button);
        testDriverButton.setOnClickListener(this);

        //Get language
        LanguageChange.getMyLanguage(this);
    }



    @Override
    public void onClick(View v) {

        int onClickId = v.getId();

        Intent intent = null;

        switch (onClickId)
        {
            case R.id.loginBtn:

                if (this.connectedToInternet())// && this.checkFields())
                {
                    String username                 = this.usernameET.getText().toString();
                    String password                 = this.passwordET.getText().toString();

                    this.loginUser(username, password);
                }

                break;

            case R.id.registerLabel:

                // Go to register Activity
                intent = new Intent(this, Register.class);
                startActivity(intent);
                finish();

                break;

            case R.id.testUser_button:
                if (this.connectedToInternet() && this.checkFields())
                {
                    String username = "acko";
                    String password = "password";

                    this.loginUser(username, password);
                }
                break;

            case R.id.testDriver_button:
                if (this.connectedToInternet() && this.checkFields())
                {
                    String username = "leorado";
                    String password = "password";

                    this.loginUser(username, password);
                }
                break;
        }
    }

    private boolean connectedToInternet()
    {
        boolean retValue = false;

        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()
                == NetworkInfo.State.CONNECTED)
        {
            retValue = true;
        }

        else
        {
            Toast.makeText(this, R.string.connect_to_network, Toast.LENGTH_SHORT).show();
        }

        return retValue;
    }

    @Override
    public void onBackPressed() {
        //UserLocalStore userLocalStore = new UserLocalStore(this);

        finish();
    }

    private boolean checkFields()
    {
        boolean retValue = true;
        String toastString = "";

        if (this.usernameET.getText().toString().equals(""))
        {
            toastString += "Enter your username";
            retValue = false;
        }

        if (this.passwordET.getText().toString().equals(""))
        {
            if (!toastString.equals(""))
            {
                toastString += "\n";
            }

            toastString += "Enter your password";
            retValue = false;
        }

        if (!retValue)
        {
            Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
        }

        return retValue;
    }

    private void loginUser(String username, String password)
    {
        ServerRequest serverRequest     = new ServerRequest(this);
        JSONObject responseUser;//         = serverRequest.loginUser(username, password);
        UserLocalStore userLocalStore   = new UserLocalStore(this);


        if ((responseUser = serverRequest.loginUser(username, password)) != null)
        {
            int responseUserType;
            try
            {
                JSONObject meta_data = responseUser.getJSONObject("meta_data");
                responseUserType = meta_data.getInt(UserStaticAttributes._userType);
            }
            catch (JSONException e)
            {
                responseUserType = -1;
                e.printStackTrace();
            }

            Intent intent;
            if (responseUserType == User.USER_TYPE_DRIVER)
            {
                Driver loggedInUser =
                        Driver.getDriverFromJSONObject(responseUser);

                userLocalStore.storeUser(loggedInUser);

                intent = new Intent(this, DriversMainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (responseUserType == User.USER_TYPE_PASSENGER)
            {
                Passenger loggedInUser =
                        Passenger.getUserFromJSONObject(responseUser);

                userLocalStore.storeUser(loggedInUser);

                intent = new Intent(this, PassangerMainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(this, "False credentials", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this, "False credentials", Toast.LENGTH_LONG).show();
        }
    }
}
