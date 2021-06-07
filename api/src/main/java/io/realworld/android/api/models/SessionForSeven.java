package io.realworld.android.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SessionForSeven {

	@SerializedName("date")
	private String date;

	@SerializedName("vaccine")
	private String vaccine;

	@SerializedName("slots")
	private List<String> slots;

	@SerializedName("min_age_limit")
	private int minAgeLimit;

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("available_capacity")
	private int availableCapacity;

	@SerializedName("available_capacity_dose2")
	private int availableCapacityDose2;

	@SerializedName("available_capacity_dose1")
	private int availableCapacityDose1;

	public String getDate(){
		return date;
	}

	public String getVaccine(){
		return vaccine;
	}

	public List<String> getSlots(){
		return slots;
	}

	public int getMinAgeLimit(){
		return minAgeLimit;
	}

	public String getSessionId(){
		return sessionId;
	}

	public int getAvailableCapacity(){
		return availableCapacity;
	}

	public int getAvailableCapacityDose2(){
		return availableCapacityDose2;
	}

	public int getAvailableCapacityDose1(){
		return availableCapacityDose1;
	}
}