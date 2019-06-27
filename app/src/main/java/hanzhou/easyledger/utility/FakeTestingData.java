package hanzhou.easyledger.utility;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.TransactionEntry;

public class FakeTestingData {
    private static String[] month = {"Jan","Feb","Mar","Apr","May",
            "Jun","Jul","Aug","Sep","Oct","Nov","","Dec"};

    private static String[] category = {
            "restaurant", "supermarket", "petrol", "housing", "bill",
            "travel","shopping", "kids","education",
            "a super long category name that is long enough to test the recyclerview's reaction"};

    public static List<TransactionEntry> create10kTransactions(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();


//        category[(int)(Math.random()*category.length)]



        return transactionEntryList;
    }

    public static List<TransactionEntry> create25UntaggedTransactions(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();


        for (int i = 0; i < 25; i++) {
            transactionEntryList.add(getARandomTransaction());
        }


        return transactionEntryList;
    }

    private static TransactionEntry getARandomTransaction(){
        String year = "19";
        return new TransactionEntry(
                "n/a",
                Integer.parseInt(year+getRandomMonthNDate()),
                getRandomAmount(),
                category[(int)(Math.random()*category.length)],
                "sample");
    }



    private static double getRandomAmount() {
        double output=0;

        output = Math.random()*3000;
        return output;
    }

    private static String getRandomMonthNDate(){
        String output = "";
        output +=(int)(Math.random()*12+1);
        output +=(int)(Math.random()*31+1);
        return output;
    }


}
