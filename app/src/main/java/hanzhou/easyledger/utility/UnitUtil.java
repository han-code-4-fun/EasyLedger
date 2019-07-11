package hanzhou.easyledger.utility;

import android.icu.util.LocaleData;
import android.os.Build;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UnitUtil {
    private static final String TAG = UnitUtil.class.getSimpleName();

    public static String displayPositiveMoney(double input){
        double temp = Math.abs(input);
        return String.valueOf(temp);
    }


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

    public static String getTimeIntInMoreReadableFormat(int inputTime){
        String output ="";
        String temp = String.valueOf(inputTime);
        String month = temp.charAt(2)+""+temp.charAt(3);
        int monthIndex = Integer.parseInt(month);
        monthIndex--;
        month = FakeTestingData.month[monthIndex];


        output = "20"+temp.charAt(0)+temp.charAt(1)+"/"+month+"/"+temp.charAt(4)+temp.charAt(5);

        return output;
    }

    public static int setDatePickerDateIntoAppIntDate(int year, int month, int day){
        return (year-2000)*10000+ (month+1)*100+day;
    }
}
