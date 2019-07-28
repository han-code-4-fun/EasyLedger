package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.GsonHelper;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.GeneralViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModel;
import hanzhou.easyledger.chart_personalization.LabelFormatterCurrentBarChart;
import hanzhou.easyledger.chart_personalization.MonthValueFormatter;
import hanzhou.easyledger.chart_personalization.MyLargeValueFormatter;
import hanzhou.easyledger.chart_personalization.MyPercentFormatter;
import hanzhou.easyledger.chart_personalization.WeekValueFormatter;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.BackGroundColor;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.chart_personalization.MyMarkerView;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.ChartDataViewModel;

/*
 *   This Fragment shows 3 charts in one of two ways:
 *       expense piechart  +  revenue piechart + history comparison piechart
 *   OR  expense barchart  +  revenue barchart + history comparison barchart
 *
 *   For each chart combination, user can select to display expense/revenue chart
 *   for current weeks or current MONTHS_CAP
 *
 *   for the history comparison barchart, user can select number of periods (week or month) to compare
 *
 * */

public class ChartFragment extends Fragment implements
        OnChartValueSelectedListener {

    private static final String TAG = ChartFragment.class.getSimpleName();

    private static final String CATEGORY_BARCHART = "user_choose_barchart";
    private static final String CATEGORY_PIECHART = "user_choose_piechart";

    private static final String CHART_REVENUE = "a_revenue_chart";
    private static final String CHART_EXPENSE = "a_expense_chart";

    private TransactionDB mDb;

    private AppCompatActivity mAppCompatActivity;


    private GeneralViewModel mGeneralViewModel;
    private SPViewModel sharedPreferenceViewModel;
    private ChartDataViewModel mChartDataViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;

    /*belowing 5 items value based on sharedPreferencce*/
    private int mHistoryPeriodType;
    private int mNumberOfPeriodsToCompare;
    private String mUserSelection;
    private int mCurrentPeriodType;
    private boolean mIsPieChartShowPercentage;

    private boolean mIsCustomCurrentPeriod;

    private int mCurrentChartType;

    private int mCurrentChartStartingDate;
    private int mNumOfCustomDays;


    private List<Integer> mDateListForEachPeriod;

    private PieChart mCurrentPieChartRevenue;
    private PieChart mCurrentPieChartExpense;
    private BarChart mCurrentBarChartRevenue;
    private BarChart mCurrentBarChartExpense;

    private BarChart mHistoryBarChart;

    private TextView mHistoryChartDescription;

    private HashMap<String, Float> mCategoryRevenue;
    private HashMap<String, Float> mCategoryExpense;

    private BackGroundColor mColors;

    private SharedPreferences mAppPreferences;

    private GsonHelper mGsonHelper;

    private int testNumber = 0;

    private int mHistoryChartStartDate, mHistoryChartEndDate;

    private int mRevenueCategorySize;
    private int mExpenseCategorySize;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDb = TransactionDB.getInstance(context);
        mAppCompatActivity = (AppCompatActivity) context;

        mAppPreferences = PreferenceManager.getDefaultSharedPreferences(mAppCompatActivity);
        mColors = new BackGroundColor();
        mGsonHelper = GsonHelper.getInstance();
        mGsonHelper.setmSharedPreferences(mAppPreferences);

//        mAppPreferences = mAppCompatActivity.getSharedPreferences(Constant.APP_PREFERENCE, Context.MODE_PRIVATE);

        loadPreferenceSetting();


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        mCurrentPieChartRevenue = rootView.findViewById(R.id.chart_current_piechart_revenue);
        mCurrentPieChartExpense = rootView.findViewById(R.id.chart_current_piechart_expense);

        mCurrentBarChartRevenue = rootView.findViewById(R.id.chart_current_barchart_revenue);
        mCurrentBarChartExpense = rootView.findViewById(R.id.chart_current_barchart_expense);

        mHistoryBarChart = rootView.findViewById(R.id.chart_history_barchart);

        setChartHeightForAllTypeOfScreenHeight();

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();
        /*this bar chart will always show*/
        initializeHistoryBarChart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mAdapterActionViewModel.setmIsInBaseFragment(true);

    }

    /*
        everything is in the NestedScrollView.
        setup charts height 90% of visible screen size, so
        user will notices that there are more charts downwards
     */
    private void setChartHeightForAllTypeOfScreenHeight() {

       int screenHeight = getIdealScreenHeight();

       setChartHeight(screenHeight);

    }

    private int getIdealScreenHeight() {

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (mAppCompatActivity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        Log.d("test_dimension", "getIdealScreenHeight:  actionbar ->"+actionBarHeight);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mAppCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        int screenWidth = displayMetrics.widthPixels;

        Log.d("test_dimension", "getIdealScreenHeight:   screenwidth is ****** "+ screenWidth);

        Log.d("test_dimension", "getIdealScreenHeight:  screenHeight ->"+screenHeight);

        int bottomNavigationViewHeight = Math.round(getResources().getDimension(R.dimen.bottom_navigation_view_height));
        Log.d("test_dimension", "getIdealScreenHeight:  bottomNavigationViewHeight ->"+bottomNavigationViewHeight);

        /*after getting the real display size by removing actionbar
         and bottomNavigationView, make height times 0.9 */
        screenHeight = screenHeight - bottomNavigationViewHeight - actionBarHeight;

        Log.d("test_dimension", "screenHeight  after remove actionbar and bottomnavigationview ->"+screenHeight);


        screenHeight = (int) Math.round(screenHeight * 0.9);


        Log.d("test_dimension", "screenHeight  after * 0.9 ->"+screenHeight);


        return screenHeight;
    }

    private void setChartHeight(int screenHeight) {
        ViewGroup.LayoutParams layoutParams = mCurrentPieChartRevenue.getLayoutParams();
        layoutParams.height = screenHeight;
        mCurrentPieChartRevenue.setLayoutParams(layoutParams);

        layoutParams = mCurrentPieChartExpense.getLayoutParams();
        layoutParams.height = screenHeight;
        mCurrentPieChartExpense.setLayoutParams(layoutParams);

        layoutParams = mCurrentBarChartRevenue.getLayoutParams();
        layoutParams.height = screenHeight;
        mCurrentBarChartRevenue.setLayoutParams(layoutParams);

        layoutParams = mCurrentBarChartExpense.getLayoutParams();
        layoutParams.height = screenHeight;
        mCurrentBarChartExpense.setLayoutParams(layoutParams);

        layoutParams = mHistoryBarChart.getLayoutParams();
        layoutParams.height = screenHeight;
        mHistoryBarChart.setLayoutParams(layoutParams);
    }


    private void setupViewModel() {

        mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_CHART);

        sharedPreferenceViewModel = ViewModelProviders.of(mAppCompatActivity).get(SPViewModel.class
        );

        setupSharedPreferenceViewModelObserverForChartDisplay(sharedPreferenceViewModel);

        mChartDataViewModel = ViewModelProviders.of(mAppCompatActivity).get(ChartDataViewModel.class);
        mChartDataViewModel.initializeDatesForVM(mHistoryChartStartDate, mHistoryChartEndDate, mCurrentChartStartingDate);

        setupChartDataViewModelObserver(mChartDataViewModel);


//        ChartDataInitialViewModelFactory factory = new ChartDataInitialViewModelFactory(mCurrentChartStartingDate, mHistoryChartStartDate, mHistoryChartEndDate, mDb);
//        //this view model only works for the fragment itself
//        ChartDataViewModel mChartDataViewModel = ViewModelProviders.of(this, factory).get(ChartDataViewModel.class);

        //this view model only works for the fragment itself
//        ChartDataViewModel mChartDataViewModel = ViewModelProviders.of(mAppCompatActivity).get(ChartDataViewModel.class);

//        mChartDataViewModel.setmChartSettingChanged(false);


    }




    private void initializeHistoryBarChart() {

        mHistoryBarChart.setOnChartValueSelectedListener(this);
        mHistoryBarChart.getDescription().setEnabled(true);

        mHistoryBarChart.setPinchZoom(true);
        mHistoryBarChart.setDrawBarShadow(false);
        mHistoryBarChart.setDrawGridBackground(false);

        /*
         *   MPAndroidChart bug, version lower than API24 cannot have custom Marker due to memory issue
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.barchart_marker_view);
            // For bounds control
            mv.setChartView(mHistoryBarChart);
            // Set the marker to the mHistoryBarChart
            mHistoryBarChart.setMarker(mv);
        }


        YAxis leftAxis = mHistoryBarChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        /*Sets the top axis space in percent of the full range.*/
        leftAxis.setSpaceTop(35f);

        leftAxis.setAxisMinimum(0f);
        mHistoryBarChart.getAxisRight().setEnabled(false);


    }


    private void setDataHistoryBarChart(List<TransactionEntry> allEntries) {


        float barWidth = 0.4f;

        Legend l = mHistoryBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(12f);

        if (mHistoryPeriodType != R.id.dialog_history_period_by_month) {
            /*display an extra legend to explain the meaning of weekly x-axis label*/
            List<LegendEntry> entries = new ArrayList<>();
            LegendEntry lEntry = new LegendEntry();
            lEntry.label = getString(R.string.chart_history_chart_description);
            lEntry.formColor = Color.WHITE;
            entries.add(lEntry);
            l.setExtra(entries);
        }

        List<Float> chartDataRevenueSums = new ArrayList<>();
        List<Float> chartDataExpenseSums = new ArrayList<>();

        extractEntriesToRevenueAndExpenseVariableForHistoryChart(allEntries, chartDataRevenueSums, chartDataExpenseSums);

        ArrayList<BarEntry> mRevenuesBarEntries = new ArrayList<>();
        ArrayList<BarEntry> mExpensesBarEntries = new ArrayList<>();

        Log.d("test_tt", "bar entries before fill data" + mRevenuesBarEntries.toString() + "  ()()()  " + mExpensesBarEntries.toString());
        convertTwoSumListIntoBarEntriesList(mRevenuesBarEntries, mExpensesBarEntries, chartDataRevenueSums, chartDataExpenseSums);
        Log.d("test_tt", "bar entries after fill data" + mRevenuesBarEntries.toString() + "  ()()()  " + mExpensesBarEntries.toString());


        BarDataSet barDataSetR, barDataSetE;


        barDataSetR = new BarDataSet(mRevenuesBarEntries, getString(R.string.bar_chart_comparison_label_revenue));
        barDataSetR.setColor(getResources().getColor(R.color.color_money_in));
        barDataSetE = new BarDataSet(mExpensesBarEntries, getString(R.string.bar_chart_comparison_label_expense));
        barDataSetE.setColor(getResources().getColor(R.color.color_money_out));

        BarData data = new BarData(barDataSetR, barDataSetE);
        data.setValueFormatter(new MyLargeValueFormatter());

        mHistoryBarChart.setData(data);

        // specify the width each bar should have
        mHistoryBarChart.getBarData().setBarWidth(barWidth);


        setAxisRangeNUpdateChart();

//
//        int barChartXAxisStartTime;
//
//        XAxis xAxis = mHistoryBarChart.getXAxis();
//
//        if (mHistoryPeriodType == R.id.dialog_history_period_by_month) {
//            MonthValueFormatter xAxisFormatter = new MonthValueFormatter();
//
//
//            barChartXAxisStartTime = Integer.parseInt(
//                    DateTimeFormat.forPattern("YYYYMM").print(
//                            LocalDate.now().minusMonths(mNumberOfPeriodsToCompare)));
//            //todo   THIS IS AFTER FORMATER
//            Log.d("test_ff7", " month starting point  " + barChartXAxisStartTime);
//            xAxisFormatter.setStartDate(LocalDate.now().minusMonths(mNumberOfPeriodsToCompare));
//            xAxis.setValueFormatter(xAxisFormatter);
//
//        } else {
//            WeekValueFormatter xAxisFormatter = new WeekValueFormatter(mHistoryBarChart);
//            xAxisFormatter.setMaxPeriod(mNumberOfPeriodsToCompare);
//            xAxis.setValueFormatter(xAxisFormatter);
//
//            barChartXAxisStartTime = 0;
//            Log.d("test_flow555", " week  starting point  " + barChartXAxisStartTime);
//
//        }
//
//
//        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMinimum(barChartXAxisStartTime);
//        xAxis.setAxisMaximum(barChartXAxisStartTime + mHistoryBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * mNumberOfPeriodsToCompare);
//
//        if (mHistoryPeriodType == R.id.dialog_history_period_by_month) {
//            //todo, problems here
//
//
//        } else {
//
//
//        }
//
//
//        xAxis.setAxisMinimum(barChartXAxisStartTime);
//        xAxis.setAxisMaximum(barChartXAxisStartTime + mHistoryBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * mNumberOfPeriodsToCompare);
//
//
//        mHistoryBarChart.groupBars(barChartXAxisStartTime, groupSpace, barSpace);
//
//        mHistoryBarChart.invalidate();

    }

    private void setAxisRangeNUpdateChart() {
        float groupSpace = 0.08f;
        float barSpace = 0.06f;
        int barChartXAxisStartTime;

        XAxis xAxis = mHistoryBarChart.getXAxis();

        if (mHistoryPeriodType == R.id.dialog_history_period_by_month) {
            MonthValueFormatter xAxisFormatter = new MonthValueFormatter();


            barChartXAxisStartTime = Integer.parseInt(
                    DateTimeFormat.forPattern("YYYYMM").print(
                            LocalDate.now().minusMonths(mNumberOfPeriodsToCompare)));
            //todo   THIS IS AFTER FORMATER
            Log.d("test_ff7", " month starting point  " + barChartXAxisStartTime);
            xAxisFormatter.setStartDate(LocalDate.now().minusMonths(mNumberOfPeriodsToCompare));
            xAxis.setValueFormatter(xAxisFormatter);

        } else {
            WeekValueFormatter xAxisFormatter = new WeekValueFormatter(mHistoryBarChart);
            xAxisFormatter.setMaxPeriod(mNumberOfPeriodsToCompare);
            xAxis.setValueFormatter(xAxisFormatter);

            barChartXAxisStartTime = 0;
            Log.d("test_flow555", " week  starting point  " + barChartXAxisStartTime);

        }


        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(barChartXAxisStartTime);
        xAxis.setAxisMaximum(barChartXAxisStartTime + mHistoryBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * mNumberOfPeriodsToCompare);

        if (mHistoryPeriodType == R.id.dialog_history_period_by_month) {
            //todo, problems here


        } else {


        }


        xAxis.setAxisMinimum(barChartXAxisStartTime);
        xAxis.setAxisMaximum(barChartXAxisStartTime + mHistoryBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * mNumberOfPeriodsToCompare);


        mHistoryBarChart.groupBars(barChartXAxisStartTime, groupSpace, barSpace);

        mHistoryBarChart.invalidate();
    }

    private void convertTwoSumListIntoBarEntriesList(
            ArrayList<BarEntry> mRevenuesBarEntries, ArrayList<BarEntry> mExpensesBarEntries,
            List<Float> revenueSumList, List<Float> expenseSumList) {

        /* i < revenueSumList.size() is to handle the case where there is no entrys */
        for (int i = 0; i < mNumberOfPeriodsToCompare && i < revenueSumList.size(); i++) {

            if (mHistoryPeriodType == R.id.dialog_history_period_by_month) {
                //display by month-period data where x-axis value will be displayed on YYYYMM

                float currentMonth = Integer.parseInt(
                        DateTimeFormat.forPattern("YYYYMM").print(
                                LocalDate.now().minusMonths(mNumberOfPeriodsToCompare - i)));
                Log.d("test_ff7", "currentMonth is : " + currentMonth);
                mRevenuesBarEntries.add(new BarEntry(currentMonth, revenueSumList.get(i)));
                mExpensesBarEntries.add(new BarEntry(currentMonth, expenseSumList.get(i)));
            } else {
                Log.d("test_ff7", "currentweek is : " + i);

                //display by week-period data whic x-axis value will be displayed by number of weeks
                mRevenuesBarEntries.add(new BarEntry(i, revenueSumList.get(i)));
                mExpensesBarEntries.add(new BarEntry(i, expenseSumList.get(i)));
            }
        }
    }


    private void extractEntriesToRevenueAndExpenseVariableForHistoryChart
            (List<TransactionEntry> allEntries, List<Float> revenues, List<Float> expenses) {


        int testCounterRevenue = 0;
        int testCounterExpense = 0;
        int testCounterTotalOfEntries = 0;

        /*pointer in odd number to get ending date of a period*/
        int pointerForDates = 1;
        float tempRevenueSum = 0f;
        float tempExpenseSum = 0f;
        for (TransactionEntry entry : allEntries) {

            if (entry.getTime() > UnitUtil.getTodayInAppTimeFormat()) {
                Toast.makeText(getActivity(), "time excess today", Toast.LENGTH_LONG).show();
                return;
            }

            while (entry.getTime() > mDateListForEachPeriod.get(pointerForDates)) {
                revenues.add(tempRevenueSum);
                tempRevenueSum = 0;
                expenses.add(tempExpenseSum);
                tempExpenseSum = 0;
                pointerForDates += 2;

            }

            if (entry.getAmount() >= 0) {
                //add to revenue
                tempRevenueSum += entry.getAmount();
                testCounterRevenue++;
            } else {
                //add to expense
                tempExpenseSum += Math.abs(entry.getAmount());
                testCounterExpense++;
            }

        }
        //add last period's sum
        revenues.add(tempRevenueSum);
        expenses.add(tempExpenseSum);


    }


    private void setDataForCurrentChart(String type) {

        if (mUserSelection.equals(CATEGORY_PIECHART)) {
            enableCurrentPieChart();
            if (type.equals(CHART_EXPENSE)) {
                initializePieChart(mCurrentPieChartExpense, CHART_EXPENSE);

                setPieChartData(mCurrentPieChartExpense, mCategoryExpense, CHART_EXPENSE);

            } else {
                initializePieChart(mCurrentPieChartRevenue, CHART_REVENUE);
                setPieChartData(mCurrentPieChartRevenue, mCategoryRevenue, CHART_REVENUE);
            }

        } else {
            enableCurrentBarChart();

            if (type.equals(CHART_EXPENSE)) {
                //populate CHART_EXPENSE chart
                initializeCurrentBarChart(mCurrentBarChartExpense);
                setCurrentBarChartData(mCurrentBarChartExpense, mCategoryExpense, CHART_EXPENSE);
            } else {
                //populate CHART_REVENUE chart
                initializeCurrentBarChart(mCurrentBarChartRevenue);
                setCurrentBarChartData(mCurrentBarChartRevenue, mCategoryRevenue, CHART_REVENUE);

            }

        }

    }

    private void setCurrentBarChartData(BarChart barChart, HashMap<String, Float> hashMap, String type) {

        ArrayList<BarEntry> values = new ArrayList<>();
        String[] categoriesArray;
        String barDataSetString;
        List<Integer> colors;

        if (type.equals(CHART_REVENUE)) {
            mRevenueCategorySize = mGsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_REVENUE).size();
            categoriesArray = new String[mRevenueCategorySize];
            barDataSetString = getString(R.string.chart_name_text_revenue);
            colors = mColors.getNonRepeatingLightColors(mRevenueCategorySize);
        } else {
            mExpenseCategorySize = mGsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_EXPENSE).size();
            categoriesArray = new String[mExpenseCategorySize];
            barDataSetString = getString(R.string.chart_name_text_expense);
            colors = mColors.getNonRepeatingDarkColors(mExpenseCategorySize);

        }
        extractHashMapToArrayListOfBarEntryNCategories(hashMap, values, categoriesArray);

        BarDataSet barDataSet;

        barDataSet = new BarDataSet(values, barDataSetString);
        barDataSet.setDrawIcons(false);

        barDataSet.setColors(colors);

        int[] colorArray = covertFromListToArray(colors);


        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(10f);

        l.setExtra(colorArray, categoriesArray);


        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.barchart_marker_view, categoriesArray);
        // For bounds control
        mv.setChartView(barChart);
        barChart.setMarker(mv);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new MyLargeValueFormatter());


        barChart.setData(data);


        XAxis xAxis = barChart.getXAxis();

        xAxis.setGranularity(1f);

        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(categoriesArray.length);
        xAxis.setValueFormatter(new LabelFormatterCurrentBarChart(categoriesArray));

        barChart.invalidate();

    }

    private int[] covertFromListToArray(List<Integer> colors) {
        int[] output = new int[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            output[i] = colors.get(i);
        }
        return output;
    }

    private void initializeCurrentBarChart(BarChart barChart) {
        barChart.setOnChartValueSelectedListener(this);
        barChart.getDescription().setEnabled(false);


        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        /*Sets the top axis space in percent of the full range.*/
        leftAxis.setSpaceTop(35f);

        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);

    }

    private void enableCurrentPieChart() {
        /*enable piechart, disable barchart*/
        mCurrentBarChartExpense.setVisibility(View.GONE);
        mCurrentBarChartRevenue.setVisibility(View.GONE);
        mCurrentPieChartRevenue.setVisibility(View.VISIBLE);
        mCurrentPieChartExpense.setVisibility(View.VISIBLE);
    }

    private void enableCurrentBarChart() {
        /*enable barchart, disable piechart*/
        mCurrentPieChartExpense.setVisibility(View.GONE);
        mCurrentPieChartRevenue.setVisibility(View.GONE);
        mCurrentBarChartExpense.setVisibility(View.VISIBLE);
        mCurrentBarChartRevenue.setVisibility(View.VISIBLE);
    }

    private void initializePieChart(PieChart chart, String chartType) {


        //todo enabled percentage
        //enable percentage
        if (mIsPieChartShowPercentage) {
            chart.setUsePercentValues(true);
        } else {
            chart.setUsePercentValues(false);
        }
        chart.getDescription().setEnabled(false);

        //chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.9f);

        chart.setDrawHoleEnabled(true);

        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);

        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(55f);
        chart.setTransparentCircleRadius(58f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);

        chart.setRotationEnabled(true);

        chart.setHighlightPerTapEnabled(true);

        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        chart.setEntryLabelTextSize(12f);

    }

    private void setPieChartData(PieChart chart, HashMap<String, Float> categoryHashMap, String chartType) {
        //PieEntry entry = new PieEntry(float, String(name));
        ArrayList<PieEntry> entries = fromHashMapToArrayListOfPieEntry(categoryHashMap);

        PieDataSet pieDataSet = new PieDataSet(entries, "Categories");
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(9f);

        ArrayList<Integer> colors;

        SpannableString charCenterTxt;

        PieData data = new PieData();


        if (chartType.equals(CHART_EXPENSE)) {
            chart.setEntryLabelColor(Color.WHITE);
            pieDataSet.setValueTextColor(Color.WHITE);

            colors = mColors.getNonRepeatingDarkColors(entries.size());


            data.setValueTextColor(Color.WHITE);
            charCenterTxt = generateCenterSpannableText(
                    getString(R.string.chart_name_text_expense),
                    mCategoryExpense.size(),
                    entries.size()
            );
        } else {
            chart.setEntryLabelColor(Color.BLACK);
            pieDataSet.setValueTextColor(Color.BLACK);
            colors = mColors.getNonRepeatingLightColors(entries.size());

            data.setValueTextColor(Color.BLACK);
            charCenterTxt = generateCenterSpannableText(
                    getString(R.string.chart_name_text_revenue),
                    mCategoryRevenue.size(),
                    entries.size()
            );
        }
        pieDataSet.setColors(colors);
        ;

        data.setDataSet(pieDataSet);

        data.setValueFormatter(new MyPercentFormatter(chart));
        data.setValueTextSize(11f);
        chart.setData(data);

        chart.setCenterText(charCenterTxt);

        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
        //do nothing
    }


    private ArrayList<PieEntry> fromHashMapToArrayListOfPieEntry(HashMap<String, Float> categoryHashMap) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryHashMap.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            if (value != 0.0f) {
                /*do not display 0 value entry in piechart*/
                entries.add(new PieEntry(value, key));
            }
        }
        return entries;
    }

    private void extractHashMapToArrayListOfBarEntryNCategories(
            HashMap<String, Float> categoryHashMap, ArrayList<BarEntry> entries, String[] categoriesArray) {

        int counter = 0;
        for (Map.Entry<String, Float> entry : categoryHashMap.entrySet()) {
            String key = entry.getKey();

            Float value = entry.getValue();

            entries.add(new BarEntry(counter, value));
            categoriesArray[counter] = key;
            counter++;
        }

    }

    private void initializeHashMap(HashMap<String, Float> stringValueSet, List<String> listCategory) {
        for (String s : listCategory) {
            stringValueSet.put(s, 0f);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void categorizeExpenseValues(TransactionEntry entry, HashMap<String, Float> inputHasMap) {
        float temp;
        if (inputHasMap.containsKey(entry.getCategory())) {
            temp = inputHasMap.get(entry.getCategory());
            temp += Math.abs(entry.getAmount());
            inputHasMap.put(entry.getCategory(), temp);
        } else {
            temp = inputHasMap.get(getString(R.string.category_others));
            temp += Math.abs(entry.getAmount());
            inputHasMap.put(getString(R.string.category_others), temp);

        }
    }


    @SuppressWarnings("ConstantConditions")
    private void categorizeRevenueValues(TransactionEntry entry, HashMap<String, Float> inputHashMap) {
        float temp;
        if (inputHashMap.containsKey(entry.getCategory())) {
            temp = inputHashMap.get(entry.getCategory());
            temp += entry.getAmount();
            inputHashMap.put(entry.getCategory(), temp);
        } else {
                /*
                    if this category was not in user's current category
                    e.g. untagged transactions, past deleted category, add to others
                */
            temp = inputHashMap.get(getString(R.string.category_others));
            temp += entry.getAmount();
            inputHashMap.put(getString(R.string.category_others), temp);
        }
    }


    private SpannableString generateCenterSpannableText(String input, int numCategory, int availableCategory) {
        String tempCategory = String.valueOf(numCategory);
        String tempAvailable = String.valueOf(availableCategory);
        int lengthTotalCategory = tempCategory.length();
        int lengthAvailableCategory = tempAvailable.length();


        SpannableString s;
        if (numCategory == availableCategory) {
            s = new SpannableString(input + "\nData from all " + numCategory + " categories");
            s.setSpan(new RelativeSizeSpan(1.7f), 0, input.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), input.length(), s.length(), 0);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), input.length(), s.length(), 0);
            s.setSpan(new RelativeSizeSpan(.8f), input.length(), s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
            /*set keywords color*/
            s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()),
                    s.length() - (11 + lengthTotalCategory),
                    s.length() - 11,
                    0);

        } else {
            s = new SpannableString(input + "\nData comes from " + availableCategory + "/" + numCategory + " categories");
            s.setSpan(new RelativeSizeSpan(1.7f), 0, input.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), input.length(), s.length(), 0);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), input.length(), s.length(), 0);
            s.setSpan(new RelativeSizeSpan(.8f), input.length(), s.length(), 0);
            /*set keywords color*/
            s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()),
                    s.length() - (11 + lengthTotalCategory + lengthAvailableCategory + 1),
                    s.length() - (11 + lengthTotalCategory + 1),
                    0);
            s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                    s.length() - (11 + lengthTotalCategory + lengthAvailableCategory + 1),
                    s.length() - (11 + lengthTotalCategory + 1),
                    0);


        }


        return s;
    }


    private void loadPreferenceSetting() {


        mHistoryPeriodType = mAppPreferences.getInt(
                Constant.SETTING_CHART_HISTORY_PERIOD_TYPE_KEY,
                Constant.SETTING_CHART_HISTORY_PERIOD_TYPE_DEFAULT_VAL);

        mNumberOfPeriodsToCompare = mAppPreferences.getInt(
                Constant.SETTING_CHART_HISTORY_PERIOD_NUMBER_KEY,
                Constant.SETTING_CHART_HISTORY_PERIOD_NUMBER_DEFAULT
        );

        Log.d("test_ff7", "mNumberOfPeriodsToCompare  is assign : " + mNumberOfPeriodsToCompare);

        mCurrentChartType = mAppPreferences.getInt(
                Constant.SETTING_CHART_CURRENT_CHART_TYPE_KEY,
                Constant.SETTING_CHART_CURRENT_CHART_TYPE_DEFAULT_VAL
        );
        if (mCurrentChartType == Constant.SETTING_CHART_CURRENT_CHART_TYPE_DEFAULT_VAL) {
            mUserSelection = CATEGORY_PIECHART;
        } else {
            mUserSelection = CATEGORY_BARCHART;
        }

        mCurrentPeriodType = mAppPreferences.getInt(

                Constant.SETTING_CHART_CURRENT_CHART_PERIOD_KEY,
                Constant.SETTING_CHART_CURRENT_CHART_PERIOD_DEFAULT_VAL
        );

        if (mCurrentPeriodType == R.id.radioButton_current_period_type_custom) {
            mIsCustomCurrentPeriod = true;
            mNumOfCustomDays = mAppPreferences.getInt(
                    Constant.SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_KEY,
                    Constant.SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_DEFAULT_VAL
            );
        }

        mIsPieChartShowPercentage = mAppPreferences.getBoolean(
                Constant.SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_KEY,
                Constant.SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_DEFAULT_VAL
        );


        setDataCurrentPeriod(mNumOfCustomDays);
        setPeriodForHistoryChartOnPeriodType();
    }

    private void setDataCurrentPeriod(int customDays) {
        if (mCurrentPeriodType == R.id.radioButton_current_period_type_month) {

            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentMonth();

        } else if (mCurrentPeriodType == R.id.radioButton_current_period_type_week) {

            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentWeek();

        } else if (mCurrentPeriodType == R.id.radioButton_current_period_type_year) {
            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentYear();
        } else {

            /*it's custom period*/
            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentCustom(customDays);

        }
    }

    private void setPeriodForHistoryChartOnPeriodType() {

        if (mHistoryPeriodType == R.id.dialog_history_period_by_month) {

            mDateListForEachPeriod =
                    UnitUtil.getArrayOfStartEndDatesOnNumberOfCompareMonths(mNumberOfPeriodsToCompare);
        } else {

            mDateListForEachPeriod =
                    UnitUtil.getArrayOfStartEndDatesOnNumberOfCompareWeeks(mNumberOfPeriodsToCompare);
        }

        mHistoryChartStartDate = mDateListForEachPeriod.get(0);

        //get the last value in the mDateListForEachPeriod;
        mHistoryChartEndDate = mDateListForEachPeriod.get(mDateListForEachPeriod.size() - 1);

    }


