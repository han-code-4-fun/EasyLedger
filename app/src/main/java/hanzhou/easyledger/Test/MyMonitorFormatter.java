package hanzhou.easyledger.Test;

import android.util.Log;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Collection;

public class MyMonitorFormatter  extends ValueFormatter {

    private String[] mValues = new String[] {};
    private int mValueCount = 0;

    /**
     * An empty constructor.
     * Use `setValues` to set the axis labels.
     */
    public MyMonitorFormatter() {
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    public MyMonitorFormatter(String[] values) {
        if (values != null)
            setValues(values);
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    public MyMonitorFormatter(Collection<String> values) {
        if (values != null)
            setValues(values.toArray(new String[values.size()]));
    }

    @Override
    public String getFormattedValue(float value) {

        Log.d("test_ff7", "MONITOR FORMATTER getFormattedValue: ---> "+value);

//        int index = Math.round(value);
//
//        if (index < 0 || index >= mValueCount || index != (int)value)
//            return "";
//
//        return mValues[index];
        return String.valueOf(value);
    }

    public String[] getValues()
    {
        return mValues;
    }

    public void setValues(String[] values)
    {
        if (values == null)
            values = new String[] {};

        this.mValues = values;
        this.mValueCount = values.length;
    }

}
