package hanzhou.easyledger.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import hanzhou.easyledger.data.RepositoryDB;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;

public class AddTransactionViewModel extends ViewModel {

    private LiveData<TransactionEntry> transactionEntry;


    public AddTransactionViewModel(TransactionDB DB, int transactionID) {
        transactionEntry = RepositoryDB.getInstance().getTransactionByID(transactionID);
    }

    public LiveData<TransactionEntry> getTransactionEntry() {
        return transactionEntry;
    }

}
