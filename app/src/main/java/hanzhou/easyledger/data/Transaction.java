package hanzhou.easyledger.data;

public class Transaction {

    private int mID;

    private String mTime;

    private double mAmount;

    private String mCatogory;
    private String mRemark;


    public Transaction(String mTime, double mAmount, String mCatogory, String mRemark) {
        this.mTime = mTime;
        this.mAmount = mAmount;
        this.mCatogory = mCatogory;
        this.mRemark = mRemark;
    }

    public Transaction(int mID, String mTime, double mAmount, String mCatogory, String mRemark) {
        this.mID = mID;
        this.mTime = mTime;
        this.mAmount = mAmount;
        this.mCatogory = mCatogory;
        this.mRemark = mRemark;
    }





    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public double getmAmount() {
        return mAmount;
    }

    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public String getmCatogory() {
        return mCatogory;
    }

    public void setmCatogory(String mCatogory) {
        this.mCatogory = mCatogory;
    }

    public String getmRemark() {
        return mRemark;
    }

    public void setmRemark(String mRemark) {
        this.mRemark = mRemark;
    }
}
