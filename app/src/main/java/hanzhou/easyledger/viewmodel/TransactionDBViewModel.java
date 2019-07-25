package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import hanzhou.easyledger.data.Repository;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class TransactionDBViewModel extends AndroidViewModel {

    //LiveData that comes from ROOM
    private LiveData<List<TransactionEntry>> transactionsByLedger;
    private LiveData<List<TransactionEntry>> allTransactions;


    private MediatorLiveData<List<TransactionEntry>>
            entriesListOnUserInput = new MediatorLiveData<>();


    private TransactionDB mDB;

    private Repository mRepository;

    public TransactionDBViewModel(@NonNull Application application) {
        super(application);
        mDB = TransactionDB.getInstance(this.getApplication());

        mRepository= Repository.getInstance();

        //for future development, e.g. user creates multiple ledger
//        transactionsByLedger = mDB.transactionDAO().loadTransactionByLedger(Constant.UNTAGGED);
        allTransactions = mDB.transactionDAO().loadAllTransactions();

    }

//
//    public LiveData<List<TransactionEntry>> getTransactionsByLedger() {
//        return transactionsByLedger;
//    }

    public void updateTransactionOnUserInput(String input){

        if(input.equals(Constant.UNTAGGED)){
            transactionsByLedger = mRepository.getUntaggedTransaction();
        }else{
            if(input.equals("OVERALL")){
                transactionsByLedger = mRepository.getAllTransactions();
            }else{
                Log.d("test_test", "updateTransactionOnUserInput:   ledger name -> "+input);
                transactionsByLedger = mRepository.getTransactionByLedger(input);
            }

        }


    }

    public LiveData<List<TransactionEntry>> getTransactionsByLedger() {
        return transactionsByLedger;
    }

    public LiveData<List<TransactionEntry>> getlistOfTransactionsOnUserInput(){
        return entriesListOnUserInput;
    }

    public LiveData<List<TransactionEntry>> getAllTransactions() {
        return allTransactions;
    }


    //todo  switchmap works too
//    private MutableLiveData<String> ledgerName = new MutableLiveData<>();
//    private LiveData<List<TransactionEntry>> listEntries = Transformations.switchMap(ledgerName,
//            new Function<String, LiveData<List<TransactionEntry>>>() {
//        @Override
//        public LiveData<List<TransactionEntry>> apply(String input) {
//            return mDB.transactionDAO().loadTransactionByLedger(input);
//        }
//    });
//
//    public void setLedgerName(String index) {
//        ledgerName.setValue(index);
//    }
//
//    public LiveData<List<TransactionEntry>> getListEntriesByLedger() {
//        return listEntries;
//    }


}
