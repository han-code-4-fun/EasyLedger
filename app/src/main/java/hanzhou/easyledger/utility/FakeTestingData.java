package hanzhou.easyledger.utility;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import hanzhou.easyledger.data.TransactionEntry;

public class FakeTestingData {
    public static String[] month = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",  "Dec"};

    public static String[] ledger = {
            "RBC", "Cash"
    };

    private static int mCurrentMonth = 1;

    private static void synchronizeCurrentMonth(){
        mCurrentMonth = UnitUtil.getTodayInAppTimeFormat()/100 - 1900;

    }

    public static ArrayList<String> testgetString(){
        ArrayList<String> output = new ArrayList<>();

        for (int i = 1  ; i < 11; i++) {
            output.add(""+i);
        }


        return output;
    }

    private static String[] categoryRevenue = {
            Constant.UNTAGGED, "Salary", "Commission", "Investment", "Interest",
            "Rental Income", "Pension", "RRSP", "Business Income",
            "Capital gains", "Debt Collection", "Lottery", "Others"};

    private static String[] categorySpending = {
            Constant.UNTAGGED, "Travel", "Supermarket", "Restaurant", "Shopping",
            "Commuting", "Auto", "Education", "Children",
            "Medical", "Personal Care", "House",
            "Credit Card Payment", "Others"};


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

    public static List<String> getLedgers() {
        List<String> output = new ArrayList<>();
        output.add("RBC");
        output.add("Cash");

        return output;
    }


    //todo, make "others" undeletable
    public static List<String> getRevenueCategory() {
        List<String> output = new ArrayList<>();

        output.add("Salary");
        output.add("Commission");
        output.add("Investment");
        output.add("Interest");
        output.add("Rental Income");
        output.add("Pension");
        output.add("RRSP");
        output.add("Business Income");
        output.add("Capital gains");
        output.add("Debt Collection");
        output.add("Lottery");
        output.add("Others");

        return output;
    }

    public static List<String> getExpenseCategory() {
        List<String> output = new ArrayList<>();

        output.add("Travel");
        output.add("Supermarket");
        output.add("Restaurant");
        output.add("Shopping");
        output.add("Commuting");
        output.add("Auto");
        output.add("Education");
        output.add("Children");
        output.add("Medical");
        output.add("Personal Care");
        output.add("House");
        output.add("Credit Card Payment");
        output.add("Others");

        return output;
    }



    public static List<TransactionEntry> create10kTransactions() {
        List<TransactionEntry> transactionEntryList = new ArrayList<>();

        return transactionEntryList;
    }


    public static List<TransactionEntry> create10DesignateTestingDataInCurrentWeek() {
        synchronizeCurrentMonth();
        List<TransactionEntry> transactionEntryList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinThisWeek());
        }

        return transactionEntryList;
    }

    private static TransactionEntry getAnEntryRevenueWithinThisWeek() {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(),
                getRandomMonthNDateWithThisWeek(),
                amountTemp,
                getRandomCategory(amountTemp),
                getRandomRemark()
        );
    }

    private static int getRandomMonthNDateWithThisWeek() {
        int dayOfWeek = LocalDate.now().getDayOfWeek();
        return getRandomMonthNDateWithinXdays(dayOfWeek);
    }

    public static List<TransactionEntry> create10DesignateTestingDataInCurrentMonth() {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinThisMonth());
        }

        return transactionEntryList;
    }

    private static TransactionEntry getAnEntryRevenueWithinThisMonth() {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(),
                getRandomMonthNDateWithThisMonth(),
                amountTemp,
                getRandomCategory(amountTemp),
                getRandomRemark()
        );
    }

    private static int getRandomMonthNDateWithThisMonth() {
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        return getRandomMonthNDateWithinXdays(dayOfMonth);
    }


    public static List<TransactionEntry> create10DesignateTestingDataInCertaindays(int daysBackFromToday) {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinXdays(daysBackFromToday));
        }

        return transactionEntryList;
    }


    private static TransactionEntry getAnEntryRevenueWithinXdays(int daysBackFromToday) {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(),
                getRandomMonthNDateWithinXdays(daysBackFromToday),
                amountTemp,
                getRandomCategory(amountTemp),
                getRandomRemark()
        );
    }

    private static int getRandomMonthNDateWithinXdays(int daysBackFromToday) {
        int randomDays = return0toXRandom(daysBackFromToday);
        String date = DateTimeFormat.forPattern("YYMMdd").print(LocalDate.now().minusDays(randomDays));
        return Integer.parseInt(date);
    }

    private static int return0toXRandom(int daysBackFromToday) {
        return new Random().nextInt(daysBackFromToday);
    }


    public static List<TransactionEntry> create1000Transactions() {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();


        for (int i = 0; i < 1000; i++) {
            transactionEntryList.add(getARandomTransaction());
        }


        return transactionEntryList;
    }

    public static List<TransactionEntry> create5UntaggedTransactions() {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();


        for (int i = 0; i < 5; i++) {
            transactionEntryList.add(getARandomUntaggedTransaction());
        }


        return transactionEntryList;
    }

    private static TransactionEntry getARandomUntaggedTransaction() {
        return new TransactionEntry(
                getRandomLedger(),
                getRandomMonthNDate(),
                getRandomAmountBetween3000PositiveNNegative(),
                Constant.UNTAGGED,
                getRandomRemark());
    }


    private static TransactionEntry getARandomTransaction() {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(),
                getRandomMonthNDate(),
                amountTemp,
                getRandomCategory(amountTemp),
                getRandomRemark());
    }

    private static String getRandomLedger() {
        String output =ledger[(int) (Math.random() * ledger.length)];
        Log.d("test_flow9", "getRandomLedger: "+ output);
        return output;
    }

    private static String getRandomCategory(float amount) {
        if (amount >= 0) {
            return categoryRevenue[(int) (Math.random() * categoryRevenue.length)];
        } else {
            return categorySpending[(int) (Math.random() * categorySpending.length)];
        }
    }


    private static float getRandomAmountBetween3000PositiveNNegative() {
        float output = 0;

        Random random = new Random();

        output = random.nextFloat() * 6000 - 3000;
        output = Math.round(output * 100);
        output = output / 100;

        return output;
    }

    private static int getRandomMonthNDate() {
//        String[] year = {"18","19"};
//        int selectYear = Integer.parseInt(year[(int) (Math.random() * year.length)]);
//
//        String output = String.valueOf(selectYear);
//        int temp;
//        if(selectYear == 19){
//            temp = (int)(Math.random() * (mCurrentMonth) + 1);
//        }else{
//
//            temp = (int) (Math.random() * 12 + 1);
//
//        }
//
//        if (temp < 10) {
//            output += ("0" + temp);
//        } else {
//            output += temp;
//        }
//        if(selectYear == 19 && temp == 07){
//
//        }else{
//            temp = (int) (Math.random() * 31 + 1);
//        }
//
//        if (temp < 10) {
//            output += "0" + temp;
//        } else {
//            output += temp;
//        }

        int tempPastDays = (int)(Math.random()*400);

        return UnitUtil.fromJodaTimeLocalDateToAppDateInteger(
                LocalDate.now().minusDays(tempPastDays)
        );

    }

    private static String getRandomRemark() {
        String output;
        output = remarks[(int) (Math.random() * remarks.length)];

        return output;
    }




}
