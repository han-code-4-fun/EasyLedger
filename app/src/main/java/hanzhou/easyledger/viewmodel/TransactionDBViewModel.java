package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class TransactionDBViewModel extends AndroidViewModel {

    //LiveData that comes from ROOM
    private LiveData<List<TransactionEntry>> transactionsByLedger;
    private LiveData<List<TransactionEntry>> allTransactions;
    private LiveData<List<TransactionEntry>> untaggedTransactions;

    //store the state of toolbar
    private MutableLiveData<Boolean> mIsActionMode;

    private MutableLiveData<Boolean> mIsAllSelected;

    //record the number of entries that is seleced by user
    private MutableLiveData<Integer> mTransactionSelectedNumber;

    //two triggers that from MainActivity to RecyclerView to do select/deselect all entries
    private MutableLiveData<Boolean> mSelectAllTrigger;
    private MutableLiveData<Boolean> mDeselectAllTrigger;

    //keep track of user selection
    private SparseBooleanArray selectedBooleanArrayViewMode;

    private String currentLedger;


    public TransactionDBViewModel(@NonNull Application application) {
        super(application);
        TransactionDB mDB = TransactionDB.getInstance(this.getApplication());

        //for future development, e.g. user creates multiple ledger
        transactionsByLedger = mDB.transactionDAO().loadTransactionByLedger(Constant.UNTAGGED);

        allTransactions = mDB.transactionDAO().loadAllTransactions();
        untaggedTransactions = mDB.transactionDAO().loadUntaggedTransactions(Constant.UNTAGGED);

        mIsActionMode = new MutableLiveData<>();
        mIsActionMode.setValue(false);


        mIsAllSelected = new MutableLiveData<>();
        mIsAllSelected.setValue(false);

        mTransactionSelectedNumber = new MutableLiveData<>();
        mTransactionSelectedNumber.setValue(0);


        mSelectAllTrigger = new MutableLiveData<>();
        mSelectAllTrigger.setValue(false);

        mDeselectAllTrigger = new MutableLiveData<>();
        mDeselectAllTrigger.setValue(false);

        selectedBooleanArrayViewMode = new SparseBooleanArray();

        currentLedger= Constant.CALLFROMOVERVIEW;
    }


    public LiveData<List<TransactionEntry>> getTransactionsByLedger() {
        return transactionsByLedger;
    }
    public LiveData<List<TransactionEntry>> getAllTransactions() {
        return allTransactions;
    }

    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {
        return untaggedTransactions;
    }


    public void setActionModeState(boolean state) {
        mIsActionMode.setValue(state);
    }

    public LiveData<Boolean> getActionModeState() {
        return mIsActionMode;
    }

    public void setmTransactionSelectedNumber() {
        mTransactionSelectedNumber.setValue(selectedBooleanArrayViewMode.size());
    }

    public LiveData<Integer> getTransactionSelectedNumberLiveData() {
        return mTransactionSelectedNumber;
    }

    public void setmIsAllSelected(boolean input) {

        mIsAllSelected.setValue(input);
    }

    public MutableLiveData<Boolean> getmIsAllSelected() {
        return mIsAllSelected;
    }

    public void setSelectAllTrigger(boolean input) {

        mSelectAllTrigger.setValue(input);
    }

    public MutableLiveData<Boolean> getmSelectAllTrigger() {
        return mSelectAllTrigger;
    }

    public void setDeselectAllTrigger(boolean input) {
        mDeselectAllTrigger.setValue(input);
    }

    public MutableLiveData<Boolean> getmDeselectAllTrigger() {
        return mDeselectAllTrigger;
    }


    //todo, optimize later
    public List<TransactionEntry> getSelectedTransactions() {
        List<TransactionEntry> entries;
        if (currentLedger.equals(Constant.CALLFROMOVERVIEW)) {
            entries = untaggedTransactions.getValue();
        } else {

            entries = allTransactions.getValue();
        }

        int[] selectedNumbers = new int[selectedBooleanArrayViewMode.size()];
        //todo, may have issue from ++i to i++
        for (int i = 0; i < selectedBooleanArrayViewMode.size(); i++) {
            selectedNumbers[i]=selectedBooleanArrayViewMode.keyAt(i);
        }

        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.length);

        for (Integer i : selectedNumbers) {
            output.add(entries.get(i));
        }

        return output;
    }

    public void emptySelectedItems(){
        selectedBooleanArrayViewMode.clear();
    }

    public boolean getAValueFromSelectedItems(int position){
        return selectedBooleanArrayViewMode.get(position);
    }

    public void deleteAValueFromSelectedItems(int position){
        selectedBooleanArrayViewMode.delete(position);
    }

    public void putAValueIntoSelectedItems(int position, boolean value){
        selectedBooleanArrayViewMode.put(position, value);
    }

    public int getNumberOfSelectedItems(){
        return selectedBooleanArrayViewMode.size();
    }


    public String getCurrentLedger() {
        return currentLedger;
    }

    public void setCurrentLedger(String currentLedger) {
        this.currentLedger = currentLedger;
    }


}
