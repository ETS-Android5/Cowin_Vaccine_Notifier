package io.realworld.android.cowinvaccinenotifier.Data;

import java.util.List;

public class Subscription {

    private String state;
    private String district;
    private List<Integer> ages;
    private List<Integer> doses;
    private long districtId;

    public Subscription(String state, String district, long districtId, List<Integer> ages, List<Integer> doses) {
        this.state = state;
        this.district = district;
        this.districtId = districtId;
        this.ages = ages;
        this.doses = doses;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public void setAges(List<Integer> ages) {
        this.ages = ages;
    }

    public List<Integer> getDoses() {
        return doses;
    }

    public void setDoses(List<Integer> doses) {
        this.doses = doses;
    }
}
