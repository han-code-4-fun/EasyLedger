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
import hanzhou.easyledger.data.RepositoryDB;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewadapter.TransactionAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.GeneralViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;


/*
 *  Recyclerview Fragment that can be used by other classes to show a standard list
 *
 */
public class DetailTransactionFragment extends Fragment {

    private static final String TAG = DetailTransactionFragment.class.getSimpleName();

    private static final String INPUT = "user_in_put";

    private static final String PARENT_NAME = "who_called_current_frag";

    private AdapterNActionBarViewModel mAdapterActionViewModel;

    private TransactionAdapter mAdapter;

    private AppCompatActivity mAppCompatActivity;

    private String mInput;

    private String mLedgerName;

    private String mVisibleLedger;


    public static DetailTransactionFragment newInstance(String ledger) {

        DetailTransactionFragment fragment = new DetailTransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INPUT, ledger);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mLedgerName = getArguments().getString(INPUT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mAdapterActionViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_detail_transaction, container, false);

        mAdapter = new TransactionAdapter(mAdapterActionViewModel,mAppCompatActivity);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview_detail_transaction);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);

        setupViewModelObserver();
        return rootView;
    }


    private void setupViewModelObserver() {
        final TransactionDBViewModel mTransactionViewModel =
                ViewModelProviders.of(mAppCompatActivity).get(TransactionDBViewModel.class);

        GeneralViewModel mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);

        mGeneralViewModel.getmCurrentLedger().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mVisibleLedger =s ;
            }
        });


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
                /*set to default style when the 'false' state is updated from user's interactino with toolbar*/
                if (!aBoolean) {
                    mAdapter.deselectAll();
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


        /*trigger that react to user's click from the action bar*/
        mAdapterActionViewModel.getmDeselectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mAdapter.deselectAll();
                    mAdapterActionViewModel.setDeselectAllTrigger(false);
                }
            }
        });

        /*trigger that react to user's click from the action bar*/
        mAdapterActionViewModel.getmSelectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mAdapter.selectAll();
                    mAdapterActionViewModel.setSelectAllTrigger(false);
                }
            }
        });



        mAdapterActionViewModel.getmCategorizeItemsToOthersTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            TransactionDB.getInstance(mAppCompatActivity).transactionDAO().updateListOfTransactions(
                                    mAdapterActionViewModel.categorizeSelectedItemsToOthers(mAdapter.getAdateperData())
                            );
                        }
                    });

                    mAdapterActionViewModel.setmCategorizeItemsToOthersTrigger(false);
                }
            }
        });
    }



}
