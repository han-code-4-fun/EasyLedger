package hanzhou.easyledger.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.GeneralViewModel;
import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModel;

public class OverviewFragment extends Fragment {


    private OverviewFragmentViewModel mOverviewFragmentViewModel;

    private HorizontalBarChart mBarChart;

    private AppCompatActivity mAppCompatActivity;


    private TextView mTotalAmount;

    private float mRevenueFloatingPoint;
    private float mExpenseFloatingPoint;

    /* get from revenus/spend comparision to set barchart range*/
    private float mBiggerNumber;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;

        mExpenseFloatingPoint = 0f;
        mRevenueFloatingPoint = 0f;
        mBiggerNumber = 0f;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview, container, false);


        mBarChart = root.findViewById(R.id.overview_total_balance_barchart);

        initiazlizeBarChart();

        mTotalAmount = root.findViewById(R.id.overview_total_balance_amount);

        mAppCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
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


    private void initiazlizeBarChart() {

        setChartStyle();

        setChartData();

    }

    /*make chart to display only*/
    private void setChartStyle() {

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

        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setTextSize(10f);

        xAxis.setDrawAxisLine(false);

    }


    /*set YLeftAxis' maximum based on the maximum get from DB*/
    private void setYLeftAxis() {
        YAxis yLeftAxis = mBarChart.getAxisLeft();

        /*set YLeftAxis' maximum based on the maximum get from DB*/
        yLeftAxis.setAxisMaximum(mBiggerNumber);

        yLeftAxis.setAxisMinimum(0f);

        yLeftAxis.setEnabled(false);
    }


    private void setChartData() {

        /*place revenue on top of spend in barchart*/
        BarEntry revenue = new BarEntry(1f, mRevenueFloatingPoint);
        BarEntry spend = new BarEntry(0f, mExpenseFloatingPoint);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(revenue);
        entries.add(spend);

        BarDataSet barDataSet = new BarDataSet(entries, getString(R.string.overview_bar_data_label));

        barDataSet.setColors(
                /*set upper chart (revenue) color*/
                ContextCompat.getColor(mBarChart.getContext(), R.color.color_money_in),
                /*set lower chart (spend) color*/
                ContextCompat.getColor(mBarChart.getContext(), R.color.color_money_out));
        //todo, important
        barDataSet.setBarShadowColor(
                ContextCompat.getColor(mBarChart.getContext(), R.color.color_deactive));
        barDataSet.setValueTextSize(15f);
        barDataSet.setDrawValues(true);


        BarData barData = new BarData(barDataSet);


        barData.setBarWidth(0.9f);

        mBarChart.setData(barData);

        /*refresh barchart*/
        mBarChart.invalidate();
    }


    private void setupViewModel() {

        GeneralViewModel mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_OVERVIEW);

        AdapterNActionBarViewModel mAdapterActionViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);
        mOverviewFragmentViewModel = ViewModelProviders.of(mAppCompatActivity).get(OverviewFragmentViewModel.class);

        mOverviewFragmentViewModel.getlistOfTransactionsInTimeRange().observe(this, new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                calculateSpendingNRevenueSum(transactionEntryList);
            }
        });
        SPViewModel mSharedPreferenceViewModel = ViewModelProviders.of(mAppCompatActivity).get(SPViewModel.class
        );
        mOverviewFragmentViewModel.getRevenue().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aDouble) {
                mRevenueFloatingPoint = aDouble;
                synchronizeOverviewBalanceData();
            }
        });

        mOverviewFragmentViewModel.getSpend().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aDouble) {
                mExpenseFloatingPoint = Math.abs(aDouble);
                synchronizeOverviewBalanceData();

            }
        });


    }

    private void synchronizeOverviewBalanceData() {
        mBiggerNumber = Math.max(mExpenseFloatingPoint, mRevenueFloatingPoint);
        setYLeftAxis();
        setChartData();
    }

    @SuppressLint("SetTextI18n")
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

        mTotalAmount.setText(UnitUtil.formatMoney(revenue + spending));

    }
}
