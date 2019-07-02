package hanzhou.easyledger.temp;

import android.util.Log;
import android.util.SparseBooleanArray;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.viewmodel.CrossFragmentCommunicationViewModel;

public class Temp2CrossFragmentVM {
    private static final String TAG =  CrossFragmentCommunicationViewModel.class.getSimpleName();

    private MutableLiveData<Boolean> mIsActionModeLiveData;


    private MutableLiveData<Integer> mTransactionSelectedLiveData;

    //used to record runtime selected items (from recyclerview)
    private MutableLiveData<SparseBooleanArray> mSelectedItems;

    private MutableLiveData<List<TransactionEntry>> mTransactionEntriesFromDB;



    public Temp2CrossFragmentVM() {
        super();
        mIsActionModeLiveData = new MutableLiveData<>();
        mIsActionModeLiveData.setValue(false);

        mTransactionSelectedLiveData = new MutableLiveData<>();
        mTransactionSelectedLiveData.setValue(0);

        mSelectedItems = new MutableLiveData<>();
        mSelectedItems.setValue(new SparseBooleanArray());

    }



    public void setActionMode(boolean state){
        mIsActionModeLiveData.setValue(state);
    }
    public MutableLiveData<Boolean> getIsActionModeLiveData() {
        return mIsActionModeLiveData;
    }



    public void setTransactionSelectedNumber(int number){
        mTransactionSelectedLiveData.setValue(number);
    }
    public MutableLiveData<Integer> getTransactionSelectedNumberLiveData(){

        return mTransactionSelectedLiveData;
    }


    public void switchSelectedState(int position){
        //item has been selected/
//        if (mSelectedItems.get(position)) {
//            mSelectedItems.delete(position);
//        } else {
//            mSelectedItems.put(position, true);
//        }


        if (mSelectedItems.getValue().get(position)) {
            //todo, may have a problem
            mSelectedItems.getValue().delete(position);
        } else {
            mSelectedItems.getValue().put(position, true);
        }
        Log.d(TAG, "switchSelectedState: position "+ position);
    }

    public MutableLiveData<SparseBooleanArray> getSelectedItems(){
        return mSelectedItems;
    }

    //todo, make it without int input
//    public void selectAllItems(int transactionListSize){
//        for(int i =0;i<transactionListSize;i++){
//            //todo, may have a problem, need to S...Array x = m.getValue, change it, and m.setValue(x)
//            if(!mSelectedItems.getValue().get(i)){
//                mSelectedItems.getValue().put(i,true);
//            }
//        }
//    }
    public void selectAllItems(){

        for(int i =0;i<mTransactionEntriesFromDB.getValue().size();i++){
            //todo, may have a problem, need to S...Array x = m.getValue, change it, and m.setValue(x)
            if(!mSelectedItems.getValue().get(i)){
                mSelectedItems.getValue().put(i,true);
            }
        }
    }

    public void clearSelectedItems(){
        SparseBooleanArray temp = mSelectedItems.getValue();
        temp.clear();
        mSelectedItems.setValue(temp);
    }

    public List<Integer> getSelectedItemAsListOfInteger(){
        List<Integer> items = new ArrayList<>(mSelectedItems.getValue().size());
        for (int i = 0; i < mSelectedItems.getValue().size(); ++i) {
            items.add(mSelectedItems.getValue().keyAt(i));
        }

        return items;


    }

    public List<TransactionEntry> getSelectedItemAsEntries(){
        List<Integer> temp = getSelectedItemAsListOfInteger();
        List<TransactionEntry> output = new ArrayList<>(temp.size());

        for(Integer i : temp){
            output.add(mTransactionEntriesFromDB.getValue().get(i));
        }

        return output;
    }


    public void setTransactionEntriesFromDB(List<TransactionEntry> inputEntries){
        mTransactionEntriesFromDB.setValue(inputEntries);
    }


    public MutableLiveData<List<TransactionEntry>> getTransactionEntriesFromDB(){
        return mTransactionEntriesFromDB;
    }


    public boolean isAllItemSelected(){

        return mSelectedItems.getValue().size() ==
                mTransactionEntriesFromDB.getValue().size();
    }

}
