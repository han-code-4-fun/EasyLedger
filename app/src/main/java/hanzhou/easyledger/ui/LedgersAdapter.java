package hanzhou.easyledger.ui;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.ui.DetailTransactionFragment;
import hanzhou.easyledger.utility.Constant;

public class LedgersAdapter extends FragmentPagerAdapter {

    private Fragment fragment;

    //todo  the 'numberOfLedgers' has relationship with ledger in database
    private static int numberOfLedgers = 0;

    private static LedgersAdapter sAdapterInstance;


    public LedgersAdapter(FragmentManager fm, Fragment fragment) {
        super(fm);
        this.fragment = fragment;
    }


    @Override
    public Fragment getItem(int position) {

        return new DetailTransactionFragment(Constant.CALLFROMLEDGER);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragment.getResources().getString(R.string.title_default_ledger);
    }

    @Override
    public int getCount() {
        return 1;
    }
}
