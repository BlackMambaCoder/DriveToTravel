package rs.elfak.mosis.drivetotravel.drivetotravel1.Handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by LEO on 15.8.2016..
 */
public class ReceiveLocationHandler extends Handler {// implements Parcelable {

    private String locations;

//    protected ReceiveLocationHandler(Parcel in) {
//        locations = in.readString();
//    }
//
//    public static final Creator<ReceiveLocationHandler> CREATOR = new Creator<ReceiveLocationHandler>() {
//        @Override
//        public ReceiveLocationHandler createFromParcel(Parcel in) {
//            return new ReceiveLocationHandler(in);
//        }
//
//        @Override
//        public ReceiveLocationHandler[] newArray(int size) {
//            return new ReceiveLocationHandler[size];
//        }
//    };

    public ReceiveLocationHandler(Looper looper)
    {
        super(looper);
    }

    @Override
    public void handleMessage(Message inputMessage)
    {
        this.locations = (String)inputMessage.obj;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(locations);
//    }
}
