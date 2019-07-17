package hanzhou.easyledger.utility;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class UnitUtil {
    private static final String TAG = UnitUtil.class.getSimpleName();

    private static LocalDate now = LocalDate.now();

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

    public static int getStartingDateCurrentWeek(){
//        int daysPastCurrentWeek = now.getDayOfWeek();
//
//        int year = (now.getYear())-2000;
//        int month = now.getMonthOfYear();
//        int dayBeginOfWeek = (now.getDayOfMonth()) - daysPastCurrentWeek +1;

        int startingDate =fromJodaTimeLocalDateToAppDateInteger(now.dayOfWeek().withMinimumValue());
        Log.d("test_flow13", "setRevenueDateCurrentWeek: "+ startingDate);

        return startingDate;
    }

    public static int getStartingDateCurrentMonth(){
        int year = (now.getYear())-2000;
        int month = now.getMonthOfYear();

//        int startingDate =year*10000+month*100+1;
        int startingDate = fromJodaTimeLocalDateToAppDateInteger(now.dayOfMonth().withMinimumValue());
        Log.d("test_flow13", "setRevenueDateCurrentMonth: "+ startingDate);

        return startingDate;
    }

    public static int getStartingDateCurrentCustom(int customDays) {

        int startingDate = fromJodaTimeLocalDateToAppDateInteger(now.minusDays(customDays));
        Log.d("test_f_custom_dates", "getStartingDateCurrentCustom: "+ startingDate);

        return startingDate;
    }

    public static List<Integer> getArrayOfStartEndDatesOnNumberOfCompareMonths(int numberOfMonths){

        List<Integer> result = new ArrayList<>();
        for (int i = numberOfMonths; i >0; i--) {
            int[] temp = getStartingEndDateOfAMonth(now.minusMonths(i));
            result.add(temp[0]);
            result.add(temp[1]);
        }


        return result;
    }

    public static List<Integer> getArrayOfStartEndDatesOnNumberOfCompareWeeks (int numberOfWeeks){
        List<Integer> result = new ArrayList<>();
        for (int i = numberOfWeeks; i >0; i--) {
            int[] temp = getStartingEndDateOfAWeek(now.minusWeeks(i));
            result.add(temp[0]);
            result.add(temp[1]);
        }


        return result;
    }



    public static int[] getStartingEndDateOfAMonth(LocalDate inputDate){
        int[] output = new int[2];

        int startDateOfMonth = fromJodaTimeLocalDateToAppDateInteger(inputDate.withDayOfMonth(1));
        int endDateOfMonth = fromJodaTimeLocalDateToAppDateInteger(inputDate.dayOfMonth().withMaximumValue());

        output[0] = startDateOfMonth;
        output[1] = endDateOfMonth;

        return output;
    }
    private static int[] getStartingEndDateOfAWeek(LocalDate inputDate) {
        int[] output = new int[2];


        int startDateOfWeek = fromJodaTimeLocalDateToAppDateInteger(inputDate.dayOfWeek().withMinimumValue());
        int endDateOfWeek = fromJodaTimeLocalDateToAppDateInteger(inputDate.dayOfWeek().withMaximumValue());

        output[0] = startDateOfWeek;
        output[1] = endDateOfWeek;

        return output;
    }

    private static Integer fromJodaTimeLocalDateToAppDateInteger(LocalDate inputDate){
        return  Integer.parseInt(DateTimeFormat.forPattern("YYMMdd").print(inputDate));
    }

    public static String fromJodaTimeLocalDateToMonthLabel(LocalDate inputDate){

        return  DateTimeFormat.forPattern("YYYY/MMM").print(inputDate);
    }



}
