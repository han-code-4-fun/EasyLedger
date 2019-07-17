package hanzhou.easyledger.chartsetting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;


/*
 *   A scrollable Setting screen to display different charts in ChartFragment
 *
 *
 * */

public class ChartDialogSetting extends AppCompatDialogFragment {

    private final String TAG = ChartDialogSetting.class.getSimpleName();


    private AppCompatActivity mAppCompatActivity;
    private SharedPreferences mAppPreferences;

    private RadioGroup mRGroupCurrentPeriodType;
    private RadioGroup mRGroupCurrentChartType;
    private RadioGroup mRGroupHistoryPeriodType;

    private RadioButton mRBtnTypeCustomNumberOfDays;
    private EditText mETNumberOfDaysBefore;


    private Button mBtnPlus;
    private Button mBtnMinus;
    private TextView mTVDisplayHistoryPeriods;

    private LinearLayout mSwitchPercentageAmountLayout;
    private Switch mSwitchPercentageAmount;

    private RadioButton mRBtnCurrentTypeWeek;
    private RadioButton mRBtnCurrentTypeMonth;

    private RadioButton mRBtnCurrentTypePie;
    private RadioButton mRBtnCurrentTypeBar;


    private RadioButton mRBtnHistoryPeriodWeek;
    private RadioButton mRBtnHistoryPeriodMonth;

    private int mNumberDays;
    private boolean mIsSettingChanged;
    private boolean mIsDaysEntryValid;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        mAppPreferences = mAppCompatActivity.getSharedPreferences(
                Constant.APP_PREF_SETTING, Context.MODE_PRIVATE
        );
        mIsDaysEntryValid = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //dialog_rgroup_revenue_expense_type

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.
                from(getActivity()).
                inflate(R.layout.dialog_chart_setting, null);


        mETNumberOfDaysBefore = view.findViewById(R.id.dialog_number_of_days_before);
        mETNumberOfDaysBefore.addTextChangedListener(textWatcher);

        mSwitchPercentageAmountLayout = view.findViewById(R.id.dialog_layout_switch_percentage_amount);

        mSwitchPercentageAmount = view.findViewById(R.id.dialog_switch_percentage_amount);
        mSwitchPercentageAmount.setOnClickListener(swithClickListener);

        mRGroupCurrentPeriodType = view.findViewById(R.id.dialog_rgroup_revenue_expense_period_type);
        mRGroupCurrentPeriodType.setOnCheckedChangeListener(radioGroupCurrentPeriodTypeChangeListenerGeneral);


        mRGroupCurrentChartType = view.findViewById(R.id.dialog_rgroup_revenue_expense_chart_type);
        mRGroupCurrentChartType.setOnCheckedChangeListener(radioGroupCurrentChartTypeChangeListener);

        mRGroupHistoryPeriodType = view.findViewById(R.id.dialog_rgroup_history_period_type);
        mRGroupHistoryPeriodType.setOnCheckedChangeListener(radioGroupChangeListenerGeneral);

        mBtnPlus = view.findViewById(R.id.dialog_plus_number);
        mBtnPlus.setOnClickListener(mBtnPlusClickListener);

        mBtnMinus = view.findViewById(R.id.dialog_minus_number);
        mBtnMinus.setOnClickListener(mBtnMinusClickListener);

        mTVDisplayHistoryPeriods = view.findViewById(R.id.dialog_display_number_history_periods);


        loadPreferenceSetting();

        Log.d("test_dialog", "onCreateDialog: ");


        //todo, use string
        builder.setView(view)
                .setNegativeButton(getString(R.string.dialog_negative_btn_title), cancelBTNClickListener)
                .setPositiveButton(getString(R.string.dialog_positive_btn_title), userInputValid);


