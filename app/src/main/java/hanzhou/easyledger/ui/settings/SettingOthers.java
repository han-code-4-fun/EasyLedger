package hanzhou.easyledger.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import hanzhou.easyledger.R;

public class SettingOthers extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_others);

    }
}
