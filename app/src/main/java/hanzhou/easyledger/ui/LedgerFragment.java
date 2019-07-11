package hanzhou.easyledger.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewadapter.LedgersAdapter;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

/*
 *   Fragment that shows detailed transactions based on number of 'ledger'
 *   that user created, by default there is one ledger
 *
 * */
public class LedgerFragment extends Fragment {

    private static final String TAG = LedgerFragment.class.getSimpleName();

    //todo
    private TransactionDB mDb;
    private AppCompatActivity appCompatActivity;
    private TransactionDBViewModel viewModel;


    private LedgersAdapter ledgersAdapter;
    private Toolbar toolBar;
    private TextView textViewOnToolBar;

    private boolean isInActionModel;
    private boolean isAllSelected;

    private int selectedNum;

    private MenuItem delete;
    private MenuItem edit;
    private MenuItem selectAll;
    private MenuItem ignore;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(Constant.TESTFLOW+TAG, "onAttach: AAAAAAAAAAAA  "+this.hashCode());

        mDb = TransactionDB.getInstance(context);
        appCompatActivity = (AppCompatActivity) getActivity();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(Constant.TESTFLOW+TAG, "onDetach: AAAAAAAAAAAA  "+this.hashCode());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
        toolBar = appCompatActivity.findViewById(R.id.toolbar_layout);


        textViewOnToolBar = appCompatActivity.findViewById(R.id.toolbar_textview);
//        textViewOnToolBar.setVisibility(View.GONE);


//        NUMBER_OF_LEDGERS = HelperUtilMoveToSharedPreferenceLater.getNumberOfTransactionTables();

        final View rootView = inflater.inflate(R.layout.fragment_ledger, container, false);

        final TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

        ViewPager viewPager = rootView.findViewById(R.id.transaction_viewpager);


        //todo, watch out, if add to back stack, will not populate this if
        ledgersAdapter = new LedgersAdapter(getChildFragmentManager(), this);
        //todo,  track if there is change while user fragment is not in forground and user add/remove ledgers

        viewPager.setAdapter(ledgersAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setMinimumWidth(250);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setupViewModel();
    }


}


