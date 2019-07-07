package hanzhou.easyledger.utility;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import hanzhou.easyledger.data.TransactionEntry;

public class FakeTestingData {
    private static String[] month = {"Jan","Feb","Mar","Apr","May",
            "Jun","Jul","Aug","Sep","Oct","Nov","","Dec"};

    public static String[] ledger = {
            "RBC","HSBC"
    };

    private static String[] category = {
            "restaurant", "supermarket", "petrol",
            "travel","shopping",Constant.UNTAGGED,
            "medical"};

    private static String[] remarks = {
            "Shoppers Drug Mart",
            "TNT supermarket",
            "Random Sushi",
            "Super Ramen",
            "Levis",
            "Costco",
            "Movie",
            "Hilton Hotel",
            "Canada air",
            "Avis rental",
            "Fido",
            "BC Hydro"
    };

    public static List<TransactionEntry> create10kTransactions(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();

        return transactionEntryList;
    }



    public static List<TransactionEntry> create10DesignateTestingDataInCurrentWeek(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();
        for (int i = 0; i <10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinThisWeek());
        }

        return transactionEntryList;
    }

    private static TransactionEntry getAnEntryRevenueWithinThisWeek(){
        return new TransactionEntry(
                ledger[0],
                getRandomMonthNDateWithThisWeek(),
                getRandomAmountBetween3000PositiveNNegative(),
                getRandomCategory(),
                getRandomRemark()
        );
    }

    private static int getRandomMonthNDateWithThisWeek(){
        DateTime dt = new DateTime();
        int dayOfWeek = dt.getDayOfWeek();
        return getRandomMonthNDateWithinXdays(dayOfWeek);
    }

    public static List<TransactionEntry> create10DesignateTestingDataInCurrentMonth(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();
        for (int i = 0; i <10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinThisMonth());
        }

        return transactionEntryList;
    }
    private static TransactionEntry getAnEntryRevenueWithinThisMonth(){
        return new TransactionEntry(
                ledger[0],
                getRandomMonthNDateWithThisMonth(),
                getRandomAmountBetween3000PositiveNNegative(),
                getRandomCategory(),
                getRandomRemark()
        );
    }
    private static int getRandomMonthNDateWithThisMonth(){
        DateTime dt = new DateTime();
        int dayOfMonth = dt.getDayOfMonth();
        return getRandomMonthNDateWithinXdays(dayOfMonth);
    }


    public static List<TransactionEntry> create10DesignateTestingDataInCertaindays(int daysBackFromToday){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();

        for (int i = 0; i <10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinXdays(daysBackFromToday));
        }

        return transactionEntryList;
    }


    private static TransactionEntry getAnEntryRevenueWithinXdays(int daysBackFromToday){
        return new TransactionEntry(
                ledger[0],
                getRandomMonthNDateWithinXdays(daysBackFromToday),
                getRandomAmountBetween3000PositiveNNegative(),
                getRandomCategory(),
                getRandomRemark()
        );
    }
    private static int getRandomMonthNDateWithinXdays(int daysBackFromToday){
        Date today =LocalDate.now().minusDays(return0toXRandom(daysBackFromToday)).toDate();
        return UnitUtil.formatTime(today);
    }

    private static int return0toXRandom(int daysBackFromToday){
        return new Random().nextInt(daysBackFromToday);
    }


    public static List<TransactionEntry> create20Transactions(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();


        for (int i = 0; i <20; i++) {
            transactionEntryList.add(getARandomTransaction());
        }


        return transactionEntryList;
    }

    public static List<TransactionEntry> create20UntaggedTransactions(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();


//        for (int i = 0; i <20; i++) {
            transactionEntryList.add(getARandomUntaggedTransaction());
//        }


        return transactionEntryList;
    }

    private static TransactionEntry getARandomUntaggedTransaction(){
        return new TransactionEntry(
                ledger[0],
                getRandomMonthNDate(),
                getRandomAmountBetween3000PositiveNNegative(),
                Constant.UNTAGGED,
                getRandomRemark());
    }



    private static TransactionEntry getARandomTransaction(){

        return new TransactionEntry(
                ledger[0],
                getRandomMonthNDate(),
                getRandomAmountBetween3000PositiveNNegative(),
                getRandomCategory(),
                getRandomRemark());
    }

    private static String getRandomLedger(){
        return ledger[(int)(Math.random()*ledger.length)];
    }

    private static String getRandomCategory() {
        return category[(int)(Math.random()*category.length)];
    }


    private static float getRandomAmountBetween3000PositiveNNegative() {
        float output=0;

        Random random = new Random();

        output = random.nextFloat()*6000-3000;
        output = Math.round(output*100);
        output = output/100;

        return output;
    }

    private static int getRandomMonthNDate(){

        String output = "19";
        int temp =(int)(Math.random()*12+1);
        if(temp<10) {
            output += ("0"+temp);
        }else{
            output += temp;
        }
        temp = (int)(Math.random()*31+1);
        if(temp < 10){
            output += "0"+ temp;
        }else{
            output += temp;
        }

        return Integer.parseInt(output);
    }

    private static String getRandomRemark(){
        String output;
        output = remarks[(int)(Math.random()*remarks.length)];

        return output;
    }


}
