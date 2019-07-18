package hanzhou.easyledger.viewmodel;

import android.util.Log;

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
    private LiveData<List<TransactionEntry>> mHistoryListEntryPeriod;


    private MutableLiveData<List<TransactionEntry>> mAllListMutable;

    private MutableLiveData<Boolean> mChartSettingChanged;

    private TransactionDB mDB;

    private int mHistoryStartDate;
    private int mHistoryEndDate;

    private MutableLiveData<Integer> mHistoryStartDateNew;
    private MutableLiveData<Integer> mHistoryEndDateNew;




//    public ChartDataViewModel(@NonNull Application application) {
//        super(application);
//
//            mDB = TransactionDB.getInstance(application);
//
//
//
//
//
//            mChartSettingChanged = new MutableLiveData<>();
//            mChartSettingChanged.setValue(false);
//
//
//
////        mHistoryStartDateNew = new MutableLiveData<>();
////        mHistoryStartDateNew.setValue(HistoryChartStartDate);
////        mHistoryEndDateNew = new MutableLiveData<>();
////        mHistoryEndDateNew.setValue(HistoryChartEndDate);
////        mHistoryListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(
////                mHistoryStartDateNew.getValue(), mHistoryEndDateNew.getValue());
//    }

    public ChartDataViewModel(int CurrentChartStartDate, int HistoryChartStartDate,
                              int HistoryChartEndDate, TransactionDB transactionDB){
        mDB = transactionDB;
        Log.d("test_f_custom_dates  VM", "VM  current startingdate is "+ CurrentChartStartDate);
        mRevenueListEntry = mDB.transactionDAO().loadTransactionRevenuePeriod(CurrentChartStartDate);
        mExpenseListEntry = mDB.transactionDAO().loadTransactionExpensePeriod(CurrentChartStartDate);
        Log.d("test_f_custom_dates  VM", "VM  HISTORY startingdate & enddate is "+ HistoryChartStartDate+" ()() "+HistoryChartEndDate);

        mHistoryListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(
                HistoryChartStartDate, HistoryChartEndDate);
        mChartSettingChanged = new MutableLiveData<>();
        mChartSettingChanged.setValue(false);


        mAppexecutor = AppExecutors.getInstance();
//        mHistoryStartDateNew = new MutableLiveData<>();
//        mHistoryStartDateNew.setValue(HistoryChartStartDate);
//        mHistoryEndDateNew = new MutableLiveData<>();
//        mHistoryEndDateNew.setValue(HistoryChartEndDate);
//        mHistoryListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(
//                mHistoryStartDateNew.getValue(), mHistoryEndDateNew.getValue());

    }





    public LiveData<List<TransactionEntry>> getmRevenueListEntry(){
        return mRevenueListEntry;
    }

    public LiveData<List<TransactionEntry>> getmExpenseListEntry(){

        return mExpenseListEntry;
    }

    public LiveData<List<TransactionEntry>> getmHistoryListEntryPeriod(){
        return mHistoryListEntryPeriod;
    }


    public LiveData<Boolean> getmChartSettingChanged() {
        return mChartSettingChanged;
    }

    public void setmChartSettingChanged(Boolean input) {
        this.mChartSettingChanged.setValue(input);
    }

    public void setmRevenueListEntry(int CurrentChartStartDate){
        mRevenueListEntry = mDB.transactionDAO().loadTransactionRevenuePeriod(CurrentChartStartDate);
    }

    public void setmExpenseListEntry(int CurrentChartStartDate){

        mExpenseListEntry = mDB.transactionDAO().loadTransactionExpensePeriod(CurrentChartStartDate);
    }

    public void setmAllListEntryPeriod(int HistoryChartStartDate,int HistoryChartEndDate){

        mHistoryStartDate = HistoryChartStartDate;
        mHistoryEndDate = HistoryChartEndDate;

        mAppexecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mHistoryListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(
                        mHistoryStartDate, mHistoryEndDate);
            }
        });

        Log.d("test_new", " VM  all list updated ");
        Log.d("test_new", " VM  all list size "+ mHistoryListEntryPeriod.getValue().size());


    }


    public void updateAllListStartDate(int newStartDate){
        mHistoryStartDate = newStartDate;
        mHistoryListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(
                mHistoryStartDate, mHistoryEndDate);

    }
    public void updateAllListEndDate(int newEndDate){
        mHistoryEndDate = newEndDate;
        mHistoryListEntryPeriod = mDB.transactionDAO().loadTransactionInPeriodForChart(
                mHistoryStartDate, mHistoryEndDate);

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
                mHistoryListEntryPeriod= mDB.transactionDAO().loadTransactionInPeriodForChart(startingDay, endingDay);
            }

        });
    }*/
}
