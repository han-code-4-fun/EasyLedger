package hanzhou.easyledger.viewmodel;

import android.util.Log;
import android.util.SparseBooleanArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.ui.OverviewFragment;
import hanzhou.easyledger.utility.Constant;

public class TransactionDBViewModel extends ViewModel {

    private LiveData<List<TransactionEntry>> transactionsByLedger;
    private LiveData<List<TransactionEntry>> allTransactions;
    private LiveData<List<TransactionEntry>> untaggedTransactions;


    private MutableLiveData<Boolean> mIsActionModeLiveData;

    private MutableLiveData<Boolean> mIsAllSelected;

    private MutableLiveData<Integer> mTransactionSelectedNumber;
    private MutableLiveData<Integer> mSizeOfTransactions;


    //for adapter used to record runtime selected items (from recyclerview)
    private MutableLiveData<SparseBooleanArray> mSelectedItemsArray;
    /* a easier-edited version bind with mSelectedItemsArray
     that avoids NullPointerException */
    private SparseBooleanArray tempArraySelectedItems;




    public TransactionDBViewModel(TransactionDB mDB, String inputTag) {

        transactionsByLedger = mDB.transactionDAO().loadTransactionByLedger(inputTag);
        //for further use
        allTransactions = mDB.transactionDAO().loadAllTransactions();
        //for further use
        untaggedTransactions = mDB.transactionDAO().loadTransactionByLedger(Constant.untagged);

        mIsActionModeLiveData = new MutableLiveData<>();
        mIsActionModeLiveData.setValue(false);


        mIsAllSelected = new MutableLiveData<>();
        mIsAllSelected.setValue(false);

        mTransactionSelectedNumber = new MutableLiveData<>();
        mTransactionSelectedNumber.setValue(0);

        mSizeOfTransactions = new MutableLiveData<>();
        mSizeOfTransactions.setValue(0);

        tempArraySelectedItems = new SparseBooleanArray();
        mSelectedItemsArray = new MutableLiveData<>();
        synchronizeSelectedItemsArrayWithInViewModelClass(tempArraySelectedItems);


    }


    public LiveData<List<TransactionEntry>> getTransactionsByLedger(){return transactionsByLedger;}

    //for further use
    public LiveData<List<TransactionEntry>> getAllTransactions(){return allTransactions;}

    //for further use
    public LiveData<List<TransactionEntry>> getUntaggedTransactions() {return untaggedTransactions;}


    public void setActionModeState(boolean state){mIsActionModeLiveData.setValue(state);}

    public LiveData<Boolean> getActionModeState() {return mIsActionModeLiveData;}

    public LiveData<Integer> getTransactionSelectedNumberLiveData(){
        return mTransactionSelectedNumber;
    }

    public void setmSizeOfTransactions(int number){
        mSizeOfTransactions.setValue(number);
    }

    public MutableLiveData<Integer> getmSizeOfTransactions(){
        return mSizeOfTransactions;
    }

    public void updateSelectedItemsArray(int position){

        if (tempArraySelectedItems.get(position)) {
            tempArraySelectedItems.delete(position);
        } else {
            tempArraySelectedItems.put(position, true);
        }
        synchronizeSelectedItemsArrayWithInViewModelClass(tempArraySelectedItems);
    }

    public void clearSelectedItemsArray(){

        for(int i = 0; i < tempArraySelectedItems.size(); i++) {
            int key = tempArraySelectedItems.keyAt(i);
            tempArraySelectedItems.put(key, false);
        }
        //pass SparseBooleanArray to recyclerView adapter before 'clear' itself
        setSelectedItemsArray(tempArraySelectedItems);
        tempArraySelectedItems.clear();
        setTransactionSelectedNumber(tempArraySelectedItems);
    }

    public void selectAllItems(int size){
        for(int i =0;i<size;i++){
            //todo, may have a problem, need to S...Array x = m.getValue, change it, and m.setValue(x)
            if(!tempArraySelectedItems.get(i)){
                tempArraySelectedItems.put(i,true);
            }
        }

        synchronizeSelectedItemsArrayWithInViewModelClass(tempArraySelectedItems);
    }

    public List<TransactionEntry> listOfSelectedTransactions(){
        List<TransactionEntry> output = new ArrayList<>();
        for(int i = 0; i < tempArraySelectedItems.size(); i++) {
            int key = tempArraySelectedItems.keyAt(i);
            output.add(transactionsByLedger.getValue().get(key));
        }

        return output;
    }



    public void synchronizeSelectedItemsArrayWithInViewModelClass(SparseBooleanArray tempArraySelectedItems) {
        setSelectedItemsArray(tempArraySelectedItems);
        setTransactionSelectedNumber(tempArraySelectedItems);
    }

    private void setSelectedItemsArray(SparseBooleanArray inputArray){
        mSelectedItemsArray.setValue(inputArray);
    }

    private void setTransactionSelectedNumber(SparseBooleanArray inputArray){
        int num= inputArray.size();
        mTransactionSelectedNumber.setValue(num);
    }

    public LiveData<SparseBooleanArray> getSelectedItemsArray(){
        return mSelectedItemsArray;
    }


}
