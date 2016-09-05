package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

/**
 * Created by Alexa on 9/3/2016.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Marker markerShowingInfoWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        markerShowingInfoWindow = marker;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // Getting view from the layout file info_window_layout
        View popUp = inflater.inflate(R.layout.custom_info_window_layout, null);

        TextView username = (TextView) popUp.findViewById(R.id.infowindow_username);
        TextView name = (TextView) popUp.findViewById(R.id.infowindow_name);
        TextView phone = (TextView) popUp.findViewById(R.id.infowindow_phone);
        TextView acc = (TextView) popUp.findViewById(R.id.infowindow_account);

        //Format data
        username.setText(marker.getTitle());

        //Account data
        String[] accData = marker.getSnippet().split(",");

        name.setText(accData[0]);
        phone.setText(accData[1]);
        acc.setText(accData[2]);

        // Returning the view containing InfoWindow contents
        return popUp;

    }
}
