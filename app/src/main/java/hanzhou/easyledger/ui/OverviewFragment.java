package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.temp.TestTempActivity;
import hanzhou.easyledger.utility.BackPressHandler;
import hanzhou.easyledger.utility.FakeTestingData;
import hanzhou.easyledger.viewmodel.OverviewViewModel;

public class OverviewFragment extends Fragment
implements TransactionAdapter.CustomListItemClickListener,

        MainActivity.OnBackPressedLinkActivityToFragment {

    private static final String TAG = OverviewFragment.class.getSimpleName();

    private OverviewViewModel viewModel;

    private SparseBooleanArray mSelectedItems;

    HorizontalBarChart mBarChart;

    TransactionAdapter mAdapter;

    private List<TransactionEntry> mTransactionEntryList;
    private TransactionDB mDb;
    private int numberOfSelectedTransaction;

    RecyclerView mRecyclerView;

    private Toolbar toolBar;

    private TextView textViewOnToolBar;

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
        appCompatActivity = appCompatActivity = (AppCompatActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview,container,false);


        toolBar = getActivity().findViewById(R.id.toolbar_layout);

        textViewOnToolBar = getActivity().findViewById(R.id.toolbar_textview);
        textViewOnToolBar.setVisibility(View.GONE);
        mSelectedItems = new SparseBooleanArray();



        mBarChart = root.findViewById(R.id.overview_total_balance_barchart);

        int range = 200;

        setBarChart(2, range);


        /*AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDAO().insertListOfTransactions(
                        FakeTestingData.create5UntaggedTransactions());
            }
        });*/




        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mAdapter =new TransactionAdapter(getContext(),this);

        mRecyclerView =root.findViewById(R.id.overview_transaction_untagged_recyclerview);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);
        numberOfSelectedTransaction = mAdapter.getSelectedItemCount();

        setupViewModel();


       return root;
    }











    @Override
    public void customOnListItemClick(int clickedItemIndex) {

        if (mAdapter.getIsToolBarInAction()) {

            mAdapter.switchSelectedState(clickedItemIndex);

            numberOfSelectedTransaction = mAdapter.getSelectedItemCount();

            switchToolBarModeBetweenSingleNMultiple(numberOfSelectedTransaction);

            String display = numberOfSelectedTransaction +" "+getResources().getString(R.string.string_toolbar_selection_word);

            textViewOnToolBar.setText(display);

        } else {

            TransactionEntry thisRecord = mTransactionEntryList.get(clickedItemIndex);

            Log.d(TAG, "customOnListItemClick: clicked item number "+clickedItemIndex);
            Toast.makeText(
                    this.getActivity(),
                    "you clicked "+clickedItemIndex+" item, which is "+thisRecord.getRemark(),
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

    /*    if (id == android.R.id.home) {

            if(mAdapter.getIsToolBarInAction()){
                setToolBarToOriginMode();
            }

            return true;
        }else{
            if(id == R.id.toolbar_edit){
                startActivity(new Intent(this.getContext(), TestTempActivity.class));

            }
        }*/
        switch (id){
            case android.R.id.home:
                if(mAdapter.getIsToolBarInAction()){
                    setToolBarToOriginMode();
                }
                break;

            case R.id.toolbar_edit:
                startActivity(new Intent(this.getContext(), TestTempActivity.class));
                break;
            case R.id.toolbar_delete:
                //todo implementing deleting

                break;
            case R.id.toolbar_ignore:
                //todo put them all into others category

                break;
            case R.id.toolbar_select_all:
                //todo, select all the transctions in the view

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onBackPressed() {
        if(mAdapter.getIsToolBarInAction()){
            setToolBarToOriginMode();
        }else{
            return BackPressHandler.isUserPressedTwice(this.getContext());
        }

        return false;

    }

    private void setToolBarToActionMode(){

        toolBar.getMenu().clear();
        toolBar.setTitle(R.string.empty_string);
        emptySelectedTransaction();

        String display = numberOfSelectedTransaction +" "+
                getResources().getString(R.string.string_toolbar_selection_word);

        toolBar.inflateMenu(R.menu.toolbar_action_select_one);

        mAdapter.setIsToolBarInAction(true);

        textViewOnToolBar.setText(display);

        textViewOnToolBar.setVisibility(View.VISIBLE);



        mAdapter.notifyDataSetChanged();

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setToolBarToOriginMode(){

        toolBar.getMenu().clear();

        toolBar.setTitle(R.string.app_name);

        toolBar.inflateMenu(R.menu.toolbar_mainactivity);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);

        textViewOnToolBar.setVisibility(View.GONE);

        mAdapter.setIsToolBarInAction(false);

        emptySelectedTransaction();

        mAdapter.notifyDataSetChanged();
    }

    private void switchToolBarModeBetweenSingleNMultiple(int num){
        String display = num +" "+
                getResources().getString(R.string.string_toolbar_selection_word);
        if(num<=1){
            //todo optimize this
//            if(!toolBar.getMenu().equals(R.menu.toolbar_action_select_one)){
                //only run if toolbar doesn't implement toolbar_action_select_one
                Log.d(TAG, "switchToolBarModeBetweenSingleNMultiple: 11111");
                toolBar.getMenu().clear();
                toolBar.inflateMenu(R.menu.toolbar_action_select_one);
                appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            }
        }else{
            toolBar.getMenu().clear();
            toolBar.inflateMenu(R.menu.toolbar_action_select_multi);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void emptySelectedTransaction() {
        mAdapter.clearSelectedState();

        numberOfSelectedTransaction = 0;
    }


    private void setBarChart(int i, int range) {
        ArrayList<BarEntry> entrySpending = new ArrayList<>();
        ArrayList<BarEntry> entryRevenus = new ArrayList<>();
        float barWidth = 3f;
        float spaceForBar = 1f;
        entryRevenus.add(new BarEntry(spaceForBar, 1450));
        entrySpending.add(new BarEntry(spaceForBar*2, 750));

        BarDataSet barDataSetSpending = new BarDataSet(entrySpending, "Money out");
        barDataSetSpending.setColor(getResources().getColor(R.color.color_money_out));

        BarDataSet barDataSetRevenue = new BarDataSet(entryRevenus, "Money in");
        barDataSetRevenue.setColor(getResources().getColor(R.color.color_money_in));

        BarData data = new BarData();
        data.addDataSet(barDataSetSpending);
        data.addDataSet(barDataSetRevenue);

//        data.setBarWidth(barWidth);

        mBarChart.setData(data);
        mBarChart.invalidate();//refresh data
        barChartSetting();

    }



    private void barChartSetting(){
        barChartSetStyle();
        barChartSetNoInteraction();

    }

    private void barChartSetStyle(){
        mBarChart.setFitBars(true);
        mBarChart.setVisibleYRange(0,1500, YAxis.AxisDependency.LEFT);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getAxisRight().setDrawGridLines(false);
    }

    private void barChartSetNoInteraction(){
        mBarChart.setTouchEnabled(false);
        mBarChart.setDragEnabled(false);
        mBarChart.setScaleEnabled(false);
        mBarChart.setScaleXEnabled(false);
        mBarChart.setScaleYEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDoubleTapToZoomEnabled(false);

    }

    private void setupViewModel() {
        viewModel= ViewModelProviders.of(this).get(OverviewViewModel.class);
        viewModel.getUntaggedTransactions().observe(this, new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntries) {
                Log.d(TAG, "Updating untagged (new) transaction from LiveData in ViewModel");
                mAdapter.setData(transactionEntries);
            }
        });

    }
}
