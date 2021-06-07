package io.realworld.android.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StatesResponse {

	@SerializedName("ttl")
	private int ttl;

	@SerializedName("states")
	private List<State> states;

	public int getTtl(){
		return ttl;
	}

	public List<State> getStates(){
		return states;
	}
}