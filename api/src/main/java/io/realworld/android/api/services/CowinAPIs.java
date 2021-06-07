package io.realworld.android.api.services;

import io.realworld.android.api.models.AppointmentsForSevenResponse;
import io.realworld.android.api.models.AppointmentsResponse;
import io.realworld.android.api.models.DistrictsResponse;
import io.realworld.android.api.models.StatesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CowinAPIs {

    @GET("admin/location/states")
    Call<StatesResponse> getStates(
            @Header("User-Agent") String token
    );

    @GET("admin/location/districts/{state_id}")
    Call<DistrictsResponse> getDistricts(
            @Path("state_id") int state_id ,
            @Header("User-Agent") String token
    );

    @GET("appointment/sessions/public/findByDistrict")
    Call<AppointmentsResponse> getAppointmentsByDistrict(
            @Query("district_id") int district_id ,
            @Query("date") String date,
            @Header("User-Agent") String token
    );

    @GET("appointment/sessions/public/calendarByDistrict")
    Call<AppointmentsForSevenResponse> getAppointmentsForSeven(
            @Query("district_id") int district_id,
            @Query("date") String date,
            @Header("User-Agent") String token
    );
}
