package hanzhou.easyledger.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import hanzhou.easyledger.utility.Constant;

public class Repository {

    private TransactionDB mTransactionDB;
    private static Repository mInstance;

    private Repository() {

    }

    public static Repository getInstance() {
        if (mInstance == null) {
            mInstance = new Repository();
        }
        return mInstance;
    }

    public void initializeRepository(Application application) {
        mTransactionDB = TransactionDB.getInstance(application);
//        mTransactionDAO = mTransactionDB.transactionDAO();
    }

    public LiveData<List<TransactionEntry>> getPeriodOfEntries(int start, int end) {
        return mTransactionDB.transactionDAO().loadTransactionInPeriodForChart(start, end);
    }

    public LiveData<List<TransactionEntry>> getCurrentRevenueOfEntries(int startDate) {
        return mTransactionDB.transactionDAO().loadTransactionRevenuePeriod(startDate);
    }

    public LiveData<List<TransactionEntry>> getCurrentExpenseOfEntries(int startDate) {
        return mTransactionDB.transactionDAO().loadTransactionExpensePeriod(startDate);
    }

    public LiveData<List<TransactionEntry>> getPeriodOfEntriesForOverview(int start) {
        return mTransactionDB.transactionDAO().loadTransactionByTimeUserDefined(start);
    }

    public LiveData<List<TransactionEntry>> getUntaggedTransaction() {
        return mTransactionDB.transactionDAO().loadUntaggedTransactions(Constant.UNTAGGED);
    }

    public LiveData<List<TransactionEntry>> getTransactionByLedger(String ledgerName) {
        return mTransactionDB.transactionDAO().loadTransactionByLedger(ledgerName);
    }

    public LiveData<List<TransactionEntry>> getAllTransactions() {
        return mTransactionDB.transactionDAO().loadAllTransactions();
    }


}
