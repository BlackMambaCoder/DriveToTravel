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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LanguageChange;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class Register extends ActionBarActivity implements View.OnClickListener {

    private Button registerBtn;
    private TextView toLoginLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.registerBtn = (Button)findViewById(R.id.btnRegister);
        this.registerBtn.setOnClickListener(this);

        this.toLoginLabel = (TextView)findViewById(R.id.gotoLoginLabel);
        this.toLoginLabel.setOnClickListener(this);

        //Get language
        LanguageChange.getMyLanguage(this);

        UserLocalStore userLocalStore = new UserLocalStore(this);
        userLocalStore.setUserLoggedIn(false);
    }

    @Override
    public void onClick(View v) {

        int clickId = v.getId();

        Intent intent;
        ServerRequest serverRequest = new ServerRequest(this);

        switch (clickId)
        {
            case R.id.btnRegister:

                if (this.connectedToInternet() && this.checkFields())
                {
                    User user = this.FillUserAttributes();

                    RadioButton rBtnDriverUser = (RadioButton)findViewById(R.id.radioButtonRegisterActivityDriver);
                    //RadioButton rBtnPassengerUser = (RadioButton)findViewById(R.id.radioButtonRegisterActivityPassenger);

                    if (rBtnDriverUser.isChecked())
                    {
                        Driver driver = new Driver(user);

                        if (serverRequest.connectToServer(driver) && this.storeUserToLocalStore(driver))
                        {
                            //this.storeUserToLocalStore(driver);
                            intent = new Intent(this, ActivityDriverMain.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, "Error to store driver. Try in a few minutes again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        // Store passenger
                    }
                }

                break;

            case R.id.gotoLoginLabel:

                // Login activity
                intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();

                break;
        }
    }

    private User FillUserAttributes ()
    {
        EditText editText;
        String textFromLayout = "";
        User user = new User();

        editText = (EditText)findViewById(R.id.etRegName);
        textFromLayout = editText.getText().toString();
        user.setName(textFromLayout);

        editText = (EditText)findViewById(R.id.etRegSurname);
        textFromLayout = editText.getText().toString();
        user.setSurname(textFromLayout);

        editText = (EditText)findViewById(R.id.etRegUsername);
        textFromLayout = editText.getText().toString();
        user.setUsername(textFromLayout);

        editText = (EditText)findViewById(R.id.etRegPassword);
        textFromLayout = editText.getText().toString();
        user.setPassword(textFromLayout);

        return user;
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

    private boolean checkFields()
    {
        EditText editText;
        String toastString = "";
        boolean retValue = true;
        //User user = new User();

        editText = (EditText)findViewById(R.id.etRegName);
        if (editText.getText().toString().equals(""))
        {
            toastString += "Enter your name";
            retValue = false;
        }

        editText = (EditText)findViewById(R.id.etRegSurname);
        if (editText.getText().toString().equals(""))
        {
            if (!toastString.equals(""))
            {
                toastString += "\n";
            }

            toastString += "Enter your surname";
            retValue = false;
        }

        editText = (EditText)findViewById(R.id.etRegUsername);
        if (editText.getText().toString().equals(""))
        {
            if (!toastString.equals(""))
            {
                toastString += "\n";
            }

            toastString += "Enter your Username";
            retValue = false;
        }

        editText = (EditText)findViewById(R.id.etRegPassword);
        if (editText.getText().toString().equals(""))
        {
            if (!toastString.equals(""))
            {
                toastString += "\n";
            }

            toastString += "Enter your Password";
            retValue = false;
        }

        if (!retValue)
        {
            Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
        }

        return retValue;
    }

    private boolean storeUserToLocalStore(Driver driverToStore)
    {
        boolean retValue = false;
        UserLocalStore userLocalStore = new UserLocalStore(this);

        if (!userLocalStore.getUserLoggedIn())
        {
            userLocalStore.storeUser(driverToStore);
            userLocalStore.setUserLoggedIn(true);
            retValue = true;
        }

        return retValue;
    }
}
