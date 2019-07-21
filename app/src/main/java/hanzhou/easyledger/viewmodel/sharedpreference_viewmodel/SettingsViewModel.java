package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mCategoryToEdit;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        mCategoryToEdit = new MutableLiveData<>();

    }

    public void setSettingEditType(String input){
        mCategoryToEdit.setValue(input);
    }

    public MutableLiveData<String> getmCategoryType(){
        return mCategoryToEdit;
    }
 }
