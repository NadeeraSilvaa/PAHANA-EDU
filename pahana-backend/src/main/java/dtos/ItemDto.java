package dtos;

public class ItemDto {
    private int itId;
    private String itName;
    private double itPrice;

    public int getItId() {
        return itId;
    }

    public void setItId(int itId) {
        this.itId = itId;
    }

    public String getItName() {
        return itName;
    }

    public void setItName(String itName) {
        this.itName = itName;
    }

    public double getItPrice() {
        return itPrice;
    }

    public void setItPrice(double itPrice) {
        this.itPrice = itPrice;
    }
}