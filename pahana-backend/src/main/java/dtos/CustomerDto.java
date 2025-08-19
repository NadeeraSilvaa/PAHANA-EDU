package dtos;

public class CustomerDto {
    private int accNum;
    private String custName;
    private String custAddr;
    private String custPhone;
    private int custUnits;

    public int getAccNum() {
        return accNum;
    }

    public void setAccNum(int accNum) {
        this.accNum = accNum;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddr() {
        return custAddr;
    }

    public void setCustAddr(String custAddr) {
        this.custAddr = custAddr;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public int getCustUnits() {
        return custUnits;
    }

    public void setCustUnits(int custUnits) {
        this.custUnits = custUnits;
    }
}