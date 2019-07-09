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

    private MutableLiveData<Boolean> mIsInQuestionFragment;

    private MutableLiveData<Boolean> mIsInSettingsFragment;

    private MutableLiveData<Boolean> mIsInAddNEditFragment;

    private MutableLiveData<Integer> mClickedEntryID;



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

        currentLedger = Constant.CALLFROMOVERVIEW;

        mIsInQuestionFragment = new MutableLiveData<>();
        mIsInQuestionFragment.setValue(false);

        mClickedEntryID = new MutableLiveData<>();
        mClickedEntryID.setValue(null);

        mIsInSettingsFragment = new MutableLiveData<>();
        mIsInSettingsFragment.setValue(false);

        mIsInAddNEditFragment = new MutableLiveData<>();
        mIsInAddNEditFragment.setValue(false);
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
    public List<TransactionEntry> getSelectedTransactions(List<TransactionEntry> inputList) {
        List<TransactionEntry> entries;

        entries= inputList;


        int[] selectedNumbers = new int[selectedBooleanArrayViewMode.size()];
        for (int i = 0; i < selectedBooleanArrayViewMode.size(); i++) {
            selectedNumbers[i] = selectedBooleanArrayViewMode.keyAt(i);
        }

        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.length);

        for (Integer i : selectedNumbers) {
            output.add(entries.get(i));
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


    public String getCurrentLedger() {
        return currentLedger;
    }

    public void setCurrentLedger(String currentLedger) {
        this.currentLedger = currentLedger;
    }

    public MutableLiveData<Boolean> getmIsInQuestionFragment() {
        return mIsInQuestionFragment;
    }

    public void setmIsInQuestionFragment(Boolean input) {
        this.mIsInQuestionFragment.setValue(input);
    }

    public MutableLiveData<Integer> getmClickedEntryID() {
        return mClickedEntryID;
    }

    public void setmClickedEntryID(Integer inputID) {
        this.mClickedEntryID.setValue(inputID);
    }

    public void setmClickedEntryIDToNull(){
        mClickedEntryID.setValue(null);
    }

    public MutableLiveData<Boolean> getmIsInSettingsFragment() {
        return mIsInSettingsFragment;
    }

    public void setmIsInSettingsFragment(Boolean input) {
        this.mIsInSettingsFragment.setValue(input);
    }

    public MutableLiveData<Boolean> getmIsInAddNEditFragment() {
        return mIsInAddNEditFragment;
    }

    public void setmIsInAddNEditFragment(Boolean input) {
        this.mIsInAddNEditFragment.setValue(input);
    }



}
