package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hanzhou.easyledger.data.RepositoryDB;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mCategoryToEdit;

    private MutableLiveData<Boolean> mRefreshLedgerFragmentTrigger;

    private RepositoryDB mDBRepository;
    public SettingsViewModel(@NonNull Application application) {
        super(application);

        mDBRepository = RepositoryDB.getInstance();
        mDBRepository.initializeRepository(application);
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
        mDBRepository.renameHistoryLedger(inputString,deletedLedgerName);
    }

    public void renameHistoryCategory(String inputString, String deletedCategory){
        mDBRepository.renameHistoryCategory(inputString, deletedCategory);
    }

    public MutableLiveData<Boolean> getmRefreshLedgerFragmentTrigger() {
        return mRefreshLedgerFragmentTrigger;
    }

    public void setmRefreshLedgerFragmentTrigger(Boolean input) {
        this.mRefreshLedgerFragmentTrigger.setValue(input);
    }
}
