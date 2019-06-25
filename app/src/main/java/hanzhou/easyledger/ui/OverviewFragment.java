package hanzhou.easyledger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import hanzhou.easyledger.data.Transaction;
import hanzhou.easyledger.util.FakeTestingData;

public class OverviewFragment extends Fragment
implements TransactionAdapter.CustomListItemClickListener{

    HorizontalBarChart barChart;

    TransactionAdapter transactionAdapter;

    private List<Transaction> mTransactionList;

    RecyclerView recyclerView;


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
        barChart= root.findViewById(R.id.overview_total_balance_barchart);

        int range = 200;

        setBarChart(2, range);

        mTransactionList = FakeTestingData.create10UntaggedTransactions();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(RecyclerView.VERTICAL);

        transactionAdapter =new TransactionAdapter(getActivity(), mTransactionList,this);

        recyclerView=root.findViewById(R.id.overview_transaction_untagged_recyclerview);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(transactionAdapter);


       return root;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mainactivity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menu_mainactivity_settings:
                //todo
                break;
            case R.id.menu_mainactivity_question:
                startActivity(new Intent(getContext(), QuestionActivity.class));
                break;
            case R.id.menu_mainactivity_feedback:
                //todo
                break;
        }


        return super.onOptionsItemSelected(item);
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

        barChart.setData(data);
        barChart.invalidate();//refresh data
        barChartSetting();

    }

    private void barChartSetting(){
        barChartSetStyle();
        barChartSetNoInteraction();

    }

    private void barChartSetStyle(){
        barChart.setFitBars(true);
        barChart.setVisibleYRange(0,1500, YAxis.AxisDependency.LEFT);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
    }

    private void barChartSetNoInteraction(){
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);

    }

    @Override
    public void customOnListItemClick(int clickedItemIndex) {
        Transaction thisRecord = mTransactionList.get(clickedItemIndex);

        Toast.makeText(
                getActivity(),
                "you clicked "+clickedItemIndex+" item, which is "+thisRecord.getmRemark(),
                Toast.LENGTH_LONG).show();
    }
}
