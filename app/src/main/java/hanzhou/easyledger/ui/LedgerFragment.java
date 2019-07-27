package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    private AppCompatActivity appCompatActivity;
    private TransactionDBViewModel viewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;
    private SharedPreferences mSharedPreferences;
    private ViewPager viewPager;

    private GsonHelper mGsonHelper;
    private LedgersAdapter mLedgersAdapter;


//    private Toolbar toolBar;
//    private TextView textViewOnToolBar;

    private ArrayList<String> mLedgersList;

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
        mGsonHelper = GsonHelper.getInstance();
        mGsonHelper.setmSharedPreferences(mSharedPreferences);


        mLedgersList = mGsonHelper.getLedgers(Constant.LEDGERS);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("test_life", "onCreate: ______________ledger frag");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_ledger, container, false);
//        mGsonHelper.setmSharedPreferences(mSharedPreferences);
        /*
            refresh the whole LedgerFragment to avoid strange viewpager title and data mismatch
            behaviour after crazily add/delete ledgers
            this will trigger the viewmodel in MainActivity to refresh current LedgerFragment
        */
//        if (!mLedgersList.equals(mGsonHelper.getLedgers(Constant.LEDGERS))) {
//            mLedgersList = mGsonHelper.getLedgers(Constant.LEDGERS);
//            final SettingsViewModel settingsViewModel = ViewModelProviders.of(appCompatActivity).get(SettingsViewModel.class);
//            settingsViewModel.setmRefreshLedgerFragmentTrigger(true);
//
//        }else{

            Log.d("test_life", "onCreateView: ________________________ledger frag");


            TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

            viewPager = rootView.findViewById(R.id.transaction_viewpager);

            mLedgersAdapter = new LedgersAdapter(getChildFragmentManager(), mLedgersList);

            viewPager.setAdapter(mLedgersAdapter);

            viewPager.addOnPageChangeListener(listener);

            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        }

        return rootView;



    }
    private ViewPager.SimpleOnPageChangeListener listener =  new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            Log.i("test_222", "page selected " + position);
            currentPage = position;
        }
    };



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("test_life", "onActivityCreated: ____________ledger frag");
        setupViewModel();
        String temp = "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("test_life", "onDestroyView: ____________Ledger Frag");
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapterActionViewModel.setmIsInBaseFragment(true);
    }

    private void setupViewModel() {
        mAdapterActionViewModel = ViewModelProviders.of(appCompatActivity).get(AdapterNActionBarViewModel.class);
        mLedgersAdapter.setViewModel(mAdapterActionViewModel);
        SPViewModelFactory factory = new SPViewModelFactory(mSharedPreferences);
        SPViewModel spViewModel = ViewModelProviders.of(appCompatActivity,factory).get(SPViewModel.class);

        spViewModel.getmLedgersList().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ArrayList<String> temp = mGsonHelper.convertJsonToArrayListString(s);
                if(!mLedgersList.equals(temp)){

                    mLedgersList = mGsonHelper.convertJsonToArrayListString(s);
                    mLedgersAdapter.setmLedgers(mLedgersList);
                    final SettingsViewModel settingsViewModel = ViewModelProviders.of(appCompatActivity).get(SettingsViewModel.class);
                    settingsViewModel.setmRefreshLedgerFragmentTrigger(true);


                }
            }
        });
    }


}


