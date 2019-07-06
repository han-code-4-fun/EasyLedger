package hanzhou.easyledger.ui;

import android.content.Context;
import android.graphics.Color;
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
    private List<TransactionEntry> mTransactionEntryList;
    private TransactionDBViewModel mViewModel;
    boolean isInActionMode;


    public interface CustomListItemClickListener {
        void customOnListItemClick(int position);
    }

    private TransactionAdapter(CustomListItemClickListener listener,TransactionDBViewModel inputVM) {

        mOnClickListener = listener;
        mViewModel = inputVM;

    }

    /* make a singleton instance*/
    public static TransactionAdapter getInstance(CustomListItemClickListener listener,TransactionDBViewModel inputVM){
        if(sInstance == null){
            sInstance = new TransactionAdapter( listener, inputVM);
        }

        return sInstance;
    }


    public void setAdapterData(List<TransactionEntry> inputEntries) {
        mTransactionEntryList = inputEntries;
        notifyDataSetChanged();

    }

    public void updateSelectedItemsArray(int position){

        if (mViewModel.getAValueFromSelectedItems(position)) {
            mViewModel.deleteAValueFromSelectedItems(position);
        } else {
            mViewModel.putAValueIntoSelectedItems(position, true);
        }

        mViewModel.setmTransactionSelectedNumber();

        if(mViewModel.getNumberOfSelectedItems() == getItemCount()){
            mViewModel.setmIsAllSelected(true);
        }else{
            mViewModel.setmIsAllSelected(false);
        }
        this.notifyItemChanged(position);
    }



    public void selectAll() {
        for(int i =0;i<mTransactionEntryList.size(); i++){
            if(!mViewModel.getAValueFromSelectedItems(i)){
                mViewModel.putAValueIntoSelectedItems(i,true);
                this.notifyItemChanged(i);
            }
        }
        mViewModel.setmTransactionSelectedNumber();

        mViewModel.setmIsAllSelected(true);
    }


    public void deselectAll() {
        mViewModel.emptySelectedItems();
        mViewModel.setmTransactionSelectedNumber();
        this.notifyDataSetChanged();
    }



    public TransactionEntry getClickedEntry(int input) {
        return mTransactionEntryList.get(input);
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View transactionListView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_transaction, parent, false);

        return new TransactionViewHolder(transactionListView);


    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, final int position) {

        Log.d("flow", "onBindViewHolder: update THIS data");
        TransactionEntry currentRecord = mTransactionEntryList.get(position);


        holder.time.setText(String.valueOf(currentRecord.getTime()));
        holder.amount.setText(UnitUtil.formatMoney(currentRecord.getAmount()));
        holder.category.setText(currentRecord.getCategory());
        holder.remark.setText(currentRecord.getRemark());
        if (isInActionMode) {
            holder.amount.setBackgroundColor(Color.RED);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateSelectedItemsArray(position);
                }
            });
            holder.checkBox.setChecked(mViewModel.getAValueFromSelectedItems(position));

        } else {
            holder.amount.setBackgroundColor(Color.WHITE);
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        if (mTransactionEntryList == null){ return 0;}

        return mTransactionEntryList.size();
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

            /*ensure custom onclicklistener is working and
                the deleting animation is on-going while customer clicked this position*/
            if (mOnClickListener != null && position != RecyclerView.NO_POSITION) {

                if(isInActionMode){updateSelectedItemsArray(position);}
                else{
                    //open new activity/fragment from ui activity/fragment which implement this listener
                    mOnClickListener.customOnListItemClick(position);
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {

            if (!isInActionMode) {  mViewModel.setActionModeState(true); }
            return false;
        }
    }
}
