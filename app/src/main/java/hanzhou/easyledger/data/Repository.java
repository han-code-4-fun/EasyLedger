package hanzhou.easyledger.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import hanzhou.easyledger.utility.Constant;

public class Repository {
    private AppExecutors mAppexecutor;

    private TransactionDB mTransactionDB;

    private final TransactionDAO mTransactionDAO;

    private List<TransactionEntry> mTransactionListByLedger;

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

    public LiveData<List<TransactionEntry>> getPeriodOfEntriesForOverview(int start){
        return mTransactionDAO.loadTransactionByTimeUserDefined(start);
    }
    public LiveData<List<TransactionEntry>> getUntaggedTransaction(){
        return mTransactionDAO.loadUntaggedTransactions(Constant.UNTAGGED);
    }

   public LiveData<List<TransactionEntry>> getTransactionByLedger(String ledgerName){
        return mTransactionDAO.loadTransactionByLedger(ledgerName);
   }

    public LiveData<List<TransactionEntry>> getAllTransactions(){
        return mTransactionDAO.loadAllTransactions();
    }


}
