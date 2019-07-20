package hanzhou.easyledger.viewadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import hanzhou.easyledger.R;
import hanzhou.easyledger.ui.settings.SettingEditLedger;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {

    private ArrayList<String> mList;
    private Context mContext;

    public SettingAdapter(ArrayList<String> mList, Context inputContext) {
        this.mList = mList;
        mContext = inputContext;
    }

    public void setData(ArrayList<String> inputList){
        mList = inputList;
        notifyDataSetChanged();
    }

    public ArrayList<String> getData(){
        return mList;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_setting, parent, false);

        return new SettingViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        holder.textView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public boolean isCurrentCategoryOthers(int position){
        return mList.get(position).equals("Others");
    }

    public void swapPosition(int positionFrom, int positionTo){
        Collections.swap(mList, positionFrom, positionTo);
        notifyItemMoved(positionFrom, positionTo);

//        String temp = "";
//        for (String x :
//                mList) {
//            temp +=x;
//        }
//        Log.d("test_setting", "after swapPosition: "+ temp);
    }

    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
//        String temp = "";
//        for (String x :
//                mList) {
//            temp +=x;
//        }
//        Log.d("test_setting", "after remove: "+ temp);

    }


    public class SettingViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SettingViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.setting_list_data_tv);

        }
    }
}