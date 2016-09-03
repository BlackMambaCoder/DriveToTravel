package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Passenger;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.User;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LanguageChange;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class  Register extends ActionBarActivity implements View.OnClickListener {

    private Button registerBtn;
    private TextView toLoginLabel;
    private ImageView profilePictureView;
    private Bitmap profileBitmap;
    private RadioButton selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        this.registerBtn = (Button)findViewById(R.id.btnRegister);
        this.registerBtn.setOnClickListener(this);
        this.selectedUser = (RadioButton) findViewById(R.id.radioButtonRegisterActivityPassenger);

        this.toLoginLabel = (TextView)findViewById(R.id.gotoLoginLabel);
        this.toLoginLabel.setOnClickListener(this);

        this.profilePictureView = (ImageView) findViewById(R.id.register_profile_image);

        //Click on profile picture
        this.profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

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

                        driver = serverRequest.storeUser(driver);

                        if (driver != null && this.storeUserToLocalStore(driver))
                        {
                            //this.storeUserToLocalStore(driver);
                            intent = new Intent(this, PassangerMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, R.string.error_to_store_driver_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        // Store passenger
                        Passenger passenger = new Passenger(user);

                        passenger = serverRequest.storeUser(passenger);

                        if (passenger != null && this.storeUserToLocalStore(passenger))
                        {
                            intent = new Intent(this, PassangerMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, R.string.error_to_store_driver_message, Toast.LENGTH_SHORT).show();
                        }
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

        User user;

        if(selectedUser.isChecked())
        {
          user = new Passenger();
        }
        else
        {
            user = new Driver();
        }

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
            toastString += R.string.enter_first_name_message;
            retValue = false;
        }

        editText = (EditText)findViewById(R.id.etRegSurname);
        if (editText.getText().toString().equals(""))
        {
            if (!toastString.equals(""))
            {
                toastString += "\n";
            }

            toastString += R.string.enter_last_name_message;
            retValue = false;
        }

        editText = (EditText)findViewById(R.id.etRegUsername);
        if (editText.getText().toString().equals(""))
        {
            if (!toastString.equals(""))
            {
                toastString += "\n";
            }

            toastString += R.string.enter_username_message;
            retValue = false;
        }

        editText = (EditText)findViewById(R.id.etRegPassword);
        if (editText.getText().toString().equals(""))
        {
            if (!toastString.equals(""))
            {
                toastString += "\n";
            }

            toastString += R.string.enter_password_message;
            retValue = false;
        }

        if (!retValue)
        {
            Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
        }

        return retValue;
    }

    private boolean storeUserToLocalStore(Driver userToStore)
    {
        boolean retValue = false;
        UserLocalStore userLocalStore = new UserLocalStore(this);

        if (!userLocalStore.getUserLoggedIn())
        {
            userLocalStore.storeUser(userToStore);
            userLocalStore.setUserLoggedIn(true);
            retValue = true;
        }

        return retValue;
    }

    private boolean storeUserToLocalStore(Passenger userToStore)
    {
        boolean retValue = false;
        UserLocalStore userLocalStore = new UserLocalStore(this);

        if (!userLocalStore.getUserLoggedIn())
        {
            userLocalStore.storeUser(userToStore);
            userLocalStore.setUserLoggedIn(true);
            retValue = true;
        }

        return retValue;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            profileBitmap = (Bitmap) extras.get("data");

            profilePictureView.setImageBitmap(profileBitmap);
        }
    }
}
