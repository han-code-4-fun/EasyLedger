package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.GsonHelper;
import hanzhou.easyledger.viewadapter.LedgersAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.GeneralViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModelFactory;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

/*
 *   Fragment that shows detailed transactions based on number of 'ledger'
 *   that user created, by default there is one ledger
 *
 * */
public class LedgerFragment extends Fragment {

    private static final String TAG = LedgerFragment.class.getSimpleName();
    private int currentPage;

    private AppCompatActivity mAppCompatActivity;

    private GeneralViewModel mGeneralViewModel;
    private TransactionDBViewModel mTransactionDBViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;
    private SharedPreferences mSharedPreferences;
    private ViewPager viewPager;

    private GsonHelper mGsonHelper;
    private LedgersAdapter mLedgersAdapter;


    private ArrayList<String> mLedgersList;

    private boolean isInActionModel;
    private boolean isAllSelected;

    private int selectedNum;

    private MenuItem delete;
    private MenuItem edit;
    private MenuItem selectAll;
    private MenuItem ignore;


    private String mCurrentVisiblePage;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mAppCompatActivity = (AppCompatActivity) context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAppCompatActivity);
        mGsonHelper = GsonHelper.getInstance();
        mGsonHelper.setmSharedPreferences(mSharedPreferences);


        mLedgersList = mGsonHelper.getLedgers(Constant.LEDGERS);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_ledger, container, false);


        TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

        viewPager = rootView.findViewById(R.id.transaction_viewpager);

        mLedgersAdapter = new LedgersAdapter(getChildFragmentManager(), mLedgersList);


        viewPager.setAdapter(mLedgersAdapter);

        viewPager.addOnPageChangeListener(listener);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        return rootView;


    }

    private ViewPager.SimpleOnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mGeneralViewModel.setmCurrentLedger(mLedgersList.get(position));
            mGeneralViewModel.setmIsSwitchViewPager(true);
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }


    private void setupViewModel() {

        mTransactionDBViewModel = ViewModelProviders.of(mAppCompatActivity).get(TransactionDBViewModel.class);

        mLedgersAdapter.setViewModel(mTransactionDBViewModel);


        mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_LEDGER);

        mAdapterActionViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);


        SPViewModelFactory factory = new SPViewModelFactory(mSharedPreferences);
        SPViewModel spViewModel = ViewModelProviders.of(mAppCompatActivity, factory).get(SPViewModel.class);

        spViewModel.getmLedgersList().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                ArrayList<String> temp = mGsonHelper.convertJsonToArrayListString(s);
                if (!mLedgersList.equals(temp)) {
                    mLedgersList = temp;
                    final SettingsViewModel settingsViewModel = ViewModelProviders.of(mAppCompatActivity).get(SettingsViewModel.class);
                    settingsViewModel.setmRefreshLedgerFragmentTrigger(true);


                }
            }
        });
    }


}


