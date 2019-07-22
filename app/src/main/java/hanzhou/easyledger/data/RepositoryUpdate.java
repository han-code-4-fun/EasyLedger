package hanzhou.easyledger.data;

import android.app.Application;

public class RepositoryUpdate {

    private final TransactionDAO mTransactionDAO;

    private AppExecutors mAppExecutors;

    public RepositoryUpdate(Application application){
        TransactionDB mTransactionDB = TransactionDB.getInstance(application);
        mTransactionDAO = mTransactionDB.transactionDAO();
        mAppExecutors=AppExecutors.getInstance();
    }

    public void renameHistoryLedger(final String inputString, final String deletedLedgerName){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDAO.markHistoryLedger(inputString, deletedLedgerName);

            }
        });
    }

    public void renameHistoryCategory(final String inputString, final String deletedCategory){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDAO.markHistoryCategory(inputString, deletedCategory);

            }
        });
    }
}
