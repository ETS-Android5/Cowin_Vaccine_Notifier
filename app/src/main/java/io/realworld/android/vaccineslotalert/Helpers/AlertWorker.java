package io.realworld.android.vaccineslotalert.Helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.paperdb.Paper;
import io.realworld.android.api.CowinClient;
import io.realworld.android.api.models.AppointmentsForSevenResponse;
import io.realworld.android.api.models.Center;
import io.realworld.android.api.models.SessionForSeven;
import io.realworld.android.vaccineslotalert.Activies.SplashScreenActivity;
import io.realworld.android.vaccineslotalert.Data.Alert;
import io.realworld.android.vaccineslotalert.Data.Subscription;
import io.realworld.android.vaccineslotalert.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertWorker extends Worker {
    Context context;

    private final CowinClient cowinClient = new CowinClient();


    public AlertWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Paper.init(context);
        HashMap<String, List<Center> > test = Paper.book().read("test31", new HashMap<>());
        List<Alert> alerts = Paper.book().read("test11", new ArrayList<>());


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = df.format(c);
        Log.d("dd-MM-yyyy", date);

        List<Subscription> subscriptions = Paper.book().read("subscription", new ArrayList<>());
        for(int i = 0; i < subscriptions.size(); i++) {
            Log.d("dd-MM-yyyy", String.valueOf(subscriptions.get(i).getDistrictId()));
            long districtID = subscriptions.get(i).getDistrictId();
            String pinCode = subscriptions.get(i).getPin();

            if (districtID != 0L && pinCode == null) {
                long district = districtID;

                List<Integer> ages = subscriptions.get(i).getAges();
                List<Integer> doses = subscriptions.get(i).getDoses();

                StringBuilder builder = new StringBuilder(String.valueOf(district));
                for (int a = 0; a < ages.size(); a++) {
                    builder.append(ages.get(a));
                }
                for (int d = 0; d < doses.size(); d++) {
                    builder.append(doses.get(d));
                }
                String code = builder.toString();
                String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";
                Call<AppointmentsForSevenResponse> call = cowinClient.api
                        .getAppointmentsForSeven(district, date, user_agent);
                call.enqueue(new Callback<AppointmentsForSevenResponse>() {
                    @Override
                    public void onResponse(Call<AppointmentsForSevenResponse> call, Response<AppointmentsForSevenResponse> response) {
                        AppointmentsForSevenResponse appointments = response.body();
                        List<Center> newCenterList = appointments.getCenters();

                        if (test.containsKey(code)) {
                            List<Center> centerList = test.get(code);
                            int count = 0;
                            for (int i = 0; i < newCenterList.size(); i++) {
                                int centerId = newCenterList.get(i).getCenterId();

                                int in = getIndexCenterByProperty(centerId, centerList);

                                if (in != -1) {
                                    Center center = centerList.get(in);
                                    Center newCenter = newCenterList.get(i);
                                    List<SessionForSeven> sessionList = center.getSessions();
                                    List<SessionForSeven> newSessionList = newCenter.getSessions();

                                    for (int j = 0; j < newSessionList.size(); j++) {
                                        String sessionId = newSessionList.get(j).getSessionId();
                                        Log.d("testo", sessionId);
                                        int ind = getIndexSessionByProperty(sessionId, sessionList);

                                        if (ind != -1) {
                                            if ((newSessionList.get(j).getAvailableCapacity()
                                                    > sessionList.get(ind).getAvailableCapacity())
                                                    || (newSessionList.get(j).getAvailableCapacityDose1()
                                                    > sessionList.get(ind).getAvailableCapacityDose1()) ||
                                                    (newSessionList.get(j).getAvailableCapacityDose2()
                                                            > sessionList.get(ind).getAvailableCapacityDose2())) {

                                                if (ages.size() == 1) {
                                                    if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                        if (doses.size() == 1) {
                                                            if (doses.get(0) == 1 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose1() > 0 &&
                                                                    (newSessionList.get(j).getAvailableCapacityDose1()
                                                                            > sessionList.get(ind).getAvailableCapacityDose1())) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            } else if (doses.get(0) == 2 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose2() > 0 &&
                                                                    (newSessionList.get(j).getAvailableCapacityDose2()
                                                                            > sessionList.get(ind).getAvailableCapacityDose2())) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            }
                                                        } else {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }

                                                    }
                                                } else {
                                                    if (doses.size() == 1) {
                                                        if (doses.get(0) == 1 &&
                                                                newSessionList.get(j).getAvailableCapacityDose1() > 0 &&
                                                                (newSessionList.get(j).getAvailableCapacityDose1()
                                                                        > sessionList.get(ind).getAvailableCapacityDose1())) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        } else if (doses.get(0) == 2 &&
                                                                newSessionList.get(j).getAvailableCapacityDose2() > 0 &&
                                                                (newSessionList.get(j).getAvailableCapacityDose2()
                                                                        > sessionList.get(ind).getAvailableCapacityDose2())) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }
                                                    } else {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if (newSessionList.get(j).getAvailableCapacity() > 0) {
                                                if (ages.size() == 1) {
                                                    if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                        if (doses.size() == 1) {
                                                            if (doses.get(0) == 1 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            } else if (doses.get(0) == 2 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            }
                                                        } else {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }

                                                    }
                                                } else {
                                                    if (doses.size() == 1) {
                                                        if (doses.get(0) == 1 &&
                                                                newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        } else if (doses.get(0) == 2 &&
                                                                newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }
                                                    } else {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Center newCenter = newCenterList.get(i);
                                    List<SessionForSeven> newSessionList = newCenter.getSessions();
                                    for (int j = 0; j < newSessionList.size(); j++) {
                                        if (newSessionList.get(j).getAvailableCapacity() > 0) {

                                            if (ages.size() == 1) {
                                                if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                    if (doses.size() == 1) {
                                                        if (doses.get(0) == 1 &&
                                                                newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        } else if (doses.get(0) == 2 &&
                                                                newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }
                                                    } else {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    }
                                                }
                                            } else {
                                                Alert alert = new Alert(newCenter.getName(),
                                                        newCenter.getAddress() + ", "
                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                alerts.add(alert);
                                                count++;
                                            }

                                        }
                                    }
                                }
                            }
                            if (count > 0) {
                                displayNotification("Vaccine Available!",
                                        count + " new centers in " +
                                                newCenterList.get(0).getDistrictName() + " are available for vaccination ");
                                Log.d("testo", "no notifi");
                            }

                            test.put(code, newCenterList);
                            Paper.book().write("test31", test);
                            Paper.book().write("test11", alerts);
                        } else {
                            int count = 0;
                            for (int i = 0; i < newCenterList.size(); i++) {
                                Center newCenter = newCenterList.get(i);
                                List<SessionForSeven> newSessionList = newCenter.getSessions();
                                for (int j = 0; j < newSessionList.size(); j++) {
                                    if (newSessionList.get(j).getAvailableCapacity() > 0) {

                                        if (ages.size() == 1) {
                                            if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                if (doses.size() == 1) {
                                                    if (doses.get(0) == 1 &&
                                                            newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    } else if (doses.get(0) == 2 &&
                                                            newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    }
                                                } else {
                                                    Alert alert = new Alert(newCenter.getName(),
                                                            newCenter.getAddress() + ", "
                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                    alerts.add(alert);
                                                    count++;
                                                }
                                            }
                                        } else {
                                            if (doses.size() == 1) {
                                                if (doses.get(0) == 1 &&
                                                        newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                    Alert alert = new Alert(newCenter.getName(),
                                                            newCenter.getAddress() + ", "
                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                    alerts.add(alert);
                                                    count++;
                                                } else if (doses.get(0) == 2 &&
                                                        newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                    Alert alert = new Alert(newCenter.getName(),
                                                            newCenter.getAddress() + ", "
                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                    alerts.add(alert);
                                                    count++;
                                                }
                                            } else {
                                                Alert alert = new Alert(newCenter.getName(),
                                                        newCenter.getAddress() + ", "
                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                alerts.add(alert);
                                                count++;
                                            }
                                        }
                                    }
                                }
                            }
                            if (count > 0) {
                                displayNotification("Vaccine Available!",
                                        count + " new sessions in " +
                                                newCenterList.get(0).getDistrictName() + " are available for vaccination ");
                                Log.d("testo", "no notifi");
                            }

                            test.put(code, newCenterList);
                            Paper.book().write("test31", test);
                            Paper.book().write("test11", alerts);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppointmentsForSevenResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network Failure! Please check your internet connection and retry", Toast.LENGTH_SHORT).show();

                    }
                });
                HashMap<String, List<Center>> testD = Paper.book().read("test31");
                Log.d("test", String.valueOf(testD.size()));

                for (HashMap.Entry mapElement : testD.entrySet()) {
                    List<Center> key = (List<Center>) mapElement.getValue();

                    for (Center item : key) {
                        Log.d("test", item.getName());
                    }
                }
            } else if (districtID == 0L &&  pinCode!=null){
                List<Integer> ages = subscriptions.get(i).getAges();
                List<Integer> doses = subscriptions.get(i).getDoses();

                StringBuilder builder = new StringBuilder(pinCode);
                for (int a = 0; a < ages.size(); a++) {
                    builder.append(ages.get(a));
                }
                for (int d = 0; d < doses.size(); d++) {
                    builder.append(doses.get(d));
                }
                String code = builder.toString();
                String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";
                Call<AppointmentsForSevenResponse> call = cowinClient.api
                        .getAppointmentsForSevenByPin(pinCode, date, user_agent);
                call.enqueue(new Callback<AppointmentsForSevenResponse>() {
                    @Override
                    public void onResponse(Call<AppointmentsForSevenResponse> call, Response<AppointmentsForSevenResponse> response) {
                        AppointmentsForSevenResponse appointments = response.body();
                        if(appointments != null) {
                            List<Center> newCenterList = appointments.getCenters();

                            if (test.containsKey(code)) {
                                List<Center> centerList = test.get(code);
                                int count = 0;
                                for (int i = 0; i < newCenterList.size(); i++) {
                                    int centerId = newCenterList.get(i).getCenterId();

                                    int in = getIndexCenterByProperty(centerId, centerList);

                                    if (in != -1) {
                                        Center center = centerList.get(in);
                                        Center newCenter = newCenterList.get(i);
                                        List<SessionForSeven> sessionList = center.getSessions();
                                        List<SessionForSeven> newSessionList = newCenter.getSessions();

                                        for (int j = 0; j < newSessionList.size(); j++) {
                                            String sessionId = newSessionList.get(j).getSessionId();
                                            Log.d("testo", sessionId);
                                            int ind = getIndexSessionByProperty(sessionId, sessionList);

                                            if (ind != -1) {
                                                if ((newSessionList.get(j).getAvailableCapacity()
                                                        > sessionList.get(ind).getAvailableCapacity())
                                                        || (newSessionList.get(j).getAvailableCapacityDose1()
                                                        > sessionList.get(ind).getAvailableCapacityDose1()) ||
                                                        (newSessionList.get(j).getAvailableCapacityDose2()
                                                                > sessionList.get(ind).getAvailableCapacityDose2())) {

                                                    if (ages.size() == 1) {
                                                        if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                            if (doses.size() == 1) {
                                                                if (doses.get(0) == 1 &&
                                                                        newSessionList.get(j).getAvailableCapacityDose1() > 0 &&
                                                                        (newSessionList.get(j).getAvailableCapacityDose1()
                                                                                > sessionList.get(ind).getAvailableCapacityDose1())) {
                                                                    Alert alert = new Alert(newCenter.getName(),
                                                                            newCenter.getAddress() + ", "
                                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                    alerts.add(alert);
                                                                    count++;
                                                                } else if (doses.get(0) == 2 &&
                                                                        newSessionList.get(j).getAvailableCapacityDose2() > 0 &&
                                                                        (newSessionList.get(j).getAvailableCapacityDose2()
                                                                                > sessionList.get(ind).getAvailableCapacityDose2())) {
                                                                    Alert alert = new Alert(newCenter.getName(),
                                                                            newCenter.getAddress() + ", "
                                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                    alerts.add(alert);
                                                                    count++;
                                                                }
                                                            } else {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            }

                                                        }
                                                    } else {
                                                        if (doses.size() == 1) {
                                                            if (doses.get(0) == 1 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose1() > 0 &&
                                                                    (newSessionList.get(j).getAvailableCapacityDose1()
                                                                            > sessionList.get(ind).getAvailableCapacityDose1())) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            } else if (doses.get(0) == 2 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose2() > 0 &&
                                                                    (newSessionList.get(j).getAvailableCapacityDose2()
                                                                            > sessionList.get(ind).getAvailableCapacityDose2())) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            }
                                                        } else {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (newSessionList.get(j).getAvailableCapacity() > 0) {
                                                    if (ages.size() == 1) {
                                                        if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                            if (doses.size() == 1) {
                                                                if (doses.get(0) == 1 &&
                                                                        newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                                    Alert alert = new Alert(newCenter.getName(),
                                                                            newCenter.getAddress() + ", "
                                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                    alerts.add(alert);
                                                                    count++;
                                                                } else if (doses.get(0) == 2 &&
                                                                        newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                                    Alert alert = new Alert(newCenter.getName(),
                                                                            newCenter.getAddress() + ", "
                                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                    alerts.add(alert);
                                                                    count++;
                                                                }
                                                            } else {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            }

                                                        }
                                                    } else {
                                                        if (doses.size() == 1) {
                                                            if (doses.get(0) == 1 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            } else if (doses.get(0) == 2 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            }
                                                        } else {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Center newCenter = newCenterList.get(i);
                                        List<SessionForSeven> newSessionList = newCenter.getSessions();
                                        for (int j = 0; j < newSessionList.size(); j++) {
                                            if (newSessionList.get(j).getAvailableCapacity() > 0) {

                                                if (ages.size() == 1) {
                                                    if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                        if (doses.size() == 1) {
                                                            if (doses.get(0) == 1 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            } else if (doses.get(0) == 2 &&
                                                                    newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                                Alert alert = new Alert(newCenter.getName(),
                                                                        newCenter.getAddress() + ", "
                                                                                + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                        newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                        newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                        newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                                alerts.add(alert);
                                                                count++;
                                                            }
                                                        } else {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }
                                                    }
                                                } else {
                                                    Alert alert = new Alert(newCenter.getName(),
                                                            newCenter.getAddress() + ", "
                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                    alerts.add(alert);
                                                    count++;
                                                }

                                            }
                                        }
                                    }
                                }
                                if (count > 0) {
                                    displayNotification("Vaccine Available!",
                                            count + " new centers in " +
                                                    newCenterList.get(0).getDistrictName() + " are available for vaccination ");
                                    Log.d("testo", "no notifi");
                                }

                                test.put(code, newCenterList);
                                Paper.book().write("test31", test);
                                Paper.book().write("test11", alerts);
                            } else {
                                int count = 0;
                                for (int i = 0; i < newCenterList.size(); i++) {
                                    Center newCenter = newCenterList.get(i);
                                    List<SessionForSeven> newSessionList = newCenter.getSessions();
                                    for (int j = 0; j < newSessionList.size(); j++) {
                                        if (newSessionList.get(j).getAvailableCapacity() > 0) {

                                            if (ages.size() == 1) {
                                                if ((newSessionList.get(j).getMinAgeLimit() == ages.get(0))) {
                                                    if (doses.size() == 1) {
                                                        if (doses.get(0) == 1 &&
                                                                newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        } else if (doses.get(0) == 2 &&
                                                                newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                            Alert alert = new Alert(newCenter.getName(),
                                                                    newCenter.getAddress() + ", "
                                                                            + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                    newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                    newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                    newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                            alerts.add(alert);
                                                            count++;
                                                        }
                                                    } else {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    }
                                                }
                                            } else {
                                                if (doses.size() == 1) {
                                                    if (doses.get(0) == 1 &&
                                                            newSessionList.get(j).getAvailableCapacityDose1() > 0) {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    } else if (doses.get(0) == 2 &&
                                                            newSessionList.get(j).getAvailableCapacityDose2() > 0) {
                                                        Alert alert = new Alert(newCenter.getName(),
                                                                newCenter.getAddress() + ", "
                                                                        + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                                newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                                newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                                newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                        alerts.add(alert);
                                                        count++;
                                                    }
                                                } else {
                                                    Alert alert = new Alert(newCenter.getName(),
                                                            newCenter.getAddress() + ", "
                                                                    + newCenter.getDistrictName() + ", " + newCenter.getStateName(),
                                                            newCenter.getFeeType(), newSessionList.get(j).getDate(), newSessionList.get(j).getVaccine(),
                                                            newSessionList.get(j).getMinAgeLimit(), newSessionList.get(j).getAvailableCapacity(),
                                                            newSessionList.get(j).getAvailableCapacityDose1(), newSessionList.get(j).getAvailableCapacityDose2());
                                                    alerts.add(alert);
                                                    count++;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (count > 0) {
                                    displayNotification("Vaccine Available!",
                                            count + " new sessions in " +
                                                    newCenterList.get(0).getDistrictName() + " are available for vaccination ");
                                    Log.d("testo", "no notifi");
                                }

                                test.put(code, newCenterList);
                                Paper.book().write("test31", test);
                                Paper.book().write("test11", alerts);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No response for PIN Code "+ pinCode + " in Cowin server",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AppointmentsForSevenResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network Failure! Please check your internet connection and retry", Toast.LENGTH_SHORT).show();

                    }
                });
                HashMap<String, List<Center>> testD = Paper.book().read("test31");
                Log.d("test", String.valueOf(testD.size()));

                for (HashMap.Entry mapElement : testD.entrySet()) {
                    List<Center> key = (List<Center>) mapElement.getValue();

                    for (Center item : key) {
                        Log.d("test", item.getName());
                    }
                }
            }
        }
        return Result.success();
    }

    private int getIndexCenterByProperty(int yourString, List<Center> objList) {
        for (int i = 0; i < objList.size(); i++) {
            if (objList.get(i) !=null && objList.get(i).getCenterId() == yourString ) {
                return i;
            }
        }
        return -1;// not there is list
    }

    private int getIndexSessionByProperty(String yourString, List<SessionForSeven> objList) {
        for (int i = 0; i < objList.size(); i++) {
            if (objList.get(i) !=null && objList.get(i).getSessionId().equals(yourString)) {
                return i;
            }
        }
        return -1;// not there is list
    }

    public boolean equal (Center c, Center o) {
        List<SessionForSeven> sessionForSevenList = c.getSessions();
        List<SessionForSeven> sessions = o.getSessions();
        //Can be a bug
        int s1 = c.getCenterId();
        int s2 = o.getCenterId();
        Log.d("testo", s1+" "+ s2);
        for(int i=0; i<sessionForSevenList.size(); i++){

            if((sessionForSevenList.get(i).getAvailableCapacity() > sessions.get(i).getAvailableCapacity())
                    || (sessionForSevenList.get(i).getAvailableCapacityDose1() > sessions.get(i).getAvailableCapacityDose1())||
                    (sessionForSevenList.get(i).getAvailableCapacityDose2() > sessions.get(i).getAvailableCapacityDose2())){

                Log.d("testo",
                        s1 +" "+s2);
                Log.d("testo",
                        sessionForSevenList.get(i).getAvailableCapacity() +" "+ sessions.get(i).getAvailableCapacity());

                return false;
            }
        }
        return true;
    }

    private void displayNotification(String task, String desc){

        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("coding", "coding1", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        Intent resultIntent = new Intent(context, SplashScreenActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "coding")
                .setContentTitle(task)
                .setContentText(desc)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(Color.WHITE)
                .setSmallIcon(R.drawable.ic_notification);

        Notification notification = builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(desc))
                .build();

        Random r = new Random();
        manager.notify(r.nextInt(), notification);
        playNotificationSound();

    }

    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
