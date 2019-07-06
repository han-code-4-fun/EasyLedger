package hanzhou.easyledger.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.ChartDataViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBVMFactory;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

public class OverviewFragment extends Fragment{

    private static final String TAG = OverviewFragment.class.getSimpleName();

    private ChartDataViewModel mChartViewModel;

    HorizontalBarChart mBarChart;

    LinearLayoutManager layoutManager;

    private RecyclerView recyclerView;

    private AppCompatActivity appCompatActivity;

    private double revenue;
    private double spend;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview,container,false);


        mBarChart = root.findViewById(R.id.overview_total_balance_barchart);
        mBarChart.calculateOffsets();

        int range = 200;

        setBarChart(2, range);



        appCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.overview_recyclerview_for_untagged_transactions,
                        new DetailTransactionFragment(Constant.CALLFROMOVERVIEW))
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
        appCompatActivity = null;
    }



    private void setBarChart(int i, int range) {
        ArrayList<BarEntry> entrySpending = new ArrayList<>();
        ArrayList<BarEntry> entryRevenus = new ArrayList<>();
        float barWidth = 3f;
        float spaceForBar = 1f;
//        entryRevenus.add(new BarEntry(spaceForBar, Math.round(revenue)));
//        entrySpending.add(new BarEntry(spaceForBar*2, Math.round(spend)));

        entryRevenus.add(new BarEntry(spaceForBar, 2000));
        entrySpending.add(new BarEntry(spaceForBar*2, 1200));

        BarDataSet barDataSetSpending = new BarDataSet(entrySpending, "Money out");
        barDataSetSpending.setColor(getResources().getColor(R.color.color_money_out));

        BarDataSet barDataSetRevenue = new BarDataSet(entryRevenus, "Money in");
        barDataSetRevenue.setColor(getResources().getColor(R.color.color_money_in));

        BarData data = new BarData();
        data.addDataSet(barDataSetSpending);
        data.addDataSet(barDataSetRevenue);

//        data.setBarWidth(barWidth);

        barChartSetting();

        mBarChart.setData(data);
        mBarChart.invalidate();//refresh data


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
        mChartViewModel = ViewModelProviders.of(appCompatActivity).get(ChartDataViewModel.class);

        mChartViewModel.getRevenue().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                revenue = aDouble;
            }
        });

        mChartViewModel.getSpend().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                spend = aDouble;
            }
        });

    }
}
