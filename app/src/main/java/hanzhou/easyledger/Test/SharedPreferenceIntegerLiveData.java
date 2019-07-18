package hanzhou.easyledger.Test;

import android.content.SharedPreferences;

public class SharedPreferenceIntegerLiveData extends SharedPreferenceLiveData<Integer>{
    public SharedPreferenceIntegerLiveData(SharedPreferences prefs, String key, Integer defValue) {
        super(prefs, key, defValue);
    }

    @Override
    Integer getValueFromPreferences(String key, Integer defValue) {
        return sharedPrefs.getInt(key,defValue);
    }
}
