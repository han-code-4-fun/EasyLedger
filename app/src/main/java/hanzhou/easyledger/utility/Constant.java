package hanzhou.easyledger.utility;

import android.content.SharedPreferences;
import android.util.Log;

import hanzhou.easyledger.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Constant {

    public static final String UNTAGGED = "n/a";

    public static final String FRAG_CALL_FROM_OVERVIEW = "this_recyclerview_open_from_overview_fragment";
    public static final String FRAG_CALL_FROM_LEDGER = "this_recyclerview_open_from_ledger_fragment";

    public static final String TESTFLOW = "test_";

//    public static final String APP_PREFERENCE = "sharedPreferences";

    public static final String LEDGERS = "sharedPreference_ledgers";

    public static final String CATEGORY_ADD = "add_a_category";

    public static final String  CATEGORY_OTHERS = "Others";

    public static final String CATEGORY_TYPE_REVENUE = "revenue_categories";

    public static final String CATEGORY_TYPE_EXPENSE = "expense_categories";

    public static final String SHARED_PREF_INITIAL = "shared_preference_initialization";

    public static final String SETTING_CHART_HISTORY_PERIOD_NUMBER_KEY = "history_period_number";
    public static final int SETTING_CHART_HISTORY_PERIOD_NUMBER_DEFAULT = 6;

    public static final String SETTING_CHART_HISTORY_PERIOD_TYPE_KEY = "history_period_type";
    public static final int SETTING_CHART_HISTORY_PERIOD_TYPE_DEFAULT_VAL = R.id.dialog_history_period_by_month;


    public static final String SETTING_CHART_CURRENT_CHART_TYPE_KEY = "current_chart_type";
    public static final int SETTING_CHART_CURRENT_CHART_TYPE_DEFAULT_VAL = R.id.radioButton_current_period_type_piechart;

    public static final String SETTING_CHART_CURRENT_CHART_PERIOD_KEY = "current_chart_period";
    public static final int SETTING_CHART_CURRENT_CHART_PERIOD_DEFAULT_VAL = R.id.radioButton_current_period_type_month;


    public static final String SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_KEY = "current_chart_period_custom";
    public static final int SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_DEFAULT_VAL = 0;

    public static final String SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_KEY = "is_piechart_percentage_or_amount";
    public static final boolean SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_DEFAULT_VAL = true;

    public static final String SETTING_GENERAL_OVERVIEW_DATE_RANGE_LIST_KEY = "setting_general_overview_date_range_list_key";
    public static final String SETTING_GENERAL_OVERVIEW_DATE_RANGE_BY_MONTH = "overview_by_month";
    public static final String SETTING_GENERAL_OVERVIEW_DATE_RANGE_BY_WEEK ="overview_by_week";
    public static final String SETTING_GENERAL_OVERVIEW_DATE_RANGE_CUSTOM_RANGE ="overview_by_custom_range";

    public static final String SETTING_GENERAL_OVERVIEW_DATE_RANGE_CUSTOM_KEY ="setting_general_overview_date_range_custom_seekbar_key";

    public static final String SETTING_AUTO_TAGGER_KEY = "setting_auto_tagger_key";


    public static final String[] DEFAULT_CATEGORIES_REVENUE = {
            "Salary", "Interest", "Commission", "Investment",
            "Pension", "RRSP", "Rental Income", "Business Income", "Capital gains",
            "Credit Card Pay In", "Debt Collection", "Lottery", CATEGORY_OTHERS
    };

    public static final String[] DEFAULT_CATEGORIES_EXPENSE = {
            "Restaurant", "Shopping", "Commuting", "Supermarket", "Auto", "Travel", "Education",
            "House", "Personal Care", "Children", "Medical", "Credit Card Pay-out", CATEGORY_OTHERS
    };

    public static final String LEDGER_OVERALL = "OVERALL";

    public static final String[] DEFAULT_LEDGER = {Constant.LEDGER_OVERALL, "CASH", Constant.RBC_LEDGER_NAME};


    public static final String RBC_NUMBER = "722258";

    public static final String RBC_LEDGER_NAME = "RBC";




    public static final String BMO_NUMBER = "-1";

    public static final String CIBC_NUMBER = "-2";

    public static final String HSBC_NUMBER = "-3";

    /*For developer use to check the present RBC msg format*/
    private static final String RBC_TEMPLATE_CREDIT_CARD =
            "Purchase of $3.94 from RBC credit card 1121 made JUL21 at STARBUCKS #04274#. Stop-txt STOP/Help-txt HELP";

    private static final String RBC_TEMPLATE_DEBIT_CARD_WITHDRAWAL =
            "RBC: Withdrawal of $1.37 from RBC acct 4784 made JUL22. Stop-txt STOP/Help-txt HELP";

    private static final String RBC_TEMPLATE_DEBIT_CARD_DEPOSIT =
            "RBC: Deposit of $2.00 to RBC acct 4784 made JUL22. Stop-txt STOP/Help-txt HELP";

    public static final String[] MONTHS_CAP = {
      "JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"
    };
    public static String[] MONTHS_NON_CAP = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",  "Dec"};

    public static final String RBC_DEPOSIT= "Deposit";
    public static final String RBC_WITHDRAWAL = "Withdrawal";

    public static final String CREDIT_CARD = "credit card";


    private static boolean isAutoTaggerOn = true;
    public static boolean getIsAutoTaggerOn(){
        return isAutoTaggerOn;
    }
    public static void setIsAutoTaggerOn(boolean input){
        isAutoTaggerOn = input;
        Log.d("test_setting", "setIsAutoTaggerOn: current state -> "+ isAutoTaggerOn);
    }



}
