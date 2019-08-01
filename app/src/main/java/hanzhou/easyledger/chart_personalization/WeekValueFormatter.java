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

        int x = mMaxPeriod - (int) value;
        if(x <= 1) return "last week";
        return x + " weeks";
    }


}
