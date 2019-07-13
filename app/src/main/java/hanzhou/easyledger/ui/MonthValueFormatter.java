package hanzhou.easyledger.ui;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;


public class MonthValueFormatter extends ValueFormatter
{

    private final String[] mMonths = new String[]{
           "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private final BarLineChartBase<?> chart;

    public MonthValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

//        int days = (int) value;
//
//        int year = determineYear(days);
//
//        int month = determineMonth(days);
//        String monthName = mMonths[month % mMonths.length];
//        String yearName = String.valueOf(year);
//
//        if (chart.getVisibleXRange() > 30 * 6) {
//
//            return monthName + " " + yearName;
//        } else {
//
//            int dayOfMonth = determineDayOfMonth(days, month + 12 * (year - 2016));
//
//            String appendix = "th";
//
//            switch (dayOfMonth) {
//                case 1:
//                    appendix = "st";
//                    break;
//                case 2:
//                    appendix = "nd";
//                    break;
//                case 3:
//                    appendix = "rd";
//                    break;
//                case 21:
//                    appendix = "st";
//                    break;
//                case 22:
//                    appendix = "nd";
//                    break;
//                case 23:
//                    appendix = "rd";
//                    break;
//                case 31:
//                    appendix = "st";
//                    break;
//            }
//
//            return dayOfMonth == 0 ? "" : dayOfMonth + appendix + " " + monthName;
//        }

        String temp = String.valueOf((int)value);
//        Log.d("test_ff6", "temp: "+temp);
        int month = Integer.parseInt(temp.substring(4));
//        Log.d("test_ff5", "month: "+month);
//        Log.d("test_ff5", "month-1:   ->"+(month-1));
        Log.d("test_ff7", "month:   ->"+temp.substring(4));

        return temp.substring(0,4)+" "+mMonths[month];
    }


}
