package hanzhou.easyledger.ui.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingMain extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener{

    private AdapterNActionBarViewModel adapterNActionBarViewModel;
    private AppCompatActivity appCompatActivity;
    private Toolbar toolbar;
    private ListPreference mListOfDayRanges;

    private SeekBarPreference seekBarPreference;

    private PreferenceCategory mOverviewCategory;
    private String mCurrentOverviewDatesRange;

    public SettingMain() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_main);

        mOverviewCategory = findPreference(getString(R.string.setting_category_overview_key));
        seekBarPreference = findPreference(getString(R.string.setting_overview_custom_range_seekbar_key));


        mListOfDayRanges = findPreference(getString(R.string.setting_general_overview_date_range_list_key));

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        mCurrentOverviewDatesRange =sharedPreferences.getString(mListOfDayRanges.getKey(), getString(R.string.empty_string));
        if(!mCurrentOverviewDatesRange.equals(getString(R.string.setting_overview_date_range_custom_range_key))){
            mOverviewCategory.removePreference(seekBarPreference);
        }
        PreferenceScreen prefScreen = getPreferenceScreen();

        int count = prefScreen.getPreferenceCount();

        Preference settingEditLedger = findPreference(getString(R.string.setting_transaction_edit_ledger_key));
        if(settingEditLedger!= null){

            settingEditLedger.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    appCompatActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.base_fragment, new SettingEditLedger())
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            });
        }

//        // Go through all of the preferences, and set up their preference summary.
//        for (int i = 0; i < count; i++) {
//            Preference p = prefScreen.getPreference(i);
//            // You don't need to set up preference summaries for checkbox preferences because
//            // they are already set up in xml using summaryOff and summary On
//            if (!(p instanceof CheckBoxPreference)) {
//                String value = sharedPreferences.getString(p.getKey(), "");
//                setPreferenceSummary(p, value);
//            }
//        }

        // COMPLETED (3) Add the OnPreferenceChangeListener specifically to the EditTextPreference
        // Add the preference listener which checks that the size is correct to the size preference
//        Preference preference = findPreference(getString(R.string.pref_size_key));
//        preference.setOnPreferenceChangeListener(this);
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
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Figure out which preference was changed
        Preference preference = findPreference(s);
        if (null != preference) {
            // Updates the summary for the preference
//            if (!(preference instanceof CheckBoxPreference)) {
//                String value = sharedPreferences.getString(preference.getKey(), "");
//                setPreferenceSummary(preference, value);
//            }
            if(preference instanceof ListPreference){
                String value = sharedPreferences.getString(preference.getKey(), "");
                mCurrentOverviewDatesRange = value;
                Log.d("test_preference", "onSharedPreferenceChanged:  selected list key is -> "+ value);
                if(value.equals(getString(R.string.setting_overview_date_range_custom_range_key)) ){
                    mOverviewCategory.addPreference(seekBarPreference);
                        seekBarPreference.setUpdatesContinuously(true);
                }else{

                    seekBarPreference.setUpdatesContinuously(false);
                    mOverviewCategory.removePreference(seekBarPreference);
                }
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // In this context, we're using the onPreferenceChange listener for checking whether the
        // size setting was set to a valid value.

        Toast error = Toast.makeText(getContext(), "Please select a number between 0.1 and 3", Toast.LENGTH_SHORT);

        // Double check that the preference is the size preference
//        String sizeKey = getString(R.string.pref_size_key);
//        if (preference.getKey().equals(sizeKey)) {
//            String stringSize = (String) newValue;
//            try {
//                float size = Float.parseFloat(stringSize);
//                // If the number is outside of the acceptable range, show an error.
//                if (size > 3 || size <= 0) {
//                    error.show();
//                    return false;
//                }
//            } catch (NumberFormatException nfe) {
//                // If whatever the user entered can't be parsed to a number, show an error
//                error.show();
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapterNActionBarViewModel.setmIsInSettingsFragment(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar =  appCompatActivity.findViewById(R.id.toolbar_layout);
        toolbar.setTitle(getString(R.string.title_settings_fragment));
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar_empty, menu);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapterNActionBarViewModel.setmIsInSettingsFragment(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    private void setupViewModel() {
        adapterNActionBarViewModel = ViewModelProviders.of(appCompatActivity).get(AdapterNActionBarViewModel.class);
    }
}
