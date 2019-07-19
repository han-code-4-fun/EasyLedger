package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.content.SharedPreferences;

public class SPIntLiveData extends SPLiveData<Integer> {
    public SPIntLiveData(SharedPreferences prefs, String key, Integer defValue) {
        super(prefs, key, defValue);
    }

    @Override
    Integer getValueFromPreferences(String key, Integer defValue) {
        return sharedPrefs.getInt(key,defValue);
    }
}
