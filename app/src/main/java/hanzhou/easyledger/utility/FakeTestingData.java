package hanzhou.easyledger.utility;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.TransactionEntry;

public class FakeTestingData {
    private static String[] month = {"Jan","Feb","Mar","Apr","May",
            "Jun","Jul","Aug","Sep","Oct","Nov","","Dec"};

    private static String[] category = {
            "restaurant", "supermarket", "petrol", "housing", "bill",
            "travel","shopping", "kids","education",
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

    public static List<TransactionEntry> create20UntaggedTransactions(){
        List<TransactionEntry> transactionEntryList =new ArrayList<>();


        for (int i = 0; i < 20; i++) {
            transactionEntryList.add(getARandomTransaction());
        }


        return transactionEntryList;
    }

    private static TransactionEntry getARandomTransaction(){

        return new TransactionEntry(
                Constant.untagged,
                getRandomMonthNDate(),
                getRandomAmount(),
                getRandomCategory(),
                getRandomRemark());
    }

    private static String getRandomCategory() {
        return category[(int)(Math.random()*category.length)];
    }


    private static double getRandomAmount() {
        double output=0;

        output = Math.random()*3000;
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
