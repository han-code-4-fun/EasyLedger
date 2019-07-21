package hanzhou.easyledger.viewmodel.sharedpreference_viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import hanzhou.easyledger.data.TransactionEntry;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mCategoryToEdit;
//
//    private MutableLiveData<ArrayList<String>> mCategoryExpense;
//    private MutableLiveData<ArrayList<String>> mCategoryRevenue;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        mCategoryToEdit = new MutableLiveData<>();
//        mCategoryExpense= new MutableLiveData<>();
//        mCategoryRevenue = new MutableLiveData<>();
    }

    public void setSettingEditType(String input){
        mCategoryToEdit.setValue(input);
    }

    public MutableLiveData<String> getmCategoryType(){
        return mCategoryToEdit;
    }

//    public MutableLiveData<ArrayList<String>> getmCategoryExpense() {
//        return mCategoryExpense;
//    }
//
//    public MutableLiveData<ArrayList<String>> getmCategoryRevenue() {
//        return mCategoryRevenue;
//    }
//
//    public void setmCategoryExpense(ArrayList<String> input) {
//        this.mCategoryExpense.setValue(input);
//    }
//
//    public void setmCategoryRevenue(ArrayList<String> input) {
//        this.mCategoryRevenue.setValue(input);
//    }
}
