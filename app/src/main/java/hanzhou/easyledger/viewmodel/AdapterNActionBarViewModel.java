package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;

public class AdapterNActionBarViewModel extends AndroidViewModel {

    /*store the state of toolbar*/
    private MutableLiveData<Boolean> mIsActionMode;

    /*store the state of current screen's all selection*/
    private MutableLiveData<Boolean> mIsAllSelected;

    /*record the number of entries that is seleced by user*/
    private MutableLiveData<Integer> mTransactionSelectedNumber;

    /*two triggers that from MainActivity to tell adapter to select/deselect all entries*/
    private MutableLiveData<Boolean> mSelectAllTrigger;
    private MutableLiveData<Boolean> mDeselectAllTrigger;


    /*trigger that used to communicate between fragments*/
    private MutableLiveData<Boolean> mCategorizeItemsToOthersTrigger;


    /*keep track of user selection*/
    private SparseBooleanArray selectedBooleanArrayViewMode;

    /*hold id to be edit in AddNEditTransaction Fragment*/
    private MutableLiveData<Integer> mClickedEntryID;

    /*show current selected category in category sectino of AddNEditTransaction fragment*/
    private MutableLiveData<String> mSelectedCategory;


    public AdapterNActionBarViewModel(@NonNull Application application) {
        super(application);

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


        mClickedEntryID = new MutableLiveData<>();
        mClickedEntryID.setValue(null);


        mSelectedCategory = new MutableLiveData<>();
        mSelectedCategory.setValue("");


        mCategorizeItemsToOthersTrigger = new MutableLiveData<>();
        mCategorizeItemsToOthersTrigger.setValue(false);
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

        if (input) {
            emptySelectedItems();
            setmTransactionSelectedNumber();
            setmIsAllSelected(false);
        }

        mDeselectAllTrigger.setValue(input);
    }

    public MutableLiveData<Boolean> getmDeselectAllTrigger() {
        return mDeselectAllTrigger;
    }


    /*
        get selected items from inner variable SparseBooleanArray and
        mapping them to inputEntries (all entries of current fragment)
    */
    public List<TransactionEntry> getSelectedTransactions(List<TransactionEntry> inputList) {


        int[] selectedNumbers = getSelectedBooleanArrayIntoArrayOfPositionInCurrentListEntires();

        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.length);

        for (Integer i : selectedNumbers) {
            output.add(inputList.get(i));
        }

        return output;
    }

    private int[] getSelectedBooleanArrayIntoArrayOfPositionInCurrentListEntires() {

        int[] selectedNumbers = new int[selectedBooleanArrayViewMode.size()];

        for (int i = 0; i < selectedBooleanArrayViewMode.size(); i++) {
            selectedNumbers[i] = selectedBooleanArrayViewMode.keyAt(i);
        }
        return selectedNumbers;
    }

     /*
        get selected items from inner variable SparseBooleanArray and
        mapping them to all UntaggedTransactions
    */

    public List<TransactionEntry> categorizeSelectedItemsToOthers(List<TransactionEntry> inputList) {

        int[] selectedNumbers = getSelectedBooleanArrayIntoArrayOfPositionInCurrentListEntires();

        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.length);

        for (Integer i : selectedNumbers) {
            TransactionEntry entry = inputList.get(i);
            entry.setCategory(Constant.CATEGORY_OTHERS);
            output.add(entry);
        }
        return output;
    }


    public void emptySelectedItems() {
        selectedBooleanArrayViewMode.clear();
    }

    public boolean getAValueFromSelectedItems(int position) {
        return selectedBooleanArrayViewMode.get(position);
    }

    public void deleteAValueFromSelectedItems(int position) {
        selectedBooleanArrayViewMode.delete(position);
    }

    public void putAValueIntoSelectedItems(int position, boolean value) {
        selectedBooleanArrayViewMode.put(position, value);
    }

    public int getNumberOfSelectedItems() {
        return selectedBooleanArrayViewMode.size();
    }

    public int getFirstSelectedItems() {
        return selectedBooleanArrayViewMode.keyAt(0);
    }


    public MutableLiveData<Integer> getmClickedEntryID() {
        return mClickedEntryID;
    }

    public void setmClickedEntryID(Integer inputID) {
        this.mClickedEntryID.setValue(inputID);
    }

    public void setmClickedEntryIDToNull() {
        mClickedEntryID.setValue(null);
    }


    public MutableLiveData<String> getmSelectedCategory() {
        return mSelectedCategory;
    }

    public void setmSelectedCategory(String input) {
        this.mSelectedCategory.setValue(input);
    }


    public MutableLiveData<Boolean> getmCategorizeItemsToOthersTrigger() {
        return mCategorizeItemsToOthersTrigger;
    }

    public void setmCategorizeItemsToOthersTrigger(boolean input) {
        this.mCategorizeItemsToOthersTrigger.setValue(input);
    }
}
