//package hanzhou.easyledger.temp;
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
//import androidx.appcompat.widget.Toolbar;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.nio.channels.InterruptedByTimeoutException;
//import java.util.ArrayList;
//import java.util.List;
//
//import hanzhou.easyledger.R;
//import hanzhou.easyledger.data.TransactionEntry;
//import hanzhou.easyledger.ui.TransactionAdapter;
//import hanzhou.easyledger.utility.UnitUtil;
//import hanzhou.easyledger.viewmodel.CrossFragmentCommunicationViewModel;
//
//public class TempTransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
//
//private static final String TAG = TransactionAdapter.class.getSimpleName();
//private CustomListItemClickListener mOnClickListener;
//private Context mContext;
//private List<TransactionEntry> mTransactionEntryList;
//private SparseBooleanArray mSelectedItems;
//private boolean mIsInChoiceMode;
//
//private boolean newIsInActionMode;
//private Toolbar toolbar;
//private CrossFragmentCommunicationViewModel mCrossVM;
//
//
//
//public interface CustomListItemClickListener {
//    void customOnListItemClick(int position);
//    void customOnListItemLongClick(int position);
//}
//
//    public TransactionAdapter(Context context,
//                              TransactionAdapter.CustomListItemClickListener listener,
//                              CrossFragmentCommunicationViewModel inputVM
//    ){
//        mContext = context;
//        mOnClickListener = listener;
//        this.mSelectedItems = new SparseBooleanArray();
//        mCrossVM = inputVM;
//    }
//
//
//
//
//    public void setData(List<TransactionEntry> inputEntries) {
//        mTransactionEntryList = inputEntries;
//        notifyDataSetChanged();
//
//    }
//
//    public void switchSelectedState(int position) {
//        mCrossVM.switchSelectedState(position);
//        Log.d(TAG, "switchSelectedState:  --------->"+ position);
//        notifyItemChanged(position);
//    }
//
//
//    public void selectAll(){
////        for(int i =0;i<mTransactionEntryList.size();i++){
////            if(!mSelectedItems.get(i)){
////                mSelectedItems.put(i,true);
////            }
////        }
//        mCrossVM.selectAllItems(mTransactionEntryList.size());
//        Log.d(TAG, "selectAll:  --------->"+ mSelectedItems.size());
//        notifyDataSetChanged();
//    }
//
//
//    public boolean getIsToolBarInAction() {
//
////        return mIsInChoiceMode;
//        return mCrossVM.getActionModeState().getValue();
//    }
//
//    public void setIsToolBarInAction(boolean isInChoiceMode) {
//        mCrossVM.setActionMode(isInChoiceMode);
//        mIsInChoiceMode = isInChoiceMode;
//    }
//
//    public List<TransactionEntry> getSelectedTransactions(){
//        List<Integer> selectedNumbers = getSelectedItems();
//        List<TransactionEntry> output = new ArrayList<>(selectedNumbers.size());
//
//        for(Integer i : selectedNumbers){
//            output.add(mTransactionEntryList.get(i));
//        }
//
//        return output;
//    }
//
//    public List<Integer> getSelectedItems() {
////        List<Integer> items = new ArrayList<>(mSelectedItems.size());
////        for (int i = 0; i < mSelectedItems.size(); ++i) {
////            items.add(mSelectedItems.keyAt(i));
////        }
////        Log.d(TAG, "getSelectedItems:  --------->"+ mSelectedItems.size());
//
//
//        //todo, change from List<Integer> to SparseBooleanArray later
//
//
//        List<Integer> items = new ArrayList<>(mCrossVM.getSelectedItems().size());
//        for (int i = 0; i < mCrossVM.getSelectedItems().size(); ++i) {
//            items.add(mCrossVM.getSelectedItems().keyAt(i));
//        }
//        return items;
//    }
//
//    public void clearSelectedState() {
//        List<Integer> selection = getSelectedItems();
////        mSelectedItems.clear();
//        mCrossVM.getSelectedItems().clear();
//        Log.d(TAG, "getSelectedItems:  --------->"+ mCrossVM.getSelectedItems().size());
//        for (Integer i : selection) {
//            notifyItemChanged(i);
//        }
//    }
//
//    public TransactionEntry getClickedOne(int input){
//        return mTransactionEntryList.get(input);
//    }
//
//    public int getSelectedItemCount() {
////        return mSelectedItems.size();
//        return mCrossVM.getSelectedItems().size();
//    }
//
//    @NonNull
//    @Override
//    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View transactionListView = LayoutInflater
//                .from(mContext)
//                .inflate(R.layout.list_item_transaction, parent, false);
//
//        return new TransactionAdapter.TransactionViewHolder(transactionListView);
//
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int position) {
//
//        TransactionEntry currentRecord= mTransactionEntryList.get(position);
//
//
//
//        holder.time.setText(String.valueOf(currentRecord.getTime()));
//        holder.amount.setText(UnitUtil.moneyFormater.format(currentRecord.getAmount()));
//        holder.category.setText(currentRecord.getCategory());
//        holder.remark.setText(currentRecord.getRemark());
//
//        if(mCrossVM.getActionModeState().getValue()){
//            holder.amount.setBackgroundColor(mContext.getResources().getColor(R.color.color_money_out));
//            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setChecked(mCrossVM.getSelectedItems().get(position));
//        }else{
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
//public class TransactionViewHolder extends RecyclerView.ViewHolder
//        implements  View.OnClickListener, View.OnLongClickListener {
//    TextView time;
//    TextView amount;
//    TextView category;
//    TextView remark;
//    CheckBox checkBox;
//
//
//
//    public TransactionViewHolder(@NonNull View itemView) {
//        super(itemView);
//
//        time = itemView.findViewById(R.id.transaction_time);
//        amount = itemView.findViewById(R.id.transaction_amount);
//        category = itemView.findViewById(R.id.transaction_category);
//        remark = itemView.findViewById(R.id.transaction_remark);
//
//        checkBox = itemView.findViewById(R.id.transaction_checkbox);
//        checkBox.setVisibility(View.GONE);
//
//        itemView.setOnClickListener(this);
//        itemView.setOnLongClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        int position = getAdapterPosition();
//
//        if(mIsInChoiceMode){
//            if(checkBox.isChecked()){
//                checkBox.setChecked(false);
//            }else{
//                checkBox.setChecked(true);
//            }
//        }
//
//        mOnClickListener.customOnListItemClick(position);
//    }
//
//    @Override
//    public boolean onLongClick(View view) {
//        Log.d("testlongclick", "onLongClick: get signal adapter");
////            int position = getAdapterPosition();
////            mOnClickListener.customOnListItemLongClick(position);
////            return false;
//        if(!mCrossVM.getActionModeState().getValue()){
//
//            clearSelectedState();
//            mCrossVM.setActionMode(true);
//
//        }
//        //todo, handle while action mode, user click to another fragment.
//        return false;
//    }
//}
//}
