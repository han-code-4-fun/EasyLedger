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
import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;


/*
 *  Recyclerview Fragment that can be used by other classes to show a standard list
 *
 */
public class DetailTransactionFragment extends Fragment
        implements TransactionAdapter.CustomListItemClickListener {

    private static final String TAG = DetailTransactionFragment.class.getSimpleName();

    private TransactionDBViewModel mViewModel;
    private OverviewFragmentViewModel mOverviewViewModel;

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


        mViewModel = ViewModelProviders.of(appCompatActivity).get(TransactionDBViewModel.class);
        mViewModel.setCurrentLedger(whoCalledMe);


        mOverviewViewModel = ViewModelProviders.of(appCompatActivity).get(OverviewFragmentViewModel.class);

        setActionModeToFalse();

        View rootView = inflater.inflate(R.layout.fragment_detail_transaction, container, false);

        mAdapter = TransactionAdapter.getInstance(this, mViewModel);

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
    public void customOnListItemClick(int position) {
        //todo, open a transaction editing activity/fragment
        TransactionEntry transactionEntry = mAdapter.getClickedEntry(position);
        Log.d(TAG, "clicked " + position + " -> " + transactionEntry.getRemark());
    }


    private void setActionModeToFalse() {
        boolean isActionMode = mViewModel.getActionModeState().getValue();
        if (isActionMode) {
            mViewModel.setActionModeState(false);
        }
    }

    private void setupViewModelObserver() {

        //todo, 2nd stage, handle different data for different ledgers

        if(whoCalledMe.equals(Constant.CALLFROMLEDGER)){
            if(mViewModel.getUntaggedTransactions().hasActiveObservers()){
                Log.d("test_flow2", "has active observer, remove them ");

                mViewModel.getUntaggedTransactions().removeObservers(getViewLifecycleOwner());
            }
        }

        mViewModel.getUntaggedTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntries) {

                if (whoCalledMe.equals(Constant.CALLFROMOVERVIEW)) {
                    Log.d("test_flow2", "observer: calling untagged");
                    Log.d("test_flow2", "observer: hash "+ hash);
                    mAdapter.setAdapterData(transactionEntries);
                }

            }
        });
        mViewModel.getAllTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntries) {
                if (whoCalledMe.equals(Constant.CALLFROMLEDGER)) {

                    Log.d("test_flow2", "observer calling alltransacitons");
                    Log.d("test_flow2", "observer: hash "+ hash);
                    mAdapter.setAdapterData(transactionEntries);
                }
            }
        });


        mViewModel.getActionModeState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mAdapter.isInActionMode = aBoolean;
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
        mViewModel.getmDeselectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mAdapter.deselectAll();
                    mViewModel.setDeselectAllTrigger(false);
                }
            }
        });

        //trigger that react to user's click from the action bar
        mViewModel.getmSelectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mAdapter.selectAll();
                    mViewModel.setSelectAllTrigger(false);
                }
            }
        });
    }

}
