package hanzhou.easyledger.ui;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private static TransactionAdapter sInstance = null;

    private static final String TAG = TransactionAdapter.class.getSimpleName();

    private CustomListItemClickListener mOnClickListener;
    private Context mContext;
    private List<TransactionEntry> mTransactionEntryList;
    private TransactionDBViewModel mViewModel;
    public boolean isInActionMode;

    private int hash;

    private int adapterHash;


    public interface CustomListItemClickListener {
        void customOnListItemClick(int position);
        void customOnListItemLongClick(int position);
    }

    private TransactionAdapter(Context context,
                               CustomListItemClickListener listener,
                               TransactionDBViewModel inputVM) {

        mContext = context;
        mOnClickListener = listener;
        mViewModel = inputVM;
        setupViewModelObserver();

//        mViewModel.getActionModeState().observe((AppCompatActivity) mContext, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                isInActionMode = aBoolean;
//                //set to default style when the 'false' state is updated from user's input of action bar
//                if(!isInActionMode){
//                    deselectAll();
//                }else {
//                    //todo, check if error
//                    //todo
//                    //todo
//                    notifyDataSetChanged();
//                }
//            }
//        });
//
//
//        //trigger that react to user's click from the action bar
//        mViewModel.getmDeselectAllTrigger().observe((AppCompatActivity) mContext, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if(aBoolean){
//                    deselectAll();
//                    Log.d(Constant.TESTFLOW+TAG, "viewmodel observer, deselected all and now set DeselectAllTrigger to false");
//                    mViewModel.setDeselectAllTrigger(false);
//                }
//            }
//        });
//
//        //trigger that react to user's click from the action bar
//        mViewModel.getmSelectAllTrigger().observe((AppCompatActivity) mContext, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if(aBoolean){
//                    selectAll();
//                    Log.d(Constant.TESTFLOW+TAG, "viewmodel observer, selected all and now set SelectAllTrigger to false");
//                    mViewModel.setSelectAllTrigger(false);
//                }
//            }
//        });



    }


    private void setupViewModelObserver() {
        mViewModel.getActionModeState().observe((AppCompatActivity) mContext, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    deselectAll();
                }

            }
        });
    }


    public static TransactionAdapter getInstance(Context context,CustomListItemClickListener listener,TransactionDBViewModel inputVM){
        if(sInstance == null){
            sInstance = new TransactionAdapter(context, listener, inputVM);
        }

        return sInstance;
    }


    public void setData(List<TransactionEntry> inputEntries) {
        mTransactionEntryList = inputEntries;
        //todo, need to consider viewmodel obj???
        Log.d(Constant.TESTFLOW+TAG, "setData: mTransactionEntryList.size() is now "+ mTransactionEntryList.size());
        notifyDataSetChanged();

    }

    //todo, done
    public void updateSelectedItemsArray(int position){

        if (mViewModel.selectedBooleanArrayViewMode.get(position)) {
            mViewModel.selectedBooleanArrayViewMode.delete(position);
        } else {
            mViewModel.selectedBooleanArrayViewMode.put(position, true);
        }

        Log.d(Constant.TESTFLOW+ TAG, "now  mTransactionSelecteedNumber -> "+
                mViewModel.selectedBooleanArrayViewMode.size());
        Log.d(Constant.TESTFLOW+ TAG, "now  selectedBooleanArrayViewMode -> "+
                mViewModel.selectedBooleanArrayViewMode.toString());
        mViewModel.setmTransactionSelectedNumber();


        if(mViewModel.selectedBooleanArrayViewMode.size()== getItemCount()){
            mViewModel.setmIsAllSelected(true);
        }else{
            mViewModel.setmIsAllSelected(false);
        }
        this.notifyItemChanged(position);
    }
