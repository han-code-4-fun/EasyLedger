package hanzhou.easyledger.viewadapter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.ArrayList;

import hanzhou.easyledger.R;
import hanzhou.easyledger.ui.DetailTransactionFragment;
import hanzhou.easyledger.utility.Constant;

public class LedgersAdapter extends FragmentPagerAdapter {

//    private Fragment fragment;

    //todo  the 'numberOfLedgers' has relationship with ledger in database
    private static int numberOfLedgers = 0;

    private static LedgersAdapter sAdapterInstance;

    private ArrayList<String> mLedgers;

    public LedgersAdapter(FragmentManager fm, /*Fragment fragment*/  ArrayList<String> ledgers) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        this.fragment = fragment;
        mLedgers=ledgers;
    }

    public void setmLedgers(ArrayList<String> input){
        mLedgers = input;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {

        String temp= "";
        for (String s :
                mLedgers) {
            temp += " "+s;
        }
        Log.d("test_life", "ledgers in ledger adapter" + temp);

        return DetailTransactionFragment.newInstance(mLedgers.get(position),Constant.FRAG_CALL_FROM_LEDGER);
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
