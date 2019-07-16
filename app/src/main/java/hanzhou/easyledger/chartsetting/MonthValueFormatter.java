package hanzhou.easyledger.chartsetting;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import hanzhou.easyledger.utility.UnitUtil;


public class MonthValueFormatter extends IndexAxisValueFormatter
{

    private final String[] mMonths = new String[]{
           "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private int mInitialValue;


    private LocalDate mStartingDate;

    public MonthValueFormatter() {
        mInitialValue = -1000;
    }

    @Override
    public String getFormattedValue(float value) {

        /*set first value as a starting point for counting the x-axis labels*/
        if(mInitialValue == -1000){
            mInitialValue = (int)value;
            mStartingDate = LocalDate.parse(""+mInitialValue, DateTimeFormat.forPattern("YYYYMM"));
        }

        int difference = (int)value - mInitialValue;


        return UnitUtil.fromJodaTimeLocalDateToMonthLabel(mStartingDate.plusMonths(difference));

    }


}
