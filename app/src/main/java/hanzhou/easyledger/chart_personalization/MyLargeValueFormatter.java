package hanzhou.easyledger.chart_personalization;

import com.github.mikephil.charting.formatter.LargeValueFormatter;

public class MyLargeValueFormatter extends LargeValueFormatter {
    public MyLargeValueFormatter() {
        super();
    }

    public MyLargeValueFormatter(String appendix) {
        super(appendix);
    }

    @Override
    public String getFormattedValue(float value) {
        return "$"+super.getFormattedValue(value);
    }
}
