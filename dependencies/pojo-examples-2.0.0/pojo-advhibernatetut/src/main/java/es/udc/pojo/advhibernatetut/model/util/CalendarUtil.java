package es.udc.pojo.advhibernatetut.model.util;

import java.util.Calendar;

public final class CalendarUtil {

    private CalendarUtil() {}

    public static Calendar getDate(int day, int month, int year) {

        Calendar date = Calendar.getInstance();

        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.YEAR, year);
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.AM_PM, Calendar.AM);

        return date;

    }

}
