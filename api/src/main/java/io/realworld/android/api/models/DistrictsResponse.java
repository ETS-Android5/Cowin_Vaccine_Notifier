package io.realworld.android.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DistrictsResponse {

	@SerializedName("districts")
	private List<District> districts;

	@SerializedName("ttl")
	private int ttl;

	public List<District> getDistricts(){
		return districts;
	}

	public int getTtl(){
		return ttl;
	}
}