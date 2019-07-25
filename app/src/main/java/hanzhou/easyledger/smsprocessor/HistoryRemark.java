package hanzhou.easyledger.smsprocessor;

import java.util.HashMap;

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

    public void addToRemarks(String remark, String category) {
        mRemarks.put(remark, category);
    }

    public void removeFromRemarks(String remark) {
        mRemarks.remove(remark);
    }

    public void saveToFile(GsonHelper gsonHelper) {
        gsonHelper.saveHashMapToSharedPreference(mRemarks, Constant.PREFERENCE_KEY_REMARK);
    }

}
