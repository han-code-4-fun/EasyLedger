package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class OverviewFragmentViewModel extends ViewModel {


    private LiveData<List<TransactionEntry>> untaggedTransactions;
    private LiveData<List<TransactionEntry>> listOfTransactionsOfCertainDaysChooseByUser;

    private MutableLiveData<Float> revenue;
    private MutableLiveData<Float> spend;



  public OverviewFragmentViewModel(int inputTime, TransactionDB mDB){
      untaggedTransactions = mDB.transactionDAO().loadUntaggedTransactions(Constant.UNTAGGED);

      listOfTransactionsOfCertainDaysChooseByUser =
              mDB.transactionDAO().loadTransactionByTimeUserDefined(inputTime);
      revenue =new MutableLiveData<>();
      revenue.setValue(0.0f);

      spend = new MutableLiveData<>();
      spend.setValue(0.0f);
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

      if (currentLedger.equals(Constant.CALLFROMOVERVIEW)) {
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
