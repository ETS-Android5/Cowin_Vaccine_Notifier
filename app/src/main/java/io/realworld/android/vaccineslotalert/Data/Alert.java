package io.realworld.android.vaccineslotalert.Data;


public class Alert {

  private String name;
  private String address;
  private String feeType;
  private String date;
  private String vaccine;
  private int ageLimit;
  private int capacity;
  private int dose1;
  private int dose2;

    public Alert(String name, String address, String feeType, String date, String vaccine, int ageLimit, int capacity, int dose1, int dose2) {
        this.name = name;
        this.address = address;
        this.feeType = feeType;
        this.date = date;
        this.vaccine = vaccine;
        this.ageLimit = ageLimit;
        this.capacity = capacity;
        this.dose1 = dose1;
        this.dose2 = dose2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getDose1() {
        return dose1;
    }

    public void setDose1(int dose1) {
        this.dose1 = dose1;
    }

    public int getDose2() {
        return dose2;
    }

    public void setDose2(int dose2) {
        this.dose2 = dose2;
    }
}
