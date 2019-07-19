package hanzhou.easyledger.chart_personalization;

import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import hanzhou.easyledger.utility.UnitUtil;


//public class MonthValueFormatter extends IndexAxisValueFormatter
public class MonthValueFormatter extends ValueFormatter

{

    private final String[] mMonths = new String[]{
           "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private int mInitialValue;


    private LocalDate mStartingDate;

    public MonthValueFormatter() {
        mInitialValue = -1000;
    }

    public void setStartDate(LocalDate inputDate){
        mStartingDate = inputDate;
        mInitialValue = Integer.parseInt(
                DateTimeFormat.forPattern("YYYYMM").print(
                        mStartingDate));
    }

    @Override
    public String getFormattedValue(float value) {

        int difference = (int) value - mInitialValue;
        LocalDate displayDate;
        if(difference >0){
            displayDate = mStartingDate.plusMonths(difference);
        }else if(difference<0){
            displayDate = mStartingDate.minusMonths(difference);
        }else{
            displayDate = mStartingDate;
        }

        return UnitUtil.fromJodaTimeLocalDateToMonthLabel(displayDate);


//        try {
//            Log.d("test_ff7", "formatter    getFormattedValue: " + value);
//
//            /*set first value as a starting point for counting the x-axis labels*/
//            if (mInitialValue == -1000) {
//                mInitialValue = (int) value;
//                mStartingDate = LocalDate.parse("" + (int)value, DateTimeFormat.forPattern("YYYYMM"));
//            }
//
//            int difference = (int) value - mInitialValue;
//            Log.d("test_ff7", "formatter    mInitialValue: " + mInitialValue);
//
//            Log.d("test_ff7", "formatter    difference: " + difference);
//
//
//            return UnitUtil.fromJodaTimeLocalDateToMonthLabel(mStartingDate.plusMonths(difference));
//
//        }catch(IllegalFieldValueException e){
//            Log.d("test_ff7", "formatter    catch exception: " + value);
//
//            /*set first value as a starting point for counting the x-axis labels*/
//            if (mInitialValue == -1000) {
//                mInitialValue = (int) value;
//                mStartingDate = LocalDate.parse("" + value, DateTimeFormat.forPattern("YYYYMM"));
//            }
//
//
//            return ""+value;
//        }

    }


}
