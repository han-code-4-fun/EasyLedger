package hanzhou.easyledger.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import hanzhou.easyledger.data.TransactionDB;

public class TransactionDBVMFactory extends ViewModelProvider.NewInstanceFactory {
    private final TransactionDB transactionDB;
    private final String inputTag;

    public TransactionDBVMFactory(TransactionDB transactionDB, String inputTag) {
        this.transactionDB = transactionDB;
        this.inputTag = inputTag;
    }

//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//
//        return (T) new TransactionDBViewModel(transactionDB, inputTag);
//    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d("flow", "start to  create: from TransactionDBVMFactory");
        return (T) new TransactionDBViewModel(transactionDB, inputTag);
    }
}
