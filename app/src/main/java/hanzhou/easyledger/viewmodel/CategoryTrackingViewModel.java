package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CategoryTrackingViewModel extends AndroidViewModel {
    private MutableLiveData<String> mSelectedCategory;

    public CategoryTrackingViewModel(@NonNull Application application) {
        super(application);
        mSelectedCategory = new MutableLiveData<>();
        mSelectedCategory.setValue("");
    }

    public MutableLiveData<String> getmSelectedCategory() {
        return mSelectedCategory;
    }

    public void setmSelectedCategory(String input) {
        this.mSelectedCategory.setValue(input);
    }
}
