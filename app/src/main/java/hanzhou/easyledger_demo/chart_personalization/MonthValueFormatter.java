package hanzhou.easyledger_demo.chart_personalization;

import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import hanzhou.easyledger_demo.utility.Constant;
import hanzhou.easyledger_demo.utility.UnitUtil;


public class MonthValueFormatter extends ValueFormatter {


    private int mInitialValue;


    private LocalDate mStartingDate;

    public MonthValueFormatter() {
        mInitialValue = -1000;
    }

    public void setStartDate(LocalDate inputDate) {
        mStartingDate = inputDate;
        mInitialValue = Integer.parseInt(
                DateTimeFormat.forPattern(Constant.DATE_TIME_FORMAT_YEAR_MONTH).print(
                        mStartingDate));
    }

    @Override
    public String getFormattedValue(float value) {

        int difference = (int) value - mInitialValue;
        LocalDate displayDate;
        if (difference > 0) {
            displayDate = mStartingDate.plusMonths(difference);
        } else if (difference < 0) {
            displayDate = mStartingDate.minusMonths(difference);
        } else {
            displayDate = mStartingDate;
        }

        return UnitUtil.fromJodaTimeLocalDateToMonthLabel(displayDate);


    }


}
