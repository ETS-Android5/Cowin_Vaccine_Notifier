package io.realworld.android.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AppointmentsForSevenResponse {

	@SerializedName("centers")
	private List<Center> centers;

	public List<Center> getCenters(){
		return centers;
	}
}