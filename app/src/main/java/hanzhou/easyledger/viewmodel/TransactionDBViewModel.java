package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.ui.OverviewFragment;
import hanzhou.easyledger.utility.Constant;

public class TransactionDBViewModel extends ViewModel {

    private static final String TAG = OverviewFragment.class.getSimpleName();

    private LiveData<List<TransactionEntry>> untaggedTransactions;

    private LiveData<List<TransactionEntry>> allTransactions;




    public TransactionDBViewModel(TransactionDB mDB, String inputTag) {

        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        //the overview fragment only diaplay these (new) transactions that needed to be tagged by user
        untaggedTransactions = mDB.transactionDAO().loadTransactionByLedger(inputTag);
        allTransactions = mDB.transactionDAO().loadAllTransactions();
    }

    public LiveData<List<TransactionEntry>> getUntaggedTransactions(){
        return untaggedTransactions;
    }

    public LiveData<List<TransactionEntry>> getAllTransactions(){
        return allTransactions;
    }
}
