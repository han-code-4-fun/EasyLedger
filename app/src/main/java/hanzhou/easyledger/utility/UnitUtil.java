package hanzhou.easyledger.utility;

import android.icu.util.LocaleData;
import android.os.Build;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UnitUtil {
    private static final String TAG = UnitUtil.class.getSimpleName();




    public static String getMonthDayToday() {
        Date today = new Date();
        SimpleDateFormat formatterMonthDay = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return formatterMonthDay.format(today);
    }

    public static String formatMoney(double money){
        DecimalFormat moneyFormater = new DecimalFormat("$#.##");
        return moneyFormater.format(money);
    }

    //return format YYMMDD
    public static int getHalfYearTime(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            LocalDate halfYear = LocalDate.now().minusDays(180);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

            return Integer.valueOf(halfYear.format(formatter));
        }
        return 100000;
    }

    public static int formatTime(Date inputDate){
        SimpleDateFormat formatterDBTime = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String output = formatterDBTime.format(inputDate);
        Log.d(TAG, "formatTime: half year ago is "+ output);
        return Integer.valueOf(output);
    }
}
