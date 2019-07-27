package hanzhou.easyledger.smsprocessor;

import android.content.Context;
import android.util.Log;

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
public class SMSToTransactionEntry {

    private static final String TAG = SMSToTransactionEntry.class.getSimpleName();

    public static void processRBCBankingSMS(final Context context, String msgBody) {

        TransactionEntry transactionEntry = null;

        //todo, assign a ledger that is used temperatorily
        String ledgerName = "RBC";

        if (msgBody.contains(Constant.CREDIT_CARD)) {

            transactionEntry = extractSMSCredit(context, ledgerName, msgBody);

        } else if (msgBody.contains(Constant.RBC_DEPOSIT)) {

            transactionEntry = extractSMSDebit(context,ledgerName, msgBody, Constant.RBC_DEPOSIT);

        } else if (msgBody.contains(Constant.RBC_WITHDRAWAL)) {
            transactionEntry = extractSMSDebit(context, ledgerName, msgBody, Constant.RBC_WITHDRAWAL);
        }

        if (transactionEntry != null) {
            insertEntry(context, transactionEntry);
        }
    }


    public static void processBMOBankingSMS(final Context context, String msgBody) {

    }

    public static void processCIBCBankingSMS(final Context context, String msgBody) {

    }

    public static void processHSBCBankingSMS(final Context context, String msgBody) {

    }

    private static TransactionEntry extractSMSCredit(Context context,String ledgerName, String msgBody) {
        try {

            int amountPosition = msgBody.indexOf('$') + 1;
            int dotPosition = msgBody.indexOf('.', amountPosition);
            int amountEndPosition = dotPosition + 3;
            float amount = Float.parseFloat(msgBody.substring(amountPosition, amountEndPosition));
            amount = 0 - amount;

            int timePosition = msgBody.indexOf("made") + 5;

            String time = msgBody.substring(timePosition, msgBody.indexOf("at", timePosition)-1);

            int timeInt = UnitUtil.fromMMMDDFormatToAppDateFormat(time);

            //todo, change the remark for credit card
            int creditCardRemarkStart = msgBody.indexOf("at", timePosition)+3;

            String remark = msgBody.substring(creditCardRemarkStart, msgBody.indexOf('.',creditCardRemarkStart) ) ;
            remark = remark.trim().toLowerCase();
            String category = AutoCategorizer.process(context, remark, amount);

            return new TransactionEntry(ledgerName, timeInt, amount, category, remark);


        } catch (Exception e) {

            Log.e(TAG, "extractSMSDebit:   error while converting sms to TransactionEntry ", e);

            return null;
        }
    }

    private static TransactionEntry extractSMSDebit(Context context, String ledgerName, String msgBody, String remark) {
        try {
            int amountPosition = msgBody.indexOf('$') + 1;

            int dotPosition = msgBody.indexOf('.', amountPosition);

            int amountEndPosition = dotPosition + 3;

            float amount = Float.parseFloat(msgBody.substring(amountPosition, amountEndPosition));

            if (remark.equals(Constant.RBC_WITHDRAWAL)) {
                amount = 0 - amount;
            }
            int timePosition = msgBody.indexOf("made") + 5;

            String time = msgBody.substring(timePosition, msgBody.indexOf('.', timePosition));

            int timeInt = UnitUtil.fromMMMDDFormatToAppDateFormat(time);

            remark = remark.toLowerCase();

            String category = AutoCategorizer.process(context,remark, amount);

            return new TransactionEntry(ledgerName, timeInt, amount, category, remark);

        } catch (Exception e) {
            Log.e(TAG, "extractSMSDebit:   error while converting sms to TransactionEntry ", e);
            return null;
        }
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