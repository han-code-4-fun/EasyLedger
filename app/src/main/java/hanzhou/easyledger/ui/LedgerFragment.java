package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.GsonHelper;
import hanzhou.easyledger.viewadapter.LedgersAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

/*
 *   Fragment that shows detailed transactions based on number of 'ledger'
 *   that user created, by default there is one ledger
 *
 * */
public class LedgerFragment extends Fragment {

    private static final String TAG = LedgerFragment.class.getSimpleName();


    private AppCompatActivity appCompatActivity;
    private TransactionDBViewModel viewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;
    private SharedPreferences mSharedPreferences;


    private LedgersAdapter ledgersAdapter;
    private GsonHelper mGsonHelper;

    private ArrayList<String> mLedgersList;
//    private Toolbar toolBar;
//    private TextView textViewOnToolBar;

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

        appCompatActivity = (AppCompatActivity) context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(appCompatActivity);
        mGsonHelper = new GsonHelper(appCompatActivity);

        //todo, user viewmodel to hold this list
        mLedgersList =mGsonHelper.getLedgers(Constant.LEDGERS);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_ledger, container, false);

        final TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

        ViewPager viewPager = rootView.findViewById(R.id.transaction_viewpager);


        //todo, watch out, if add to back stack, will not populate this if
        ledgersAdapter = new LedgersAdapter(getChildFragmentManager(), mLedgersList);
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
        setupViewModel();
    }



    @Override
    public void onResume() {
        super.onResume();
        mAdapterActionViewModel.setmIsInBaseFragment(true);
    }

    private void setupViewModel() {
        mAdapterActionViewModel = ViewModelProviders.of(appCompatActivity).get(AdapterNActionBarViewModel.class);
    }


}


