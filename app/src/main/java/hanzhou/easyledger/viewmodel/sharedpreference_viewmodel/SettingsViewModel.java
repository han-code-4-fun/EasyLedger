package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hanzhou.easyledger.data.RepositoryUpdate;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mCategoryToEdit;
//
//    private MutableLiveData<ArrayList<String>> mCategoryExpense;
//    private MutableLiveData<ArrayList<String>> mCategoryRevenue;

    private MutableLiveData<Boolean> mRefreshLedgerFragmentTrigger;

    private RepositoryUpdate mRepositoryUpdate;
    public SettingsViewModel(@NonNull Application application) {
        super(application);

        mRepositoryUpdate = RepositoryUpdate.getInstance();
        mRepositoryUpdate.initializeRepository(application);
        mCategoryToEdit = new MutableLiveData<>();
        mRefreshLedgerFragmentTrigger = new MutableLiveData<>();
        mRefreshLedgerFragmentTrigger.setValue(false);
    }

    public void setSettingEditType(String input){
        mCategoryToEdit.setValue(input);
    }

    public MutableLiveData<String> getmCategoryType(){
        return mCategoryToEdit;
    }

    public void renameHistoryLedger(String inputString, String deletedLedgerName){
        mRepositoryUpdate.renameHistoryLedger(inputString,deletedLedgerName);
    }

    public void renameHistoryCategory(String inputString, String deletedCategory){
        mRepositoryUpdate.renameHistoryCategory(inputString, deletedCategory);
    }

    public MutableLiveData<Boolean> getmRefreshLedgerFragmentTrigger() {
        return mRefreshLedgerFragmentTrigger;
    }

    public void setmRefreshLedgerFragmentTrigger(Boolean input) {
        this.mRefreshLedgerFragmentTrigger.setValue(input);
    }
}
