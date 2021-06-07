package io.realworld.android.api.models;

import com.google.gson.annotations.SerializedName;

public class State {

	@SerializedName("state_name")
	private String stateName;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("state_name_l")
	private String stateNameL;

	public String getStateName(){
		return stateName;
	}

	public int getStateId(){
		return stateId;
	}

	public String getStateNameL(){
		return stateNameL;
	}
}