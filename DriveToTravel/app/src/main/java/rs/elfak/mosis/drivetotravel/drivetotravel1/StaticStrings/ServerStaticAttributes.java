package rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings;

/**
 * Created by LEO on 23.3.2016..
 */
public class ServerStaticAttributes
{
    public static String _serverAddress = "http://192.168.0.170/";
    public static String _serverPath = "ProjectsPHP/DriveToTravel/mongoDB/";
    public static String _createDriverScript = "createDriver.php?";
    public static String _fetchDriverByUsername = "getDriverByUsername.php?";

    public static String _CREATE_TOUR_SCRIPT = "createTour.php";


    public static int _readTimeOut = 10000;
    public static int _connectTimeOut = 15000;
    public static String _requestMethod = "POST";

    public static String SERVER_ROOT_URL = "http://192.168.0.103";

    public static String REGISTER_URL           = "/user/register";
    public static String LOGIN_URL              = "/user/login";
    public static String ADD_FRIEND_URL         = "/user/add_friend";
    public static String REMOVE_FRIEND_URL      = "/user/remove_friend";
    public static String FRIEND_WITH_URL        = "/user/friend_with";
    public static String UPDATE_LOCATION_URL    = "/user/update_location";

    public static String STORE_TOUR             = "/tour/create";
    public static String SEARCH_TOURS_URL       = "/tour/search_tour";

}
