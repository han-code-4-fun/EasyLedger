package hanzhou.easyledger_demo.ui.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import java.util.ArrayList;

import hanzhou.easyledger_demo.R;
import hanzhou.easyledger_demo.utility.Constant;
import hanzhou.easyledger_demo.utility.GsonHelper;
import hanzhou.easyledger_demo.viewmodel.GeneralViewModel;
import hanzhou.easyledger_demo.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingMain extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private AppCompatActivity mAppCompatActivity;


    private Toolbar toolbar;

    private SeekBarPreference seekBarPreference;

    private PreferenceCategory mOverviewCategory;
    private String mCurrentOverviewDatesRange;

    private SettingsViewModel mSettingsViewModel;

    private GsonHelper mGsonHelper;


    public SettingMain() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);

        mGsonHelper = GsonHelper.getInstance();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_main);

        SharedPreferences mSharedPreferences = getPreferenceScreen().getSharedPreferences();

        toolbar = mAppCompatActivity.findViewById(R.id.toolbar_layout);

        mOverviewCategory = findPreference(getString(R.string.setting_category_overview_key));
        seekBarPreference = findPreference(getString(R.string.setting_overview_custom_range_seekbar_key));


        ListPreference mListOfDayRanges = findPreference(getString(R.string.setting_general_overview_date_range_list_key));



        /*initially hide seekbar if not the right selesction*/
        mCurrentOverviewDatesRange = mSharedPreferences.getString(mListOfDayRanges.getKey(), getString(R.string.empty_string));


        if (!mCurrentOverviewDatesRange.equals(getString(R.string.setting_overview_date_range_custom_range_key))) {
            mOverviewCategory.removePreference(seekBarPreference);
        }
        setPreferenceSummary(mListOfDayRanges, mCurrentOverviewDatesRange);


        Preference settingEditLedger = findPreference(getString(R.string.setting_transaction_edit_ledger_key));
        if (settingEditLedger != null) {

            settingEditLedger.setOnPreferenceClickListener(mEditLedgerListener);
        }

        Preference editRevenueCategory = findPreference(getString(R.string.setting_others_edit_category_revenue_key));
        if (editRevenueCategory != null) {

            editRevenueCategory.setOnPreferenceClickListener(mEditRevenueCategoryListener);
        }

        Preference editExpenseCategory = findPreference(getString(R.string.setting_others_edit_category_expense_key));
        if (editExpenseCategory != null) {

            editExpenseCategory.setOnPreferenceClickListener(mEditExpenseCategoryListener);
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar_empty, menu);
        toolbar.setTitle(getString(R.string.title_settings_fragment));
        mAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        /* Figure out which preference was changed */
        Preference preference = findPreference(s);
        if (preference != null) {

            if (preference instanceof ListPreference) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                mCurrentOverviewDatesRange = value;
                setPreferenceSummary(preference, value);

                if (value.equals(getString(R.string.setting_overview_date_range_custom_range_key))) {

                    mOverviewCategory.addPreference(seekBarPreference);
                    seekBarPreference.setUpdatesContinuously(true);

                } else {

                    seekBarPreference.setUpdatesContinuously(false);
                    mOverviewCategory.removePreference(seekBarPreference);
                }
            } else if (preference instanceof CheckBoxPreference) {
                String key = preference.getKey();

                if (key.equals(getString(R.string.setting_others_msg_tracker_rbc_default_key))) {
                    /*
                        when checkbox for extracting RBC is turning on,
                        there must be an ledger called 'RBC', if not, app will create one
                    */
                    if (((CheckBoxPreference) preference).isChecked()) {
                        ArrayList<String> ledgers = mGsonHelper.getLedgers(Constant.LEDGERS);

                        if (!ledgers.contains(Constant.RBC_LEDGER_NAME)) {
                            if (ledgers.size() > 1) {
                                ledgers.add(1, Constant.RBC_LEDGER_NAME);
                            } else {
                                ledgers.add(Constant.RBC_LEDGER_NAME);
                            }
                            mGsonHelper.saveDataToSharedPreference(ledgers, Constant.LEDGERS);

                            Toast.makeText(
                                    mAppCompatActivity,
                                    getString(R.string.create_rbc_ledger_toast_msg),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            /*For list preferences, figure out the label of the selected value*/
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                /*Set the summary to that label*/
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }


    private void setupViewModel() {

        GeneralViewModel mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);

        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_SETTING);


        mSettingsViewModel = ViewModelProviders.of(mAppCompatActivity).get(SettingsViewModel.class);
    }


    private Preference.OnPreferenceClickListener mEditExpenseCategoryListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {

            mSettingsViewModel.setSettingEditType(Constant.CATEGORY_TYPE_EXPENSE);
            goToSettingEditingFragment();
            return true;
        }
    };

    private Preference.OnPreferenceClickListener mEditRevenueCategoryListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            mSettingsViewModel.setSettingEditType(Constant.CATEGORY_TYPE_REVENUE);

            goToSettingEditingFragment();
            return true;

        }
    };

    private Preference.OnPreferenceClickListener mEditLedgerListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {

            mSettingsViewModel.setSettingEditType(Constant.LEDGERS);

            goToSettingEditingFragment();
            return true;
        }
    };

    private void goToSettingEditingFragment() {
        mAppCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.fragment_base, new SettingAddNEditFragment())
                .addToBackStack(Constant.FRAG_NAME_SETTING_ADD_EDIT)
                .commit();
    }

}
