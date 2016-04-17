package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Driver;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.ServerRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        Log.w("Error", "no error in onCreate");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button:
                //this.GetDriver();
                Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void AddDriver ()
    {
        Driver user = new Driver();

        user.setName("Leonhard");
        user.setSurname("Radonic");
        user.setUsername("leorado");
        user.setPassword("password");
        user.seteMail("mail");
        user.setPhoneNumber("09871234");
        user.setCarModel("Mondeo");

//        ServerRequest serverRequest = new ServerRequest(this);
//        serverRequest.storeDriver(user);

        //Toast.makeText(this, serverRequest.retStr, Toast.LENGTH_SHORT).show();
    }

    private void GetDriver()
    {
        Log.w("*****BREAK_POINT*****", "MainActivity GetDriver");
        ServerRequest serverRequest = new ServerRequest(this);
        //serverRequest.fetchDriverByUsername("leorado");
    }
}
