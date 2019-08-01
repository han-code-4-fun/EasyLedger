package hanzhou.easyledger.chart_personalization;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.LocalDate;

public class WeekValueFormatter extends ValueFormatter {
    private final BarLineChartBase<?> chart;
    private LocalDate now = LocalDate.now();

    private int mMaxPeriod;

    public WeekValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    public void setMaxPeriod(int maxPeriod){
        mMaxPeriod= maxPeriod;
    }


    @Override
    public String getFormattedValue(float value) {


//        String date = String.valueOf((int)value);
//        LocalDate past = LocalDate.parse(date, DateTimeFormat.forPattern("YYYYMMdd"));
//
//        int weekFromNow = Weeks.weeksBetween(past, now).getWeeks();

//        return weekFromNow+" weeks ago";

        int x = mMaxPeriod - (int) value;
        if(x <= 1) return "last week";
        return x + " weeks";
    }


}
