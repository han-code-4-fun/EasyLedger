package hanzhou.easyledger.Test;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hanzhou.easyledger.utility.Constant;

public class SPViewModel extends ViewModel {

    private MutableLiveData<SharedPreferences> sharedPreferences;
//    private MutableLiveData<Integer> mChartHistoryPeriodNumber;

    private SPIntLiveData mChartHistoryPeriodNumber;
    private SPIntLiveData mChartHistoryPeriodType;
    private SPIntLiveData mChartCurrentChartType;
    private SPIntLiveData mChartCurrentPeriodType;
    private SPIntLiveData mChartCurrentPeriodCustomNum;

    private SPBoolLiveData mChartPercentageAmountBool;


//    public SPViewModel(SharedPreferences inputSP , String inputKey, int inputVal) {
//       /* sharedPreferences = new MutableLiveData<>();
//        sharedPreferences.setValue(inputSP);
//        mChartHistoryPeriodNumber = new MutableLiveData<>();*/
//
//       testInteger = new SPIntLiveData(inputSP,inputKey,inputVal);
//
//    }

    /* public MutableLiveData<Integer> getHistoryPeriod(){
         return mChartHistoryPeriodNumber;
     }

     private void updateHistoryPeriod(int input){
         mChartHistoryPeriodNumber.setValue(input);
     }*/
    public SPViewModel(SharedPreferences inputSP) {
       /* sharedPreferences = new MutableLiveData<>();
        sharedPreferences.setValue(inputSP);
        mChartHistoryPeriodNumber = new MutableLiveData<>();*/

        mChartHistoryPeriodNumber = new SPIntLiveData(
                inputSP,
                Constant.SETTING_CHART_HISTORY_PERIOD_NUMBER_KEY,
                Constant.SETTING_CHART_HISTORY_PERIOD_NUMBER_DEFAULT);

        mChartHistoryPeriodType = new SPIntLiveData(
                inputSP,
                Constant.SETTING_CHART_HISTORY_PERIOD_TYPE_KEY,
                Constant.SETTING_CHART_HISTORY_PERIOD_TYPE_DEFAULT_VAL
        );

        mChartCurrentChartType = new SPIntLiveData(
                inputSP,
                Constant.SETTING_CHART_CURRENT_CHART_TYPE_KEY,
                Constant.SETTING_CHART_CURRENT_CHART_TYPE_DEFAULT_VAL
        );

        mChartCurrentPeriodType = new SPIntLiveData(
                inputSP,
                Constant.SETTING_CHART_CURRENT_CHART_PERIOD_KEY,
                Constant.SETTING_CHART_CURRENT_CHART_PERIOD_DEFAULT_VAL
        );

        mChartCurrentPeriodCustomNum = new SPIntLiveData(
                inputSP,
                Constant.SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_KEY,
                Constant.SETTING_CHART_CURRENT_CHART_PERIOD_CUSTOM_DEFAULT_VAL
        );

        mChartPercentageAmountBool = new SPBoolLiveData(
                inputSP,
                Constant.SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_KEY,
                Constant.SETTING_CHART_PIECHART_PERCENTAGE_AMOUNT_DEFAULT_VAL
        );

    }


    public SPIntLiveData getChartHistoryPeriodNumber() {
        return mChartHistoryPeriodNumber;

    }

    public SPIntLiveData getmChartHistoryPeriodType(){
        return mChartHistoryPeriodType;
    }

    public SPIntLiveData getmChartCurrentChartType(){
        return mChartCurrentChartType;
    }

    public SPIntLiveData getmChartCurrentPeriodType(){
        return mChartCurrentPeriodType;
    }

    public SPIntLiveData getmChartCurrentPeriodCustomNum(){
        return mChartCurrentPeriodCustomNum;
    }

    public SPBoolLiveData getmChartPercentageAmountBool(){
        return mChartPercentageAmountBool;
    }


}
