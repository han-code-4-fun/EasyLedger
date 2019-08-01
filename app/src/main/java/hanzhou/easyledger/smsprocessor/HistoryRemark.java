package hanzhou.easyledger.smsprocessor;


import java.util.HashMap;

import hanzhou.easyledger.data.RepositoryDB;
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

    boolean isContainRemark(String inputRemark) {
        return mRemarks.containsKey(inputRemark);
    }

    String getCategoryFromRemark(String inputRemark) {
        return mRemarks.get(inputRemark);
    }

    private boolean addToRemarks(String remark, String category) {
        /*
         *    don't update "Withdrawal" and "Deposit" remarks
         *    because they are app's remark for debit card account
         *
         * */
        if (!remark.equals(Constant.RBC_WITHDRAWAL.toLowerCase())
                && !remark.equals(Constant.RBC_DEPOSIT.toLowerCase())) {

            /*only return true, if inserted new and modified previous*/

            if (!mRemarks.containsKey(remark)) {

                mRemarks.put(remark, category);
                return true;

            } else {

                String replacedCategory = mRemarks.put(remark, category);
                if (replacedCategory == null || !replacedCategory.equals(category)) {
                    return true;
                }
            }
        }
        return false;
    }


    private void saveToFile(GsonHelper gsonHelper) {
        gsonHelper.saveHashMapToSharedPreference(mRemarks, Constant.PREFERENCE_KEY_REMARK);
    }

    public void synchronizeUserTaggingBehaviour(String remark, String category, GsonHelper gsonHelper) {

        if (addToRemarks(remark, category)) {
            saveToFile(gsonHelper);
            applyUpdateToExistingUntaggedTransaction(remark, category);
        }
    }

    private void applyUpdateToExistingUntaggedTransaction(String remark, String category) {
        RepositoryDB.getInstance().applyUpdateToExistingUntaggedTransaction(remark, category);
    }

}
