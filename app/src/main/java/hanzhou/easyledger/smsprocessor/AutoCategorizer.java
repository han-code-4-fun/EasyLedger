package hanzhou.easyledger.smsprocessor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

/*
 *   Auto tag users transaction based on user's history tag
 *
 *   e.g. if user tagg a remark(which is from bank MSG) shows "safeway" as Category "Grocery",
 *   all the later "safeway" remark that extracted from sms will be auto-matically categorize as "Grocery"
 *
 *
 * */

public class AutoCategorizer {
    /*
     *   at this stage, the app only work on RBC's sms because I am trying to contact other banks to get their data
     */


    public static String process(Context context, String remark, float amount) {

        if (!remark.equals("")) {

            if (Constant.getIsAutoTaggerOn()) {

                if (HistoryRemark.getInstance().isContainRemark(remark)) {

                    String category = HistoryRemark.getInstance().getCategoryFromRemark(remark);
                    SharedPreferences defaultSp =
                            context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
                    if(amount >=0){

                        //todo convert to arraylist
                        defaultSp.getString(Constant.CATEGORY_TYPE_REVENUE, null);

                    }else{

                        //todo convert to arraylist

                        defaultSp.getString(Constant.CATEGORY_TYPE_EXPENSE, null);
                    }

                }

            }
        }

        return Constant.UNTAGGED;
    }

}
