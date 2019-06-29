package hanzhou.easyledger.viewmodel;

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


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new TransactionDBViewModel(transactionDB, inputTag);
    }
}
