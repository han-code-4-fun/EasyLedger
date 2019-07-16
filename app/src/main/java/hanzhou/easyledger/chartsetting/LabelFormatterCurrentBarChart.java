package hanzhou.easyledger.chartsetting;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Collection;

public class LabelFormatterCurrentBarChart extends ValueFormatter {
    private String[] mValues = new String[] {};
    private int mValueCount = 0;


    public LabelFormatterCurrentBarChart() {
    }


    public LabelFormatterCurrentBarChart(String[] valueOfEntryLabels) {
        if (valueOfEntryLabels != null)
            setValues(valueOfEntryLabels);
    }


    public LabelFormatterCurrentBarChart(Collection<String> valueOfEntryLabels) {
        if (valueOfEntryLabels != null)
            setValues(valueOfEntryLabels.toArray(new String[valueOfEntryLabels.size()]));
    }

    @Override
    public String getFormattedValue(float value) {
        int index = Math.round(value);

        if (index < 0 || index >= mValueCount || index != (int)value)
            return "";
        if(mValues[index].length()>5){

            return mValues[index].substring(0,5)+"...";
        }
        return mValues[index];
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