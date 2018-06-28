package www.sydlinaonline.com.userpharmacy.Model;

public class PharmacyAndMedicine {
    String pharmacyKey;
    String medicineKey;
    String medicineQuantity;

    public String getMedicineQuantity() {
        return medicineQuantity;
    }

    public void setMedicineQuantity(String medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
    }
    public PharmacyAndMedicine(){

    }

    public PharmacyAndMedicine(String pharmacyKey, String medicineKey,String medicineQuantity) {
        this.pharmacyKey = pharmacyKey;
        this.medicineKey = medicineKey;
        this.medicineQuantity = medicineQuantity;
    }

    public String getPharmacyKey() {
        return pharmacyKey;
    }

    public void setPharmacyKey(String pharmacyKey) {
        this.pharmacyKey = pharmacyKey;
    }

    public String getMedicineKey() {
        return medicineKey;
    }

    public void setMedicineKey(String medicineKey) {
        this.medicineKey = medicineKey;
    }

}
