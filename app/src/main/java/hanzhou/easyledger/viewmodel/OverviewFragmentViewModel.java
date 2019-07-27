package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import hanzhou.easyledger.data.RepositoryDB;
import hanzhou.easyledger.data.TransactionEntry;

public class OverviewFragmentViewModel extends AndroidViewModel {


    private LiveData<List<TransactionEntry>> untaggedTransactions;
//    private LiveData<List<TransactionEntry>> listOfTransactionsOfCertainDaysChooseByUser;

//    private MediatorLiveData<List<TransactionEntry>> untaggedTransactions = new MediatorLiveData<>();
    private MediatorLiveData<List<TransactionEntry>>
            listOfTransactionsOfCertainDaysChooseByUser = new MediatorLiveData<>();

    private MutableLiveData<Float> revenue;
    private MutableLiveData<Float> spend;

//    private TransactionDB mDB;

    private RepositoryDB mDBRepository;

    public OverviewFragmentViewModel(@NonNull Application application) {
        super(application);


        mDBRepository = RepositoryDB.getInstance();
        mDBRepository.initializeRepository(application);

        untaggedTransactions = mDBRepository.getUntaggedTransaction();


        revenue =new MutableLiveData<>();
        revenue.setValue(0.0f);

        spend = new MutableLiveData<>();
        spend.setValue(0.0f);
    }




    public void updateTransactionOverviewPeriod(int time){
        listOfTransactionsOfCertainDaysChooseByUser.
                addSource(mDBRepository.getPeriodOfEntriesForOverview(time), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                listOfTransactionsOfCertainDaysChooseByUser.setValue(transactionEntryList);
            }
        });
    }


    public LiveData<List<TransactionEntry>> getlistOfTransactionsInTimeRange(){
        return listOfTransactionsOfCertainDaysChooseByUser;
    }


    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {
        return untaggedTransactions;
    }


    public void setRevenue(Float input){
        revenue.setValue(input);
    }

    public LiveData<Float> getRevenue(){
        return revenue;
    }

    public void setSpend(Float input){
        spend.setValue(input);
    }

    public LiveData<Float> getSpend(){
        return spend;
    }




}
