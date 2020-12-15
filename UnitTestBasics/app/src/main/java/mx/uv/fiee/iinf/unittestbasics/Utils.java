package mx.uv.fiee.iinf.unittestbasics;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final int MILLIS = 1000;

    public static boolean checkEmailForValidity(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find ();
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile ("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static Date calendarDate(long epocSeconds) {
        Calendar c = Calendar.getInstance (TimeZone.getTimeZone ("UTC"));
        c.setTimeInMillis (epocSeconds * 1000);
        return c.getTime ();
    }

}