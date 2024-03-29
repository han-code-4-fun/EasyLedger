package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.content.SharedPreferences;

public class SPBoolLiveData extends SPLiveData<Boolean> {
    public SPBoolLiveData(SharedPreferences prefs, String key, Boolean defValue) {
        super(prefs, key, defValue);
    }

    @Override
    Boolean getValueFromPreferences(String key, Boolean defValue) {
        return sharedPrefs.getBoolean(key, defValue);
    }
}
