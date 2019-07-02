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
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private static final String TAG = TransactionAdapter.class.getSimpleName();
    private CustomListItemClickListener mOnClickListener;
    private Context mContext;
    private List<TransactionEntry> mTransactionEntryList;
    private SparseBooleanArray mSelectedItemsArray;

    private TransactionDBViewModel mViewModel;

    private boolean isInActionMode;


    public interface CustomListItemClickListener {
        void customOnListItemClick(int position);
//        void customOnListItemLongClick(int position);
    }

    public TransactionAdapter(Context context,
                                CustomListItemClickListener listener,
                              TransactionDBViewModel inputVM) {

        mContext = context;
        mOnClickListener = listener;
        this.mSelectedItemsArray = new SparseBooleanArray();
        mViewModel = inputVM;


        mViewModel.getActionModeState().observe((AppCompatActivity) mContext, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInActionMode = aBoolean;
                //let show recyclerview show a different style/color
                notifyDataSetChanged();
            }
        });

        mViewModel.getSelectedItemsArray().observe((AppCompatActivity) mContext, new Observer<SparseBooleanArray>() {
            @Override
            public void onChanged(SparseBooleanArray sparseBooleanArray) {
                mSelectedItemsArray = sparseBooleanArray;
                for (int i = 0; i < mSelectedItemsArray.size(); i++) {
                    notifyItemChanged(mSelectedItemsArray.keyAt(i));
                }
            }
        });

//        mViewModel.getIsAllSelected().observe((AppCompatActivity) mContext, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if(aBoolean){
//                    selectAll();
//                }else{
//                    deselectAll();
//                }
//                notifyDataSetChanged();
//            }
//        });

    }


    public void setData(List<TransactionEntry> inputEntries) {
        mTransactionEntryList = inputEntries;
        //todo, need to consider viewmodel obj???
        notifyDataSetChanged();

    }


    private void selectAll() {
        for(int i =0;i<getItemCount();i++){
            if(!mSelectedItemsArray.get(i)){
                mSelectedItemsArray.put(i,true);
            }
        }
//        mViewModel.selectAllItems();
        mViewModel.synchronizeSelectedItemsArrayWithInViewModelClass(mSelectedItemsArray);
        Log.d("testtest_Adapter", "selectAll:  --------->" + mSelectedItemsArray.size());
    }

    private void deselectAll() {

        mSelectedItemsArray.clear();
        mViewModel.synchronizeSelectedItemsArrayWithInViewModelClass(mSelectedItemsArray);
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

    private List<TransactionEntry> getSelectedTransactions() {
        List<Integer> selectedNumbers = getSelectedItems();


        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.size());

        for (Integer i : selectedNumbers) {
            output.add(mTransactionEntryList.get(i));
        }

        return output;
    }

    private List<Integer> getSelectedItems() {
        List<Integer> selectedNumbers = new ArrayList<>(mSelectedItemsArray.size());
        for (int i = 0; i < mSelectedItemsArray.size(); ++i) {
            selectedNumbers.add(mSelectedItemsArray.keyAt(i));
        }
        return selectedNumbers;

    }

//    public List<Integer> getSelectedItems() {
//        List<Integer> items = new ArrayList<>(mSelectedItemsArray.size());
//        for (int i = 0; i < mSelectedItemsArray.size(); ++i) {
//            items.add(mSelectedItemsArray.keyAt(i));
//        }
//        Log.d(TAG, "getSelectedItems:  --------->"+ mSelectedItemsArray.size());


        //todo, change from List<Integer> to SparseBooleanArray later


//        List<Integer> items = new ArrayList<>(mCrossVM.getSelectedItems().size());
//        for (int i = 0; i < mCrossVM.getSelectedItems().size(); ++i) {
//            items.add(mCrossVM.getSelectedItems().keyAt(i));
//        }
//        return items;
//        return mViewModel.getSelectedItemAsListOfInteger();
//    }


    //done
    public void clearSelectedState() {

        List<Integer> selection = getSelectedItems();

        Log.d(TAG, "getSelectedItems:  --------->" + selection.size());
        mSelectedItemsArray.clear();
        mViewModel.synchronizeSelectedItemsArrayWithInViewModelClass(mSelectedItemsArray);

        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }


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


        if (isInActionMode) {
            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.color_money_out));
            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setChecked(mViewModel.getSelectedItemsArray().getValue().get(position));
            holder.checkBox.setChecked(mSelectedItemsArray.get(position));
        } else {
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

        mViewModel.setmSizeOfTransactions(temp);
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
            mOnClickListener.customOnListItemClick(position);
            /*
                make sure custom onclicklistener is working and
                the deleting animation is on-going while customer clicked this position
            */
            if (mOnClickListener != null && position != RecyclerView.NO_POSITION) {


                mViewModel.updateSelectedItemsArray(position);

                Log.d("testtest_VH_Onclick",
                        "mViewModel.updateSelectedItemsArray(position) -->" +
                                position + " now is -> " + mSelectedItemsArray.get(position)
                );

                //todo, test if working witout below lines
             /*   if (isInActionMode) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        //todo, pass  value to viewmodel
                    } else {
                        checkBox.setChecked(true);
                    }
                }*/

                notifyItemChanged(position);

//                mOnClickListener.customOnListItemClick(position);
            }

        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("testlongclick", "onLongClick: get signal adapter");

            if (!isInActionMode) {
                clearSelectedState();
                //the only way to enter a toolbar action mode
                mViewModel.setActionModeState(true);

            }
            //todo, handle while action mode, user click to another fragment.
            return false;
        }
    }
}
