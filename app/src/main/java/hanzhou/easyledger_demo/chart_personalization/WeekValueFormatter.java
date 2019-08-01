package hanzhou.easyledger_demo.chart_personalization;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.LocalDate;

import hanzhou.easyledger_demo.utility.Constant;

public class WeekValueFormatter extends ValueFormatter {
    private final BarLineChartBase<?> chart;
    private LocalDate now = LocalDate.now();

    private int mMaxPeriod;

    public WeekValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    public void setMaxPeriod(int maxPeriod) {
        mMaxPeriod = maxPeriod;
    }


    @Override
    public String getFormattedValue(float value) {

        int x = mMaxPeriod - (int) value;
        if (x <= 1) return Constant.TXT_LAST_WEEK;
        return x + " " + Constant.TXT_WEEKS;
    }


}
