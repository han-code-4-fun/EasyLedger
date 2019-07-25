package hanzhou.easyledger.data;

import android.app.Application;

import hanzhou.easyledger.utility.Constant;

public class RepositoryUpdate {

    private static RepositoryUpdate mInstance;

    private RepositoryUpdate(){

    }

    public static RepositoryUpdate getInstance(){
        if(mInstance == null){
            mInstance = new RepositoryUpdate();
        }
        return mInstance;
    }

    private TransactionDB mTransactionDB;
    private AppExecutors mAppExecutors;



    public void initializeRepository(Application application){
        mTransactionDB = TransactionDB.getInstance(application);

        mAppExecutors=AppExecutors.getInstance();
    }



    public void renameHistoryLedger(final String inputString, final String deletedLedgerName){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDB.transactionDAO().markHistoryLedger(inputString, deletedLedgerName);

            }
        });
    }

    public void renameHistoryCategory(final String inputString, final String deletedCategory){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDB.transactionDAO().markHistoryCategory(inputString, deletedCategory);

            }
        });
    }

    public void applyUpdateToExistingUntaggedTransaction(final String remark, final String category){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTransactionDB.transactionDAO().applyUpdateCategory(remark,category, Constant.UNTAGGED);
            }
        });
    }


}
