package hanzhou.easyledger.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.BackGroundColor;
import hanzhou.easyledger.utility.FakeTestingData;
import hanzhou.easyledger.utility.MyMarkerView;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.ChartDataViewModel;

public class ChartFragment extends Fragment implements
        OnChartValueSelectedListener {

    private static final String TAG = ChartFragment.class.getSimpleName();

    public static final String CATEGORY_BARCHART = "user_choose_barchart";
    public static final String CATEGORY_PIECHART = "user_choose_piechart";

    public static final String CHART_REVENUE = "a_revenue_chart";
    public static final String CHART_EXPENSE = "a_expense_chart";

    private TransactionDB mDb;
    private AppCompatActivity mAppCompatActivity;
    private ChartDataViewModel mChartDataViewModel;

    private String userSelection;

    private int mDisplayStartingDate;
    private int mNumberOfMonthsToCompare;

    private List<Integer> monthsDateListForEachPeriod;

    private PieChart mPieChartRevenue;
    private PieChart mPieChartExpense;
    private BarChart mBarChartComparison;

    private HashMap<String, Float> mCategoryRevenue;
    private HashMap<String, Float> mCategoryExpense;

    private ArrayList<BarEntry> mRevenuesBarEntries;
    private ArrayList<BarEntry> mExpensesBarEntries;

    private BackGroundColor mColors;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDb = TransactionDB.getInstance(context);
        mAppCompatActivity = (AppCompatActivity) context;

        //todo, user make selection
        mDisplayStartingDate = UnitUtil.getStartingDateCurrentMonth();

//        mDisplayStartingDate = UnitUtil.getStartingDateCurrentMonth();

        mNumberOfMonthsToCompare = 6;
        userSelection = CATEGORY_PIECHART;

        mColors = new BackGroundColor();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        mChartDataViewModel = ViewModelProviders.of(mAppCompatActivity).get(ChartDataViewModel.class);

        setDataStartingDate();
        setComparisonMonths();

        mPieChartRevenue = rootView.findViewById(R.id.chart_piechart_revenue);
        mPieChartExpense = rootView.findViewById(R.id.chart_piechart_expense);

        mBarChartComparison = rootView.findViewById(R.id.chart_barchart_comparision);

//        mCategoryRevenue = new HashMap<>();
//        mCategoryExpense = new HashMap<>();

        mRevenuesBarEntries = new ArrayList<>();
        mExpensesBarEntries = new ArrayList<>();


        initializeComparisonBarChart();


        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mChartDataViewModel.getmExpenseListEntry().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {

                mCategoryExpense = new HashMap<>();
                initializeHashMap(mCategoryExpense, FakeTestingData.getSpendCategory());

                for (TransactionEntry entry : transactionEntryList) {
//                    sumValuesToCategory(entry, mCategoryRevenue, mCategoryExpense);
//                    sumValuesToCategory(entry, mCategoryRevenue, mCategoryExpense);
                    categorizeExpenseValues(entry, mCategoryExpense);

                }

                setDataForRevenueNExpendseChart(CHART_EXPENSE);
            }
        });

        mChartDataViewModel.getmRevenueListEntry().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                mCategoryRevenue = new HashMap<>();
                initializeHashMap(mCategoryRevenue, FakeTestingData.getRevenueCategory());

                for (TransactionEntry entry : transactionEntryList) {
                    categorizeRevenueValues(entry, mCategoryRevenue);
                }
                setDataForRevenueNExpendseChart(CHART_REVENUE);

            }
        });

        mChartDataViewModel.getmAllListEntryPeriod().observe(getViewLifecycleOwner(), new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {

                Log.d("test_ff2", "getmAllListEntryPeriod  -> onChanged:  total ->" + transactionEntryList.size());

                //todo, add each into dataset of multiple barchart
                setDataComparisonBarChart(transactionEntryList);

            }
        });

    }


    private void initializeComparisonBarChart() {
        mBarChartComparison.setOnChartValueSelectedListener(this);
        mBarChartComparison.getDescription().setEnabled(false);
        //todo, may need to change this
        mBarChartComparison.setPinchZoom(true);
        mBarChartComparison.setDrawBarShadow(false);
        mBarChartComparison.setDrawGridBackground(false);

        MyMarkerView mv = new MyMarkerView(mAppCompatActivity, R.layout.barchart_marker_view);

        // For bounds control
        mv.setChartView(mBarChartComparison);
        // Set the marker to the mBarChartComparison
        mBarChartComparison.setMarker(mv);

        Legend l = mBarChartComparison.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);


        XAxis xAxis = mBarChartComparison.getXAxis();

        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        ValueFormatter xAxisFormatter = new MonthValueFormatter(mBarChartComparison);
        xAxis.setValueFormatter(xAxisFormatter);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                return String.valueOf((int) value);
