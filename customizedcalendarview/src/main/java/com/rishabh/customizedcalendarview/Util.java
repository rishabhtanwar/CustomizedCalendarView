package com.rishabh.customizedcalendarview;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {

  public static String monthYearNameBySwipeIndex(int swipeCount, String dateFormat) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, swipeCount);
    String previousMonthYear = new SimpleDateFormat(dateFormat).format(cal.getTime());
    return previousMonthYear;
  }
  public static void setDayStart(Calendar date) {
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
  }

  public static Calendar getCurrentDate() {
    Calendar newDate = Calendar.getInstance();
    newDate.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH),
        newDate.get(Calendar.DAY_OF_MONTH));
    newDate.set(Calendar.HOUR_OF_DAY, 0);
    newDate.set(Calendar.MINUTE, 0);
    newDate.set(Calendar.SECOND, 0);
    newDate.set(Calendar.MILLISECOND, 0);
    return newDate;
  }

  public static Calendar getOneWeekLaterDate() {
    Calendar newDate = Calendar.getInstance();
    newDate.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH),
        newDate.get(Calendar.DAY_OF_MONTH));
    newDate.add(Calendar.DAY_OF_MONTH, 7);
    newDate.set(Calendar.HOUR_OF_DAY, 0);
    newDate.set(Calendar.MINUTE, 0);
    newDate.set(Calendar.SECOND, 0);
    newDate.set(Calendar.MILLISECOND, 0);
    return newDate;
  }

  public static Calendar getOneWeekLaterDateFromStartDate(long startDate) {
    Calendar newDate = Calendar.getInstance();
    newDate.setTimeInMillis(startDate);
    newDate.add(Calendar.DAY_OF_MONTH, 7);
    newDate.set(Calendar.HOUR_OF_DAY, 0);
    newDate.set(Calendar.MINUTE, 0);
    newDate.set(Calendar.SECOND, 0);
    newDate.set(Calendar.MILLISECOND, 0);
    return newDate;
  }
}
