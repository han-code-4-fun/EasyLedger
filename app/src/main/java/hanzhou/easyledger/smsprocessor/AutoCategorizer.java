package hanzhou.easyledger.smsprocessor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.GsonHelper;
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






    /*
    *
    *  return a category or n/a (untagged transaction) according to user's remark, based on user history remark-cateogry hashmap
    *
    *  and whether the returned category a present category
    *
    *
    * */
    public static String process(Context context, String remark, float amount) {

        if (!remark.equals("")) {

            if (Constant.getIsAutoTaggerOn()) {

                /*get hashmaped remark-category value and check if user 'used' this remark before of not*/

                if (HistoryRemark.getInstance().isContainRemark(remark)) {

                    String category = HistoryRemark.getInstance().getCategoryFromRemark(remark);

                    SharedPreferences defaultSp =
                            context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);

                    ArrayList<String> categoriesList;
                    if(amount >=0){

                        categoriesList = GsonHelper.getInstance().getDataFromSharedPreference(Constant.CATEGORY_TYPE_REVENUE);
                    }else{

                        categoriesList = GsonHelper.getInstance().getDataFromSharedPreference(Constant.CATEGORY_TYPE_EXPENSE);
                    }
                    /*only need to auto-mark if category is present categories*/
                    if(categoriesList.contains(category)){
                        return category;
                    }
                }

            }
        }

        return Constant.UNTAGGED;
    }

}
