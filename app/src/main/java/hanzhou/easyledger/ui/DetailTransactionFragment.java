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

    private String mParentFragInput;


    public static DetailTransactionFragment newInstance(String inputCurrentScreen) {

        DetailTransactionFragment fragment = new DetailTransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARENT_NAME, inputCurrentScreen);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;

        Log.d("test_frag", "onAttach: ");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParentFragInput = getArguments().getString(PARENT_NAME);
        }
        Log.d("test_frag", "onCreate: " );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


//        if (getArguments() != null) {
//            mInput = getArguments().getString(INPUT);
//
//        }
        Log.d("test_frag", "onCreateView: " );
        mAdapterActionViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_detail_transaction, container, false);

        mAdapter = new TransactionAdapter(mAdapterActionViewModel);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview_detail_transaction);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);

        setupViewModelObserver();
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setupViewModelObserver();
        Log.d("test_frag", "onActivityCreated: " );

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("test_frag", "onStop: ");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("test_frag", "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test_frag", "onDestroy: " );
    }

    private void setupViewModelObserver() {
        final TransactionDBViewModel mTransactionViewModel =
                ViewModelProviders.of(mAppCompatActivity).get(TransactionDBViewModel.class);

        GeneralViewModel mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
//        mGeneralViewModel.getCurrentScreen().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                mParentFragInput =s;
//                if(mParentFragInput.equals(Constant.FRAG_NAME_OVERVIEW)){
//                    mTransactionViewModel.getUntaggedTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
//                        @Override
//                        public void onChanged(List<TransactionEntry> transactionEntryList) {
//                            mAdapter.setAdapterData(transactionEntryList);
//
//                        }
//                    });
//                }else if(mParentFragInput.equals(Constant.FRAG_NAME_LEDGER)){
//
//
//
//                    mTransactionViewModel.getListEntriesByLedger().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
//                        @Override
//                        public void onChanged(List<TransactionEntry> transactionEntryList) {
//                            mAdapter.setAdapterData(transactionEntryList);
//
//                        }
//                    });
//                }

//            }
//        });


        if(mParentFragInput.equals(Constant.FRAG_NAME_OVERVIEW)){
            mTransactionViewModel.getUntaggedTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
                @Override
                public void onChanged(List<TransactionEntry> transactionEntryList) {
                    mAdapter.setAdapterData(transactionEntryList);

                }
            });
        }else {


            mTransactionViewModel.getListEntriesByLedger().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
                @Override
                public void onChanged(List<TransactionEntry> transactionEntryList) {
                    mAdapter.setAdapterData(transactionEntryList);

                }
            });
        }



////        mTransactionViewModel.setTransactionListFromInputLedgerName(mInput);
////todo, in ledger frag and in overview fragm
//
//        mTransactionViewModel.getTransactionsByLedger().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
//            @Override
//            public void onChanged(List<TransactionEntry> transactionEntryList) {
//                mAdapter.setAdapterData(transactionEntryList);
//
//            }
//        });


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
                if (aBoolean) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            TransactionDB.getInstance(mAppCompatActivity).transactionDAO().deleteListOfTransactions(
                                    mAdapterActionViewModel.getSelectedTransactions(mTransactionViewModel.getAllTransactions().getValue())
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
                if (aBoolean) {

                    mAdapterActionViewModel.setmClickedEntryID(mAdapter.getOneSelectedEntryID());

                    mAdapterActionViewModel.setmEditAnEntryTrigger(false);
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
