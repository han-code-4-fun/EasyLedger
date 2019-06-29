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

import android.util.Log;
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
import hanzhou.easyledger.viewmodel.TransactionDBVMFactory;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

public class DetailTransactionFragment extends Fragment
implements  TransactionAdapter.CustomListItemClickListener,
            MainActivity.OnBackPressedLinkActivityToFragment{

    private static final String TAG = DetailTransactionFragment.class.getSimpleName();

    private TransactionDBViewModel mViewModel;

    private TransactionAdapter mAdapter;

    private RecyclerView mRecyclerView;


    private Toolbar toolBar;

    private TextView textViewOnToolBar;

    private TransactionDB mDb;

    private AppCompatActivity appCompatActivity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDb = TransactionDB.getInstance(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        appCompatActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        toolBar = appCompatActivity.findViewById(R.id.toolbar_layout);

        textViewOnToolBar = appCompatActivity.findViewById(R.id.toolbar_textview);
        textViewOnToolBar.setVisibility(View.GONE);

        View rootView =inflater.inflate(R.layout.fragment_detail_transaction, container, false);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.VERTICAL);

        mAdapter = new TransactionAdapter(this.getContext(),this);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_detail_transaction);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();

        return rootView;
    }

    private void setupViewModel() {
        //todo
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //todo, implement tag for different ledgers


        TransactionDBVMFactory factory = new TransactionDBVMFactory(mDb, Constant.untagged);
        mViewModel = ViewModelProviders.of(appCompatActivity, factory).get(TransactionDBViewModel.class);

        mViewModel.getAllTransactions().observe(appCompatActivity, new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntries) {
                mAdapter.setData(transactionEntries);

            }
        });
    }


    @Override
    public void customOnListItemClick(int clickedItemIndex) {

        if (mAdapter.getIsToolBarInAction()) {

            mAdapter.switchSelectedState(clickedItemIndex);

            displayToolbarIconBasedOnNumberOfSelectedItems(mAdapter.getSelectedItemCount());

            textViewOnToolBar.setText(displayToolbarText());

        } else {
            TransactionEntry transactionEntry = mAdapter.getClickedOne(clickedItemIndex);
            Toast.makeText(
                    this.getActivity(),
                    "clicked "+clickedItemIndex+" -> "+transactionEntry.getRemark(),
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void customOnListItemLongClick(int position) {

        //only set toolbar to action mode if it is not
        if(!mAdapter.getIsToolBarInAction()){
            emptySelectedTransaction();
            setToolBarToActionMode();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:

                if(mAdapter.getIsToolBarInAction()){ setToolBarToOriginMode();}

                break;

            case R.id.toolbar_edit:

                startActivity(new Intent(appCompatActivity, TestTempActivity.class));
                setToolBarToOriginMode();

                break;
            case R.id.toolbar_delete:

                //todo implementing deleting
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().deleteListOfTransactions(
                                mAdapter.getSelectedTransactions()
                        );
                    }
                });
                setToolBarToOriginMode();
                mAdapter.notifyDataSetChanged();

                break;
            case R.id.toolbar_ignore:
                //todo put them all into others category

                break;
            case R.id.toolbar_select_all:
                //select/de-select all the transctions in the view
                if(mAdapter.getSelectedItemCount() ==mAdapter.getItemCount()){
                    mAdapter.clearSelectedState();
                    textViewOnToolBar.setText(displayToolbarText());
                }else{
                    mAdapter.selectAll();
                    textViewOnToolBar.setText(displayToolbarText());
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onBackPressed() {
        if(mAdapter.getIsToolBarInAction()){
            setToolBarToOriginMode();
        }else{
            return BackPressHandler.isUserPressedTwice(appCompatActivity);
        }

        return false;

    }

    private void setToolBarToActionMode(){

        toolBar.getMenu().clear();
        toolBar.setTitle(R.string.empty_string);

        emptySelectedTransaction();

        String display = mAdapter.getSelectedItemCount() +" "+
                getResources().getString(R.string.string_toolbar_selection_word);

        toolBar.inflateMenu(R.menu.toolbar_action_mode);

        /*  ignore btn is for auto-set selected item to 'Others' category
         *  when entering toolbar action mode, no item has selected,
         *  there is not need to display edit and ignore
         * */

        toolBar.getMenu().findItem(R.id.toolbar_ignore).setVisible(false);
        toolBar.getMenu().findItem(R.id.toolbar_edit).setVisible(false);

        mAdapter.setIsToolBarInAction(true);

        textViewOnToolBar.setText(display);

        textViewOnToolBar.setVisibility(View.VISIBLE);

        mAdapter.notifyDataSetChanged();

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void setToolBarToOriginMode(){

        toolBar.getMenu().clear();

        toolBar.setTitle(R.string.app_name);

        toolBar.inflateMenu(R.menu.toolbar_normal_mode);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);

        textViewOnToolBar.setVisibility(View.GONE);

        mAdapter.setIsToolBarInAction(false);

        emptySelectedTransaction();

        mAdapter.notifyDataSetChanged();
    }

    private void displayToolbarIconBasedOnNumberOfSelectedItems(int num){
        if(num != 0){
            toolBar.getMenu().findItem(R.id.toolbar_ignore).setVisible(true);
        }else{
            toolBar.getMenu().findItem(R.id.toolbar_ignore).setVisible(false);
        }
        if(num == 1){
            toolBar.getMenu().findItem(R.id.toolbar_edit).setVisible(true);
        }else{
            toolBar.getMenu().findItem(R.id.toolbar_edit).setVisible(false);

        }

    }

    private void emptySelectedTransaction() {
        mAdapter.clearSelectedState();
    }

    private String displayToolbarText(){
        String display = mAdapter.getSelectedItemCount() +" "
                +getResources().getString(R.string.string_toolbar_selection_word);
        return display;
    }
}
