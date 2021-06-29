package io.realworld.android.vaccineslotalert.Activies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

import io.paperdb.Paper;
import io.realworld.android.vaccineslotalert.R;

public class OnboardingActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_onboarding);
        getSupportActionBar().hide();

        Paper.init(this);
        fragmentManager = getSupportFragmentManager();

        PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getPages());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Paper.book().write("first", false);
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private ArrayList<PaperOnboardingPage> getPages() {
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Add Location",
                "Click the + icon and select your state, district and other parameters",
                Color.parseColor("#FF00FB"), R.drawable.ic_add_circle, R.drawable.ic_add_key);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Get Alerts",
                "You will get notified if any slot will open in your selected location for vaccination. You can see all details of available centers in alert screen",
                Color.parseColor("#00FFE6"), R.drawable.ic_notifications_list, R.drawable.ic_notifications_key);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("All Subscriptions",
                "You can add as many location as you want for free. You can delete it anytime",
                Color.parseColor("#FFE600"), R.drawable.ic_sub_list, R.drawable.ic_edit_location_key);
        PaperOnboardingPage scr4 = new PaperOnboardingPage("Get Started",
                "Just add locations as per your need and take rest. App will let you know if any vaccination center is available for your selected location. Swipe to GET STARTED",
                Color.parseColor("#2A6FF9"), R.mipmap.ic_launcher, R.drawable.ic_go_key);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        elements.add(scr4);

        return elements;
    }
}