package hanzhou.easyledger_demo.chart_personalization;

import com.github.mikephil.charting.formatter.LargeValueFormatter;

import hanzhou.easyledger_demo.utility.Constant;

public class MyLargeValueFormatter extends LargeValueFormatter {
    public MyLargeValueFormatter() {
        super();
    }


    @Override
    public String getFormattedValue(float value) {
        return Constant.DOLLOR_SIGN + super.getFormattedValue(value);
    }
}
