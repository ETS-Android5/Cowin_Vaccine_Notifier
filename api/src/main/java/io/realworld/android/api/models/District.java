package io.realworld.android.api.models;

import com.google.gson.annotations.SerializedName;

public class District {

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("district_name_l")
	private String districtNameL;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("district_id")
	private int districtId;

	public String getDistrictName(){
		return districtName;
	}

	public String getDistrictNameL(){
		return districtNameL;
	}

	public int getStateId(){
		return stateId;
	}

	public int getDistrictId(){
		return districtId;
	}
}