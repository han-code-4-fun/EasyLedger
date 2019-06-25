package hanzhou.easyledger.util;

import java.util.ArrayList;
import java.util.List;

import hanzhou.easyledger.data.Transaction;
import hanzhou.easyledger.ui.MainActivity;

public class FakeTestingData {
    private static String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","","Dec"};
    private static String[] category = {"restaurant", "supermarket", "petrol", "housing", "bill", "travel","shopping", "kids","education"};

    public static List<Transaction> create10kTransactions(){
        List<Transaction> transactionList =new ArrayList<>();


//        category[(int)(Math.random()*category.length)]



        return transactionList;
    }

    public static List<Transaction> create10UntaggedTransactions(){
        List<Transaction> transactionList =new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            transactionList.add(getARandomTransaction());
        }


        return transactionList;
    }

    private static Transaction getARandomTransaction(){
        String time = "2019";
        return new Transaction(
                time.concat(month[(int)(Math.random()*12)]),
                Math.random()*3500,
                "",
                "test remark"
        );
    }
}
