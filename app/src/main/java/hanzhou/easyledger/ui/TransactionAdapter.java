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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.UnitUtil;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private CustomListItemClickListener mOnClickListener;
    private Context mContext;
    private List<TransactionEntry> mTransactionEntryList;
    private SparseBooleanArray mSelectedItems;
    private boolean mIsInChoiceMode;



    public interface CustomListItemClickListener {
        void customOnListItemClick(int position);
        void customOnListItemLongClick(int position);
    }

    public TransactionAdapter(Context context,
                              CustomListItemClickListener listener){
        mContext = context;
        mOnClickListener = listener;
        this.mSelectedItems = new SparseBooleanArray();
        mIsInChoiceMode = false;
    }

    public void setData(List<TransactionEntry> inputEntries) {
        mTransactionEntryList = inputEntries;
        notifyDataSetChanged();
    }

    public void switchSelectedState(int position) {

        //item has been selected/
        if (this.mSelectedItems.get(position)) {
            mSelectedItems.delete(position);
        } else {
            mSelectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public boolean getIsToolBarInAction() {
        return mIsInChoiceMode;
    }

    public void setIsToolBarInAction(boolean isInChoiceMode) {
        this.mIsInChoiceMode = isInChoiceMode;
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); ++i) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    public void clearSelectedState() {
        List<Integer> selection = getSelectedItems();
        mSelectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    public int getSelectedItemCount() {
        return mSelectedItems.size();
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

        TransactionEntry currentRecord= mTransactionEntryList.get(position);

        holder.time.setText(String.valueOf(currentRecord.getTime()));
        holder.amount.setText(UnitUtil.moneyFormater.format(currentRecord.getAmount()));
        holder.category.setText(currentRecord.getCategory());
        holder.remark.setText(currentRecord.getRemark());

        if(mIsInChoiceMode){
            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.color_money_out));
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(mSelectedItems.get(position));
        }else{
            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.transaction_default_background));
            holder.checkBox.setVisibility(View.GONE);
                holder.checkBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        if (mTransactionEntryList == null) {
            return 0;
        }
        return mTransactionEntryList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder
    implements  View.OnClickListener, View.OnLongClickListener {
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

            if(checkBox.isChecked()){
                checkBox.setChecked(false);
            }else{
                checkBox.setChecked(true);
            }

            mOnClickListener.customOnListItemClick(position);
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("testlongclick", "onLongClick: get signal adapter");
            int position = getAdapterPosition();
            mOnClickListener.customOnListItemLongClick(position);
            return false;
        }
    }
}
