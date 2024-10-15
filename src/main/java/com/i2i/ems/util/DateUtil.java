package com.i2i.ems.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

/**
 * <p>
 * Utility class that provides methods to perform date related operations.
 * </p>
 */
public class DateUtil {
  /**
   * <p>
   * Returns the number of years between two dates.
   * </p>
   *
   * @param date1 the first date
   * @param date2 the second date
   * @return the number of years between the two dates
   */
  public static int getYearsBetween(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      return -1;
    }
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate date1InLocalDate = LocalDate.parse(dateFormat.format(date1));
    LocalDate date2InLocalDate = LocalDate.parse(dateFormat.format(date2));
    return Period.between(date1InLocalDate, date2InLocalDate).getYears();
  }
}
