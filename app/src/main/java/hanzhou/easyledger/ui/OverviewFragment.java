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
import hanzhou.easyledger.viewmodel.TransactionDBVMFactory;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

public class OverviewFragment extends Fragment{

    private static final String TAG = OverviewFragment.class.getSimpleName();

    private TransactionDBViewModel viewModel;

//    private SparseBooleanArray mSelectedItems;

    HorizontalBarChart mBarChart;

//    TransactionAdapter mAdapter;

    LinearLayoutManager layoutManager;

    private TransactionDB mDb;
//    private int numberOfSelectedTransaction;

    private RecyclerView recyclerView;

    private Toolbar toolBar;

    private TextView textViewOnToolBar;

    private AppCompatActivity appCompatActivity;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDb = TransactionDB.getInstance(context);
        Log.d("test_hash", "+++++++Overview Fragment Attached++++++++"+ this.hashCode());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        appCompatActivity = (AppCompatActivity) getActivity();
        Log.d("frg_flow_b_b", "onCreate: -5-5-5-5-5-5-5-");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview,container,false);
        Log.d("frg_flow_b_b", "onCreateView: -4-4-4-4-4-");

        toolBar = appCompatActivity.findViewById(R.id.toolbar_layout);


        textViewOnToolBar = appCompatActivity.findViewById(R.id.toolbar_textview);
        textViewOnToolBar.setVisibility(View.GONE);



        mBarChart = root.findViewById(R.id.overview_total_balance_barchart);

        int range = 200;

        setBarChart(2, range);


//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.transactionDAO().insertListOfTransactions(
//                        FakeTestingData.create20UntaggedTransactions());
//            }
//        });



        appCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.overview_recyclerview_for_untagged_transactions,
                        new DetailTransactionFragment(Constant.CALLFROMOVERVIEW))

                .commit();
        Log.d(Constant.TESTFLOW+TAG, "onCreateView:  create another DetailTransactionFragment() class");

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("frg_flow_b_b", "onActivityCreated: -3-3-3-3-3-3");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("frg_flow_b_b", "onStart: -2-2-2-2-2-2-");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("frg_flow_b_b", "onStart: -1-1-1--1-1");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("frg_flow_b", "onStart: 11111111111111");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("frg_flow_b", "onStop: 222222");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("frg_flow_b", "onDestroyView: 33333333");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("frg_flow_b", "onDestroy: 444444");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("test_hash", "_____Overview Fragment onDetached _______ "+ this.hashCode());
    }
    //
//    @Override
//    public void customOnListItemClick(int clickedItemIndex) {
//
//        if (mAdapter.getIsToolBarInAction()) {
//
//            mAdapter.switchSelectedState(clickedItemIndex);
//
//            displayToolbarIconBasedOnNumberOfSelectedItems(mAdapter.getSelectedItemCount());
//
//            textViewOnToolBar.setText(displayToolbarText());
//
//        } else {
//            TransactionEntry transactionEntry = mAdapter.getClickedOne(clickedItemIndex);
//            Toast.makeText(
//                    this.getActivity(),
//                "clicked "+clickedItemIndex+" -> "+transactionEntry.getRemark(),
//                    Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//    @Override
//    public void customOnListItemLongClick(int position) {
//
//        //only set toolbar to action mode if it is not
//        if(!mAdapter.getIsToolBarInAction()){
//            emptySelectedTransaction();
//            setToolBarToActionMode();
//        }
//    }



