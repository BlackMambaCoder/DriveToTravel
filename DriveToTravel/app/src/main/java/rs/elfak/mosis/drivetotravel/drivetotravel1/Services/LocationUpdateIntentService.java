//package rs.elfak.mosis.drivetotravel.drivetotravel1.Services;
//
//import android.app.IntentService;
//import android.app.Service;
//import android.content.Intent;
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.ResultReceiver;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Other.LocListener;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.SendDeviceLocationDataAsyncTask;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Threads.SendLocationThread;
//
///**
// * An {@link IntentService} subclass for handling asynchronous task requests in
// * a service on a separate handler thread.
// * <p/>
// * TODO: Customize class - update intent actions, extra parameters and static
// * helper methods.
// */
//public class LocationUpdateIntentService extends IntentService {
//
//
//    public static final int STATUS_RUNNING          = 675;
//    public static final int STATUS_FINISHED         = 676;
//    public static final int STATUS_ERROR            = 677;
//
//    private boolean stopWorking = true;
//
//    public LocationUpdateIntentService() {
//        super(LocationUpdateIntentService.class.getName());
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        SendLocationThread thread = new SendLocationThread();
//        thread.run();
//    }
//
////    /**
////     * Handle action Foo in the provided background thread with the provided
////     * parameters.
////     */
////    private void handleActionFoo(String param1, String param2) {
////        // TODO: Handle action Foo
////        throw new UnsupportedOperationException("Not yet implemented");
////    }
////
////    /**
////     * Handle action Baz in the provided background thread with the provided
////     * parameters.
////     */
////    private void handleActionBaz(String param1, String param2) {
////        // TODO: Handle action Baz
////        throw new UnsupportedOperationException("Not yet implemented");
////    }
//}
