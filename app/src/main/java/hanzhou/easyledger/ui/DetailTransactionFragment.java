package hanzhou.easyledger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.temp.TestTempActivity;
import hanzhou.easyledger.utility.BackPressHandler;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.CrossFragmentCommunicationViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBVMFactory;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

public class DetailTransactionFragment extends Fragment
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

    //1st step of starting Fragment
    //create DB instance
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDb = TransactionDB.getInstance(context);

    }

    //enable onCreateOptionsMenu, save AppCompatActivity to local variable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        toolBar = getActivity().findViewById(R.id.toolbar_layout);

        textViewOnToolBar = getActivity().findViewById(R.id.toolbar_textview);
        textViewOnToolBar.setVisibility(View.GONE);

        View rootView = inflater.inflate(R.layout.fragment_detail_transaction, container, false);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);

        mViewModel = ViewModelProviders.of(getActivity()).get(TransactionDBViewModel.class);


        //todo, if error, try to change from appCompatActivity to getActivity()
        mAdapter = new TransactionAdapter(getActivity(), this, mViewModel);

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

        //todo, implement tag for different ledgers


        factory = new TransactionDBVMFactory(mDb, Constant.untagged);

        mViewModel = ViewModelProviders.of(getActivity(), factory).get(TransactionDBViewModel.class);

        //todo, 1st stage, handle parent from different Fragment to perform (untagged or all transactions)
        //todo, 2nd stage, handle different data for different ledgers
        mViewModel.getTransactionsByLedger().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntries) {
                mAdapter.setData(transactionEntries);
        //      mCrossVM.setTransactionEntriesFromDB(transactionEntries);
            }
        });
        mViewModel.getActionModeState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isActionModeLocal = aBoolean;
            }
        });
    }


    @Override
    public void customOnListItemClick(int position) {
        if (isActionModeLocal) {

//            mViewModel.updateSelectedItemsArray(position);

        } else {
            TransactionEntry transactionEntry = mAdapter.getClickedData(position);
            Toast.makeText(
                    this.getActivity(),
                    "clicked " + position + " -> " + transactionEntry.getRemark(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
