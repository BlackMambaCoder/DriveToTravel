//package rs.elfak.mosis.drivetotravel.drivetotravel1.Server;
//
//import android.content.Context;
//import android.util.Log;
//
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Entities.Tour;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Model.UserLocalStore;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.FetchTourDataAsyncTask;
//import rs.elfak.mosis.drivetotravel.drivetotravel1.Server.AsyncTasks.StoreTourDataAsyncTask;
//
///**
// * Created by LEO on 6.4.2016..
// */
//public class TourServerRequest
//{
////    public static boolean AddTour (Tour tourArg, Context context)
////    {
////        boolean retValue = false;
////
////        StoreTourDataAsyncTask storeTour = new StoreTourDataAsyncTask(tourArg, context);
////
////        try
////        {
////            storeTour.execute().get();
////
////            if (storeTour.getTour() != null)
////            {
////                retValue = true;
////            }
////        }
////        catch (InterruptedException e)
////        {
////            Log.e("*****BREAK_POINT*****", "\nServerRequest store tour: \n" + e.getMessage() + "\n======================");
////        }
////        catch (ExecutionException e)
////        {
////            Log.e("*****BREAK_POINT*****", "\nServerRequest store tour: \n" + e.getMessage() + "\n======================");
////        }
////
////        return retValue;
////    }
////
////    public static List<Tour> getTours(Context context)
////    {
////        FetchTourDataAsyncTask fetchTourDataAsyncTask = new FetchTourDataAsyncTask(context);
////
////        List<Tour> returnValue = null;
////
////        try
////        {
////            FetchTourDataAsyncTask._fetchType = 1;
////            fetchTourDataAsyncTask.execute(new UserLocalStore(context).getDriver().getId()).get();
////            returnValue = fetchTourDataAsyncTask.getTours();
////        }
////        catch (InterruptedException e)
////        {
////            Log.e("*****BREAK_POINT*****", "TourServerRequest/getTours: " + e.getMessage());
////            returnValue = null;
////        }
////        catch (ExecutionException e)
////        {
////            Log.e("*****BREAK_POINT*****", "TourServerRequest/getTours: " + e.getMessage());
////            returnValue = null;
////        }
////
////        return returnValue;
////    }
//}
