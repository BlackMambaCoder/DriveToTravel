package rs.elfak.mosis.drivetotravel.drivetotravel1.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import rs.elfak.mosis.drivetotravel.drivetotravel1.Threads.SendLocationThread;

/**
 * Created by LEO on 14.8.2016..
 */
public class LocationUpdateService extends Service {
    SendLocationThread locationThread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int userId = intent.getIntExtra("userid", -1);

        if (userId != -1)
        {
            this.locationThread = new SendLocationThread(userId);
            this.locationThread.run();
        }
        else
        {
            this.onDestroy();
        }

        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SendLocationThread.runThread = false;
    }
}
