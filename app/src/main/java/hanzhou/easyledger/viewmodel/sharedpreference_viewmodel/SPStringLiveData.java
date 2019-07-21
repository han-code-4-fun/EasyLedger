package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.content.SharedPreferences;

public class SPStringLiveData extends SPLiveData<String> {

    public SPStringLiveData(SharedPreferences prefs, String key, String defValue) {
        super(prefs, key, defValue);
    }

    @Override
    String getValueFromPreferences(String key, String defValue) {
        return sharedPrefs.getString(key,defValue);
    }
}
