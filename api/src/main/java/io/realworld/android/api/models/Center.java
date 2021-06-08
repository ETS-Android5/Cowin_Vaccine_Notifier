package io.realworld.android.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Center {

	@SerializedName("address_l")
	private String addressL;

	@SerializedName("pincode")
	private String pincode;

	@SerializedName("sessions")
	private List<SessionForSeven> sessions;

	@SerializedName("address")
	private String address;

	@SerializedName("vaccine_fees")
	private List<VaccineFee> vaccineFees;

	@SerializedName("district_name_l")
	private String districtNameL;

	@SerializedName("name_l")
	private String nameL;

	@SerializedName("fee_type")
	private String feeType;

	@SerializedName("state_name_l")
	private String stateNameL;

	@SerializedName("long")
	private double jsonMemberLong;

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("block_name")
	private String blockName;

	@SerializedName("block_name_l")
	private String blockNameL;

	@SerializedName("center_id")
	private int centerId;

	@SerializedName("state_name")
	private String stateName;

	@SerializedName("name")
	private String name;

	@SerializedName("from")
	private String from;

	@SerializedName("to")
	private String to;

	@SerializedName("lat")
	private double lat;

	public String getAddressL(){
		return addressL;
	}

	public String getPincode(){
		return pincode;
	}

	public List<SessionForSeven> getSessions(){
		return sessions;
	}

	public String getAddress(){
		return address;
	}

	public List<VaccineFee> getVaccineFees(){
		return vaccineFees;
	}

	public String getDistrictNameL(){
		return districtNameL;
	}

	public String getNameL(){
		return nameL;
	}

	public String getFeeType(){
		return feeType;
	}

	public String getStateNameL(){
		return stateNameL;
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

	public String getBlockNameL(){
		return blockNameL;
	}

	public int getCenterId(){
		return centerId;
	}

	public String getStateName(){
		return stateName;
	}

	public String getName(){
		return name;
	}

	public String getFrom(){
		return from;
	}

	public String getTo(){
		return to;
	}

	public double getLat(){
		return lat;
	}

	@Override
	public boolean equals(Object o) {
		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
		if (!(o instanceof Center)) {
			return false;
		}

		// typecast o to Complex so that we can compare data members
		Center c = (Center) o;

		List<SessionForSeven> sessionForSevenList = c.sessions;
		//Can be a bug
		for(int i=0; i<sessionForSevenList.size(); i++){
			if(!sessionForSevenList.get(i).equals(sessions.get(i))){
				return false;
			}
		}

		return true;
	}
}