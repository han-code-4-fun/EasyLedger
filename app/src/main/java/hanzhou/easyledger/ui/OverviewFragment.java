package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModel;

public class OverviewFragment extends Fragment{

    private static final String TAG = OverviewFragment.class.getSimpleName();

    private OverviewFragmentViewModel mOverviewFragmentViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;
    private SPViewModel mSharedPreferenceViewModel;

    HorizontalBarChart mBarChart;
    BarDataSet barDataSet;

    LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private AppCompatActivity mAppCompatActivity;

    private SharedPreferences mAppPreferences;

//    /*two variable are boolean triggers that only update UI when both data updated*/
//    private boolean mReceivedRevenueData;
//    private boolean mReceivedExpenseData;

    private float mRevenueFloatingPoint;
    private float mExpenseFloatingPoint;

    /* get from revenus/spend comparision to set barchart range*/
    private float mBiggerNumber;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
//        mReceivedRevenueData =false;
//        mReceivedExpenseData = false;



        mExpenseFloatingPoint = 0f;
        mRevenueFloatingPoint = 0f;

        //todo, they should retrieve data from savedinstance
        mBiggerNumber = Math.max(mExpenseFloatingPoint, mRevenueFloatingPoint);
//        mAppPreferences = mAppCompatActivity.getSharedPreferences(Constant.APP_PREFERENCE, Context.MODE_PRIVATE);
        mAppPreferences = PreferenceManager.getDefaultSharedPreferences(mAppCompatActivity);
        loadPreference();
    }

    private void loadPreference() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapterActionViewModel.setmIsInBaseFragment(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview,container,false);



        mBarChart = root.findViewById(R.id.overview_total_balance_barchart);

        initiazlizeBarChart();


        mAppCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.overview_recyclerview_for_untagged_transactions,
                       DetailTransactionFragment.newInstance(Constant.UNTAGGED))
                .commit();

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mAppCompatActivity = null;
        Log.d("test_flow2", "overview onDetach now!!!! ");
    }



    private void initiazlizeBarChart() {

        //set barchar style,
        setChartStyle();

        //Set bar entries
        setChartData();


    }

    /*make chart to display only*/
    private void setChartStyle(){

//        mBarChart.setDrawBarShadow(false);
        mBarChart.setDescription(null);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.setDrawBarShadow(true);
        mBarChart.getXAxis().setEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.setTouchEnabled(false);
        mBarChart.setDragEnabled(false);
        mBarChart.setScaleEnabled(false);
        mBarChart.setScaleXEnabled(false);
        mBarChart.setScaleYEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDoubleTapToZoomEnabled(false);

        setXAxis();
        setYLeftAxis();

    }

    private void setXAxis() {
        XAxis xAxis = mBarChart.getXAxis();

        xAxis.setDrawGridLines(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setEnabled(false);
//        xAxis.setXOffset(5f);

        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setTextSize(10f);

        xAxis.setDrawAxisLine(false);

        //only display revenue and spend
//        xAxis.setLabelCount(2);

//        String[] balanceTitle = {
//
//                getString(R.string.overview_balance_spend),
//                getString(R.string.overview_balance_revenue)
//        };
//
//        xAxis.setValueFormatter(new OverViewBalanceXAxisValueFormatter(balanceTitle));
/* getString(R.string.overview_balance_spend),
                getString(R.string.overview_balance_revenue)*/


    }


    /*set YLeftAxis' maximum based on the maximum get from DB*/
    private void setYLeftAxis(){
        YAxis yLeftAxis = mBarChart.getAxisLeft();

        /*set YLeftAxis' maximum based on the maximum get from DB*/
        yLeftAxis.setAxisMaximum(mBiggerNumber);

        yLeftAxis.setAxisMinimum(0f);

        yLeftAxis.setEnabled(false);
    }


    private void setChartData() {

        /*use the data come from viewmodel*/
        //place revenue on top of spend in barchart
        BarEntry revenue = new BarEntry(1f, mRevenueFloatingPoint);
        BarEntry spend = new BarEntry(0f, mExpenseFloatingPoint);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(revenue);
        entries.add(spend);

        barDataSet = new BarDataSet(entries, "Bar data test");

        barDataSet.setColors(
                //set upper chart (revenue) color
                ContextCompat.getColor(mBarChart.getContext(), R.color.color_money_in),
                //set lower chart (spend) color
                ContextCompat.getColor(mBarChart.getContext(), R.color.color_money_out));
        //todo, important
        barDataSet.setBarShadowColor(
                ContextCompat.getColor(mBarChart.getContext(), R.color.color_deactive));
        barDataSet.setValueTextSize(15f);
        barDataSet.setDrawValues(true);



        BarData barData = new BarData(barDataSet);


        barData.setBarWidth(0.9f);

        mBarChart.setData(barData);



        //refresh barchart
        mBarChart.invalidate();
    }



    private void setupViewModel() {

        mAdapterActionViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);
        mOverviewFragmentViewModel = ViewModelProviders.of(mAppCompatActivity).get(OverviewFragmentViewModel.class);

        mOverviewFragmentViewModel.getlistOfTransactionsInTimeRange().observe(this, new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
//                Log.d("test_flow3", "main_activity, should be called one mOverviewDateStartTime");
                calculateSpendingNRevenueSum(transactionEntryList);
            }
        });
        mSharedPreferenceViewModel = ViewModelProviders.of(mAppCompatActivity).get(SPViewModel.class
        );
        mOverviewFragmentViewModel.getRevenue().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aDouble) {
                mRevenueFloatingPoint = aDouble;
//                mReceivedRevenueData = true;
                synchronizeBalanceData();
            }
        });

        mOverviewFragmentViewModel.getSpend().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aDouble) {
                mExpenseFloatingPoint = Math.abs(aDouble);
//                mReceivedExpenseData = true;
                synchronizeBalanceData();

            }
        });




    }

    private void synchronizeBalanceData(){
        //only update UI when two values are received
//        if(mReceivedRevenueData && mReceivedExpenseData){
            mBiggerNumber = Math.max(mExpenseFloatingPoint, mRevenueFloatingPoint);
            setYLeftAxis();
            setChartData();

//            //after update UI, set back to false to be prepared to next update
//            mReceivedRevenueData = false;
//            mReceivedExpenseData = false;
//        }

    }
    private void calculateSpendingNRevenueSum(List<TransactionEntry> transactionEntryList) {
        float revenue = 0;
        float spending = 0;
        for (TransactionEntry entry : transactionEntryList) {
            if (entry.getAmount() >= 0) {
                revenue = revenue + entry.getAmount();
            } else {
                spending = spending + entry.getAmount();
            }
        }

        mOverviewFragmentViewModel.setRevenue(revenue);
        mOverviewFragmentViewModel.setSpend(spending);

    }
}
