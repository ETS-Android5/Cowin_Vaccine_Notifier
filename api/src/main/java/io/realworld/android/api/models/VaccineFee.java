package io.realworld.android.api.models;

import com.google.gson.annotations.SerializedName;

public class VaccineFee {

	@SerializedName("vaccine")
	private String vaccine;

	@SerializedName("fee")
	private String fee;

	public String getVaccine(){
		return vaccine;
	}

	public String getFee(){
		return fee;
	}
}