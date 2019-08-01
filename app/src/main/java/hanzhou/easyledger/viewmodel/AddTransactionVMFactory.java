package hanzhou.easyledger.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hanzhou.easyledger.data.TransactionDB;

public class AddTransactionVMFactory extends ViewModelProvider.NewInstanceFactory  {
    private final TransactionDB mDB;
    private final int mTransactionID;

    public AddTransactionVMFactory(TransactionDB mDB, int mTransactionID) {
        this.mDB = mDB;
        this.mTransactionID = mTransactionID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTransactionViewModel(mDB, mTransactionID);
    }
}
