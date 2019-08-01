package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import hanzhou.easyledger.data.RepositoryDB;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class TransactionDBViewModel extends AndroidViewModel {

    //LiveData that comes from ROOM
    private LiveData<List<TransactionEntry>> transactionsByLedger;
    private LiveData<List<TransactionEntry>> allTransactions;
    private LiveData<List<TransactionEntry>> untaggedTransactions;


    private MediatorLiveData<List<TransactionEntry>> testLedger = new MediatorLiveData<>();

    private TransactionDB mDB;

    private RepositoryDB mRepository;

    public TransactionDBViewModel(@NonNull Application application) {
        super(application);
        mDB = TransactionDB.getInstance(this.getApplication());

        mRepository = RepositoryDB.getInstance();

        //for future development, e.g. user creates multiple ledger
//        transactionsByLedger = mDB.transactionDAO().loadTransactionByLedger(Constant.UNTAGGED);
        allTransactions = mDB.transactionDAO().loadAllTransactions();
        untaggedTransactions = mRepository.getUntaggedTransaction();
    }


    public void updateTransactionOnUserInput(String input){

        if(input.equals(Constant.UNTAGGED)){
            transactionsByLedger = mRepository.getUntaggedTransaction();
        }else{
            if(input.equals("OVERALL")){
                transactionsByLedger = mRepository.getAllTransactions();
            }else{
                transactionsByLedger = mRepository.getTransactionByLedger(input);
            }

        }


    }

    public LiveData<List<TransactionEntry>> getTransactionsByLedger() {
        return transactionsByLedger;
    }


    public void setTransactionListFromInputLedgerName(String input) {

//       if (input.equals(Constant.LEDGER_OVERALL)) {
//
//            testLedger.removeSource(mRepository.getTransactionByLedger(input));
//            testLedger.addSource(mRepository.getAllTransactions(), new Observer<List<TransactionEntry>>() {
//                @Override
//                public void onChanged(List<TransactionEntry> transactionEntryList) {
//                    testLedger.setValue(transactionEntryList);
//                }
//            });
//
//        }else{
//
//
//
//            testLedger.addSource(mRepository.getTransactionByLedger(input), new Observer<List<TransactionEntry>>() {
//                @Override
//                public void onChanged(List<TransactionEntry> transactionEntryList) {
//                    testLedger.setValue(transactionEntryList);
//                }
//            });
//        }




    }

//    public LiveData<List<TransactionEntry>>getTransactionsByLedger(){
//        return testLedger;
//    }

//    public LiveData<List<TransactionEntry>> getTransactionsByLedger() {
//        return transactionsByLedger;
//    }

    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {
        return untaggedTransactions;
    }

    public LiveData<List<TransactionEntry>> getAllTransactions() {
        return allTransactions;
    }


    //todo  switchmap works too
//    private MutableLiveData<String> ledgerName = new MutableLiveData<>();
//    private LiveData<List<TransactionEntry>> listEntries = Transformations.switchMap(ledgerName,
//            new Function<String, LiveData<List<TransactionEntry>>>() {
//
//        @Override
//        public LiveData<List<TransactionEntry>> apply(String input) {
//            if(input.equals(Constant.LEDGER_OVERALL)){
//                return mRepository.getAllTransactions();
//            }else{
//
//                return mRepository.getTransactionByLedger(input);
//            }
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
