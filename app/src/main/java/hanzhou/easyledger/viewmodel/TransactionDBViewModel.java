package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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

    private RepositoryDB mDBRepository;

    public TransactionDBViewModel(@NonNull Application application) {
        super(application);
        mDB = TransactionDB.getInstance(this.getApplication());

        mDBRepository = RepositoryDB.getInstance();

        //for future development, e.g. user creates multiple ledger
//        transactionsByLedger = mDB.transactionDAO().loadTransactionByLedger(Constant.UNTAGGED);
        allTransactions = mDB.transactionDAO().loadAllTransactions();
        untaggedTransactions = mDBRepository.getUntaggedTransaction();
    }


    public void setTransactionListFromInputLedgerName(String input) {

//       if (input.equals(Constant.LEDGER_OVERALL)) {
//            Log.d("test_frag", "-----------------> data for OVERALL transaction ------------>");
//
//            testLedger.removeSource(mDBRepository.getTransactionByLedger(input));
//            testLedger.addSource(mDBRepository.getAllTransactions(), new Observer<List<TransactionEntry>>() {
//                @Override
//                public void onChanged(List<TransactionEntry> transactionEntryList) {
//                    testLedger.setValue(transactionEntryList);
//                }
//            });
//
//        }else{
//
//            Log.d("test_frag", "-----------------> data for "+input+" transaction ------------>");
//
//
//            testLedger.addSource(mDBRepository.getTransactionByLedger(input), new Observer<List<TransactionEntry>>() {
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
    private MutableLiveData<String> ledgerName = new MutableLiveData<>();
    private LiveData<List<TransactionEntry>> listEntries = Transformations.switchMap(ledgerName,
            new Function<String, LiveData<List<TransactionEntry>>>() {

        @Override
        public LiveData<List<TransactionEntry>> apply(String input) {
            if(input.equals(Constant.LEDGER_OVERALL)){
                return mDBRepository.getAllTransactions();
            }else{

                return mDBRepository.getTransactionByLedger(input);
            }
        }
    });

    public void setLedgerName(String index) {
        ledgerName.setValue(index);
    }

    public LiveData<List<TransactionEntry>> getListEntriesByLedger() {
        return listEntries;
    }


}
