package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.CustomListAdapter;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

public class PassangerMainActivity extends AppCompatActivity {

    ListView listaVoznji;
    CustomListAdapter listAdapter;
    String start,stop;

    Tour[] tours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_main);
        setTitle("Home");

        listaVoznji = (ListView)findViewById(R.id.listView);

        tours = new  Tour[3];

        tours[0] = new Tour("Nis","Beograd","08-12-2016","23:9");
        tours[1] = new Tour("Zajecar","Bor","08-12-2016","23:9");
        tours[2] = new Tour("Negotin","Kladovo","08-12-2016","23:9");

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this android.R.layout.simple_list_item_1, android.R.id.text1, mobileArray);

        listAdapter = new CustomListAdapter(PassangerMainActivity.this,tours);
        listaVoznji.setAdapter(listAdapter);

       //Klik na item
       listaVoznji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Tour item =(Tour) adapterView.getItemAtPosition(i);
               // Toast.makeText(PassangerMainActivity.this, "Click: " + item.getStartLocation()+" "+item.getDestinationLocation(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PassangerMainActivity.this, ShowTourDetailsActivity.class);
                intent.putExtra("tour", item);

                startActivity(intent);
            }
        });


        //Long klik na item
        /*
        listaVoznji.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapterView, View v,
                                           int index, long arg3) {

                Tour item =(Tour) adapterView.getItemAtPosition(index);
                Toast.makeText(PassangerMainActivity.this, "Long click: " + item.getStartLocation()+" "+item.getDestinationLocation(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        */
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

            /*TODO: Ovde se salje serveru zahtev, promenljiva message a server treba da vrati
              rezultat pretrage
             */
        }
    }
}
