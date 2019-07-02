//package hanzhou.easyledger.ui;
//
//import android.content.Context;
//import android.util.Log;
//import android.util.SparseBooleanArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.Observer;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import hanzhou.easyledger.R;
//import hanzhou.easyledger.data.TransactionEntry;
//import hanzhou.easyledger.utility.UnitUtil;
//import hanzhou.easyledger.viewmodel.TransactionDBViewModel;
//
//
//public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
//
//    private static final String TAG = TransactionAdapter.class.getSimpleName();
//    private CustomListItemClickListener mOnClickListener;
//    private Context mContext;
//    private List<TransactionEntry> mTransactionEntryList;
//    private SparseBooleanArray mSelectedItemsArray;
////    private boolean mIsInChoiceMode;
//
//    //    private boolean newIsInActionMode;
////    private Toolbar toolbar;
//    private TransactionDBViewModel mViewModel;
//
//    private boolean isInActionMode;
//
//
//    public interface CustomListItemClickListener {
//        void customOnListItemClick(int position);
//
//        void customOnListItemLongClick(int position);
//    }
//
//    public TransactionAdapter(Context context,
//                              CustomListItemClickListener listener,
//                              TransactionDBViewModel inputVM
//    ) {
//        mContext = context;
//        mOnClickListener = listener;
//        this.mSelectedItemsArray = new SparseBooleanArray();
//        mViewModel = inputVM;
//
////        mCrossVM.getTransactionEntriesFromDB().observe((AppCompatActivity)mContext, new Observer<List<TransactionEntry>>() {
////            @Override
////            public void onChanged(List<TransactionEntry> transactionEntries) {
////                mTransactionEntryList = transactionEntries;
////                notifyDataSetChanged();
////            }
////        });
//        mViewModel.getActionModeState().observe((AppCompatActivity) mContext, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                isInActionMode = aBoolean;
//                Log.d("testtest_Adapter", "isInActionMode: " + aBoolean);
//            }
//        });
//
//        mViewModel.getSelectedItemsArray().observe((AppCompatActivity) mContext, new Observer<SparseBooleanArray>() {
//            @Override
//            public void onChanged(SparseBooleanArray sparseBooleanArray) {
//                mSelectedItemsArray = sparseBooleanArray;
//            }
//        });
//
//    }
//
//
//    public void setData(List<TransactionEntry> inputEntries) {
//        mTransactionEntryList = inputEntries;
//        //todo, need to consider viewmodel obj???
//        notifyDataSetChanged();
//
//    }
//
//    //
//    public void switchSelectedState(int position) {
////        mViewModel.switchSelectedState(position);
////        if (mSelectedItemsArray.get(position)) {
////            mSelectedItemsArray.delete(position);
////        } else {
////            mSelectedItemsArray.put(position, true);
////        }
//        //todo, the mSelectedItemsArray observe from mViewModel.setSelectedItemsArray already!!!
////        mViewModel.setSelectedItemsArray(mSelectedItemsArray);
//
//        //todo, tried to move to viewholder class
////        mViewModel.updateSelectedItemsArray(position);
////        Log.d("testtest_Adapter", "switchSelectedState:  --------->"+ position+" now is -> "+mSelectedItemsArray.get(position));
////
////        notifyItemChanged(position);
//    }
//
//
//    public void selectAll() {
////        for(int i =0;i<mTransactionEntryList.size();i++){
////            if(!mSelectedItemsArray.get(i)){
////                mSelectedItemsArray.put(i,true);
////            }
////        }
//        mViewModel.selectAllItems();
//        Log.d("testtest_Adapter", "selectAll:  --------->" + mSelectedItemsArray.size());
//        notifyDataSetChanged();
//    }
//
//
//    public boolean getIsToolBarInAction() {
//
////        return mIsInChoiceMode;
//        return isInActionMode;
//    }
//
//    public void setIsToolBarInAction(boolean isInChoiceMode) {
//        mViewModel.setActionMode(isInChoiceMode);
//    }
//
//    public List<TransactionEntry> getSelectedTransactions() {
//        List<Integer> selectedNumbers = getSelectedItems();
//        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.size());
//
//        for (Integer i : selectedNumbers) {
//            output.add(mTransactionEntryList.get(i));
//        }
//
//        return output;
//    }
//
//    public List<Integer> getSelectedItems() {
////        List<Integer> items = new ArrayList<>(mSelectedItemsArray.size());
////        for (int i = 0; i < mSelectedItemsArray.size(); ++i) {
////            items.add(mSelectedItemsArray.keyAt(i));
////        }
////        Log.d(TAG, "getSelectedItems:  --------->"+ mSelectedItemsArray.size());
//
//
//        //todo, change from List<Integer> to SparseBooleanArray later
//
//
////        List<Integer> items = new ArrayList<>(mCrossVM.getSelectedItems().size());
////        for (int i = 0; i < mCrossVM.getSelectedItems().size(); ++i) {
////            items.add(mCrossVM.getSelectedItems().keyAt(i));
////        }
////        return items;
//        return mViewModel.getSelectedItemAsListOfInteger();
//    }
//
//
//    //done
//    public void clearSelectedState() {
//
//        List<Integer> selection = mViewModel.getSelectedItemAsListOfInteger();
//
//        Log.d(TAG, "getSelectedItems:  --------->" + mViewModel.getSelectedItems().getValue().size());
////        mSelectedItemsArray.clear();
//        mViewModel.clearSelectedItems();
//
//        for (Integer i : selection) {
//            notifyItemChanged(i);
//        }
//    }
//
//    public TransactionEntry getClickedOne(int input) {
//        return mTransactionEntryList.get(input);
//    }
//
//    public int getSelectedItemCount() {
////        return mSelectedItemsArray.size();
//        return mViewModel.getSelectedItems().getValue().size();
//    }
//
//    @NonNull
//    @Override
//    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View transactionListView = LayoutInflater
//                .from(mContext)
//                .inflate(R.layout.list_item_transaction, parent, false);
//
//        return new TransactionViewHolder(transactionListView);
//
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
//
//        TransactionEntry currentRecord = mTransactionEntryList.get(position);
//
//
//        holder.time.setText(String.valueOf(currentRecord.getTime()));
//        holder.amount.setText(UnitUtil.moneyFormater.format(currentRecord.getAmount()));
//        holder.category.setText(currentRecord.getCategory());
//        holder.remark.setText(currentRecord.getRemark());
//
//        if (isInActionMode) {
//            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.color_money_out));
//            holder.checkBox.setVisibility(View.VISIBLE);
////            holder.checkBox.setChecked(mViewModel.getSelectedItemsArray().getValue().get(position));
//            holder.checkBox.setChecked(mSelectedItemsArray.get(position));
//        } else {
//            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.transaction_default_background));
//            holder.checkBox.setVisibility(View.GONE);
//            holder.checkBox.setChecked(false);
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mTransactionEntryList == null) {
//            return 0;
//        }
//        return mTransactionEntryList.size();
//    }
//
//    public class TransactionViewHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener, View.OnLongClickListener {
//        TextView time;
//        TextView amount;
//        TextView category;
//        TextView remark;
//        CheckBox checkBox;
//
//
//        public TransactionViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            time = itemView.findViewById(R.id.transaction_time);
//            amount = itemView.findViewById(R.id.transaction_amount);
//            category = itemView.findViewById(R.id.transaction_category);
//            remark = itemView.findViewById(R.id.transaction_remark);
//
//            checkBox = itemView.findViewById(R.id.transaction_checkbox);
//            checkBox.setVisibility(View.GONE);
//
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            int position = getAdapterPosition();
//
//            /*
//                make sure custom onclicklistener is working and
//                the deleting animation is on-going while customer clicked this position
//            */
//            if (mOnClickListener != null && position != RecyclerView.NO_POSITION) {
//
//                mViewModel.updateSelectedItemsArray(position);
//
//                Log.d("testtest_VH_Onclick",
//                        "mViewModel.updateSelectedItemsArray(position) -->" +
//                                position + " now is -> " + mSelectedItemsArray.get(position)
//                );
//
//                notifyItemChanged(position);
//
//
//                if (isInActionMode) {
//                    if (checkBox.isChecked()) {
//                        checkBox.setChecked(false);
//                        //todo, pass  value to viewmodel
//                    } else {
//                        checkBox.setChecked(true);
//                    }
//                }
//
//                mOnClickListener.customOnListItemClick(position);
//            }
//
//        }
//
//        @Override
//        public boolean onLongClick(View view) {
//            Log.d("testlongclick", "onLongClick: get signal adapter");
////            int position = getAdapterPosition();
////            mOnClickListener.customOnListItemLongClick(position);
////            return false;
//            if (!isInActionMode) {
//
//                clearSelectedState();
////                mViewModel.setActionMode(true);
//
//                mViewModel.clearSelectedItems();
//
//            }
//            //todo, handle while action mode, user click to another fragment.
//            return false;
//        }
//    }
//}
