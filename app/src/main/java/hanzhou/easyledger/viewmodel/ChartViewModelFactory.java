package hanzhou.easyledger.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hanzhou.easyledger.data.TransactionDB;

public class ChartViewModelFactory extends  ViewModelProvider.NewInstanceFactory{
    /* during test, use 180days */
    private int time;

    private TransactionDB transactionDB;

    public ChartViewModelFactory(int inputTime, TransactionDB mDB){
        time = inputTime;
        Log.d("test_flow", "ChartViewModelFactory: the time is "+ time);
        transactionDB = mDB;
    }




    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChartDataViewModel(time, transactionDB);
    }
}
