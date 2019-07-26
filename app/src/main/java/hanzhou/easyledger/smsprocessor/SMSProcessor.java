package hanzhou.easyledger.smsprocessor;


/*
*   decide which bank's sms this is, then pass to SMSToTransactionEntry to get right info
*
*
*
* */

import android.content.Context;
import android.util.Log;

import hanzhou.easyledger.utility.Constant;

public class SMSProcessor {

    public static void processExtraction(Context context, String sender, String msgBody){

        /*check sender number first, then check msgBody*/
        checkSenderNumber(context,sender,msgBody);
    }

    private static void checkSenderNumber(Context context, String sender, String msgBody){
        switch (sender){
            case Constant.RBC_NUMBER:
                SMSToTransactionEntry.processRBCBankingSMS(context,msgBody);
                break;
            case Constant.BMO_NUMBER:
                SMSToTransactionEntry.processBMOBankingSMS(context,msgBody);
                break;
            case Constant.CIBC_NUMBER:
                SMSToTransactionEntry.processCIBCBankingSMS(context,msgBody);
                break;
            case Constant.HSBC_NUMBER:
                SMSToTransactionEntry.processHSBCBankingSMS(context,msgBody);
                break;
            default:
                //do sth
                checkMSGContent(context, msgBody);
                break;

        }

    }

    private static void checkMSGContent(Context context, String msgBody){
        if(msgBody.contains("RBC")){
            SMSToTransactionEntry.processRBCBankingSMS(context, msgBody);
        }
    }

}
