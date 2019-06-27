package hanzhou.easyledger.utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class UnitUtil {
    public static DecimalFormat moneyFormater = new DecimalFormat("#.##");
    private SimpleDateFormat formatterMonthDay = new SimpleDateFormat("MM-dd");

    public SimpleDateFormat getMonthDayFormatter() {
        return formatterMonthDay;
    }
}
