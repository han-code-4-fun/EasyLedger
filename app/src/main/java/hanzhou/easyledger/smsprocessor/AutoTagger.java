package hanzhou.easyledger.smsprocessor;

import android.util.Log;

import hanzhou.easyledger.utility.Constant;

/*
*   Auto tag users transaction based on user's history tag
*
*   e.g. if user tagg a remark(which is from bank MSG) shows "safeway" as Category "supermarket" for 2 times
*   and if user turn on autotag
*
*
* */

public class AutoTagger {
    /*
    *   at this stage, only work on RBC's
    * */

    private static boolean isAutoTaggerOn = true;

    public static void turnOnOffAutoTagger(){
        if (isAutoTaggerOn){
            isAutoTaggerOn = false;
            Log.d("test_sms", "turnOnOffAutoTagger:  current state ->"+isAutoTaggerOn);
        }else{
            isAutoTaggerOn = true;
            Log.d("test_sms", "turnOnOffAutoTagger:  current state ->"+isAutoTaggerOn);

        }
    }

    public static boolean getAutoTaggerState(){
        return isAutoTaggerOn;
    }


    public static String autoMarkCategory(String remark){
        //todo, do not mark withdrawal and deposit that are from debit card
        if (isAutoTaggerOn){
            return Constant.UNTAGGED;

        }

        return Constant.UNTAGGED;
    }

}
