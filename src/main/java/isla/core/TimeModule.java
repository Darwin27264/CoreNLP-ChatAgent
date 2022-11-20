package isla.core;

import com.google.protobuf.StringValue;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TimeModule {

    public static String timeOut() {
        //Getting the current time
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        Date timeSys = new Date(System.currentTimeMillis());
        String currentTime = formatter.format(timeSys);

        String[] timePro = currentTime.split(":");

        return timePro[0] + ":" + timePro[1];
    }

    public static String dateOut(){
        //Getting the current date
        LocalDate today = LocalDate.now();

        return today.toString();
    }

    public static String weekOut(){
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

        return simpleDateformat.format(now);
    }

    public static String yearOut() {
        int intYear = Calendar.getInstance().get(Calendar.YEAR);
        return String.valueOf(intYear);
    }

    public static HashMap<String, String> TimeDate(){
        HashMap<String, String> out = new HashMap<>();
        out.put("time", timeOut());
        out.put("date", dateOut());
        out.put("week", weekOut());
        out.put("year", yearOut());

        return out;
    }

    /* test main
    public static void main(String[] args) {
        HashMap<String, String> test = TimeDate();

        System.out.println(test.get("time"));
        System.out.println(test.get("date"));
        System.out.println(test.get("week"));
    }
    */
}
