package hanzhou.easyledger.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hanzhou.easyledger.data.TransactionDB;

public class ChartDataInitialViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int mCurrentChartStartDate;
    private int mHistoryChartStartDate;
    private int mHistoryChartEndDate;

    private TransactionDB transactionDB;

    public ChartDataInitialViewModelFactory(int mCurrentChartStartDate, int mHistoryChartStartDate,
                                            int mHistoryChartEndDate) {
        this.mCurrentChartStartDate = mCurrentChartStartDate;
        this.mHistoryChartStartDate = mHistoryChartStartDate;
        this.mHistoryChartEndDate = mHistoryChartEndDate;

    }

//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T) new ChartDataViewModel(mCurrentChartStartDate, mHistoryChartStartDate,
//                mHistoryChartEndDate);
//    }
}