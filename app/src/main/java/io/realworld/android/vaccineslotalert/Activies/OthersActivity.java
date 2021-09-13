package io.realworld.android.vaccineslotalert.Activies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;
import io.realworld.android.vaccineslotalert.Data.Subscription;
import io.realworld.android.vaccineslotalert.R;

/**
 * User can select other parameters like age and dose from here
 * From here all data will send to the Workmanager
 */
public class OthersActivity extends AppCompatActivity {

    long districtId;
    FloatingActionButton done;
    String state, district, pincode;
    TextView placename;
    CheckBox group_18_44, group_45;
    CheckBox dose1, dose2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        Objects.requireNonNull(getSupportActionBar()).hide();

        districtId = getIntent().getExtras().getLong("statecode");
        state = getIntent().getExtras().getString("state");
        district = getIntent().getExtras().getString("district");
        pincode = getIntent().getExtras().getString("pin");
        Log.d("abba", "staTW "+state);
        Log.d("abba", "pin "+pincode);
        Init();

        List<Subscription> subscriptions = Paper.book().read("subscription", new ArrayList<>());
        done.setOnClickListener(v ->{
            List<Integer> ages = new ArrayList<>();
            List<Integer> doses = new ArrayList<>();
            if((group_18_44.isChecked() && group_45.isChecked()) ||
                    (!group_18_44.isChecked() && !group_45.isChecked())){
                ages.add(18);
                ages.add(45);
            } else if (!group_18_44.isChecked() && group_45.isChecked()) {
                ages.add(45);
            } else if (group_18_44.isChecked() && !group_45.isChecked()) {
                ages.add(18);
            }
            if((dose1.isChecked() && dose2.isChecked()) ||
                    (!dose1.isChecked() && !dose2.isChecked())){
                doses.add(1);
                doses.add(2);
            } else if (!dose1.isChecked() && dose2.isChecked()) {
                doses.add(2);
            } else if (dose1.isChecked() && !dose2.isChecked()) {
                doses.add(1);
            }
            if (pincode == null && state != null) {
                Subscription subscription = new Subscription(state, district, districtId, ages, doses);
                if (contains(subscriptions, subscription, state, null)) {
                    Toast.makeText(this, "This subscription is all ready added in the list", Toast.LENGTH_LONG).show();
                } else {
                    subscriptions.add(subscription);
                    Paper.book().write("subscription", subscriptions);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            } else if (pincode != null && state == null) {
                Subscription subscription = new Subscription(pincode, ages, doses);
                if (contains(subscriptions, subscription, null, pincode)) {
                    Toast.makeText(this, "This subscription is all ready added in the list", Toast.LENGTH_LONG).show();
                } else {
                    subscriptions.add(subscription);
                    Paper.book().write("subscription", subscriptions);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * Check new subscription is already there in the list or not
     * @param subscriptions Current list of subscriptions
     * @param subscription new subscription
     * @param state state
     * @param pincode pincode
     * @return subscription contains or not
     */
    private boolean contains(List<Subscription> subscriptions, Subscription subscription,
                             String state, String pincode){

        for (int i = 0; i < subscriptions.size(); i++) {
            if (pincode == null && state != null) {
                if (((subscriptions.get(i).getPin() == null)) && (subscriptions.get(i).getDistrictId() == subscription.getDistrictId()) &&
                        (subscriptions.get(i).getAges().equals(subscription.getAges())) &&
                        (subscriptions.get(i).getDoses().equals(subscription.getDoses()))) {
                    return true;
                }
            } else if (pincode != null && state == null) {
                if ((subscriptions.get(i).getPin() != null) && (subscriptions.get(i).getPin().equals(subscription.getPin())) &&
                        (subscriptions.get(i).getAges().equals(subscription.getAges())) &&
                        (subscriptions.get(i).getDoses().equals(subscription.getDoses()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * For initialize UI elements
     */
    private void Init() {
        Paper.init(this);
        done = findViewById(R.id.done);
        placename = findViewById(R.id.place_name);
        group_18_44 = findViewById(R.id.group_18);
        group_45 = findViewById(R.id.group_45);
        dose1 = findViewById(R.id.dose1);
        dose2 = findViewById(R.id.dose2);

        if(pincode == null) {
            placename.setText(state + " - " + district);
        } else {
            placename.setText("Pin - "+pincode);
        }
    }
}