package hanzhou.easyledger_demo.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import hanzhou.easyledger_demo.utility.Constant;
import hanzhou.easyledger_demo.utility.GsonHelper;
import hanzhou.easyledger_demo.utility.TestingData;

public class RepositoryDB {

    private TransactionDB mTransactionDB;
    private static RepositoryDB mInstance;
    private AppExecutors mAppExecutors;

    private RepositoryDB() {

    }

    public static RepositoryDB getInstance() {
        if (mInstance == null) {
            mInstance = new RepositoryDB();
        }
        return mInstance;
    }

    public void initializeRepository(Application application) {
        mTransactionDB = TransactionDB.getInstance(application);
        mAppExecutors = AppExecutors.getInstance();
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

    public LiveData<TransactionEntry> getTransactionByID(int id) {
        return mTransactionDB.transactionDAO().loadTransactionByID(id);
    }


    public void renameHistoryLedger(final String inputString, final String deletedLedgerName) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDB.transactionDAO().markHistoryLedger(inputString, deletedLedgerName);

            }
        });
    }

    public void renameHistoryCategory(final String inputString, final String deletedCategory) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDB.transactionDAO().markHistoryCategory(inputString, deletedCategory);

            }
        });
    }

    public void applyUpdateToExistingUntaggedTransaction(final String remark, final String category) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDB.transactionDAO().applyUpdateCategory(remark, category, Constant.UNTAGGED);
            }
        });
    }

    public void deleteSelectedTransactions(final List<TransactionEntry> input) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                mTransactionDB.transactionDAO().deleteListOfTransactions(input);
            }
        });
    }

    public void deleteAllTransactions() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                mTransactionDB.transactionDAO().deleteAll();
            }
        });
    }

    public void insertTransactions(final GsonHelper mGsonHelper) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDB.transactionDAO().insertListOfTransactions(TestingData.create1000Transactions(mGsonHelper));
            }
        });
    }


}
