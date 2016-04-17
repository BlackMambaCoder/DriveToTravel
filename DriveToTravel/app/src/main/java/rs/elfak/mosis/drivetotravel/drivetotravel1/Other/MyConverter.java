package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by LEO on 11.4.2016..
 */
public class MyConverter
{
    public static Date _String2Date(String dateArg)
    {
        /**
         * dateArg = "Sat Apr 16 11:51:00 CEST 2016"
         */
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);

        Date retValue = new Date();

        try
        {
            retValue = simpleDateFormat.parse(dateArg);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return retValue;
    }

    public static String _StringList2String (List<String> stringListArg)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element :
                stringListArg) {
            stringBuilder.append(element + ",");
        }

        if (stringBuilder.length() > 0)
        {
            stringBuilder.deleteCharAt(stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    public static List<String> _String2StringList(String stringArg)
    {
        String[] stringsForList = stringArg.split(",");
        List<String> retValue = new ArrayList<>();

        for (String element :
                stringsForList) {
            retValue.add(element);
        }

        return retValue;
    }
}
