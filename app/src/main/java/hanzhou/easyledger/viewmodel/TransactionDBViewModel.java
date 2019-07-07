package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class TransactionDBViewModel extends AndroidViewModel {

    //LiveData that comes from ROOM
    private LiveData<List<TransactionEntry>> transactionsByLedger;
    private LiveData<List<TransactionEntry>> allTransactions;



    public TransactionDBViewModel(@NonNull Application application) {
        super(application);
        TransactionDB mDB = TransactionDB.getInstance(this.getApplication());

        //for future development, e.g. user creates multiple ledger
        transactionsByLedger = mDB.transactionDAO().loadTransactionByLedger(Constant.UNTAGGED);
        allTransactions = mDB.transactionDAO().loadAllTransactions();


    }


    public LiveData<List<TransactionEntry>> getTransactionsByLedger() {
        return transactionsByLedger;
    }

    public LiveData<List<TransactionEntry>> getAllTransactions() {
        return allTransactions;
    }



}
