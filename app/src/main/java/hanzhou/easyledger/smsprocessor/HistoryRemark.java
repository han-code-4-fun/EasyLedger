package hanzhou.easyledger.smsprocessor;

import android.util.Log;

import java.util.HashMap;

import hanzhou.easyledger.data.Repository;
import hanzhou.easyledger.data.RepositoryUpdate;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.GsonHelper;

public class HistoryRemark {

    private static HistoryRemark mInstance;

    private HashMap<String, String> mRemarks;

    private HistoryRemark() {
        mRemarks = new HashMap<>();
    }

    public static HistoryRemark getInstance() {
        if (mInstance == null) {
            mInstance = new HistoryRemark();
        }

        return mInstance;
    }

    public void loadFromFile(GsonHelper gsonHelper) {
        mRemarks.clear();
        mRemarks = gsonHelper.getHashMapFromSharedPreference(Constant.PREFERENCE_KEY_REMARK);
    }

    public HashMap<String, String> exportData() {
        return mRemarks;
    }

    public boolean isContainRemark(String inputRemark) {
        return mRemarks.containsKey(inputRemark);
    }

    public String getCategoryFromRemark(String inputRemark) {
        return mRemarks.get(inputRemark);
    }

    public boolean addToRemarks(String remark, String category) {
        /*
        *    don't update "Withdrawal" and "Deposit" remarks
        *    because they are app's remark for debit card account
        *
        * */
        if(!remark.equals(Constant.RBC_WITHDRAWAL )
                && !remark.equals(Constant.RBC_DEPOSIT)) {
            mRemarks.put(remark, category);
            return true;
        }
        return false;
    }

    public void removeFromRemarks(String remark) {
        mRemarks.remove(remark);
    }

    public void saveToFile(GsonHelper gsonHelper) {
        gsonHelper.saveHashMapToSharedPreference(mRemarks, Constant.PREFERENCE_KEY_REMARK);
    }

    public void synchronizeUserTaggingBehaviour(String remark, String category, GsonHelper gsonHelper){

        if(addToRemarks(remark,category)){
            Log.d("test_tagger", " after saving tagg -> \n ------->"+mRemarks.keySet()+"\n------->"+mRemarks.values() );
            saveToFile(gsonHelper);

            applyUpdateToExistingUntaggedTransaction(remark, category);
        }

    }

    private void applyUpdateToExistingUntaggedTransaction(String remark, String category) {
        RepositoryUpdate.getInstance().applyUpdateToExistingUntaggedTransaction(remark,category);
    }

}
