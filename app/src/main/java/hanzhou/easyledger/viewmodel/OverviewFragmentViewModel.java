package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class OverviewFragmentViewModel extends AndroidViewModel{

    private LiveData<List<TransactionEntry>> untaggedTransactions;


    public OverviewFragmentViewModel(@NonNull Application application) {
        super(application);
        TransactionDB mDB = TransactionDB.getInstance(this.getApplication());
        untaggedTransactions = mDB.transactionDAO().loadUntaggedTransactions(Constant.UNTAGGED);
    }

    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {
        return untaggedTransactions;
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
