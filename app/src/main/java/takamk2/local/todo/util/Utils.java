package takamk2.local.todo.util;

import java.util.Calendar;

/**
 * Created by takamk2 on 17/03/15.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class Utils {

    public static Calendar getCalendarOfNow() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static long getTimestampOfNow() {
        return getCalendarOfNow().getTimeInMillis();
    }
}
