package hanzhou.easyledger.smsprocessor;

import android.content.Context;
import android.util.Log;

import androidx.room.util.StringUtil;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.UnitUtil;

/*
 * extract sms's information according to different Bank sms type
 *
 *
 * */
class SMSToTransactionEntry {

    private static final String TAG = SMSToTransactionEntry.class.getSimpleName();

    static void processRBCBankingSMS(final Context context, String msgBody) {

        TransactionEntry transactionEntry = null;

        String ledgerName = Constant.RBC_LEDGER_NAME;

        if (msgBody.contains(Constant.CREDIT_CARD)) {

            transactionEntry = extractSMSCredit(context, ledgerName, msgBody);

        } else if (msgBody.contains(Constant.RBC_DEPOSIT)) {

            transactionEntry = extractSMSDebit(context, ledgerName, msgBody, Constant.RBC_DEPOSIT);

        } else if (msgBody.contains(Constant.RBC_WITHDRAWAL)) {
            transactionEntry = extractSMSDebit(context, ledgerName, msgBody, Constant.RBC_WITHDRAWAL);
        }

        if (transactionEntry != null) {
            insertEntry(context, transactionEntry);
        }
    }


    static void processBMOBankingSMS(final Context context, String msgBody) {

    }

    static void processCIBCBankingSMS(final Context context, String msgBody) {

    }

    static void processHSBCBankingSMS(final Context context, String msgBody) {

    }


    /*
     *   detail extracting method all depends on bank txt msg format
     *
     * */
    private static TransactionEntry extractSMSCredit(Context context, String ledgerName, String msgBody) {
        try {

            int amountStartPosition = msgBody.indexOf('$') + 1;
            int dotPosition = msgBody.indexOf('.', amountStartPosition);
            int amountEndPosition = dotPosition + 3;
            float amount =extractAmount(msgBody, amountStartPosition, amountEndPosition);
            amount = 0 - amount;

            int timePosition = msgBody.indexOf("made") + 5;

            String time = msgBody.substring(timePosition, msgBody.indexOf("at", timePosition) - 1);

            int timeInt = UnitUtil.fromMMMDDFormatToAppDateFormat(time);

            int creditCardRemarkStart = msgBody.indexOf("at", timePosition) + 3;

            String remark = msgBody.substring(creditCardRemarkStart, msgBody.indexOf('.', creditCardRemarkStart));
            remark = remark.trim().toLowerCase();
            String category = AutoCategorizer.process(context, remark, amount);

            return new TransactionEntry(ledgerName, timeInt, amount, category, remark);


        } catch (Exception e) {

            Log.e(TAG, context.getString(R.string.error_msg_extract_sms_fail), e);

            return null;
        }
    }


    /*
     *   detail extracting method all depends on bank txt msg format
     *
     * */

    private static TransactionEntry extractSMSDebit(Context context, String ledgerName, String msgBody, String remark) {
        try {
            int amountStartPosition = msgBody.indexOf('$') + 1;

            int dotPosition = msgBody.indexOf('.', amountStartPosition);

            int amountEndPosition = dotPosition + 3;

            float amount = extractAmount(msgBody, amountStartPosition, amountEndPosition);

            if (remark.equals(Constant.RBC_WITHDRAWAL)) {
                amount = 0 - amount;
            }
            int timePosition = msgBody.indexOf("made") + 5;

            String time = msgBody.substring(timePosition, msgBody.indexOf('.', timePosition));

            int timeInt = UnitUtil.fromMMMDDFormatToAppDateFormat(time);

            remark = remark.toLowerCase();

            String category = AutoCategorizer.process(context, remark, amount);

            return new TransactionEntry(ledgerName, timeInt, amount, category, remark);

        } catch (Exception e) {
            Log.e(TAG, context.getString(R.string.error_msg_extract_sms_fail), e);
            return null;
        }
    }

    private static float extractAmount(String string, int start, int end) {
        float output = 0;

        String amount = string.substring(start, end);

        for (int i = 0; i < amount.length(); i++) {
            if (!isNumber(amount.charAt(i))) {
                amount = amount.substring(0, i) + amount.substring(i + 1);
                i--;
            }

        }

        output = Float.parseFloat(amount);

        return output;
    }

    private static boolean isNumber(char x) {
        char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
        for (char number : numbers) {
            if (x == number) {
                return true;
            }
        }
        return false;
    }


    private static void insertEntry(final Context context, final TransactionEntry entry) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                TransactionDB.getInstance(context).transactionDAO().insertTransaction(entry);
            }
        });
    }
}





















