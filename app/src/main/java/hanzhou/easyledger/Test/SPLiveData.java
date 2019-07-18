package hanzhou.easyledger.Test;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

/*
* original @author Idish
* https://stackoverflow.com/users/1203043/idish
*
* which was posted on stackoverflow by another one
* https://stackoverflow.com/questions/50649014/livedata-with-shared-preferences
*
*
* modified by Han Zhou
*
*
* */

public abstract class SPLiveData<T> extends LiveData<T> {
    SharedPreferences sharedPrefs;

    String key;
    public T defValue;

    public SPLiveData(SharedPreferences prefs, String key, T defValue) {
        this.sharedPrefs = prefs;
        this.key = key;
        this.defValue = defValue;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener
            preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (SPLiveData.this.key.equals(key)) {
                setValue(getValueFromPreferences(key, defValue));
            }
        }
    };

    abstract T getValueFromPreferences(String key, T defValue);

    @Override
    protected void onActive() {
        super.onActive();

        setValue(getValueFromPreferences(key, defValue));
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

}
