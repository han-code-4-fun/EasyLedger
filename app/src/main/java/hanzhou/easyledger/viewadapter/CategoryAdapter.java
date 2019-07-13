package hanzhou.easyledger.viewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;

public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> mCategories;
    private Context mContext;
    private AdapterNActionBarViewModel mViewModel;

    private int mActivePosition;


    public CategoryAdapter(Context context , AdapterNActionBarViewModel mInputVM) {
        mContext = context;
        mViewModel = mInputVM;
        mCategories = new ArrayList<>();
        mActivePosition = -1;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_category,parent, false);

        return new CategoryViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String currentCategory = mCategories.get(position);
        holder.categoryTV.setText(currentCategory);
        if(position == mActivePosition){
            holder.categoryTV.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }else{

            holder.categoryTV.setTextColor(ContextCompat.getColor(mContext, R.color.design_default_color_primary));
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public void setData(List<String> inputList){
        mActivePosition = -1;
        mCategories = inputList;
        notifyDataSetChanged();
    }

    public int getmActivePosition() {
        return mActivePosition;
    }

    public void setmActivePosition(int mActivePosition) {
        this.mActivePosition = mActivePosition;
    }

    public String getClickedCategory(){
        String output="";
        output = mCategories.get(mActivePosition);
        return output;
    }

    public void highlightExistingCategoryIfMatch(String inputCategory){
        int position = mCategories.indexOf(inputCategory);
        if(position!= -1){
            mActivePosition = position;
            notifyItemChanged(mActivePosition);
        }

    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {
        TextView categoryTV;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.list_item_tv_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mActivePosition != -1){
                int previousPosition = mActivePosition;
                int position = getAdapterPosition();
                mActivePosition = position;
                notifyItemChanged(previousPosition);
                notifyItemChanged(position);
            }else{

                mActivePosition = getAdapterPosition();
                notifyItemChanged(mActivePosition);

            }
            mViewModel.setmSelectedCategory(mCategories.get(mActivePosition));
        }
    }
}
