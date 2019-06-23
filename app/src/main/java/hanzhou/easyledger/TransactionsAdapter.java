package hanzhou.easyledger;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import hanzhou.easyledger.ui.DetailTransactionFragment;

public class TransactionsAdapter extends FragmentPagerAdapter {
    //todo  the 'numberOfLedgers' has relationship with ledger tables in database
    private static int numberOfLedgers = 0;

    public TransactionsAdapter(FragmentManager fm, int input) {
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
