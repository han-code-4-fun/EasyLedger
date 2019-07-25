package hanzhou.easyledger.ui.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.GsonHelper;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingMain extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, SeekBar.OnSeekBarChangeListener {

    private AppCompatActivity mAppCompatActivity;

    private AdapterNActionBarViewModel mAdapterActionViewModel;

    private SharedPreferences mSharedPreferences;

    private Toolbar toolbar;
    private ListPreference mListOfDayRanges;

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
        mSharedPreferences = getPreferenceScreen().getSharedPreferences();

        mOverviewCategory = findPreference(getString(R.string.setting_category_overview_key));
        seekBarPreference = findPreference(getString(R.string.setting_overview_custom_range_seekbar_key));


        mListOfDayRanges = findPreference(getString(R.string.setting_general_overview_date_range_list_key));



        //initially hide seekbar if not the right selesction
        mCurrentOverviewDatesRange = mSharedPreferences.getString(mListOfDayRanges.getKey(), getString(R.string.empty_string));
        if(!mCurrentOverviewDatesRange.equals(getString(R.string.setting_overview_date_range_custom_range_key))){
            mOverviewCategory.removePreference(seekBarPreference);
        }
        setPreferenceSummary(mListOfDayRanges, mCurrentOverviewDatesRange);


        Preference settingEditLedger =
                findPreference(getString(R.string.setting_transaction_edit_ledger_key));
        if(settingEditLedger!= null){

            settingEditLedger.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    mSettingsViewModel.setSettingEditType(Constant.LEDGERS);

                    mAppCompatActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.base_fragment, new SettingAddNEditFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            });
        }

        Preference editRevenueCategory =
                findPreference(getString(R.string.setting_others_edit_category_revenue_key));
        if(editRevenueCategory!= null){

            editRevenueCategory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    mSettingsViewModel.setSettingEditType(Constant.CATEGORY_TYPE_REVENUE);

                    mAppCompatActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.base_fragment, new SettingAddNEditFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            });
        }

        Preference editExpenseCategory =
                findPreference(getString(R.string.setting_others_edit_category_expense_key));
        if(editExpenseCategory!= null){

            editExpenseCategory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    mSettingsViewModel.setSettingEditType(Constant.CATEGORY_TYPE_EXPENSE);
                    mAppCompatActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.base_fragment, new SettingAddNEditFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            });
        }

//        Preference checkboxRBC = (CheckBoxPreference)findPreference(getString(R.string.setting_others_msg_tracker_rbc_default_key));
//        ((CheckBoxPreference) checkboxRBC).isChecked()
//

    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar = mAppCompatActivity.findViewById(R.id.toolbar_layout);
        toolbar.setTitle(getString(R.string.title_settings_fragment));
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar_empty, menu);
        mAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                mAppCompatActivity.getSupportFragmentManager().popBackStack();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapterActionViewModel.setmIsInBaseFragment(false);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Figure out which preference was changed
        Preference preference = findPreference(s);
        if (preference != null) {

            if(preference instanceof ListPreference){
                String value = sharedPreferences.getString(preference.getKey(), "");
                mCurrentOverviewDatesRange = value;
                setPreferenceSummary(preference, value);

                if(value.equals(getString(R.string.setting_overview_date_range_custom_range_key)) ){

                    mOverviewCategory.addPreference(seekBarPreference);
                    seekBarPreference.setUpdatesContinuously(true);

                }else{

                    seekBarPreference.setUpdatesContinuously(false);
                    mOverviewCategory.removePreference(seekBarPreference);
                }
            }else if(preference instanceof CheckBoxPreference){
                String key = preference.getKey();

                if(key.equals(getString(R.string.setting_others_msg_tracker_rbc_default_key))){
                    /*
                        when checkbox for extracting RBC is turning on,
                        there must be an ledger called RBC, if not, app will create one
                    */
                    if(((CheckBoxPreference) preference).isChecked()){
                        ArrayList<String> ledgers =mGsonHelper.getLedgers(Constant.LEDGERS);

                        if(!ledgers.contains(Constant.RBC_LEDGER_NAME)){
                            if(ledgers.size()>1){
                                ledgers.add(1, Constant.RBC_LEDGER_NAME);
                            }else{
                                ledgers.add(Constant.RBC_LEDGER_NAME);
                            }
                            mGsonHelper.saveDataToSharedPreference(ledgers,Constant.LEDGERS);

                            Toast.makeText(
                                    mAppCompatActivity,
                                    getString(R.string.create_rbc_ledger_toast_msg),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
//                    else{
//                        Toast.makeText(mAppCompatActivity, "NOT checked!", Toast.LENGTH_LONG).show();
//
//                    }

                }
            }

        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //todo, change viewmodel

//        mAdapterActionViewModel.setmIsInSettingsFragment(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }


    private void setupViewModel() {
        mAdapterActionViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);

        mSettingsViewModel = ViewModelProviders.of(mAppCompatActivity).get(SettingsViewModel.class);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}