//            }
//        });


        YAxis leftAxis = mBarChartComparison.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        /*Sets the top axis space in percent of the full range.*/
        leftAxis.setSpaceTop(35f);

        leftAxis.setAxisMinimum(0f);
        mBarChartComparison.getAxisRight().setEnabled(false);


    }


    private void setDataComparisonBarChart(List<TransactionEntry> allEntries) {


        float groupSpace = 0.08f;
        float barSpace = 0.06f;
        float barWidth = 0.4f;

        List<Float> chartDataRevenueSums = new ArrayList<>();
        List<Float> chartDataExpenseSums = new ArrayList<>();

        //seperating allEntries to individual period

        processEntireEntriesToByPeriodEnries(allEntries, chartDataRevenueSums, chartDataExpenseSums);

        Log.d("test_ff2", "chartDataRevenueSums.size(): " + chartDataRevenueSums.size());
        Log.d("test_ff2", "chartDataExpenseSums.size(): " + chartDataExpenseSums.size());

        for (int i =0; i< chartDataExpenseSums.size(); i++) {
            Log.d("test_ff3", "fffffffff: " + chartDataExpenseSums.get(i));
            Log.d("test_ff3", "fffffffff: " + chartDataRevenueSums.get(i));

        }


        ArrayList<BarEntry> mRevenuesBarEntries = new ArrayList<>();
        ArrayList<BarEntry> mExpensesBarEntries = new ArrayList<>();

        for (int i = 0; i < mNumberOfMonthsToCompare; i++) {


            //todo



//            int currentMonth = LocalDate.now().minusMonths(mNumberOfMonthsToCompare-i).getMonthOfYear();
            int currentMonth =  LocalDate.now().minusMonths(mNumberOfMonthsToCompare-i).getMonthOfYear()+
                    LocalDate.now().minusMonths(mNumberOfMonthsToCompare-i).getYear()*100;
            mRevenuesBarEntries.add(new BarEntry( currentMonth, chartDataRevenueSums.get(i)));
            mExpensesBarEntries.add(new BarEntry(currentMonth, chartDataExpenseSums.get(i)));
        }

        BarDataSet barDataSetR, barDataSetE;

        if (mBarChartComparison.getData() != null && mBarChartComparison.getData().getDataSetCount() > 0) {

            barDataSetR = (BarDataSet) mBarChartComparison.getData().getDataSetByIndex(0);
            barDataSetE = (BarDataSet) mBarChartComparison.getData().getDataSetByIndex(1);
            barDataSetR.setValues(mRevenuesBarEntries);
            barDataSetE.setValues(mExpensesBarEntries);
            mBarChartComparison.getData().notifyDataChanged();
            mBarChartComparison.notifyDataSetChanged();
        }else{
            barDataSetR = new BarDataSet(mRevenuesBarEntries, getString(R.string.bar_chart_comparison_label_revenue));
            barDataSetR.setColor(getResources().getColor(R.color.color_money_in));
            barDataSetE = new BarDataSet(mExpensesBarEntries, getString(R.string.bar_chart_comparison_label_expense));
            barDataSetE.setColor(getResources().getColor(R.color.color_money_out));

            BarData data = new BarData(barDataSetR, barDataSetE);
            data.setValueFormatter(new LargeValueFormatter());

            mBarChartComparison.setData(data);
        }

        // specify the width each bar should have
        mBarChartComparison.getBarData().setBarWidth(barWidth);


        //todo


        int startMonth = LocalDate.now().minusMonths(mNumberOfMonthsToCompare).getMonthOfYear()+
                LocalDate.now().minusMonths(mNumberOfMonthsToCompare).getYear()*100;

        // restrict the x-axis range
        mBarChartComparison.getXAxis().setAxisMinimum(startMonth);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mBarChartComparison.getXAxis().setAxisMaximum(startMonth + mBarChartComparison.getBarData().getGroupWidth(groupSpace, barSpace) * mNumberOfMonthsToCompare);
        mBarChartComparison.groupBars(startMonth, groupSpace, barSpace);
        mBarChartComparison.invalidate();


    }

    private void processEntireEntriesToByPeriodEnries(List<TransactionEntry> allEntries,List<Float> revenues,List<Float> expenses) {

        /*pointer in odd number to get ending date of a period*/
        int pointerForDates = 1;
        float tempRevenue = 0f;
        float tempExpense = 0f;
        for (TransactionEntry entry : allEntries) {

            //when this reach last period, it will not add them
            if (entry.getTime() > monthsDateListForEachPeriod.get(pointerForDates)) {
                if (pointerForDates < monthsDateListForEachPeriod.size() - 1) {
                    pointerForDates += 2;
                }

                revenues.add(tempRevenue);
                tempRevenue = 0;
                expenses.add(tempExpense);
                tempExpense = 0;

            }

            if (entry.getAmount() >= 0) {
                //add to revenue
                tempRevenue += entry.getAmount();
            } else {
                //add to expense
                tempExpense += Math.abs(entry.getAmount());
            }


        }

        //add last period's sum
        revenues.add(tempRevenue);
        expenses.add(tempExpense);

    }

    private float[] sumEntriesValues(List<TransactionEntry> entries) {
        float[] output = new float[2];
        float revenueSum = 0f;
        float expenseSum = 0f;
        for (TransactionEntry entry : entries) {
            if (entry.getAmount() >= 0) {
                revenueSum += entry.getAmount();
            } else {
                expenseSum += entry.getAmount();
            }

        }
        output[0] = revenueSum;
        output[1] = expenseSum;
        return output;
    }


    private void setDataStartingDate() {
        mChartDataViewModel.setmRevenueListEntry(mDisplayStartingDate);
        mChartDataViewModel.setmExpenseListEntry(mDisplayStartingDate);
    }

    private void setComparisonMonths() {

        monthsDateListForEachPeriod = UnitUtil.getArrayOfStartEndDatesOnNumberOfCompareMonths(mNumberOfMonthsToCompare);
//        for (int[] months : monthsDateListForEachPeriod) {
//            //todo, called the DB multiple times to get data for multiple data
//            mChartDataViewModel.setmAllListEntryPeriod(months[0], months[1]);
//        }
        int startDate = monthsDateListForEachPeriod.get(0);

        //get the last value in the monthsDateListForEachPeriod;
        int endDate = monthsDateListForEachPeriod.get(monthsDateListForEachPeriod.size() - 1);
        mChartDataViewModel.setmAllListEntryPeriod(startDate, endDate);
        Log.d("test_ff2", "setComparisonMonths: startdate+enddate is " + startDate + " + " + endDate);
    }

    private void setDataForRevenueNExpendseChart(String type) {
//        int numRevenueCategory = FakeTestingData.getRevenueCategory().size();
//        int numExpenseCategory = FakeTestingData.getSpendCategory().size();
//
//        int numberOfDateInPieChart = numExpenseCategory;

        if (userSelection.equals(CATEGORY_PIECHART)) {
            //display piechart, disable barchart

            if (type.equals(CHART_EXPENSE)) {
                initializePieChart(
                        mPieChartExpense,
                        generateCenterSpannableText(
                                getString(R.string.chart_pie_center_text_expense), mCategoryExpense.size()
                        ),
                        mCategoryExpense,
                        CHART_EXPENSE);
            } else {
                initializePieChart(
                        mPieChartRevenue,
                        generateCenterSpannableText(
                                getString(R.string.chart_pie_center_text_revenue), mCategoryRevenue.size()
                        ),
                        mCategoryRevenue,
                        CHART_REVENUE
                );
            }


        } else {
            //display barchart, disable piechart
        }


        //display multiple barchart


    }

    private void initializePieChart(
            PieChart chart,
            SpannableString inputString,
            HashMap<String, Float> categoryHashMap,
            String chartType) {

//        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);

        //chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.9f);

        //chart.setCenterTextTypeface(tfLight);

        chart.setCenterText(inputString);

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

        if (chartType.equals(CHART_EXPENSE)) {
            chart.setEntryLabelColor(Color.WHITE);
        } else {
            chart.setEntryLabelColor(Color.BLACK);

        }

        //        chart.setEntryLabelTypeface(tfRegular);

        chart.setEntryLabelTextSize(12f);

        setPieChartData(chart, categoryHashMap, chartType);
    }

    private void setPieChartData(PieChart chart, HashMap<String, Float> categoryHashMap, String chartType) {
        //PieEntry entry = new PieEntry(float, String(name));
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Float> entry : categoryHashMap.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            entries.add(new PieEntry(value, key));

        }

        PieDataSet pieDataSet = new PieDataSet(entries, "Categories");
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(9f);

        ArrayList<Integer> colors;
        if (chartType.equals(CHART_EXPENSE)) {
            colors = mColors.getNonRepeatingDarkColors(entries.size());
        } else {
            colors = mColors.getNonRepeatingLightColors(entries.size());
        }

        pieDataSet.setColors(colors);

        PieData data = new PieData(pieDataSet);
//        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        if (chartType.equals(CHART_EXPENSE)) {
            data.setValueTextColor(Color.WHITE);
        } else {
            data.setValueTextColor(Color.BLACK);
        }

//        data.setValueTypeface(tfLight);
        chart.setData(data);

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
                    e.g. untagged transactions, add to others
                */
            temp = inputHashMap.get(getString(R.string.category_others));
            temp += entry.getAmount();
            inputHashMap.put(getString(R.string.category_others), temp);
        }
    }


    private SpannableString generateCenterSpannableText(String input, int numCategory) {
        String tempCategory = String.valueOf(numCategory);
        int length = tempCategory.length();

        SpannableString s = new SpannableString(input + "\nYour have " + numCategory + " categories");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, input.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), input.length(), s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), input.length(), s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.8f), input.length(), s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - (11 + length), s.length() - 11, 0);
        return s;
    }

    private void setmNumberOfMonthsToCompare(int input) {
        mNumberOfMonthsToCompare = input;
        mRevenuesBarEntries = new ArrayList<>();
        mExpensesBarEntries = new ArrayList<>();
    }


}
