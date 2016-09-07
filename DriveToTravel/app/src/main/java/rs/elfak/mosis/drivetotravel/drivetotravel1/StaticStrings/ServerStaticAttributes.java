package rs.elfak.mosis.drivetotravel.drivetotravel1.StaticStrings;

/**
 * Created by LEO on 23.3.2016..
 */
public class ServerStaticAttributes
{
    public static int _readTimeOut = 10000;
    public static int _connectTimeOut = 15000;
    public static String _requestMethod = "POST";

    public static int USER_NAME_ERROR = 125;
    public static int PASSWORD_ERROR = 126;

    public static String _SERVER_ROOT_URL = "http://192.168.0.105";
    public static String _LOGIN_URL = "/user/login";
    public static String _REGISTER_URL = "/user/register";
    public static String UPLOAD_USER_IMAGE = "/user/upload_image";
    public static String USER_FRIEND_WITH = "/user/friend";
    public static String USER_ADD_FRIEND = "/user/add_friend";
    public static String USER_REMOVE_FRIEND = "/user/remove_friend";
    public static String UPDATE_USER_LOCATION = "/user/update_location";

    public static String CREATE_TOUR_URL = "/tour/create";
    public static String FETCH_DRIVERS_TOURS = "/tour/drivers";
    public static String FETCH_ALL_TOURS = "/tour/all";
    public static String UPDATE_TOUR_RANK = "/tour/update_rank";
    public static String SEARCH_TOUR_BY_LOCATION = "/tour/search_by_location";
    public static String ADD_PASSENGER_TO_TOUR = "/tour/add_passenger";
}
