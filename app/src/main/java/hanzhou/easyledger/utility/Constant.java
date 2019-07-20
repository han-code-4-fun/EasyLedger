package hanzhou.easyledger.utility;

import android.content.SharedPreferences;

import hanzhou.easyledger.R;

import androidx.appcompat.app.AppCompatActivity;

public class Constant {

    public static final String UNTAGGED = "n/a";

    public static final String FRAG_CALL_FROM_OVERVIEW = "this_recyclerview_open_from_overview_fragment";
    public static final String FRAG_CALL_FROM_LEDGER = "this_recyclerview_open_from_ledger_fragment";

    public static final String TESTFLOW = "test_";

    public static final String APP_PREFERENCE = "sharedPreferences";

    public static final String LEDGERS = "sharedPreference_ledgers";

    public static final String CATEGORY_REVENUE = "revenue_categories";

    public static final String CATEGORY_EXPENSE = "expense_categories";

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


}
