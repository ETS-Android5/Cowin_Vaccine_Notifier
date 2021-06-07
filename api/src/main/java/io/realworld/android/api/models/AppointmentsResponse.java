package io.realworld.android.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AppointmentsResponse {

	@SerializedName("sessions")
	private List<Session> sessions;

	public List<Session> getSessions(){
		return sessions;
	}
}