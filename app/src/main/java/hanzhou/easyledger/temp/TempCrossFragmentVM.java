package hanzhou.easyledger.temp;

import android.util.Log;
import android.util.SparseBooleanArray;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hanzhou.easyledger.viewmodel.CrossFragmentCommunicationViewModel;

public class TempCrossFragmentVM  extends ViewModel {

    private static final String TAG =  CrossFragmentCommunicationViewModel.class.getSimpleName();

    private MutableLiveData<Boolean> isActionModeLiveData;
    private boolean isActionMode = false;


    private MutableLiveData<Integer> transactionSelectedLiveData;
    private int  transactionSelected;

    //used to record runtime selected items (from recyclerview)
    private SparseBooleanArray mSelectedItems;


//    private MutableLiveData


    public TempCrossFragmentVM() {
        super();
        isActionMode = false;
        isActionModeLiveData = new MutableLiveData<>();
        isActionModeLiveData.setValue(isActionMode);

        transactionSelected = 0;
        transactionSelectedLiveData = new MutableLiveData<>();
        transactionSelectedLiveData.setValue(transactionSelected);

        mSelectedItems = new SparseBooleanArray();

    }



    public void setActionMode(boolean state){
        isActionMode = state;
        isActionModeLiveData.setValue(isActionMode);
    }
    public MutableLiveData<Boolean> getIsActionModeLiveData() {
        return isActionModeLiveData;
    }



    public void setTransactionSelected(int number){
        transactionSelected = number;
        transactionSelectedLiveData.setValue(transactionSelected);
    }
    public MutableLiveData<Integer> getTransactionSelectedLiveData(){

        return transactionSelectedLiveData;
    }

    public void switchSelectedState(int position){
        //item has been selected/
        if (mSelectedItems.get(position)) {
            mSelectedItems.delete(position);
        } else {
            mSelectedItems.put(position, true);
        }
        Log.d(TAG, "switchSelectedState: position "+ position);
    }

    public SparseBooleanArray getSelectedItems(){
        return mSelectedItems;
    }

    public void selectAllItems(int transactionListSize){
        for(int i =0;i<transactionListSize;i++){
            if(!mSelectedItems.get(i)){
                mSelectedItems.put(i,true);
            }
        }
    }



}
