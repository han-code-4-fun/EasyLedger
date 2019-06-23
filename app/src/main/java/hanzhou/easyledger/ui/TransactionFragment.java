package hanzhou.easyledger.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import hanzhou.easyledger.R;
import hanzhou.easyledger.TransactionsAdapter;

public class TransactionFragment extends Fragment {

    //todo
    public static int NUMBER_OF_LEDGERS = 6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        final View rootView =inflater.inflate(R.layout.fragment_transaction, container,false);

        final TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

        ViewPager viewPager = rootView.findViewById(R.id.transaction_viewpager);

        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getFragmentManager(),NUMBER_OF_LEDGERS);

        viewPager.setAdapter(transactionsAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setMinimumWidth(250);

        return rootView;
    }

}
