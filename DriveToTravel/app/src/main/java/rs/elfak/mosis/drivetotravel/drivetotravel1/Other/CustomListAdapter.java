package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

/**
 * Created by Alexa on 8/8/2016.
 */

public class CustomListAdapter extends BaseAdapter {

    Tour[] tour_list;

    private static LayoutInflater inflater=null;

    Context context;


    public CustomListAdapter(Context _context,Tour[] tours)
    {
        this.context = _context;
        this.tour_list = tours;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tour_list.length;
    }

    @Override
    public Object getItem(int position)
    {
        return tour_list[position];
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.passanger_driving_custom_list, null);

        holder.route = (TextView) rowView.findViewById(R.id.customlist_destination_txt);
        holder.date = (TextView) rowView.findViewById(R.id.customlist_date_txt);
        holder.time = (TextView) rowView.findViewById(R.id.customlist_time_txt);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.ENGLISH);

        String Date = dateFormat.format(tour_list[position].getStartDate().getTime());
        String Time = timeFormat.format(tour_list[position].getStartDate().getTime());

        holder.route.setText(tour_list[position].getStartLocation()+" - "+tour_list[position].getDestinationLocation());
        holder.date.setText(Date);
        holder.time.setText(Time);

        return rowView;
    }
}
