package www.sydlinaonline.com.userpharmacy.Model;

public class Medicine {

    String name;
    String quantity;
    String category;
    String price;
    String description;
    String imageUrl;
    String pharmacyKey;
    String medicineKey;

    public Medicine(){

    }

    public Medicine(String name, String price, String description, String imageUrl, String medicineKey) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.medicineKey = medicineKey;
    }

    public String getMedicineKey() {
        return medicineKey;
    }

    public void setMedicineKey(String medicineKey) {
        this.medicineKey = medicineKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPharmacyKey() {
        return pharmacyKey;
    }

    public void setPharmacyKey(String pharmacyKey) {
        this.pharmacyKey = pharmacyKey;
    }
}
