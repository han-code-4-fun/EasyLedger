package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;

public class ChartDataViewModel extends ViewModel {

    private AppExecutors mAppexecutor;

    private LiveData<List<TransactionEntry>> mRevenueListEntry;
    private LiveData<List<TransactionEntry>> mExpenseListEntry;
    private LiveData<List<TransactionEntry>> mAllListEntryPeriod;

    private MutableLiveData<Boolean> mChartSettingChanged;

    private TransactionDB mDB;


    public ChartDataViewModel(int CurrentChartStartDate, int HistoryChartStartDate,
                              int HistoryChartEndDate, TransactionDB transactionDB){
        mDB = transactionDB;
        Log.d("test_111_starting  VM", "VM  current startingdate is "+ CurrentChartStartDate);
        mRevenueListEntry = mDB.transactionDAO().loadTransactionRevenuePeriod(CurrentChartStartDate);
        mExpenseListEntry = mDB.transactionDAO().loadTransactionExpensePeriod(CurrentChartStartDate);
        Log.d("test_111_starting  VM", "VM  HISTORY startingdate & enddate is "+ HistoryChartStartDate+" ()() "+HistoryChartEndDate);

        mAllListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(
                HistoryChartStartDate, HistoryChartEndDate);
        mChartSettingChanged = new MutableLiveData<>();
        mChartSettingChanged.setValue(false);
    }



    public LiveData<List<TransactionEntry>> getmRevenueListEntry(){
        Log.d("test_flow49", "VM get revenue list: "+mRevenueListEntry.hashCode());
        return mRevenueListEntry;
    }

    public LiveData<List<TransactionEntry>> getmExpenseListEntry(){

        return mExpenseListEntry;
    }

    public LiveData<List<TransactionEntry>> getmAllListEntryPeriod(){

        return mAllListEntryPeriod;
    }


    public LiveData<Boolean> getmChartSettingChanged() {
        return mChartSettingChanged;
    }

    public void setmChartSettingChanged(Boolean input) {
        this.mChartSettingChanged.setValue(input);
    }




    /*  public void setmRevenueListEntry(final int startingDay){
         AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mRevenueListEntry= mDB.transactionDAO().loadTransactionRevenuePeriod(startingDay);
            }

        });
    }

    public void setmExpenseListEntry(final int startingDay){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mExpenseListEntry= mDB.transactionDAO().loadTransactionExpensePeriod(startingDay);
            }

        });
    }

    public void setmAllListEntryPeriod(final int startingDay, final int endingDay){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAllListEntryPeriod= mDB.transactionDAO().loadTransactionInPeriodForChart(startingDay, endingDay);
            }

        });
    }*/
}
