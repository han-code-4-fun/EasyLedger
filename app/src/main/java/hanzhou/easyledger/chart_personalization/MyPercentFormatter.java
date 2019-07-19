package hanzhou.easyledger.chart_personalization;

import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

/*
*   @author Michael P.
*
*
*   modified by Han Zhou
*
*
* */

public class MyPercentFormatter extends ValueFormatter {

    public DecimalFormat mFormat;
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
        Log.d("test_flow120", "(getFormattedValue) pie value value: "+value);

        if(value == 0f){
            return "";
        }

        return mFormat.format(value) + " %";
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        Log.d("test_flow120", "(getPieLabel) pie value value: "+value);
        if(value == 0f){
            return "";
        }
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // raw value, skip percent sign
            return "$"+mFormat.format(value);
        }
    }

}
