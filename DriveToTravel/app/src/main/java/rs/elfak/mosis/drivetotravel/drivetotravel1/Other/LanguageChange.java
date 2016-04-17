package rs.elfak.mosis.drivetotravel.drivetotravel1.Other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by LEO on 28.3.2016..
 */
public class LanguageChange {

    public static void getMyLanguage(Context context)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);


        Configuration config = context.getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }
}
