package hanzhou.easyledger_demo.chart_personalization;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

import hanzhou.easyledger_demo.utility.Constant;

/*
 *   @author Michael P.
 *
 *
 *   modified by Han Zhou
 *
 *
 * */

public class MyPercentFormatter extends ValueFormatter {

    private DecimalFormat mFormat;
    private PieChart pieChart;

    public MyPercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }


    public MyPercentFormatter(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }

    @Override
    public String getFormattedValue(float value) {

        if (value == 0f) {
            return "";
        }

        return mFormat.format(value) + " %";
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (value == 0f) {
            return "";
        }
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // raw value, skip percent sign
            return Constant.DOLLOR_SIGN + mFormat.format(value);
        }
    }

}