        return builder.create();

    }


    private void loadPreferenceSetting() {
        Log.d(TAG, "loadPreferenceSetting: ");

        int temp;

        temp = mAppPreferences.getInt(
                getString(R.string.setting_chart_dialog_history_period_number_key),
                getResources().getInteger(R.integer.setting_chart_dialog_default_history_period_number));
        mTVDisplayHistoryPeriods.setText(String.valueOf(temp));

        temp = mAppPreferences.getInt(
                getString(R.string.setting_chart_dialog_history_period_type_key),
                R.id.dialog_history_period_by_month);
        mRGroupHistoryPeriodType.check(temp);

        temp = mAppPreferences.getInt(
                getString(R.string.setting_chart_dialog_current_chart_type_key),
                R.id.radioButton_current_period_type_piechart
        );
        mRGroupCurrentChartType.check(temp);

        if (temp == R.id.radioButton_current_period_type_piechart) {
            mSwitchPercentageAmountLayout.setVisibility(View.VISIBLE);
        } else {
            mSwitchPercentageAmountLayout.setVisibility(View.GONE);
        }

        temp = mAppPreferences.getInt(
                getString(R.string.setting_chart_dialog_current_period_type_key),
                R.id.radioButton_current_period_type_month
        );
        mRGroupCurrentPeriodType.check(temp);

        mNumberDays = mAppPreferences.getInt(
                getString(R.string.setting_chart_dialog_custom_current_period_key),
                getResources().getInteger(R.integer.setting_chart_dialog_default_number_of_days_back)
        );
        Log.d(TAG, "loadPreferenceSetting: numberDays -> " + mNumberDays);
        mETNumberOfDaysBefore.setText(String.valueOf(mNumberDays));

        boolean isPercentage = mAppPreferences.getBoolean(
                getString(R.string.setting_chart_dialog_percentage_amount_key),
                getResources().getBoolean(R.bool.setting_char_dialog_percentage_amount_switch_default)
        );

        mSwitchPercentageAmount.setChecked(isPercentage);

        mIsSettingChanged = false;


    }

    private void savePreferenceSetting() {

        SharedPreferences.Editor editor = mAppPreferences.edit();

        editor.putInt(getString(R.string.setting_chart_dialog_history_period_number_key),
                Integer.parseInt(mTVDisplayHistoryPeriods.getText().toString())
        );

        editor.putInt(
                getString(R.string.setting_chart_dialog_history_period_type_key),
                mRGroupHistoryPeriodType.getCheckedRadioButtonId()
        );

        editor.putInt(getString(R.string.setting_chart_dialog_current_chart_type_key),
                mRGroupCurrentChartType.getCheckedRadioButtonId()
        );

        editor.putInt(getString(R.string.setting_chart_dialog_current_period_type_key),
                mRGroupCurrentPeriodType.getCheckedRadioButtonId()
        );

        if (mRGroupCurrentPeriodType.getCheckedRadioButtonId() == R.id.radioButton_current_period_type_custom) {
            editor.putInt(
                    getString(R.string.setting_chart_dialog_custom_current_period_key),
                    mNumberDays
            );
            Log.d("test_f_custom_dates", "save preference:  number  of days is  " + mNumberDays);
        }

        editor.putBoolean(getString(R.string.setting_chart_dialog_percentage_amount_key),
                mSwitchPercentageAmount.isChecked());

        editor.apply();
    }

    private RadioGroup.OnCheckedChangeListener radioGroupCurrentPeriodTypeChangeListenerGeneral =
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    mIsSettingChanged = true;

                    if (i == R.id.radioButton_current_period_type_custom) {
                        mETNumberOfDaysBefore.setVisibility(View.VISIBLE);
                    } else {
                        mETNumberOfDaysBefore.setVisibility(View.GONE);
                    }
                }
            };

    private RadioGroup.OnCheckedChangeListener radioGroupChangeListenerGeneral = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            mIsSettingChanged = true;
        }
    };

    private RadioGroup.OnCheckedChangeListener radioGroupCurrentChartTypeChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            mIsSettingChanged = true;
            if (i == R.id.radioButton_current_period_type_piechart) {
                mSwitchPercentageAmountLayout.setVisibility(View.VISIBLE);
            } else {
                mSwitchPercentageAmountLayout.setVisibility(View.GONE);
            }
        }
    };


    private Button.OnClickListener mBtnPlusClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mIsSettingChanged = true;
            int temp = Integer.parseInt(mTVDisplayHistoryPeriods.getText().toString());
            temp = temp + 1;
            mTVDisplayHistoryPeriods.setText(String.valueOf(temp));
        }
    };

    private Button.OnClickListener mBtnMinusClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mIsSettingChanged = true;
            int temp = Integer.parseInt(mTVDisplayHistoryPeriods.getText().toString());
            if (temp > 1) {
                temp = temp - 1;
            } else {
                Toast.makeText(mAppCompatActivity, getString(R.string.setting_warning_cannot_be_blank), Toast.LENGTH_SHORT).show();
            }
            mTVDisplayHistoryPeriods.setText(String.valueOf(temp));
        }
    };

    private Switch.OnClickListener swithClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mIsSettingChanged = true;

        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.d(TAG, "beforeTextChanged: ");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mIsSettingChanged = true;
            if (charSequence.toString().equals("")) {

                Log.d(TAG, "onTextChanged: charSequence.toString().equals(\"\")  set mIsDaysEntryValid == false");

                mIsDaysEntryValid = false;
            } else {
                Log.d(TAG, "onTextChanged: set mIsDaysEntryValid");
                mIsDaysEntryValid = true;

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {


            if (!mIsDaysEntryValid) {
                mNumberDays = 0;
                Log.d(TAG, "afterTextChanged: number days set to 0");
                mETNumberOfDaysBefore.setHintTextColor(Color.RED);
                mETNumberOfDaysBefore.setHint(R.string.setting_warning_cannot_be_blank);



            } else {
                mNumberDays = Integer.parseInt(mETNumberOfDaysBefore.getText().toString());
                Log.d(TAG, "afterTextChanged: mNumberDays  -> " + mNumberDays);


            }

        }
    };

    private DialogInterface.OnClickListener userInputValid = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            Log.d(TAG, "onClick: saveBTN   ");
            if (mIsSettingChanged) {

                /*the dialog screen will save current setting*/
                if(mIsDaysEntryValid){
                    Log.d(TAG, "onClick: mIsSettingChanged");
                    //todo, here
                    Toast.makeText(mAppCompatActivity, getString(R.string.dialog_toast_saved), Toast.LENGTH_LONG).show();
                    savePreferenceSetting();

                    BottomNavigationView bottomNavigationView = mAppCompatActivity.findViewById(R.id.bottom_navigation);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_charts);
                }else{
                    /*the dialog screen will not save current setting*/
                    Toast.makeText(mAppCompatActivity, getString(R.string.dialog_toast_not_saved), Toast.LENGTH_LONG).show();
                }

            } else {
                Log.d(TAG, "onClick: mIssetting   not changed");
                Toast.makeText(mAppCompatActivity, getString(R.string.dialog_toast_setting_not_change), Toast.LENGTH_LONG).show();

            }

        }
    };

    private DialogInterface.OnClickListener cancelBTNClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            Toast.makeText(mAppCompatActivity, "No Change Made", Toast.LENGTH_LONG).show();


        }
    };
}