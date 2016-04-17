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

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LanguageChange;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class Login extends ActionBarActivity implements View.OnClickListener{

    private Button loginBtn;
    private TextView registerTV;
    private EditText usernameET;
    private EditText passwordET;

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

        //Get language
        LanguageChange.getMyLanguage(this);
    }

    @Override
    public void onClick(View v) {

        int onClickId = v.getId();

        Intent intent;

        switch (onClickId) {
            case R.id.loginBtn:

                if (this.connectedToInternet() && this.checkFields())
                {
                    String username = "leorado"; //this.usernameET.getText().toString();

                    String password = "password";//this.passwordET.getText().toString();

                    ServerRequest serverRequest = new ServerRequest(this);

                    if (serverRequest.connectToServer(username, password))
                    {
                        Driver loggedInDriver = serverRequest.getDriver();

                        intent = new Intent(this, ActivityDriverMain.class);

                        UserLocalStore userLocalStore = new UserLocalStore(this);
                        userLocalStore.storeUser(loggedInDriver);
                        userLocalStore.setUserLoggedIn(true);

                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        if (serverRequest.getDriver() != null)
                        {
                            Toast.makeText(this, "false password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    intent = new Intent(this, ActivityDriverMain.class);
                    startActivity(intent);
                    finish();
                }

                break;

            case R.id.registerLabel:

                // Go to register Activity
                intent = new Intent(this, Register.class);
                startActivity(intent);
                finish();

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
}
