package org.rmit.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    public static String DATE_PATTERN = "dd/MM/yyyy";
    public static String DEFAULT_DATE = "1/1/1990";
    public static LocalDate newDate(String s) throws ParseException {
        try {
            return LocalDate.parse(s, DateTimeFormatter.ofPattern(DATE_PATTERN));
        } catch (Exception e) {
            return LocalDate.parse(DEFAULT_DATE, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }

    }
    public static String formatDate(LocalDate date){
        if(date == null) return DEFAULT_DATE;
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }
}
