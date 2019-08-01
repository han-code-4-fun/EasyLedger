package hanzhou.easyledger_demo.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import hanzhou.easyledger_demo.data.RepositoryDB;
import hanzhou.easyledger_demo.data.TransactionDB;
import hanzhou.easyledger_demo.data.TransactionEntry;

public class AddTransactionViewModel extends ViewModel {

    private LiveData<TransactionEntry> transactionEntry;


    public AddTransactionViewModel(TransactionDB DB, int transactionID) {
        transactionEntry = RepositoryDB.getInstance().getTransactionByID(transactionID);
    }

    public LiveData<TransactionEntry> getTransactionEntry() {
        return transactionEntry;
    }

}