//    public void updateSelectedItemsArray(int position){
//
//        if (mSelectedItemsArray.get(position)) {
//            mSelectedItemsArray.delete(position);
//        } else {
//            mSelectedItemsArray.put(position, true);
//        }
//        mViewModel.setmTransactionSelectedNumber(mSelectedItemsArray.size());
//
//
//        if(mSelectedItemsArray.size()== getItemCount()){
//            mViewModel.setmIsAllSelected(true);
//        }else{
//            mViewModel.setmIsAllSelected(false);
//        }
//        notifyItemChanged(position);
//        Log.d("testtest", "updateSelectedItemsArray: ---->"+mSelectedItemsArray.toString());
//    }


    public void clearSelectedItemsArray(){
        for(int i = 0; i < mViewModel.selectedBooleanArrayViewMode.size(); i++) {
            int key = mViewModel.selectedBooleanArrayViewMode.keyAt(i);
            if(mViewModel.selectedBooleanArrayViewMode.get(key)){
                mViewModel.selectedBooleanArrayViewMode.put(key, false);
                this.notifyItemChanged(key);
            }
        }
        mViewModel.setmTransactionSelectedNumber();
        mViewModel.setmIsAllSelected(false);
    }


    public void selectAll() {
        Log.d("test_hash", "selectAll: adapter hash  "+adapterHash);
        Log.d(Constant.TESTFLOW+TAG, "selectAll: getItemCount() is "+ mTransactionEntryList.size());
        Log.d(Constant.TESTFLOW+TAG, "selectAll: selectedBooleanArrayViewMode is "+
                mViewModel.selectedBooleanArrayViewMode.toString());
        for(int i =0;i<mTransactionEntryList.size(); i++){
            Log.d(Constant.TESTFLOW+TAG, "selectAll: "+i);
            if(!mViewModel.selectedBooleanArrayViewMode.get(i)){
                mViewModel.selectedBooleanArrayViewMode.put(i,true);
                Log.d(Constant.TESTFLOW+TAG, "steps ++++= " + mViewModel.selectedBooleanArrayViewMode.toString());
                this.notifyItemChanged(i);
            }
        }
        mViewModel.setmTransactionSelectedNumber();
        Log.d(Constant.TESTFLOW+ TAG, "selectAll: now will set mTransactionSelecteedNumber -> "+
                mViewModel.selectedBooleanArrayViewMode.size());
        Log.d(Constant.TESTFLOW+TAG, "selectAll: selectedBooleanArrayViewMode is "+
                mViewModel.selectedBooleanArrayViewMode.toString());

        mViewModel.setmIsAllSelected(true);
////        mViewModel.selectAllItems();
//        mViewModel.synchronizeSelectedItemsArrayWithInViewModelClass(mSelectedItemsArray);
//        Log.d("testtest_Adapter", "selectAll:  --------->" + mSelectedItemsArray.size());
    }


    public void deselectAll() {
        Log.d("test_hash", "deselectAll: adapter hash "+ adapterHash);
        Log.d(Constant.TESTFLOW+TAG, "deselectAll: selectedBooleanArrayViewMode is "+
                mViewModel.selectedBooleanArrayViewMode.toString());
        mViewModel.selectedBooleanArrayViewMode.clear();
        mViewModel.setmTransactionSelectedNumber();
        Log.d(Constant.TESTFLOW+ TAG, "deselectAll: now will set mTransactionSelecteedNumber -> "+
                mViewModel.selectedBooleanArrayViewMode.size());
        Log.d(Constant.TESTFLOW+TAG, "de-selectAll:  now will set isAllSelected to false");
        mViewModel.setmIsAllSelected(false);
        Log.d(Constant.TESTFLOW+TAG, "deselectAll: selectedBooleanArrayViewMode is "+
                mViewModel.selectedBooleanArrayViewMode.toString());
        this.notifyDataSetChanged();
//        mViewModel.synchronizeSelectedItemsArrayWithInViewModelClass(mSelectedItemsArray);
    }

//
//    private boolean getIsToolBarInAction() {
//
////        return mIsInChoiceMode;
//        return isInActionMode;
//    }

//    private void setToolBarActionState(boolean isInChoiceMode) {
//        mViewModel.setActionModeState(isInChoiceMode);
//    }
//
//    private List<TransactionEntry> getSelectedTransactions() {
//        List<Integer> selectedNumbers = getSelectedItems();
//
//
//        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.size());
//
//        for (Integer i : selectedNumbers) {
//            output.add(mTransactionEntryList.get(i));
//        }
//
//        return output;
//    }

//    private List<Integer> getSelectedItems() {
//        List<Integer> selectedNumbers = new ArrayList<>(mViewModel.selectedBooleanArrayViewMode.size());
//        for (int i = 0; i < mViewModel.selectedBooleanArrayViewMode.size(); ++i) {
//            selectedNumbers.add(mViewModel.selectedBooleanArrayViewMode.keyAt(i));
//        }
//        return selectedNumbers;
//    }

