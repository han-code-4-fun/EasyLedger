package hanzhou.easyledger.Test;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.viewmodel.ChartDataViewModel;

public class ChartDataHistoryVMUpdateFactory extends ViewModelProvider.NewInstanceFactory{


    private int mHistoryChartStartDate;
    private int mHistoryChartEndDate;



    public ChartDataHistoryVMUpdateFactory(int mHistoryChartStartDate,
                                            int mHistoryChartEndDate) {
        this.mHistoryChartStartDate = mHistoryChartStartDate;
        this.mHistoryChartEndDate = mHistoryChartEndDate;

    }

//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T) new ChartDataViewModel(mHistoryChartStartDate,
//                mHistoryChartEndDate);
//    }

}
