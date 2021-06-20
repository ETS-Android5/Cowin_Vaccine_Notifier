package io.realworld.android.cowinvaccinenotifier.Activies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import io.realworld.android.cowinvaccinenotifier.Helpers.CurvedBottomNavigationView;
import io.realworld.android.cowinvaccinenotifier.Helpers.AlertWorker;
import io.realworld.android.cowinvaccinenotifier.OnboardingActivity;
import io.realworld.android.cowinvaccinenotifier.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, NavController.OnDestinationChangedListener{

    private static final String TAG = "MainActivity";
    public NavController mNavController;
//    public BottomAppBar mBottomAppBar;
    public DrawerLayout mDrawerLayout;
    public Toolbar mToolbar;
    public NavOptions.Builder leftToRightBuilder, rightToLeftBuilder;
    private AppBarConfiguration mAppBarConfiguration;
    private CurvedBottomNavigationView mBottomNavigationView;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if (Paper.book().read("first", true)) {
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        } else {

            Init();


            if (Paper.book().read("work_start", false)) {
                WorkManager.getInstance().cancelAllWorkByTag("work");

                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(AlertWorker.class, 15, TimeUnit.MINUTES)
                        //.setConstraints(constraints)
                        .addTag("work")
                        .build();

                WorkManager.getInstance().enqueue(request);

            } else {
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(AlertWorker.class, 15, TimeUnit.MINUTES)
                        //.setConstraints(constraints)
                        .addTag("work")
                        .build();

                WorkManager.getInstance().enqueue(request);
                Paper.book().write("work_start", true);
            }

        }

//        fab.setOnClickListener(v -> {
//            Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
//
//            Intent intent = new Intent(MainActivity.this, StateActivity.class);
//            startActivity(intent);
//
//        });
        Log.d(TAG, "onNavigationItemSelected: animations for opening fragment to right of current one");
        leftToRightBuilder = new NavOptions.Builder();
        leftToRightBuilder.setEnterAnim(R.anim.slide_in_right);
        leftToRightBuilder.setExitAnim(R.anim.slide_out_left);
        leftToRightBuilder.setPopEnterAnim(R.anim.slide_in_left);
        leftToRightBuilder.setPopExitAnim(R.anim.slide_out_right);
        leftToRightBuilder.setLaunchSingleTop(true);

        Log.d(TAG, "onNavigationItemSelected: animations for opening fragment to left of current one");
        rightToLeftBuilder = new NavOptions.Builder();
        rightToLeftBuilder.setEnterAnim(R.anim.slide_in_left);
        rightToLeftBuilder.setExitAnim(R.anim.slide_out_right);
        rightToLeftBuilder.setPopEnterAnim(R.anim.slide_in_right);
        rightToLeftBuilder.setPopExitAnim(R.anim.slide_out_left);
        rightToLeftBuilder.setLaunchSingleTop(true);

    }

    private void Init() {
        Paper.init(this);
        mBottomNavigationView = findViewById(R.id.bottom_nav_view);
//        mBottomAppBar = findViewById(R.id.bottom_app_bar);
//        fab = findViewById(R.id.fab);

        //set up navigation
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mNavController.addOnDestinationChangedListener(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_notification)
                .build();

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);


        mBottomNavigationView.setSelectedItemId(R.id.nav_home);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp: starts");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        Log.d(TAG, "onDestinationChanged: starts");
//        fab.setOnClickListener(null);
        switch (destination.getId()) {
            case R.id.nav_home:
//                mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
//                mBottomAppBar.setVisibility(View.VISIBLE);
//                mBottomAppBar.performShow();
//                mBottomAppBar.bringToFront();

//                fab.show();
//                fab.setImageResource(R.drawable.ic_add);
//                fab.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_notification:
//                fab.show();
//                fab.setVisibility(View.VISIBLE);
//                fab.setImageResource(R.drawable.ic_add);
//                mBottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
//                mBottomAppBar.setVisibility(View.VISIBLE);
//                mBottomAppBar.performShow();
//                mBottomAppBar.bringToFront();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: starts");
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.nav_home:
                Log.d(TAG, "onNavigationItemSelected: home selected");
                if (mNavController.getCurrentDestination().getId() != R.id.nav_home) {
                    Log.d(TAG, "onNavigationItemSelected: opening home fragment");
                    mNavController.navigate(R.id.nav_home, null, rightToLeftBuilder.build());
                }
                return true;

            case R.id.nav_alert:
                Log.d(TAG, "onNavigationItemSelected: attendance selected");
                if (mNavController.getCurrentDestination().getId() != R.id.nav_notification) {
                    Log.d(TAG, "onNavigationItemSelected: opening attendance fragment");
                    mNavController.navigate(R.id.nav_notification, null, leftToRightBuilder.build());
                }
                return true;
            default:
                return false;
        }
    }
}