//    public List<Integer> getSelectedItems() {
//        List<Integer> items = new ArrayList<>(mSelectedItemsArray.size());
//        for (int i = 0; i < mSelectedItemsArray.size(); ++i) {
//            items.add(mSelectedItemsArray.keyAt(i));
//        }
//        Log.d(TAG, "getSelectedItems:  --------->"+ mSelectedItemsArray.size());




//        List<Integer> items = new ArrayList<>(mCrossVM.getSelectedItems().size());
//        for (int i = 0; i < mCrossVM.getSelectedItems().size(); ++i) {
//            items.add(mCrossVM.getSelectedItems().keyAt(i));
//        }
//        return items;
//        return mViewModel.getSelectedItemAsListOfInteger();
//    }


    //done
//    public void clearSelectedState() {
//
//        List<Integer> selection = getSelectedItems();
//
//        Log.d(TAG, "getSelectedItems:  --------->" + selection.size());
//        mViewModel.selectedBooleanArrayViewMode.clear();
////        mViewModel.synchronizeSelectedItemsArrayWithInViewModelClass(mSelectedItemsArray);
//
//        for (Integer i : selection) {
//            notifyItemChanged(i);
//        }
//    }


    public TransactionEntry getClickedData(int input) {
        return mTransactionEntryList.get(input);
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View transactionListView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_transaction, parent, false);

        return new TransactionViewHolder(transactionListView);


    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        Log.d("flow", "onBindViewHolder: update THIS data");
        TransactionEntry currentRecord = mTransactionEntryList.get(position);


        holder.time.setText(String.valueOf(currentRecord.getTime()));
        holder.amount.setText(UnitUtil.moneyFormater.format(currentRecord.getAmount()));
        holder.category.setText(currentRecord.getCategory());
        holder.remark.setText(currentRecord.getRemark());

//        Log.d(Constant.TESTFLOW+TAG, "onBindViewHolder: refresh");
//        Log.d(Constant.TESTFLOW+TAG, "onBindViewHolder: position "+ position);
        if (isInActionMode) {
            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.color_money_out));
            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setChecked(mViewModel.getSelectedItemsArray().getValue().get(position));
//            Log.d(Constant.TESTFLOW+TAG, "selectedBooleanArrayViewMode.get(position)  -> "+
//                    mViewModel.selectedBooleanArrayViewMode.get(position));
            holder.checkBox.setChecked(mViewModel.selectedBooleanArrayViewMode.get(position));
        } else {
//            Log.d(Constant.TESTFLOW+TAG, "NOT IN ACTION MODE!!!");
            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.transaction_default_background));
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        int temp;
        if (mTransactionEntryList == null) {
            temp =0;
        }else{
            temp = mTransactionEntryList.size();
        }
//        mViewModel.setmSizeOfTransactions(temp);
        return temp;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView time;
        TextView amount;
        TextView category;
        TextView remark;
        CheckBox checkBox;


        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.transaction_time);
            amount = itemView.findViewById(R.id.transaction_amount);
            category = itemView.findViewById(R.id.transaction_category);
            remark = itemView.findViewById(R.id.transaction_remark);

            checkBox = itemView.findViewById(R.id.transaction_checkbox);
            checkBox.setVisibility(View.GONE);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            /*
                make sure custom onclicklistener is working and
                the deleting animation is on-going while customer clicked this position
            */
            if (mOnClickListener != null && position != RecyclerView.NO_POSITION) {


//                mViewModel.updateSelectedItemsArray(position);

                if(isInActionMode){
                    updateSelectedItemsArray(position);
                }else{
                    //open new activity/fragment from ui activity/fragment which implement this listener
                    //todo, make the 'open' aciton through viewmodel
                    mOnClickListener.customOnListItemClick(position);
                }

                //todo, check redundant
//                notifyItemChanged(position);

                //todo, test if working witout below lines
             /*   if (isInActionMode) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        //todo, pass  value to viewmodel
                    } else {
                        checkBox.setChecked(true);
                    }
                }*/


//                mOnClickListener.customOnListItemClick(position);
            }

        }

        @Override
        public boolean onLongClick(View view) {

            if (!isInActionMode) {
                //todo, check if redundant

//                clearSelectedState();
                //the only way to enter a toolbar action mode

                Log.d(Constant.TESTFLOW+TAG, "onLongClick: now will set action mode to true");
                mViewModel.setActionModeState(true);

            }
            //todo, handle while action mode, user click to another fragment.
            return false;
        }
    }
}
