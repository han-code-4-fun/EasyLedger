package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class TransactionDBViewModel extends AndroidViewModel {
    TransactionDB mDB = TransactionDB.getInstance(this.getApplication());

    private LiveData<List<TransactionEntry>> transactionsByLedger;
    private LiveData<List<TransactionEntry>> allTransactions;
    private LiveData<List<TransactionEntry>> untaggedTransactions;

    public TransactionDBViewModel(@NonNull Application application) {
        super(application);
        transactionsByLedger = mDB.transactionDAO().loadTransactionByLedger(Constant.UNTAGGED);
        //for further use
        allTransactions = mDB.transactionDAO().loadAllTransactions();
        //for further use
        untaggedTransactions = mDB.transactionDAO().loadUntaggedTransactions(Constant.UNTAGGED);

        mIsActionModeLiveData = new MutableLiveData<>();
        mIsActionModeLiveData.setValue(false);


        mIsAllSelected = new MutableLiveData<>();
        mIsAllSelected.setValue(false);

        mTransactionSelectedNumber = new MutableLiveData<>();
        mTransactionSelectedNumber.setValue(0);

//        mSizeOfTransactions = new MutableLiveData<>();
//        mSizeOfTransactions.setValue(0);

        mSelectAllTrigger = new MutableLiveData<>();
        mSelectAllTrigger.setValue(false);

        mDeselectAllTrigger = new MutableLiveData<>();
        mDeselectAllTrigger.setValue(false);

//        mTransferSelectedListTrigger = new MutableLiveData<>();
//        mTransferSelectedListTrigger.setValue(false);

        selectedBooleanArrayViewMode = new SparseBooleanArray();


//        tempArraySelectedItems = new SparseBooleanArray();
//        mSelectedItemsArray = new MutableLiveData<>();
//        synchronizeSelectedItemsArrayWithInViewModelClass(tempArraySelectedItems);


    }


    public void prepareViewModelForChangingFragment() {
        setActionModeState(false);
        setmIsAllSelected(false);
        selectedBooleanArrayViewMode.clear();
        setmTransactionSelectedNumber();
        setSelectAllTrigger(false);
        setDeselectAllTrigger(false);
    }

    private MutableLiveData<Boolean> mIsActionModeLiveData;

    private MutableLiveData<Boolean> mIsAllSelected;

    private MutableLiveData<Integer> mTransactionSelectedNumber;
//    private MutableLiveData<Integer> mSizeOfTransactions;

    private MutableLiveData<Boolean> mSelectAllTrigger;
    private MutableLiveData<Boolean> mDeselectAllTrigger;

//    private MutableLiveData<Boolean> mTransferSelectedListTrigger;

//    private List<TransactionEntry> selectedTransactions;

    public SparseBooleanArray selectedBooleanArrayViewMode;

    private String currentLedger;


    //for adapter used to record runtime selected items (from recyclerview)
//    private MutableLiveData<SparseBooleanArray> mSelectedItemsArray;
    /* a easier-edited version bind with mSelectedItemsArray
     that avoids NullPointerException */
//    private SparseBooleanArray tempArraySelectedItems;


//    public TransactionDBViewModel(TransactionDB mDB, String inputTag) {
//
//
//    }


    public LiveData<List<TransactionEntry>> getTransactionsByLedger() {
        return transactionsByLedger;
    }

    //for further use
    public LiveData<List<TransactionEntry>> getAllTransactions() {
        return allTransactions;
    }

    //for further use
    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {
        return untaggedTransactions;
    }


    public void setActionModeState(boolean state) {
        Log.d("testtest", "-------------> action mode incoming->> " + state);
        mIsActionModeLiveData.setValue(state);
        Log.d("testtest", "-------------> action mode now ->> " + mIsActionModeLiveData.getValue());
    }

    public LiveData<Boolean> getActionModeState() {
        Log.d("test_flow", "return actionmode state: "+mIsActionModeLiveData.getValue());
        return mIsActionModeLiveData;
    }

    public void setmTransactionSelectedNumber() {
        mTransactionSelectedNumber.setValue(selectedBooleanArrayViewMode.size());
    }

    public LiveData<Integer> getTransactionSelectedNumberLiveData() {
        return mTransactionSelectedNumber;
    }

//    public void setmSizeOfTransactions(int number){
//        mSizeOfTransactions.setValue(number);
//    }
//
//    public MutableLiveData<Integer> getmSizeOfTransactions(){
//        return mSizeOfTransactions;
//    }

    public void setmIsAllSelected(boolean input) {

        mIsAllSelected.setValue(input);
        Log.d("testtest", "setmIsAllSelected:  ++++++ " + mIsAllSelected.getValue());
    }

    public MutableLiveData<Boolean> getmIsAllSelected() {
        return mIsAllSelected;
    }

    public void setSelectAllTrigger(boolean input) {

        mSelectAllTrigger.setValue(input);
        Log.d("testtest", "SSSSSSSelectAllTrigger: now is --> " + mSelectAllTrigger.getValue());
    }

    public MutableLiveData<Boolean> getmSelectAllTrigger() {
        return mSelectAllTrigger;
    }

    public void setDeselectAllTrigger(boolean input) {
        mDeselectAllTrigger.setValue(input);
        Log.d("testtest", "DDDDDDDDeselectAllTrigger: now is --> " + mDeselectAllTrigger.getValue());
    }

    public MutableLiveData<Boolean> getmDeselectAllTrigger() {
        return mDeselectAllTrigger;
    }


    //todo, optimize later
    public List<TransactionEntry> getSelectedTransactions() {
        List<TransactionEntry> entries;
        if (currentLedger.equals(Constant.CALLFROMOVERVIEW)) {//using getUntaggedTransactions
            entries = untaggedTransactions.getValue();
        } else {
            //using getAllTransactions
            entries = allTransactions.getValue();
        }

        List<Integer> selectedNumbers = new ArrayList<>(selectedBooleanArrayViewMode.size());
        for (int i = 0; i < selectedBooleanArrayViewMode.size(); ++i) {
            selectedNumbers.add(selectedBooleanArrayViewMode.keyAt(i));
        }

        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.size());

        for (Integer i : selectedNumbers) {
            output.add(entries.get(i));
        }

        return output;
    }

