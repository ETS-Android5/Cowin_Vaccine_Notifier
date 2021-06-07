package io.realworld.android.cowinvaccinenotifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class,
                15,
                TimeUnit.MINUTES)
                .addTag("work")
                .build();
        WorkManager.getInstance().enqueue(request);
        Toast.makeText(this, "Background Work Started", Toast.LENGTH_LONG).show();
    }
}