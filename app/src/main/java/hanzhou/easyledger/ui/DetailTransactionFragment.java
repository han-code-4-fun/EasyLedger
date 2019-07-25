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
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.viewadapter.TransactionAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;


/*
 *  Recyclerview Fragment that can be used by other classes to show a standard list
 *
 */
public class DetailTransactionFragment extends Fragment {

    private static final String TAG = DetailTransactionFragment.class.getSimpleName();

    private static final String LEDGER = "ledger_to_show";

    private AdapterNActionBarViewModel mAdapterActionViewModel;

    private TransactionAdapter mAdapter;

    private AppCompatActivity appCompatActivity;

    private int hash;
    private String mLedgerName;

    public static DetailTransactionFragment newInstance(String ledgerTypeName) {
        Log.d("test_life", "detail frag  new instance   "+ledgerTypeName);

        DetailTransactionFragment fragment = new DetailTransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LEDGER, ledgerTypeName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("test_life", "onCreate: detail frag");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        if (getArguments() != null) {
            mLedgerName = getArguments().getString(LEDGER);
        }
        Log.d("test_life", "onCreateView: detail frag   -> "+mLedgerName);
        mAdapterActionViewModel = ViewModelProviders.of(appCompatActivity).get(AdapterNActionBarViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_detail_transaction, container, false);

        mAdapter = new TransactionAdapter(mAdapterActionViewModel);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview_detail_transaction);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("test_life", "onActivityCreated: detail frag");
        setupViewModelObserver();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        appCompatActivity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("test_life", "onDestroyView: detail Frag");

    }

    private void setActionModeToFalse() {
        boolean isActionMode = mAdapterActionViewModel.getActionModeState().getValue();
        if (isActionMode) {
            mAdapterActionViewModel.setActionModeState(false);
        }
    }

    private void setupViewModelObserver() {

        mAdapterActionViewModel.setCurrentLedger(mLedgerName);

        TransactionDBViewModel mTransactionViewModel = ViewModelProviders.of(appCompatActivity).get(TransactionDBViewModel.class);
        mTransactionViewModel.updateTransactionOnUserInput(mLedgerName);


        mTransactionViewModel.getTransactionsByLedger().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                mAdapter.setAdapterData(transactionEntryList);
            }
        });


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

        mAdapterActionViewModel.getmDeleteItemTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            TransactionDB.getInstance(getContext()).transactionDAO().deleteListOfTransactions(
                                    mAdapterActionViewModel.getSelectedTransactions(mAdapter.getmTransactionEntryList())
                            );
                        }
                    });

                    mAdapterActionViewModel.setmDeleteItemTrigger(false);
                }
            }
        });

        mAdapterActionViewModel.getmEditAnEntryTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){

                    mAdapterActionViewModel.setmClickedEntryID(mAdapter.getOneSelectedEntryID());

                    mAdapterActionViewModel.setmEditAnEntryTrigger(false);
                }
            }
        });
    }

}
