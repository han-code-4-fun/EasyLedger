package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.ui.OverviewFragment;
import hanzhou.easyledger.utility.Constant;

public class OverviewViewModel extends AndroidViewModel {

    private static final String TAG = OverviewFragment.class.getSimpleName();

    private LiveData<List<TransactionEntry>> transactionEntryList;

    public OverviewViewModel(@NonNull Application application) {
        super(application);

        TransactionDB database =TransactionDB.getInstance(this.getApplication());

        //the overview fragment only diaplay these (new) transactions that needed to be tagged by user
        transactionEntryList = database.transactionDAO().loadTransactionByLedger(Constant.untagged);
    }

    public LiveData<List<TransactionEntry>> getUntaggedTransactions(){
        return transactionEntryList;
    }
}
