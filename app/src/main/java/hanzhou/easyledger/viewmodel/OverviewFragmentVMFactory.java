package hanzhou.easyledger.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hanzhou.easyledger.data.TransactionDB;

public class OverviewFragmentVMFactory extends ViewModelProvider.NewInstanceFactory {
    /* during test, use 180days */
    private int time;

    private TransactionDB transactionDB;

    public OverviewFragmentVMFactory(int inputTime, TransactionDB mDB){
        time = inputTime;
        transactionDB = mDB;
    }




    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OverviewFragmentViewModel(time, transactionDB);
    }

}
