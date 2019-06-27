package hanzhou.easyledger.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.UnitUtil;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private CustomListItemClickListener mOnClickListener;
    private  Context mContext;
    private List<TransactionEntry> mTransactionEntryList;
    private OverviewFragment overviewFragment;


    public interface CustomListItemClickListener {
        void customOnListItemClick(int clickedItemIndex);
    }

    public TransactionAdapter(Context context, List<TransactionEntry> transactionEntryList,
                              CustomListItemClickListener listener, OverviewFragment overviewFragment){
        mContext = context;
        this.mTransactionEntryList = transactionEntryList;
        mOnClickListener = listener;
        this.overviewFragment = overviewFragment;
    }

    public void setData(List<TransactionEntry> inputEntries) {
        mTransactionEntryList = inputEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View transactionListView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_transaction, parent, false);

        return new TransactionViewHolder(transactionListView,overviewFragment);


    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        TransactionEntry currentRecord= mTransactionEntryList.get(position);

        holder.time.setText(""+currentRecord.getTime());
        holder.amount.setText("$"+UnitUtil.moneyFormater.format(currentRecord.getAmount()));
        holder.category.setText(currentRecord.getCategory());
        holder.remark.setText(currentRecord.getRemark());

    }

    @Override
    public int getItemCount() {
        return mTransactionEntryList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder
    implements  View.OnClickListener{
         TextView time;
         TextView amount;
         TextView category;
         TextView remark;
         MainActivity mainActivity;

         LinearLayout linearLayout;


        public TransactionViewHolder(@NonNull View itemView, OverviewFragment overviewFragment) {
            super(itemView);

            time = itemView.findViewById(R.id.transaction_time);
            amount = itemView.findViewById(R.id.transaction_amount);
            category = itemView.findViewById(R.id.transaction_category);
            remark = itemView.findViewById(R.id.transaction_remark);

            linearLayout = itemView.findViewById(R.id.linearlayout_inside_recyclerview);

            itemView.setOnClickListener(this);
            linearLayout.setOnLongClickListener(overviewFragment);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.customOnListItemClick(position);
        }
    }
}
