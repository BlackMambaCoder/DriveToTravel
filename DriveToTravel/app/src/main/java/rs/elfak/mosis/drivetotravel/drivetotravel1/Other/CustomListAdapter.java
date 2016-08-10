package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

/**
 * Created by Alexa on 8/8/2016.
 */

public class CustomListAdapter extends BaseAdapter {

    String[] driveText;
    String[] dateText;
    String[] timeText;

    private static LayoutInflater inflater=null;

    Context context;


    public CustomListAdapter(Context _context,String[] _drive,String[] _date,String[] _time)
    {
        this.context = _context;
        this.driveText = _drive;
        this.dateText = _date;
        this.timeText = _time;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return driveText.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView route,date,time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.passanger_driving_custom_list, null);

        holder.route = (TextView) rowView.findViewById(R.id.customlist_destination_txt);
        holder.date = (TextView) rowView.findViewById(R.id.customlist_date_txt);
        holder.time = (TextView) rowView.findViewById(R.id.customlist_time_txt);

        holder.route.setText(driveText[position]);
        holder.date.setText(dateText[position]);
        holder.time.setText(timeText[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
