package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.CustomListAdapter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

public class PassangerMainActivity extends AppCompatActivity {

    ListView listaVoznji;
    String start,stop;

    String[] drive = {"Zaječar - Niš", "Niš - Zaječar","Beograd - Novi Sad","Negotin - Bor","Bor - Negotin"};
    String[] date  = {"08/08/2016","08/08/2016","10/08/2016","09/08/2016","09/08/2016"};
    String[] time =  {"05:30","14:00","10:00","05:30","16:00"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_main);
        setTitle("Home");

        listaVoznji = (ListView)findViewById(R.id.listView);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this android.R.layout.simple_list_item_1, android.R.id.text1, mobileArray);

        listaVoznji.setAdapter(new CustomListAdapter(PassangerMainActivity.this,drive,date,time));
    }

    private void showSearchDialog()
    {
        //Kreiranje dijaloga
        final Dialog dialog = new Dialog(PassangerMainActivity.this);

        //Postavljanje layout-a
        dialog.setContentView(R.layout.passanger_search_activity);
        dialog.setTitle("Pretraga vožnji");

        dialog.show();

        final Button cancel_btn = (Button) dialog.findViewById(R.id.passanger_dismiss_btn);
        final Button accpet_btn = (Button) dialog.findViewById(R.id.passanger_search_btn);

        final EditText start_text = (EditText) dialog.findViewById(R.id.search_start_txt);
        final EditText stop_text = (EditText) dialog.findViewById(R.id.search_stop_text);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        accpet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start = start_text.getText().toString();
                stop = stop_text.getText().toString();

                if(!start.isEmpty() && !stop.isEmpty())
                {
                    Toast.makeText(PassangerMainActivity.this,"Podaci za pretragu: "+start+" - "+stop,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(PassangerMainActivity.this,"Nisu unešeni svi podaci!!!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.passanger_main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.passanger_menu_search_btn:
                //Open search dialog
                //showSearchDialog();
                Intent intent = new Intent(this, SearchRideActivity.class);
                startActivityForResult(intent,SearchRideActivity.REQUEST_CODE);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SearchRideActivity.REQUEST_CODE)
        {
            String message=data.getStringExtra("Result");
            Toast.makeText(PassangerMainActivity.this,"Search: "+message,Toast.LENGTH_SHORT).show();
            //textView1.setText(message);
        }
    }
}
