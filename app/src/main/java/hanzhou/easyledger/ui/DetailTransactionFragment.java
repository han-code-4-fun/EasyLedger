package hanzhou.easyledger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewadapter.TransactionAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;


/*
 *  Recyclerview Fragment that can be used by other classes to show a standard list
 *
 */
public class DetailTransactionFragment extends Fragment{

    private static final String TAG = DetailTransactionFragment.class.getSimpleName();

    private TransactionDBViewModel mTransactionViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;

    private TransactionAdapter mAdapter;

    private String whoCalledMe;

    private AppCompatActivity appCompatActivity;

    private int hash;

    public DetailTransactionFragment(String parentString) {

        whoCalledMe = parentString;
        hash = hashCode();
        Log.d("test_flow2", " constructor: hash "+whoCalledMe+" "+ hash);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity=  (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        appCompatActivity=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test_flow2", "onDestroy: " +whoCalledMe + " "+hash);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //todo, implement data PERSISTANCE
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mAdapterActionViewModel = ViewModelProviders.of(appCompatActivity).get(AdapterNActionBarViewModel.class);
        mAdapterActionViewModel.setCurrentLedger(whoCalledMe);





        setActionModeToFalse();

        View rootView = inflater.inflate(R.layout.fragment_detail_transaction, container, false);

//        mAdapter = new TransactionAdapter(this, mAdapterActionViewModel);
        mAdapter = new TransactionAdapter( mAdapterActionViewModel);


        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview_detail_transaction);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);



        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModelObserver();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapterActionViewModel.setmIsInBaseFragment(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapterActionViewModel.setmIsInBaseFragment(false);

    }
//    @Override
//    public void customOnListItemClick(int position) {
//        //todo, open a transaction editing activity/fragment
//        int transactionEntryID = position;
//        Log.d("test_flow3", "Entry id is -> " + transactionEntryID);
//    }


    private void setActionModeToFalse() {
        boolean isActionMode = mAdapterActionViewModel.getActionModeState().getValue();
        if (isActionMode) {
            mAdapterActionViewModel.setActionModeState(false);
        }
    }

    private void setupViewModelObserver() {

        //todo, 2nd stage, handle different data for different ledgers

        if(mAdapterActionViewModel.getCurrentLedger().equals(Constant.FRAG_CALL_FROM_OVERVIEW)){
            OverviewFragmentViewModel mOverviewVM = ViewModelProviders.of(appCompatActivity).get(OverviewFragmentViewModel.class);
            mOverviewVM.getUntaggedTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
                @Override
                public void onChanged(List<TransactionEntry> transactionEntryList) {
                    mAdapter.setAdapterData(transactionEntryList);
                }
            });
        }else{
            TransactionDBViewModel mTransVM = ViewModelProviders.of(appCompatActivity).get(TransactionDBViewModel.class);
            mTransVM.getAllTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
                @Override
                public void onChanged(List<TransactionEntry> transactionEntryList) {
                    mAdapter.setAdapterData(transactionEntryList);
                }
            });
        }

        mAdapterActionViewModel.getActionModeState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mAdapter.setInActionMode(aBoolean);
                //set to default style when the 'false' state is updated from user's input of action bar
                if (!aBoolean) {
                    mAdapter.deselectAll();
                } else {
                    //todo, check if error
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


        //trigger that react to user's click from the action bar
        mAdapterActionViewModel.getmDeselectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mAdapter.deselectAll();
                    mAdapterActionViewModel.setDeselectAllTrigger(false);
                }
            }
        });

        //trigger that react to user's click from the action bar
        mAdapterActionViewModel.getmSelectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mAdapter.selectAll();
                    mAdapterActionViewModel.setSelectAllTrigger(false);
                }
            }
        });
    }

}
