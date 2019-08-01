package hanzhou.easyledger_demo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import hanzhou.easyledger_demo.data.RepositoryDB;
import hanzhou.easyledger_demo.data.TransactionEntry;

public class OverviewFragmentViewModel extends AndroidViewModel {


    private MediatorLiveData<List<TransactionEntry>>
            listOfTransactionsOfCertainDaysChooseByUser = new MediatorLiveData<>();

    private MutableLiveData<Float> revenue;
    private MutableLiveData<Float> spend;

    private RepositoryDB mDBRepository;

    public OverviewFragmentViewModel(@NonNull Application application) {
        super(application);

        mDBRepository = RepositoryDB.getInstance();
        mDBRepository.initializeRepository(application);


        revenue = new MutableLiveData<>();
        revenue.setValue(0.0f);

        spend = new MutableLiveData<>();
        spend.setValue(0.0f);
    }


    public void updateTransactionOverviewPeriod(int time) {
        listOfTransactionsOfCertainDaysChooseByUser.
                addSource(mDBRepository.getPeriodOfEntriesForOverview(time), new Observer<List<TransactionEntry>>() {
                    @Override
                    public void onChanged(List<TransactionEntry> transactionEntryList) {
                        listOfTransactionsOfCertainDaysChooseByUser.setValue(transactionEntryList);
                    }
                });
    }


    public LiveData<List<TransactionEntry>> getlistOfTransactionsInTimeRange() {
        return listOfTransactionsOfCertainDaysChooseByUser;
    }

    public void setRevenue(Float input) {
        revenue.setValue(input);
    }

    public LiveData<Float> getRevenue() {
        return revenue;
    }

    public void setSpend(Float input) {
        spend.setValue(input);
    }

    public LiveData<Float> getSpend() {
        return spend;
    }


}
