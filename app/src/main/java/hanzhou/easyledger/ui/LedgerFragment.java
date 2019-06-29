package hanzhou.easyledger.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import hanzhou.easyledger.R;
import hanzhou.easyledger.LedgersAdapter;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.temp.TestTempActivity;
import hanzhou.easyledger.utility.BackPressHandler;
import hanzhou.easyledger.utility.HelperUtilMoveToSharedPreferenceLater;
/*
*   Fragment that shows detailed transactions based on number of 'ledger'
*   that user created, by default there is one ledger
*
* */
public class LedgerFragment extends Fragment{

    //todo
    private static int NUMBER_OF_LEDGERS;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        NUMBER_OF_LEDGERS = HelperUtilMoveToSharedPreferenceLater.getNumberOfTransactionTables();

        final View rootView =inflater.inflate(R.layout.fragment_ledger, container,false);

        final TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

        ViewPager viewPager = rootView.findViewById(R.id.transaction_viewpager);

        LedgersAdapter ledgersAdapter = new LedgersAdapter(getChildFragmentManager(), NUMBER_OF_LEDGERS);

        viewPager.setAdapter(ledgersAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setMinimumWidth(250);

        return rootView;
    }





}
