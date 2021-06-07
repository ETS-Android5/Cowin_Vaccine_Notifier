package io.realworld.android.api;

import org.junit.Test;
import java.io.IOException;

import io.realworld.android.api.models.AppointmentsForSevenResponse;
import io.realworld.android.api.models.AppointmentsResponse;
import io.realworld.android.api.models.DistrictsResponse;
import io.realworld.android.api.models.StatesResponse;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;

public class CowinClientTest {
    private final String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";

    private final CowinClient cowinClient = new CowinClient();

    @Test
    public void getStates() throws IOException {
        Response<StatesResponse> states = cowinClient.api.getStates(user_agent).execute();
        assertNotNull(states.body());
    }

    @Test
    public void getDistricts() throws IOException {
        Response<DistrictsResponse> states = cowinClient.api
                .getDistricts(1, user_agent).execute();
        assertNotNull(states.body());
    }

    @Test
    public void getAppointmentsByDistrict() throws IOException {
        Response<AppointmentsResponse> states = cowinClient.api
                .getAppointmentsByDistrict(712, "05-06-2021", user_agent).execute();
        assertNotNull(states.body());
    }

    @Test
    public void getAppointmentsByCalenderAndDistrict() throws IOException {
        Response<AppointmentsForSevenResponse> states = cowinClient.api
                .getAppointmentsForSeven(712, "05-06-2021", user_agent).execute();
        assertNotNull(states.body());
    }
}
