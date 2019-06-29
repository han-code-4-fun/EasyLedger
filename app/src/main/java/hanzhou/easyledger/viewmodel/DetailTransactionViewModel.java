package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;
//
//public class DetailTransactionViewModel extends ViewModel {
//    // TODO: Implement the ViewModel
//
//    private static String TAG = DetailTransactionViewModel.class.getSimpleName();
//
//    private LiveData<List<TransactionEntry>> transactionEntryList;
//
//
//
//
//    public DetailTransactionViewModel(TransactionDB db, String inputTag) {
//
//        Log.d(TAG, "Actively retrieving the tasks from the DataBase by ledger");
//        //the overview fragment only diaplay these (new) transactions that needed to be tagged by user
//        transactionEntryList = db.transactionDAO().loadTransactionByLedger(inputTag);
//
//    }
//
//    public LiveData<List<TransactionEntry>> getTransactionByTag(){
//        return transactionEntryList;
//    }
//}
