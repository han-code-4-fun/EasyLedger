package hanzhou.easyledger.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hanzhou.easyledger.data.TransactionDB;

public class ChartDataViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int mCurrentChartStartDate;
    private int mHistoryChartStartDate;
    private int mHistoryChartEndDate;

    private TransactionDB transactionDB;

    public ChartDataViewModelFactory(int mCurrentChartStartDate, int mHistoryChartStartDate,
                                     int mHistoryChartEndDate, TransactionDB transactionDB) {
        this.mCurrentChartStartDate = mCurrentChartStartDate;
        this.mHistoryChartStartDate = mHistoryChartStartDate;
        this.mHistoryChartEndDate = mHistoryChartEndDate;
        this.transactionDB = transactionDB;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChartDataViewModel(mCurrentChartStartDate, mHistoryChartStartDate,
                mHistoryChartEndDate,  transactionDB);
    }
}
