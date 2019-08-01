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


    }


}
