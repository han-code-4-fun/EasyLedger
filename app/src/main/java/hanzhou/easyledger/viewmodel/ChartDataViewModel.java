package hanzhou.easyledger.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;

public class ChartDataViewModel extends ViewModel {

    private LiveData<List<TransactionEntry>> listOfTransactions7Days;

    private MutableLiveData<Double> revenue;
    private MutableLiveData<Double> spend;

    /* during test, input 180 days time, not just 7days */
    ChartDataViewModel(int time, TransactionDB mDb){
        listOfTransactions7Days = mDb.transactionDAO().loadTransactionByTime7days(time);
        revenue =new MutableLiveData<>();
        revenue.setValue(0.0);

        spend = new MutableLiveData<>();
        spend.setValue(0.0);

    }


    public LiveData<List<TransactionEntry>> getlistOfTransactions7Days(){
        return listOfTransactions7Days;
    }

    public void setRevenue(double input){
        revenue.setValue(input);
    }

    public LiveData<Double> getRevenue(){
        return revenue;
    }

    public void setSpend(double input){
        spend.setValue(input);
    }

    public LiveData<Double> getSpend(){
        return spend;
    }

}