//    private void loadPreferenceSetting() {
//
//        int mNumOfCustomDays = 0;
//
////        mHistoryPeriodType = mAppPreferences.getInt(
////
////                Constant.SETTING_CHART_HISTORY_PERIOD_TYPE_KEY,
////                R.id.dialog_history_period_by_month);
//
//
//        //todo, test let fragment gettting data from VM
//
//
//       /* mNumberOfPeriodsToCompare = mAppPreferences.getInt(
//                getString(R.string.setting_chart_dialog_history_period_number_key),
//                getResources().getInteger(R.integer.setting_chart_dialog_default_history_period_number));*/
//
//        Log.d("test_ff7", "mNumberOfPeriodsToCompare  is assign : " + mNumberOfPeriodsToCompare);
//
//      /*  int mCurrentChartType = mAppPreferences.getInt(
//                Constant.SETTING_CHART_CURRENT_CHART_TYPE_KEY,
//                R.id.radioButton_current_period_type_piechart
//        );
//        if (mCurrentChartType == R.id.radioButton_current_period_type_piechart) {
//            mUserSelection = CATEGORY_PIECHART;
//        } else {
//            mUserSelection = CATEGORY_BARCHART;
//        }*/
//
////        mCurrentPeriodType = mAppPreferences.getInt(
////                Constant.SETTING_CHART_CURRENT_CHART_PERIOD_KEY,
////                R.id.radioButton_current_period_type_month
////        );
////
////        if (mCurrentPeriodType == R.id.radioButton_current_period_type_custom) {
////            mIsCustomCurrentPeriod = true;
////            mNumOfCustomDays = mAppPreferences.getInt(
////                    Constant.SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_KEY,
////                    Constant.SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_DEFAULT_VAL
////            );
////        }
////
////        mIsPieChartShowPercentage = mAppPreferences.getBoolean(
////                Constant.SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_KEY,
////                Constant.SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_DEFAULT_VAL
////        );
//
//
////        setDataCurrentPeriod(mNumOfCustomDays);
//
//
//        //todo, test VM
//
//        /*setPeriodForHistoryChartOnPeriodType();*/
//    }
//
//    //    private void setDataCurrentPeriod(int customDays) {
////        if (mCurrentPeriodType == R.id.radioButton_current_period_type_month) {
////
////            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentMonth();
////
////        } else if (mCurrentPeriodType == R.id.radioButton_current_period_type_week) {
////
////            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentWeek();
////
////        } else if (mCurrentPeriodType == R.id.radioButton_current_period_type_year) {
////
////            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentYear();
////
////        } else {
////            /*it's custom period*/
////            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentCustom(customDays);
////
////        }
////    }
//    private void setDataCurrentPeriod() {
//        if (mCurrentPeriodType == R.id.radioButton_current_period_type_month) {
//
//            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentMonth();
//
//        } else if (mCurrentPeriodType == R.id.radioButton_current_period_type_week) {
//
//            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentWeek();
//
//        } else if (mCurrentPeriodType == R.id.radioButton_current_period_type_year) {
//
//            mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentYear();
//
//        }
//    }
//
//    private void setDataCurrentCustomPeriod(int customDays) {
//
//
//        mCurrentChartStartingDate = UnitUtil.getStartingDateCurrentCustom(customDays);
//
//
//    }
//
//    private void setPeriodForHistoryChartOnPeriodType() {
//
//        if (mHistoryPeriodType == R.id.dialog_history_period_by_month) {
//
//            mDateListForEachPeriod =
//                    UnitUtil.getArrayOfStartEndDatesOnNumberOfCompareMonths(mNumberOfPeriodsToCompare);
//        } else {
//
//            mDateListForEachPeriod =
//                    UnitUtil.getArrayOfStartEndDatesOnNumberOfCompareWeeks(mNumberOfPeriodsToCompare);
//        }
//
//        mHistoryChartStartDate = mDateListForEachPeriod.get(0);
//
//        //get the last value in the mDateListForEachPeriod;
//        mHistoryChartEndDate = mDateListForEachPeriod.get(mDateListForEachPeriod.size() - 1);
//
//    }




    private void setupSharedPreferenceViewModelObserverForChartDisplay(SPViewModel sharedPreferenceViewModel) {
        sharedPreferenceViewModel.getNumberOfHistoryPeriodSelectedByUserOnChartFrag().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (mNumberOfPeriodsToCompare != integer) {

                    mNumberOfPeriodsToCompare = integer;
                    setPeriodForHistoryChartOnPeriodType();

                    mChartDataViewModel.updatesHistoryDatesForVM(mHistoryChartStartDate, mHistoryChartEndDate);

                }
            }
        });

        sharedPreferenceViewModel.getmChartHistoryPeriodType().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (mHistoryPeriodType != integer) {

                    mHistoryPeriodType = integer;
                    setPeriodForHistoryChartOnPeriodType();
                    mChartDataViewModel.updatesHistoryDatesForVM(mHistoryChartStartDate, mHistoryChartEndDate);
                }
            }

        });

        sharedPreferenceViewModel.getmChartCurrentChartType().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (mCurrentChartType != integer) {

                    mCurrentChartType = integer;

                    if (mCurrentChartType == R.id.radioButton_current_period_type_piechart) {
                        mUserSelection = CATEGORY_PIECHART;
                    } else {
                        mUserSelection = CATEGORY_BARCHART;
                    }

                    mChartDataViewModel.updatesCurrentDatesforVM(mCurrentChartStartingDate);
                }


            }
        });

        sharedPreferenceViewModel.getmChartCurrentPeriodType().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (mCurrentPeriodType != integer) {

                    mCurrentPeriodType = integer;

                    if (mCurrentPeriodType != R.id.radioButton_current_period_type_custom) {
                        setDataCurrentPeriod(mNumOfCustomDays);
                        mChartDataViewModel.updatesCurrentDatesforVM(mCurrentChartStartingDate);
                    }

                }

            }
        });

        sharedPreferenceViewModel.getmChartCurrentPeriodCustomNum().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (mNumOfCustomDays != integer) {

                    mNumOfCustomDays = integer;

                    if (mCurrentPeriodType == R.id.radioButton_current_period_type_custom) {
                        mIsCustomCurrentPeriod = true;
                        setDataCurrentPeriod(integer);
                        mChartDataViewModel.updatesCurrentDatesforVM(mCurrentChartStartingDate);
                    }
                }

            }
        });

        sharedPreferenceViewModel.getmChartPercentageAmountBool().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mIsPieChartShowPercentage = aBoolean;

                if (mCurrentChartStartingDate != -1 && mCurrentPeriodType != -1 && mUserSelection != null) {

                    mChartDataViewModel.updatesCurrentDatesforVM(mCurrentChartStartingDate);
                }

            }
        });
    }

    private void setupChartDataViewModelObserver(ChartDataViewModel mChartDataViewModel) {
        mChartDataViewModel.getmExpenseListEntry().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {

                mCategoryExpense = new HashMap<>();
                initializeHashMap(
                        mCategoryExpense,
                        mGsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_EXPENSE)
                );
                for (int i = 0; i < transactionEntryList.size(); i++) {
                    TransactionEntry entry = transactionEntryList.get(i);
                    categorizeExpenseValues(entry, mCategoryExpense);
                }
                setDataForCurrentChart(CHART_EXPENSE);
            }
        });

        mChartDataViewModel.getmRevenueListEntry().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {

                mCategoryRevenue = new HashMap<>();

                initializeHashMap(
                        mCategoryRevenue,
                        mGsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_REVENUE)
                );

                for (TransactionEntry entry : transactionEntryList) {
                    categorizeRevenueValues(entry, mCategoryRevenue);
                }

                setDataForCurrentChart(CHART_REVENUE);

            }
        });

        mChartDataViewModel.getmHistoryListEntryPeriod().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                Log.d("test_new", "  chartFragment  observer");

                setDataHistoryBarChart(transactionEntryList);
            }
        });
    }
}
