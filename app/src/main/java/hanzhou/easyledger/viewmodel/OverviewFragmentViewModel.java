package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hanzhou.easyledger.data.Repository;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class OverviewFragmentViewModel extends AndroidViewModel {


    private LiveData<List<TransactionEntry>> untaggedTransactions;
//    private LiveData<List<TransactionEntry>> listOfTransactionsOfCertainDaysChooseByUser;

//    private MediatorLiveData<List<TransactionEntry>> untaggedTransactions = new MediatorLiveData<>();
    private MediatorLiveData<List<TransactionEntry>>
            listOfTransactionsOfCertainDaysChooseByUser = new MediatorLiveData<>();

    private MutableLiveData<Float> revenue;
    private MutableLiveData<Float> spend;

//    private TransactionDB mDB;

    private Repository mRepository;

    public OverviewFragmentViewModel(@NonNull Application application) {
        super(application);
//        mDB = TransactionDB.getInstance(application);

        mRepository = new Repository(application);

        untaggedTransactions = mRepository.getUntaggedTransaction();

//        untaggedTransactions = mDB.transactionDAO().loadUntaggedTransactions(Constant.UNTAGGED);

//        listOfTransactionsOfCertainDaysChooseByUser =
//                mDB.transactionDAO().loadTransactionByTimeUserDefined(inputTime);
        revenue =new MutableLiveData<>();
        revenue.setValue(0.0f);

        spend = new MutableLiveData<>();
        spend.setValue(0.0f);
    }


//  public OverviewFragmentViewModel(int inputTime, TransactionDB mDB){
//      untaggedTransactions = mDB.transactionDAO().loadUntaggedTransactions(Constant.UNTAGGED);
//
//      listOfTransactionsOfCertainDaysChooseByUser =
//              mDB.transactionDAO().loadTransactionByTimeUserDefined(inputTime);
//      revenue =new MutableLiveData<>();
//      revenue.setValue(0.0f);
//
//      spend = new MutableLiveData<>();
//      spend.setValue(0.0f);
//  }


    public void updateTransactionOverviewPeriod(int time){
        listOfTransactionsOfCertainDaysChooseByUser.
                addSource(mRepository.getPeriodOfEntriesForOverview(time), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                listOfTransactionsOfCertainDaysChooseByUser.setValue(transactionEntryList);
            }
        });
    }



    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {
        return untaggedTransactions;
    }

    public LiveData<List<TransactionEntry>> getlistOfTransactionsInTimeRange(){
        return listOfTransactionsOfCertainDaysChooseByUser;
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



    //todo, convert back Adpater class from singleton to normall
    /*

      if (currentLedger.equals(Constant.FRAG_CALL_FROM_OVERVIEW)) {
            entries = untaggedTransactions.getValue();
        } else {

     */
/*
    public List<TransactionEntry> getSelectedTransactions() {
        List<hanzhou.easyledger.data.TransactionEntry> entries;

        entries = untaggedTransactions.getValue();

        int[] selectedNumbers = new int[selectedBooleanArrayViewMode.size()];
        //todo, may have issue from ++i to i++
        for (int i = 0; i < selectedBooleanArrayViewMode.size(); i++) {
            selectedNumbers[i] = selectedBooleanArrayViewMode.keyAt(i);
        }

        List<hanzhou.easyledger.data.TransactionEntry> output = new ArrayList<>(selectedNumbers.length);

        for (Integer i : selectedNumbers) {
            output.add(entries.get(i));
        }

        return output;
    }*/


}
