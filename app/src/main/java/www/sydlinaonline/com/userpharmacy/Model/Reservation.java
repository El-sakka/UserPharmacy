package www.sydlinaonline.com.userpharmacy.Model;

public class Reservation {
    private String phrmacyName;
    private String userEmail;
    private String code;
    private String Location;
    private String quantity;
    private String date;
    private String medName;

    public Reservation(String phrmacyName, String userEmail, String code, String location,String quantity,String date,String medName) {
        this.phrmacyName = phrmacyName;
        this.userEmail = userEmail;
        this.code = code;
        Location = location;
        this.quantity = quantity;
        this.date = date;
        this.medName = medName;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPhrmacyName() {
        return phrmacyName;
    }

    public void setPhrmacyName(String phrmacyName) {
        this.phrmacyName = phrmacyName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
