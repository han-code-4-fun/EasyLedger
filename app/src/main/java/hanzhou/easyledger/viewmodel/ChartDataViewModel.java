package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;

public class ChartDataViewModel extends AndroidViewModel {

    private LiveData<List<TransactionEntry>> mRevenueListEntry;
    private LiveData<List<TransactionEntry>> mExpenseListEntry;
    private LiveData<List<TransactionEntry>> mAllListEntryPeriod;


    private TransactionDB mDB;

    public ChartDataViewModel(@NonNull Application application) {
        super(application);
        mDB = TransactionDB.getInstance(getApplication());
    }

    public void setmRevenueListEntry(int startingDay){
        mRevenueListEntry = mDB.transactionDAO().loadTransactionRevenuePeriod(startingDay);
    }

    public void setmExpenseListEntry(int startingDay){
        mExpenseListEntry = mDB.transactionDAO().loadTransactionExpensePeriod(startingDay);
    }

    public void setmAllListEntryPeriod(int startingDay, int endingDay){
        mAllListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(startingDay, endingDay);
    }

    public LiveData<List<TransactionEntry>> getmRevenueListEntry(){
        return mRevenueListEntry;
    }

    public LiveData<List<TransactionEntry>> getmExpenseListEntry(){
        return mExpenseListEntry;
    }
    public LiveData<List<TransactionEntry>> getmAllListEntryPeriod(){
        return mAllListEntryPeriod;
    }

}
