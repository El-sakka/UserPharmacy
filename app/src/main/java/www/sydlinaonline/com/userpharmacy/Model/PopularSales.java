package www.sydlinaonline.com.userpharmacy.Model;

public class PopularSales {

    private String medicineName;
    private int quantity;

    public PopularSales(){

    }
    public PopularSales(String medicineName, int quantity) {
        this.medicineName = medicineName;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
