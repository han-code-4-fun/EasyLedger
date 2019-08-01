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

    private LiveData<List<TransactionEntry>> transactionsByLedger;
    private LiveData<List<TransactionEntry>> untaggedTransactions;


    private RepositoryDB mRepository;

    public TransactionDBViewModel(@NonNull Application application) {
        super(application);

        mRepository = RepositoryDB.getInstance();
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


    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {
        return untaggedTransactions;
    }





}
