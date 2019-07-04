package hanzhou.easyledger.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "transactions")
public class TransactionEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String ledger;

    private int time;

    private double amount;

    private String category;

    private String remark;

    @Ignore
    public TransactionEntry(String ledger, int time, double amount, String category, String remark) {
        this.ledger = ledger;
        this.time = time;
        this.amount = amount;
        this.category = category;
        this.remark = remark;
    }

    public TransactionEntry(int id, String ledger, int time, double amount, String category, String remark) {
        this.id = id;
        this.ledger = ledger;
        this.time = time;
        this.amount = amount;
        this.category = category;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //    }
//        this.tagged = tagged;
//    public void setTagged(int tagged) {
//
//    }
//        return tagged;
//    public int getTagged() {
//    }
//        this.mRecordTime = mRecordTime;
//    public void setmRecordTime(long mRecordTime) {
//
//    }
//        return mRecordTime;
//    public long getmRecordTime() {
//
    /*    public TransactionEntry(int id, int mTime, double mAmount, String mCatogory, String mRemark, String mLedger) {
            this.id = id;
            this.mTime = mTime;
            this.mAmount = mAmount;
            this.mCatogory = mCatogory;
            this.mRemark = mRemark;
            this.mLedgerName = mLedger;
    //        this.mRecordTime = currentTime;
        }*/
//    private int tagged;
//    @ColumnInfo(name = "is_tagged")
//     */
//     *   UNTAGGED transactions marked as 0, tagged will be 1
//     *   tracks whether a transaction is tagged or not
//    /*
    /*
        mLedgerName == "n/a" means it's an UNTAGGED transaction
        that need to be tagged by user
    */
//    /*
//       this app will use only one table, so a transaction will
//       not have a timeStamp until user "tag" it. And it will
//       be used for sorting purpose (instead of the 'id' in table)
//       as this best fits user's memory
//
//       e.g. a user may received a msg from bank about a transaction,
//            but the user may not open the app to tag it,
//     */
//    @ColumnInfo(name = "timestamp_in_table")
//    private long mRecordTime;

}