//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id){
//            case android.R.id.home:
//
//                if(mAdapter.getIsToolBarInAction()){ setToolBarToOriginMode();}
//
//                break;
//
//            case R.id.toolbar_edit:
//
//                startActivity(new Intent(appCompatActivity, TestTempActivity.class));
//                setToolBarToOriginMode();
//
//                break;
//            case R.id.toolbar_delete:
//
//                //todo implementing deleting
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDb.transactionDAO().deleteListOfTransactions(
//                                mAdapter.getSelectedTransactions()
//                        );
//                    }
//                });
//                setToolBarToOriginMode();
//                mAdapter.notifyDataSetChanged();
//
//                break;
//            case R.id.toolbar_ignore:
//                //todo put them all into others category
//
//                break;
//            case R.id.toolbar_select_all:
//                //select/de-select all the transctions in the view
//                if(mAdapter.getSelectedItemCount() ==mAdapter.getItemCount()){
//                    mAdapter.clearSelectedState();
//                    textViewOnToolBar.setText(displayToolbarText());
//                }else{
//                    mAdapter.selectAll();
//                    textViewOnToolBar.setText(displayToolbarText());
//                }
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

//
//    @Override
//    public boolean onBackPressed() {
//        if(mAdapter.getIsToolBarInAction()){
//            setToolBarToOriginMode();
//        }else{
//            return BackPressHandler.isUserPressedTwice(appCompatActivity);
//        }
//
//        return false;
//
//    }
//
//    private void setToolBarToActionMode(){
//
//        toolBar.getMenu().clear();
//        toolBar.setTitle(R.string.empty_string);
//
//        emptySelectedTransaction();
//
//        String display = mAdapter.getSelectedItemCount() +" "+
//                getResources().getString(R.string.string_toolbar_selection_word);
//
//        toolBar.inflateMenu(R.menu.overview_toolbar_action_mode);
//
//        /*  ignore btn is for auto-set selected item to 'Others' category
//         *  when entering toolbar action mode, no item has selected,
//         *  there is not need to display edit and ignore
//         * */
//
//        toolBar.getMenu().findItem(R.id.toolbar_ignore).setVisible(false);
//        toolBar.getMenu().findItem(R.id.toolbar_edit).setVisible(false);
//
//        mAdapter.setIsToolBarInAction(true);
//
//        textViewOnToolBar.setText(display);
//
//        textViewOnToolBar.setVisibility(View.VISIBLE);
//
//        mAdapter.notifyDataSetChanged();
//
//        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
//    }
//
//    private void setToolBarToOriginMode(){
//
//        toolBar.getMenu().clear();
//
//        toolBar.setTitle(R.string.app_name);
//
//        toolBar.inflateMenu(R.menu.overview_toolbar_normal_mode);
//
//        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);
//
//        textViewOnToolBar.setVisibility(View.GONE);
//
//        mAdapter.setIsToolBarInAction(false);
//
//        emptySelectedTransaction();
//
//        mAdapter.notifyDataSetChanged();
//    }
//
//    private void displayToolbarIconBasedOnNumberOfSelectedItems(int num){
//        if(num != 0){
//            toolBar.getMenu().findItem(R.id.toolbar_ignore).setVisible(true);
//        }else{
//            toolBar.getMenu().findItem(R.id.toolbar_ignore).setVisible(false);
//        }
//        if(num == 1){
//            toolBar.getMenu().findItem(R.id.toolbar_edit).setVisible(true);
//        }else{
//            toolBar.getMenu().findItem(R.id.toolbar_edit).setVisible(false);
//
//        }
//
//    }
//
//    private void emptySelectedTransaction() {
//        mAdapter.clearSelectedState();
//    }
//
//    private String displayToolbarText(){
//        String display = mAdapter.getSelectedItemCount() +" "
//                +getResources().getString(R.string.string_toolbar_selection_word);
//        return display;
//    }


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
//        final TransactionDBVMFactory factory = new TransactionDBVMFactory(mDb, Constant.UNTAGGED);
//        viewModel= ViewModelProviders.of(appCompatActivity,factory).get(TransactionDBViewModel.class);
        viewModel = ViewModelProviders.of(this).get(TransactionDBViewModel.class);
        viewModel.getTransactionsByLedger().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntries) {
                Log.d(TAG, "Updating UNTAGGED (new) transaction from LiveData in ViewModel");
//                mAdapter.setData(transactionEntries);
            }
        });

    }
}
