package io.realworld.android.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Session {

	@SerializedName("date")
	private String date;

	@SerializedName("address_l")
	private String addressL;

	@SerializedName("min_age_limit")
	private int minAgeLimit;

	@SerializedName("name_l")
	private String nameL;

	@SerializedName("fee")
	private String fee;

	@SerializedName("fee_type")
	private String feeType;

	@SerializedName("long")
	private double jsonMemberLong;

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("block_name")
	private String blockName;

	@SerializedName("state_name")
	private String stateName;

	@SerializedName("from")
	private String from;

	@SerializedName("lat")
	private double lat;

	@SerializedName("pincode")
	private String pincode;

	@SerializedName("address")
	private String address;

	@SerializedName("district_name_l")
	private String districtNameL;

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("state_name_l")
	private String stateNameL;

	@SerializedName("available_capacity")
	private int availableCapacity;

	@SerializedName("block_name_l")
	private String blockNameL;

	@SerializedName("vaccine")
	private String vaccine;

	@SerializedName("slots")
	private List<String> slots;

	@SerializedName("center_id")
	private int centerId;

	@SerializedName("name")
	private String name;

	@SerializedName("to")
	private String to;

	@SerializedName("available_capacity_dose2")
	private int availableCapacityDose2;

	@SerializedName("available_capacity_dose1")
	private int availableCapacityDose1;

	public String getDate(){
		return date;
	}

	public String getAddressL(){
		return addressL;
	}

	public int getMinAgeLimit(){
		return minAgeLimit;
	}

	public String getNameL(){
		return nameL;
	}

	public String getFee(){
		return fee;
	}

	public String getFeeType(){
		return feeType;
	}

	public double getJsonMemberLong(){
		return jsonMemberLong;
	}

	public String getDistrictName(){
		return districtName;
	}

	public String getBlockName(){
		return blockName;
	}

	public String getStateName(){
		return stateName;
	}

	public String getFrom(){
		return from;
	}

	public double getLat(){
		return lat;
	}

	public String getPincode(){
		return pincode;
	}

	public String getAddress(){
		return address;
	}

	public String getDistrictNameL(){
		return districtNameL;
	}

	public String getSessionId(){
		return sessionId;
	}

	public String getStateNameL(){
		return stateNameL;
	}

	public int getAvailableCapacity(){
		return availableCapacity;
	}

	public String getBlockNameL(){
		return blockNameL;
	}

	public String getVaccine(){
		return vaccine;
	}

	public List<String> getSlots(){
		return slots;
	}

	public int getCenterId(){
		return centerId;
	}

	public String getName(){
		return name;
	}

	public String getTo(){
		return to;
	}

	public int getAvailableCapacityDose2(){
		return availableCapacityDose2;
	}

	public int getAvailableCapacityDose1(){
		return availableCapacityDose1;
	}
}