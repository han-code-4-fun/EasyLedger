package hanzhou.easyledger;

import android.location.Location;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import hanzhou.easyledger.ui.DetailTransactionFragment;

public class LedgersAdapter extends FragmentPagerAdapter {
    //todo  the 'numberOfLedgers' has relationship with ledger tables in database
    private static int numberOfLedgers = 0;


    public LedgersAdapter(FragmentManager fm, int input) {
        super(fm);
        numberOfLedgers = input;
    }

    @Override
    public Fragment getItem(int position) {
        return new DetailTransactionFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "testTitle"+position;
    }

    @Override
    public int getCount() {
        return numberOfLedgers;
    }
}
