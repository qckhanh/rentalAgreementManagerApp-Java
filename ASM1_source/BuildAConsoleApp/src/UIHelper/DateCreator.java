package UIHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCreator {
    DateCreator(){};

    public static Date newDate(String s) throws ParseException {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(s);
        } catch (Exception e) {
            return new SimpleDateFormat("MM/dd/yyyy").parse("1/1/2024");
        }

    }

    public static String formatDate(Date date){
        if(date == null) return "N/A";
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }
}
