package hanzhou.easyledger.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;

public class ChartDataViewModel extends ViewModel {

    private LiveData<List<TransactionEntry>> listOfTransactions7Days;

    private MutableLiveData<Float> revenue;
    private MutableLiveData<Float> spend;

    /* during test, input 180 days time, not just 7days */
    ChartDataViewModel(int time, TransactionDB mDb){
        listOfTransactions7Days = mDb.transactionDAO().loadTransactionByTime7days(time);
        revenue =new MutableLiveData<>();
        revenue.setValue(0.0f);

        spend = new MutableLiveData<>();
        spend.setValue(0.0f);

    }


    public LiveData<List<TransactionEntry>> getlistOfTransactions7Days(){
        return listOfTransactions7Days;
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
