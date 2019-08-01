package hanzhou.easyledger.smsprocessor;


/*
 *   decide which bank's sms this is, then pass to SMSToTransactionEntry to get right info
 *
 *
 *
 * */

import android.content.Context;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;

class SMSProcessor {

    /*
     *   identify which bank does this msg belongs to
     *   so to extract relative information according to its format
     *
     * */
    static void processExtraction(Context context, String sender, String msgBody) {

        /*check sender number first, then check msgBody*/
        checkSenderNumber(context, sender, msgBody);
    }

    private static void checkSenderNumber(Context context, String sender, String msgBody) {
        switch (sender) {
            case Constant.RBC_NUMBER:
                SMSToTransactionEntry.processRBCBankingSMS(context, msgBody);
                break;
            case Constant.BMO_NUMBER:
                SMSToTransactionEntry.processBMOBankingSMS(context, msgBody);
                break;
            case Constant.CIBC_NUMBER:
                SMSToTransactionEntry.processCIBCBankingSMS(context, msgBody);
                break;
            case Constant.HSBC_NUMBER:
                SMSToTransactionEntry.processHSBCBankingSMS(context, msgBody);
                break;
            default:
                /*do more time consuming check*/
                checkMSGContent(context, msgBody);
                break;

        }

    }

    private static void checkMSGContent(Context context, String msgBody) {
        if (msgBody.contains(context.getString(R.string.ledger_name_rbc))) {
            SMSToTransactionEntry.processRBCBankingSMS(context, msgBody);
        }
    }

}
