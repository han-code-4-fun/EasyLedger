package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SPViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SharedPreferences sharedPreferences;


    public SPViewModelFactory(SharedPreferences inputSharedPreferences) {

        sharedPreferences = inputSharedPreferences;
    }

    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SPViewModel(sharedPreferences);

    }

}
