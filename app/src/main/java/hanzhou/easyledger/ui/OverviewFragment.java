package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.FakeTestingData;
import hanzhou.easyledger.viewmodel.OverviewViewModel;

public class OverviewFragment extends Fragment
implements TransactionAdapter.CustomListItemClickListener, View.OnLongClickListener {

    private static final String TAG = OverviewFragment.class.getSimpleName();

    HorizontalBarChart mBarChart;

    TransactionAdapter mAdapter;

    private List<TransactionEntry> mTransactionEntryList;
    private TransactionDB mDb;

    RecyclerView mRecyclerView;

    private Toolbar toolBar;

    private TextView textViewOnToolBar;

    private boolean isToolBarInAction;

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
        isToolBarInAction = false;
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


//
//        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);
//
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolBar);
//
//        toolBar.setTitle(R.string.actionbar_title);


        mBarChart = root.findViewById(R.id.overview_total_balance_barchart);

        int range = 200;

        setBarChart(2, range);

        mTransactionEntryList = FakeTestingData.create25UntaggedTransactions();
//        mTransactionEntryList = new ArrayList<>();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mAdapter =new TransactionAdapter(getContext() ,mTransactionEntryList,this,this);

        mRecyclerView =root.findViewById(R.id.overview_transaction_untagged_recyclerview);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);


//        setupViewModel();


       return root;
    }

    private void setupViewModel() {
        OverviewViewModel viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        viewModel.getUntaggedTransactions().observe(this, new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntries) {
                Log.d(TAG, "Updating untagged (new) transaction from LiveData in ViewModel");
                mAdapter.setData(transactionEntries);
            }
        });
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

    @Override
    public void customOnListItemClick(int clickedItemIndex) {
        TransactionEntry thisRecord = mTransactionEntryList.get(clickedItemIndex);

        Log.d(TAG, "customOnListItemClick: clicked item number "+clickedItemIndex);
        Toast.makeText(
                this.getActivity(),
                "you clicked "+clickedItemIndex+" item, which is "+thisRecord.getRemark(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onLongClick(View view) {
        toolBar.getMenu().clear();
        toolBar.inflateMenu(R.menu.toolbar_action_select_one);
        isToolBarInAction = true;
        mAdapter.notifyDataSetChanged();

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
