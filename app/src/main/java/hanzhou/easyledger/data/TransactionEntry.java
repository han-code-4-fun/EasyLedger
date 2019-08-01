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

    private Float amount;

    private String category;

    private String remark;

    @Ignore
    public TransactionEntry(String ledger, int time, Float amount, String category, String remark) {
        this.ledger = ledger;
        this.time = time;
        this.amount = amount;
        this.category = category;
        this.remark = remark;
    }

    public TransactionEntry(int id, String ledger, int time, Float amount, String category, String remark) {
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
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


}

