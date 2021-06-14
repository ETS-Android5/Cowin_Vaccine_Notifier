package io.realworld.android.cowinvaccinenotifier.Activies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.realworld.android.cowinvaccinenotifier.Data.Subscription;
import io.realworld.android.cowinvaccinenotifier.R;

public class OthersActivity extends AppCompatActivity {

    long districtId;
    FloatingActionButton done;
    String state, district;
    TextView placename;
    CheckBox group_18_44, group_45;
    CheckBox dose1, dose2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        getSupportActionBar().hide();

        districtId = getIntent().getExtras().getLong("statecode");
        state = getIntent().getExtras().getString("state");
        district = getIntent().getExtras().getString("district");
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
            Subscription subscription = new Subscription(state, district, districtId, ages, doses);
            if(contains(subscriptions, subscription)) {
                Toast.makeText(this, "This subscription is all ready added in the list", Toast.LENGTH_LONG).show();
            } else {
                subscriptions.add(subscription);
                Paper.book().write("subscription", subscriptions);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private boolean contains(List<Subscription> subscriptions, Subscription subscription){
        for (int i = 0; i < subscriptions.size(); i++){
            if((subscriptions.get(i).getDistrictId() == subscription.getDistrictId()) &&
                    (subscriptions.get(i).getAges().equals(subscription.getAges())) &&
                    (subscriptions.get(i).getDoses().equals(subscription.getDoses()))){
                return true;
            }
        }
        return false;
    }

    private void Init() {
        Paper.init(this);
        done = findViewById(R.id.done);
        placename = findViewById(R.id.place_name);
        group_18_44 = findViewById(R.id.group_18);
        group_45 = findViewById(R.id.group_45);
        dose1 = findViewById(R.id.dose1);
        dose2 = findViewById(R.id.dose2);

        placename.setText(state + " - " + district);
    }
}