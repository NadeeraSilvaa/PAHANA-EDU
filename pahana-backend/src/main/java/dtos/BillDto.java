package dtos;

public class BillDto {
    private int bCustomerId;
    private double bTotal;

    public int getBCustomerId() {
        return bCustomerId;
    }

    public void setBCustomerId(int bCustomerId) {
        this.bCustomerId = bCustomerId;
    }

    public double getBTotal() {
        return bTotal;
    }

    public void setBTotal(double bTotal) {
        this.bTotal = bTotal;
    }
}