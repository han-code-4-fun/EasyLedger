package hanzhou.easyledger_demo.utility;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hanzhou.easyledger_demo.data.TransactionEntry;


/*
 *
 *   Testing data class that generate testing datas
 *
 * */

public class TestingData {
    public static String[] month = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private static int mCurrentMonth = 1;

    private static void synchronizeCurrentMonth() {
        mCurrentMonth = UnitUtil.getTodayInAppTimeFormat() / 100 - 1900;
    }

    public static ArrayList<String> testgetString() {
        ArrayList<String> output = new ArrayList<>();

        for (int i = 1; i < 11; i++) {
            output.add("" + i);
        }


        return output;
    }


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


    public static List<TransactionEntry> create10kTransactions() {
        List<TransactionEntry> transactionEntryList = new ArrayList<>();

        return transactionEntryList;
    }


    public static List<TransactionEntry> create10DesignateTestingDataInCurrentWeek(GsonHelper gsonHelper) {
        synchronizeCurrentMonth();
        List<TransactionEntry> transactionEntryList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinThisWeek(gsonHelper));
        }

        return transactionEntryList;
    }

    private static TransactionEntry getAnEntryRevenueWithinThisWeek(GsonHelper gsonHelper) {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(gsonHelper),
                getRandomMonthNDateWithThisWeek(),
                amountTemp,
                getRandomCategory(amountTemp, gsonHelper),
                getRandomRemark()
        );
    }

    private static int getRandomMonthNDateWithThisWeek() {
        int dayOfWeek = LocalDate.now().getDayOfWeek();
        return getRandomMonthNDateWithinXdays(dayOfWeek);
    }

    public static List<TransactionEntry> create10DesignateTestingDataInCurrentMonth(GsonHelper gsonHelper) {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinThisMonth(gsonHelper));
        }

        return transactionEntryList;
    }

    private static TransactionEntry getAnEntryRevenueWithinThisMonth(GsonHelper gsonHelper) {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(gsonHelper),
                getRandomMonthNDateWithThisMonth(),
                amountTemp,
                getRandomCategory(amountTemp, gsonHelper),
                getRandomRemark()
        );
    }

    private static int getRandomMonthNDateWithThisMonth() {
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        return getRandomMonthNDateWithinXdays(dayOfMonth);
    }


    public static List<TransactionEntry> create10DesignateTestingDataInCertaindays(int daysBackFromToday, GsonHelper gsonHelper) {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            transactionEntryList.add(getAnEntryRevenueWithinXdays(daysBackFromToday, gsonHelper));
        }

        return transactionEntryList;
    }


    private static TransactionEntry getAnEntryRevenueWithinXdays(int daysBackFromToday, GsonHelper gsonHelper) {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(gsonHelper),
                getRandomMonthNDateWithinXdays(daysBackFromToday),
                amountTemp,
                getRandomCategory(amountTemp, gsonHelper),
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


    public static List<TransactionEntry> create1000Transactions(GsonHelper gsonHelper) {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();


        for (int i = 0; i < 1000; i++) {
            transactionEntryList.add(getARandomTransaction(gsonHelper));
        }


        return transactionEntryList;
    }

    public static List<TransactionEntry> create5UntaggedTransactions(GsonHelper gsonHelper) {
        synchronizeCurrentMonth();

        List<TransactionEntry> transactionEntryList = new ArrayList<>();


        for (int i = 0; i < 5; i++) {
            transactionEntryList.add(getARandomUntaggedTransaction(gsonHelper));
        }


        return transactionEntryList;
    }

    private static TransactionEntry getARandomUntaggedTransaction(GsonHelper gsonHelper) {
        return new TransactionEntry(
                getRandomLedger(gsonHelper),
                getRandomMonthNDate(),
                getRandomAmountBetween3000PositiveNNegative(),
                Constant.UNTAGGED,
                getRandomRemark());
    }


    private static TransactionEntry getARandomTransaction(GsonHelper gsonHelper) {
        float amountTemp = getRandomAmountBetween3000PositiveNNegative();
        return new TransactionEntry(
                getRandomLedger(gsonHelper),
                getRandomMonthNDate(),
                amountTemp,
                getRandomCategory(amountTemp, gsonHelper),
                getRandomRemark());
    }

    private static String getRandomLedger(GsonHelper gsonHelper) {
        ArrayList<String> ledger = gsonHelper.getLedgers(Constant.LEDGERS);
        ledger.remove("OVERALL");
        String output = ledger.get((int) (Math.random() * ledger.size()));
        return output;
    }

    private static String getRandomCategory(float amount, GsonHelper gsonHelper) {
        ArrayList<String> ledger;
        String output;
        if (amount >= 0) {
            ledger = gsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_REVENUE);
            ledger.add(Constant.UNTAGGED);
            output = ledger.get((int) (Math.random() * ledger.size()));
        } else {
            ledger = gsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_EXPENSE);
            ledger.add(Constant.UNTAGGED);
            output = ledger.get((int) (Math.random() * ledger.size()));
        }
        return output;
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

        int tempPastDays = (int) (Math.random() * 400);

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