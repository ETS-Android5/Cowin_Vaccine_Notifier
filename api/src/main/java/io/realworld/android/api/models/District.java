package io.realworld.android.api.models;

import com.google.gson.annotations.SerializedName;

public class District {

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("district_name_l")
	private String districtNameL;

	@SerializedName("state_id")
	private long stateId;

	@SerializedName("district_id")
	private long districtId;

	public String getDistrictName(){
		return districtName;
	}

	public String getDistrictNameL(){
		return districtNameL;
	}

	public long getStateId(){
		return stateId;
	}

	public long getDistrictId(){
		return districtId;
	}
}