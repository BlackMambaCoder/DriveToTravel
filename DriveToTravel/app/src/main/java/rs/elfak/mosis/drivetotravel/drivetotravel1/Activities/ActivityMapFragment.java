package rs.elfak.mosis.drivetotravel.drivetotravel1.Activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.elfak.mosis.drivetotravel.drivetotravel1.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivityMapFragment extends Fragment {

    public ActivityMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_map, container, false);
    }
}
