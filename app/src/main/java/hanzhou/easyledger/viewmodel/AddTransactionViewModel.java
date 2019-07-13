package hanzhou.easyledger.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;

public class AddTransactionViewModel  extends ViewModel {

    private LiveData<TransactionEntry> transactionEntry;

    private MutableLiveData<String> mSelectedCategory;

    public AddTransactionViewModel(TransactionDB DB, int transactionID) {
        transactionEntry =DB.transactionDAO().loadTransactionByID(transactionID);
        mSelectedCategory = new MutableLiveData<>();
        mSelectedCategory.setValue("");
    }

    public LiveData<TransactionEntry> getTransactionEntry() {
        return transactionEntry;
    }

    public void setmSelectedCategory(String input){
        mSelectedCategory.setValue(input);
    }

    public LiveData<String> getmSelectedCategory(){
        return mSelectedCategory;
    }
}
