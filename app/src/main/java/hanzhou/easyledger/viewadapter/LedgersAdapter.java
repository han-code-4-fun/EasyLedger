package hanzhou.easyledger.viewadapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.ArrayList;

import hanzhou.easyledger.ui.DetailTransactionFragment;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.LedgerViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

public class LedgersAdapter extends FragmentPagerAdapter {

    private String mCurrentVisiblePage;

//    private Fragment fragment;

    //todo  the 'numberOfLedgers' has relationship with ledger in database
    private static int numberOfLedgers = 0;

    private static LedgersAdapter sAdapterInstance;

    private ArrayList<String> mLedgers;

    private TransactionDBViewModel mTransactionDBViewModel;



    public LedgersAdapter(FragmentManager fm, ArrayList<String> ledgers) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mLedgers=ledgers;
        mCurrentVisiblePage = "";
    }

    public void setmLedgers(ArrayList<String> input){
        mLedgers = input;
        notifyDataSetChanged();
    }

    public void setViewModel(TransactionDBViewModel inputVM ){
        mTransactionDBViewModel = inputVM;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);

//        if(!mLedgers.get(position).equals(mCurrentVisiblePage)){
//            mCurrentVisiblePage = mLedgers.get(position);
//            if(mLedgerViewModel!= null){
//
//                mLedgerViewModel.setmLedgerName(mCurrentVisiblePage);
//
//            }
//        }

    }



    @Override
    public Fragment getItem(int position) {

//        return DetailTransactionFragment.newInstance(Constant.FRAG_NAME_LEDGER);
//        mTransactionDBViewModel.setLedgerName(mLedgers.get(position));
        return DetailTransactionFragment.newInstance(mLedgers.get(position));

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mLedgers.get(position);
    }

    @Override
    public int getCount() {
        if(mLedgers == null)
            return 0;
        return mLedgers.size();
    }
}
