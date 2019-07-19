package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SPViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SharedPreferences sharedPreferences;
//    private final String key;
//    private final int defaultVal;


    public SPViewModelFactory(SharedPreferences inputSharedPreferences) {

        sharedPreferences = inputSharedPreferences;
//        key = inputKey;
//        defaultVal = inputVal;
    }

    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d("test_test_", "start to  create: from TransactionDBVMFactory");
//        return (T) new SPViewModel(sharedPreferences, key,defaultVal);
        return (T) new SPViewModel(sharedPreferences);

    }

}
