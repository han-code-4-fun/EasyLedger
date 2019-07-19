package hanzhou.easyledger.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.prefs.AbstractPreferences;

import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDAO;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;

public class Repository {
    private AppExecutors mAppexecutor;

    private TransactionDB mTransactionDB;

    private final TransactionDAO mTransactionDAO;

    public Repository(Application application){
        mTransactionDB = TransactionDB.getInstance(application);
        mTransactionDAO = mTransactionDB.transactionDAO();
        mAppexecutor = AppExecutors.getInstance();
    }

    public LiveData<List<TransactionEntry>> getPeriodOfEntries(int start, int end){
        return mTransactionDAO.loadTransactionInPeriodForChart(start, end);
    }

    public LiveData<List<TransactionEntry>> getCurrentRevenueOfEntries(int startDate){
        return mTransactionDAO.loadTransactionRevenuePeriod(startDate);
    }
    public LiveData<List<TransactionEntry>> getCurrentExpenseOfEntries(int startDate){
        return mTransactionDAO.loadTransactionExpensePeriod(startDate);
    }

}