//    public void setmTransferSelectedListTrigger(boolean input){
//        mTransferSelectedListTrigger.setValue(input);
//    }
//
//    public MutableLiveData<Boolean> getmTransferSelectedListTrigger(){
//        return mTransferSelectedListTrigger;
//    }

    public String getCurrentLedger() {
        return currentLedger;
    }

    public void setCurrentLedger(String currentLedger) {
        this.currentLedger = currentLedger;
    }

/*    public void setSelectedBooleanArrayViewMode(SparseBooleanArray inputArray){
        selectedBooleanArrayViewMode = inputArray;
    }

    public SparseBooleanArray getSelectedBooleanArrayViewMode(){
        return selectedBooleanArrayViewMode;
    }*/

//    public void updateSelectedItemsArray(int position){
//
//        if (tempArraySelectedItems.get(position)) {
//            tempArraySelectedItems.delete(position);
//        } else {
//            tempArraySelectedItems.put(position, true);
//        }
//        synchronizeSelectedItemsArrayWithInViewModelClass(tempArraySelectedItems);
//    }

//    public void clearSelectedItemsArray(){
//
//        for(int i = 0; i < tempArraySelectedItems.size(); i++) {
//            int key = tempArraySelectedItems.keyAt(i);
//            tempArraySelectedItems.put(key, false);
//        }
//        //pass SparseBooleanArray to recyclerView adapter before 'clear' itself
//        setSelectedItemsArray(tempArraySelectedItems);
//        tempArraySelectedItems.clear();
//        setTransactionSelectedNumber(tempArraySelectedItems);
//    }

//    public void selectAllItems(int size){
//        for(int i =0;i<size;i++){
//            //todo, may have a problem, need to S...Array x = m.getValue, change it, and m.setValue(x)
//            if(!tempArraySelectedItems.get(i)){
//                tempArraySelectedItems.put(i,true);
//            }
//        }
//
//        synchronizeSelectedItemsArrayWithInViewModelClass(tempArraySelectedItems);
//    }

//    public List<TransactionEntry> listOfSelectedTransactions(){
//        List<TransactionEntry> output = new ArrayList<>();
//        for(int i = 0; i < tempArraySelectedItems.size(); i++) {
//            int key = tempArraySelectedItems.keyAt(i);
//            output.add(transactionsByLedger.getValue().get(key));
//        }
//
//        return output;
//    }


//    public void synchronizeSelectedItemsArrayWithInViewModelClass(SparseBooleanArray tempArraySelectedItems) {
//        setSelectedItemsArray(tempArraySelectedItems);
//        setTransactionSelectedNumber(tempArraySelectedItems);
//    }
//
//    private void setSelectedItemsArray(SparseBooleanArray inputArray){
//        mSelectedItemsArray.setValue(inputArray);
//    }

//    private void setTransactionSelectedNumber(SparseBooleanArray inputArray){
//        int num= inputArray.size();
//        mTransactionSelectedNumber.setValue(num);
//    }
//
//    public LiveData<SparseBooleanArray> getSelectedItemsArray(){
//        return mSelectedItemsArray;
//    }


}
