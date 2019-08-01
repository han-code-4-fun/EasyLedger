package hanzhou.easyledger.utility;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class UnitUtil {
    private static final String TAG = UnitUtil.class.getSimpleName();

    private static LocalDate now = LocalDate.now();

    public static String displayPositiveMoney(double input){
        double temp = Math.abs(input);
        return String.valueOf(temp);
    }


    public static int getTodayInAppTimeFormat(){
        return fromJodaTimeLocalDateToAppDateInteger(now);
    }

    public static String getMonthDayToday() {
        Date today = new Date();
        SimpleDateFormat formatterMonthDay = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return formatterMonthDay.format(today);
    }

    public static String formatMoney(double money){
        if(money<0){
            money = money -money*2;
        }

        DecimalFormat moneyFormater = new DecimalFormat("$#,###.##");
        return moneyFormater.format(money);
    }


    public static int formatTime(Date inputDate){
        SimpleDateFormat formatterDBTime = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String output = formatterDBTime.format(inputDate);
        return Integer.valueOf(output);
    }

    public static String getTimeIntInMoreReadableFormat(int inputTime){
        String output ="";
        String temp = String.valueOf(inputTime);
        String month = temp.charAt(2)+""+temp.charAt(3);
        int monthIndex = Integer.parseInt(month);
        monthIndex--;
        month = Constant.MONTHS_NON_CAP[monthIndex];


        output = "20"+temp.charAt(0)+temp.charAt(1)+"/"+month+"/"+temp.charAt(4)+temp.charAt(5);

        return output;
    }




    public static int setDatePickerDateIntoAppIntDate(int year, int month, int day){
        return (year-2000)*10000+ (month+1)*100+day;
    }

    public static int getStartingDateCurrentWeek(){

        return fromJodaTimeLocalDateToAppDateInteger(now.dayOfWeek().withMinimumValue());
    }

    public static int getStartingDateCurrentMonth(){
        int year = (now.getYear())-2000;
        int month = now.getMonthOfYear();

        return fromJodaTimeLocalDateToAppDateInteger(now.dayOfMonth().withMinimumValue());
    }

    public static int getStartingDateCurrentYear() {

        return fromJodaTimeLocalDateToAppDateInteger(now.dayOfYear().withMinimumValue());

    }

    public static int getStartingDateCurrentCustom(int customDays) {

        return fromJodaTimeLocalDateToAppDateInteger(now.minusDays(customDays));
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

    public static Integer fromJodaTimeLocalDateToAppDateInteger(LocalDate inputDate){
        return  Integer.parseInt(DateTimeFormat.forPattern("YYMMdd").print(inputDate));
    }

    public static String fromJodaTimeLocalDateToMonthLabel(LocalDate inputDate){

        return  DateTimeFormat.forPattern("YYYY/MMM").print(inputDate);
    }

    public static int fromMMMDDFormatToAppDateFormat(String input){
        String month = input.substring(0,3);
        int monthInt = -1;
        for (int i = 0; i< Constant.MONTHS_CAP.length; i++) {
            if(month.equals(Constant.MONTHS_CAP[i])){
                monthInt = i+1;
                break;
            }
        }
        int day = Integer.parseInt(input.substring(3));
        int year =  Integer.parseInt(DateTimeFormat.forPattern("YY").print(now));

        return year*10000+monthInt*100+day;

    }



}
