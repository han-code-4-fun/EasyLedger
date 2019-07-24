package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import hanzhou.easyledger.data.RepositoryUpdate;
import hanzhou.easyledger.data.TransactionEntry;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mCategoryToEdit;
//
//    private MutableLiveData<ArrayList<String>> mCategoryExpense;
//    private MutableLiveData<ArrayList<String>> mCategoryRevenue;


    private RepositoryUpdate mRepositoryUpdate;
    public SettingsViewModel(@NonNull Application application) {
        super(application);

        mRepositoryUpdate = new RepositoryUpdate(application);
        mCategoryToEdit = new MutableLiveData<>();
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

}
