package io.realworld.android.cowinvaccinenotifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();

        if (Paper.book().read("work_start", false)) {
            WorkManager.getInstance().cancelAllWorkByTag("work");

            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                    //.setConstraints(constraints)
                    .addTag("work")
                    .build();

            WorkManager.getInstance().enqueue(request);
            Toast.makeText(this, "Background Work re-started", Toast.LENGTH_LONG).show();
        } else {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                    //.setConstraints(constraints)
                    .addTag("work")
                    .build();

            WorkManager.getInstance().enqueue(request);
            Paper.book().write("work_start", true);
            Toast.makeText(this, "Background Work Started", Toast.LENGTH_LONG).show();
        }
    }

    private void Init() {
        Paper.init(this);
    }
}