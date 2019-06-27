package hanzhou.easyledger.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import hanzhou.easyledger.R;
import hanzhou.easyledger.LedgersAdapter;
import hanzhou.easyledger.utility.DatabaseUtil;

public class LedgerFragment extends Fragment {

    //todo
    public static int NUMBER_OF_LEDGERS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NUMBER_OF_LEDGERS = DatabaseUtil.getNumberOfTransactionTables();

        final View rootView =inflater.inflate(R.layout.fragment_ledger, container,false);

        final TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

        ViewPager viewPager = rootView.findViewById(R.id.transaction_viewpager);

        LedgersAdapter ledgersAdapter = new LedgersAdapter(getFragmentManager(),NUMBER_OF_LEDGERS);

        viewPager.setAdapter(ledgersAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setMinimumWidth(250);

        return rootView;
    }

}
