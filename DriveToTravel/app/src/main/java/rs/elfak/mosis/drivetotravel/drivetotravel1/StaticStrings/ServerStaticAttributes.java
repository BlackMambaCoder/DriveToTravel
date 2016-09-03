package rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings;

/**
 * Created by LEO on 23.3.2016..
 */
public class ServerStaticAttributes
{
    public static String _serverAddress = "http://192.168.0.107";
    public static String _serverPath = "ProjectsPHP/DriveToTravel/mongoDB/";
    public static String _CREATE_TOUR_SCRIPT = "createTour.php";


    public static int _readTimeOut = 10000;
    public static int _connectTimeOut = 15000;
    public static String _requestMethod = "POST";

    public static int USER_NAME_ERROR = 125;
    public static int PASSWORD_ERROR = 126;

    public static String _SERVER_ROOT_URL = "http://192.168.0.106";
    public static String _LOGIN_URL = "/user/login";
    public static String _REGISTER_URL = "/user/register";
    public static String CREATE_TOUR_URL = "/tour/create";
    public static String FETCH_DRIVERS_TOURS = "/tour/drivers";
    public static String FETCH_ALL_TOURS = "/tour/all";
}
