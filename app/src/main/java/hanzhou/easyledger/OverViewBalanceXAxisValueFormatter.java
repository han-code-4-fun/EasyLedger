package hanzhou.easyledger;

import android.util.Log;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class OverViewBalanceXAxisValueFormatter extends IndexAxisValueFormatter {

    private String[] values;

    public OverViewBalanceXAxisValueFormatter(String[] values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value) {
        // "value" represents the position of the label on the axis (x or y)
        return this.values[(int) value];
    }

}
