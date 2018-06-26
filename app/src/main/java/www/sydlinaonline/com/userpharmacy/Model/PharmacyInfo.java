package www.sydlinaonline.com.userpharmacy.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class PharmacyInfo implements Parcelable {

    private String pharmacyName;
    private String pharmacyPhone;
    private String pharmacyLat;
    private String pharmacyLan;
    private String pharmacyKey;



    public PharmacyInfo() {
    }

    public PharmacyInfo(String pharmacyName, String pharmacyPhone, String pharmacyLat, String pharmacyLan, String pharmacyKey) {
        this.pharmacyName = pharmacyName;
        this.pharmacyPhone = pharmacyPhone;
        this.pharmacyLat = pharmacyLat;
        this.pharmacyLan = pharmacyLan;
        this.pharmacyKey = pharmacyKey;
    }

    protected PharmacyInfo(Parcel in) {
        pharmacyName = in.readString();
        pharmacyPhone = in.readString();
        pharmacyLat = in.readString();
        pharmacyLan = in.readString();
        pharmacyKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pharmacyName);
        dest.writeString(pharmacyPhone);
        dest.writeString(pharmacyLat);
        dest.writeString(pharmacyLan);
        dest.writeString(pharmacyKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PharmacyInfo> CREATOR = new Creator<PharmacyInfo>() {
        @Override
        public PharmacyInfo createFromParcel(Parcel in) {
            return new PharmacyInfo(in);
        }

        @Override
        public PharmacyInfo[] newArray(int size) {
            return new PharmacyInfo[size];
        }
    };

    public String getPharmacyLat() {
        return pharmacyLat;
    }

    public void setPharmacyLat(String pharmacyLat) {
        this.pharmacyLat = pharmacyLat;
    }

    public String getPharmacyLan() {
        return pharmacyLan;
    }

    public void setPharmacyLan(String pharmacyLan) {
        this.pharmacyLan = pharmacyLan;
    }

    public String getPharmacyKey() {
        return pharmacyKey;
    }

    public void setPharmacyKey(String pharmacyKey) {
        this.pharmacyKey = pharmacyKey;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getPharmacyPhone() {
        return pharmacyPhone;
    }

    public void setPharmacyPhone(String pharmacyPhone) {
        this.pharmacyPhone = pharmacyPhone;
    }
}

