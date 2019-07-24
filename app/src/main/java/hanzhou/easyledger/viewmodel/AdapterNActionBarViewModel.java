package hanzhou.easyledger.viewmodel;

import android.app.Application;
import android.util.Log;
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



    private MutableLiveData<Boolean> mDeleteItemTrigger;
    private MutableLiveData<Boolean> mEditAnEntryTrigger;


    //keep track of user selection
    private SparseBooleanArray selectedBooleanArrayViewMode;

    private String currentLedger;


    private MutableLiveData<Integer> mClickedEntryID;

    private MutableLiveData<String> mSelectedCategory;



    private MutableLiveData<Boolean> mIsInBaseFragment;

    private MutableLiveData<Boolean> mShowBottomNavigationBar;
    private MutableLiveData<Boolean> mShowFloatingActionBtn;






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

        currentLedger = Constant.FRAG_CALL_FROM_OVERVIEW;

//        mIsInQuestionFragment = new MutableLiveData<>();
//        mIsInQuestionFragment.setValue(false);

        mClickedEntryID = new MutableLiveData<>();
        mClickedEntryID.setValue(null);


        mSelectedCategory = new MutableLiveData<>();
        mSelectedCategory.setValue("");

        mShowBottomNavigationBar = new MutableLiveData<>();
        mShowBottomNavigationBar.setValue(true);

        mShowFloatingActionBtn = new MutableLiveData<>();
        mShowFloatingActionBtn.setValue(true);

        mIsInBaseFragment = new MutableLiveData<>();
        mIsInBaseFragment.setValue(true);

        mDeleteItemTrigger = new MutableLiveData<>();
        mDeleteItemTrigger.setValue(false);

        mEditAnEntryTrigger = new MutableLiveData<>();
        mEditAnEntryTrigger.setValue(false);
    }

    public SparseBooleanArray getTesttt(){
        return selectedBooleanArrayViewMode;
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


    public TransactionEntry getOneSelectedTransaction(List<TransactionEntry> inputList){
        TransactionEntry output;
        int selectPosition = selectedBooleanArrayViewMode.keyAt(0);
        output = inputList.get(selectPosition);
        return output;
    }

    /*
        get selected items from inner variable SparseBooleanArray and
        mapping them to inputEntries (all entries of current fragment)
    */
    public List<TransactionEntry> getSelectedTransactions(List<TransactionEntry> inputList) {

        Log.d("test_delete", "getSelectedTransactions: inputlist size "+ inputList.size() );

        int[] selectedNumbers = getSelectedBooleanArrayIntoArrayOfPositionInCurrentListEntires();

        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.length);

        for (Integer i : selectedNumbers) {
            output.add(inputList.get(i));
        }

        return output;
    }

    private int[] getSelectedBooleanArrayIntoArrayOfPositionInCurrentListEntires(){

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

    public List<TransactionEntry> categorizeSelectedItemsToOthers(List<TransactionEntry> inputList){

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

    public int getFirstSelectedItems(){
        return selectedBooleanArrayViewMode.keyAt(0);
    }


    public String getCurrentLedger() {
        return currentLedger;
    }

    public void setCurrentLedger(String currentLedger) {
        this.currentLedger = currentLedger;
    }

//    public MutableLiveData<Boolean> getmIsInQuestionFragment() {
//        return mIsInQuestionFragment;
//    }
//
//    public void setmIsInQuestionFragment(Boolean input) {
//        Log.d("test_flow5", "setmIsInQuestionFragment: the viewmodel is "+this.hashCode());
//        this.mIsInQuestionFragment.setValue(input);
//    }

    public MutableLiveData<Integer> getmClickedEntryID() {
        return mClickedEntryID;
    }

    public void setmClickedEntryID(Integer inputID) {
        this.mClickedEntryID.setValue(inputID);
    }

    public void setmClickedEntryIDToNull(){
        mClickedEntryID.setValue(null);
    }

//    public MutableLiveData<Boolean> getmIsInSettingsFragment() {
//        return mIsInSettingsFragment;
//    }
//
//    public void setmIsInSettingsFragment(Boolean input) {
//        this.mIsInSettingsFragment.setValue(input);
//    }



//    public LiveData<Boolean> getmIsInAddNEditFragment() {
//        return mIsInAddNEditFragment;
//    }
//
//    public void setmIsInAddNEditFragment(Boolean input) {
//        this.mIsInAddNEditFragment.setValue(input);
//    }
//
//    public MutableLiveData<Boolean> getmIsInEditLedgerFragment() {
//        return mIsInEditLedgerFragment;
//    }
//
//    public void setmIsInEditLedgerFragment(boolean input) {
//        this.mIsInEditLedgerFragment.setValue(input);
//    }

    public MutableLiveData<String> getmSelectedCategory() {
        return mSelectedCategory;
    }

    public void setmSelectedCategory(String input) {
        this.mSelectedCategory.setValue(input);
    }


    public MutableLiveData<Boolean> getmShowBottomNavigationBar() {
        return mShowBottomNavigationBar;
    }

    public void setmShowBottomNavigationBar(boolean input) {
        this.mShowBottomNavigationBar.setValue(input);
    }

    public MutableLiveData<Boolean> getmShowFloatingActionBtn() {
        return mShowFloatingActionBtn;
    }

    public void setmShowFloatingActionBtn(boolean input) {
        this.mShowFloatingActionBtn.setValue(input);
    }

    public MutableLiveData<Boolean> getmIsInBaseFragment() {
        return mIsInBaseFragment;
    }

    public void setmIsInBaseFragment(boolean input) {
        this.mIsInBaseFragment.setValue(input);
    }

    /*a one way trigger called from MainActivity (actionbar) to inform DetailTransacitonFragment to delete selected items*/
    public void setmDeleteItemTrigger(boolean input){
        mDeleteItemTrigger.setValue(input);
    }

    public MutableLiveData<Boolean> getmDeleteItemTrigger() {
        return mDeleteItemTrigger;
    }

    public MutableLiveData<Boolean> getmEditAnEntryTrigger() {
        return mEditAnEntryTrigger;
    }

    public void setmEditAnEntryTrigger(Boolean input) {
        this.mEditAnEntryTrigger.setValue(input);
    }
}
