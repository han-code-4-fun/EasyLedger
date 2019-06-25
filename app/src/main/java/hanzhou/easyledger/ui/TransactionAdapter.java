package hanzhou.easyledger.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.Transaction;
import hanzhou.easyledger.util.UnitUtil;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    final private CustomListItemClickListener mOnClickListener;
    private final Context mContext;
    private List<Transaction> mTransactionList;

    public interface CustomListItemClickListener {
        void customOnListItemClick(int clickedItemIndex);
    }

    public TransactionAdapter(Context context, List<Transaction> transactionList, CustomListItemClickListener listener){
        mContext = context;
        this.mTransactionList = transactionList;
        mOnClickListener = listener;
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
        Transaction currentRecord= mTransactionList.get(position);
        holder.time.setText(currentRecord.getmTime());
        holder.amount.setText("$"+UnitUtil.moneyFormater.format(currentRecord.getmAmount()));
        holder.category.setText(currentRecord.getmCatogory());
        holder.remark.setText(currentRecord.getmRemark());

    }

    @Override
    public int getItemCount() {
        return mTransactionList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder
    implements  View.OnClickListener{
        final TextView time;
        final TextView amount;
        final TextView category;
        final TextView remark;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.transaction_time);
            amount = itemView.findViewById(R.id.transaction_amount);
            category = itemView.findViewById(R.id.transaction_category);
            remark = itemView.findViewById(R.id.transaction_remark);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.customOnListItemClick(position);
        }
    }
}
