package hanzhou.easyledger.viewadapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ConcurrentModificationException;
import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {


    private static final String TAG = TransactionAdapter.class.getSimpleName();

//    private CustomListItemClickListener mOnClickListener;
    private List<TransactionEntry> mTransactionEntryList;
    private AdapterNActionBarViewModel mAdapterNActionBarViewModel;

    private boolean isInActionMode;
    private Context mContext;


    public TransactionAdapter(AdapterNActionBarViewModel inputVM, Context context) {

//        mOnClickListener = listener;
        mAdapterNActionBarViewModel = inputVM;
        mAdapterNActionBarViewModel.emptySelectedItems();
        mContext = context;
    }



    public void setAdapterData(List<TransactionEntry> inputEntries) {
        mTransactionEntryList = inputEntries;
        mAdapterNActionBarViewModel.emptySelectedItems();
        notifyDataSetChanged();

    }

    public List<TransactionEntry> getAdateperData(){
        return  mTransactionEntryList;
    }

    public void updateSelectedItemsArray(int position){

        /*if selected, unselect(delete) it*/
        if (mAdapterNActionBarViewModel.getAValueFromSelectedItems(position)) {
            mAdapterNActionBarViewModel.deleteAValueFromSelectedItems(position);
        } else {
            /*else select it*/
            mAdapterNActionBarViewModel.putAValueIntoSelectedItems(position, true);
        }

        /*update selected number that will display in the toolbar*/
        mAdapterNActionBarViewModel.setmTransactionSelectedNumber();

        if(mAdapterNActionBarViewModel.getNumberOfSelectedItems() == getItemCount()){
            mAdapterNActionBarViewModel.setmIsAllSelected(true);
        }else{
            mAdapterNActionBarViewModel.setmIsAllSelected(false);
        }
        this.notifyItemChanged(position);
    }


//    public int getOneSelectedEntryID(){
//        int position = mAdapterNActionBarViewModel.getFirstSelectedItems();
//
//        return mTransactionEntryList.get(position).getId();
//    }


    public void selectAll() {

        for(int i =0;i<mTransactionEntryList.size(); i++){
            if(!mAdapterNActionBarViewModel.getAValueFromSelectedItems(i)){
                mAdapterNActionBarViewModel.putAValueIntoSelectedItems(i,true);
                this.notifyItemChanged(i);
            }
        }
        mAdapterNActionBarViewModel.setmTransactionSelectedNumber();

        mAdapterNActionBarViewModel.setmIsAllSelected(true);
    }

    public List<TransactionEntry> getmTransactionEntryList() {
        return mTransactionEntryList;
    }

    public void deselectAll() {
        mAdapterNActionBarViewModel.emptySelectedItems();
        mAdapterNActionBarViewModel.setmTransactionSelectedNumber();
        mAdapterNActionBarViewModel.setmIsAllSelected(false);
        this.notifyDataSetChanged();
    }

    public boolean isInActionMode() {
        return isInActionMode;
    }

    public void setInActionMode(boolean inActionMode) {
        isInActionMode = inActionMode;
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

        TransactionEntry currentRecord = mTransactionEntryList.get(position);

        float amount = currentRecord.getAmount();

        holder.time.setText(UnitUtil.getTimeIntInMoreReadableFormat(currentRecord.getTime()));

        holder.amount.setText(UnitUtil.formatMoney(amount));
        if(amount>=0){
            holder.amount.setTextColor(ContextCompat.getColor(mContext,R.color.color_money_in));

        }else{
            holder.amount.setTextColor(ContextCompat.getColor(mContext,R.color.color_money_out));

        }
        holder.category.setText(currentRecord.getCategory());
        holder.remark.setText(currentRecord.getRemark());
        if (isInActionMode) {
            holder.layout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_selected_bg));
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateSelectedItemsArray(position);
                }
            });
            holder.checkBox.setChecked(mAdapterNActionBarViewModel.getAValueFromSelectedItems(position));

        } else {
            holder.layout.setBackground(mContext.getResources().getDrawable(R.drawable.background_list_item));

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

        ConstraintLayout layout;

        TextView time;
        TextView amount;
        TextView category;
        TextView remark;
        CheckBox checkBox;


        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.transaction_layout);
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

            /*ensure the deleting animation is on-going while customer clicked this position*/
            if ( position != RecyclerView.NO_POSITION) {

                if(isInActionMode){
                    updateSelectedItemsArray(position);
                }
                else{

                    int id = mTransactionEntryList.get(position).getId();

                    mAdapterNActionBarViewModel.setmClickedEntryID(id);
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {

            if (!isInActionMode) {  mAdapterNActionBarViewModel.setActionModeState(true); }
            return false;
        }
    }
}
