package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import hanzhou.easyledger.data.RepositoryDB;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionEntry;

public class ChartDataViewModel extends AndroidViewModel {

    private AppExecutors mAppexecutor;

    private final RepositoryDB mDBRepository;


    private final MediatorLiveData<List<TransactionEntry>> mHistoryListEntries= new MediatorLiveData<>();
    private final MediatorLiveData<List<TransactionEntry>> mRevenueListEntries= new MediatorLiveData<>();
    private final MediatorLiveData<List<TransactionEntry>> mExpenseListEntries= new MediatorLiveData<>();


    public ChartDataViewModel(@NonNull Application application) {
        super(application);
        mDBRepository = RepositoryDB.getInstance();
    }

    public void initializeDatesForVM(int historyStartDate,int historyEndDate, int currentStartDate){
        updatesHistoryDatesForVM(historyStartDate, historyEndDate);
        updatesCurrentDatesforVM(currentStartDate);
    }

    public void updatesHistoryDatesForVM(int historyStartDate, int historyEndDate){
        mHistoryListEntries.addSource(mDBRepository.getPeriodOfEntries(historyStartDate, historyEndDate), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                mHistoryListEntries.setValue(transactionEntryList);
            }
        });
    }

    public void updatesCurrentDatesforVM(int currentChartStartDate){
        mRevenueListEntries.addSource(mDBRepository.getCurrentRevenueOfEntries(currentChartStartDate), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                mRevenueListEntries.setValue(transactionEntryList);
            }
        });
        mExpenseListEntries.addSource(mDBRepository.getCurrentExpenseOfEntries(currentChartStartDate), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                mExpenseListEntries.setValue(transactionEntryList);
            }
        });
    }




    public LiveData<List<TransactionEntry>> getmHistoryListEntryPeriod(){

        return mHistoryListEntries;
    }

    public LiveData<List<TransactionEntry>> getmRevenueListEntry(){
        return mRevenueListEntries;
    }

    public LiveData<List<TransactionEntry>> getmExpenseListEntry(){

        return mExpenseListEntries;
    }



}
