package hanzhou.easyledger.utility;

import java.util.ArrayList;

public class HelperUtilMoveToSharedPreferenceLater {
    public static int getNumberOfTransactionTables(){
        return 2;
    }

    private static ArrayList<String> ledgers= new ArrayList<>();

    public static ArrayList<String> getLedgers() {
        return ledgers;
    }

    public static void addOneLedger(String name){
        ledgers.add(name);
    }

    public static void removeALedger(String name){
        ledgers.remove(name);
    }

}
