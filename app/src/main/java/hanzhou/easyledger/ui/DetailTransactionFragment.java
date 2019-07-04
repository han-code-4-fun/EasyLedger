package hanzhou.easyledger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.TransactionDBVMFactory;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

public  class DetailTransactionFragment extends Fragment
        implements TransactionAdapter.CustomListItemClickListener {

    private static final String TAG = DetailTransactionFragment.class.getSimpleName();

    private TransactionDBViewModel mViewModel;

    private TransactionAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private Toolbar toolBar;

    private TextView textViewOnToolBar;

    private TransactionDB mDb;

    private TransactionDBVMFactory factory;

    private boolean isActionModeLocal;

    private String whoCalledMe;

    public DetailTransactionFragment(String parentString) {
        whoCalledMe = parentString;
    }

    //1st step of starting Fragment
    //create DB instance
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDb = TransactionDB.getInstance(context);
        Log.d("test_hash", " DetailTransaction fragment attached "+ this.hashCode());


    }

    //enable onCreateOptionsMenu, save AppCompatActivity to local variable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("frg_flow", "onCreate: -5-5-5-5-5-5-5-");
    }

    //todo, save data into instance
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //setup layout
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("frg_flow", "onCreateView: -4-4-4-4-4-");

//        toolBar = getActivity().findViewById(R.id.toolbar_layout);
//
//        textViewOnToolBar = getActivity().findViewById(R.id.toolbar_textview);
//        textViewOnToolBar.setVisibility(View.GONE);

        View rootView = inflater.inflate(R.layout.fragment_detail_transaction, container, false);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);



        mViewModel = ViewModelProviders.of(getActivity()).get(TransactionDBViewModel.class);

        boolean temp  = mViewModel.getActionModeState().getValue();
        if(temp){
            mViewModel.setActionModeState(false);
        }
        //todo, if error, try to change from appCompatActivity to getActivity()
        mAdapter = TransactionAdapter.getInstance(getActivity(), this, mViewModel);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_detail_transaction);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }


    //prepare factory and viewmodel
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Log.d("frg_flow", "onActivityCreated: -3-3-3-3-3-3");

        //todo, implement tag for different ledgers


        if(whoCalledMe.equals(Constant.CALLFROMOVERVIEW)){
            //todo, 1st stage, handle parent from different Fragment to perform (UNTAGGED or all transactions)
            //todo, 2nd stage, handle different data for different ledgers
//            factory = new TransactionDBVMFactory(mDb, Constant.UNTAGGED);
//
//            mViewModel = ViewModelProviders.of(getActivity(), factory).get(TransactionDBViewModel.class);



            mViewModel.getUntaggedTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
                @Override
                public void onChanged(List<TransactionEntry> transactionEntries) {
                    mAdapter.setData(transactionEntries);
                    Log.d(Constant.TESTFLOW+TAG, "getUntaggedTransactions total -> "+ transactionEntries.size());
                    //      mCrossVM.setTransactionEntriesFromDB(transactionEntries);
                }
            });


        }else{
//            factory = new TransactionDBVMFactory(mDb, Constant.ALL_RECORDS);
//
//            mViewModel = ViewModelProviders.of(getActivity(), factory).get(TransactionDBViewModel.class);

            mViewModel.getAllTransactions().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
                @Override
                public void onChanged(List<TransactionEntry> transactionEntries) {
                    mAdapter.setData(transactionEntries);
                    Log.d(Constant.TESTFLOW+TAG, "getAllTransactions total -> "+ transactionEntries.size());
                }
            });

        }
        mViewModel.setCurrentLedger(whoCalledMe);

//        mViewModel.getActionModeState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                isActionModeLocal = aBoolean;
//            }
//        });


        mViewModel.getActionModeState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mAdapter.isInActionMode = aBoolean;
                Log.d("test_hash", "tesettestest       "+aBoolean);
                //set to default style when the 'false' state is updated from user's input of action bar
                if(!aBoolean){
                    mAdapter.deselectAll();
                }else {
                    //todo, check if error
                    //todo
                    //todo
                    Log.d("test_hash", "tesettestest       true");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


        //trigger that react to user's click from the action bar
        mViewModel.getmDeselectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    mAdapter.deselectAll();
                    Log.d(Constant.TESTFLOW+TAG, "viewmodel observer, deselected all and now set DeselectAllTrigger to false");
                    mViewModel.setDeselectAllTrigger(false);
                }
            }
        });

        //trigger that react to user's click from the action bar
        mViewModel.getmSelectAllTrigger().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    mAdapter.selectAll();
                    Log.d(Constant.TESTFLOW+TAG, "viewmodel observer, selected all and now set SelectAllTrigger to false");
                    mViewModel.setSelectAllTrigger(false);
                }
            }
        });

    }


    @Override
    public void customOnListItemClick(int position) {
//        if (isActionModeLocal) {
//
////            mViewModel.updateSelectedItemsArray(position);
//
//        } else {
        TransactionEntry transactionEntry = mAdapter.getClickedData(position);
        Toast.makeText(
                this.getActivity(),
                "clicked " + position + " -> " + transactionEntry.getRemark(),
                Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void customOnListItemLongClick(int position) {
        //handle toolbar action mode
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("frg_flow", "onStart: -2-2-2-2-2-2-");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("frg_flow", "onResume: -1-1-1-1-1-1-1-");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("frg_flow", "onPause: 11111");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("frg_flow", "onStop: 222222");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("frg_flow", "onDestroyView: 33333333");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("frg_flow", "onDestroy: 444444");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d("test_hash", "DetailTransaction fragment detached "+ this.hashCode());
    }
}