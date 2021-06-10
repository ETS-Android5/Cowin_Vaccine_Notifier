package io.realworld.android.api.models;

import com.google.gson.annotations.SerializedName;

public class State {

	@SerializedName("state_name")
	private String stateName;

	@SerializedName("state_id")
	private long stateId;

	@SerializedName("state_name_l")
	private String stateNameL;

	public String getStateName(){
		return stateName;
	}

	public long getStateId(){
		return stateId;
	}

	public String getStateNameL(){
		return stateNameL;
	}
}