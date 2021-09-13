package io.realworld.android.vaccineslotalert.Activies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import io.realworld.android.vaccineslotalert.R;

/**
 * Activity for letting user know about the app and connecting user with the developers
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViewById(R.id.teamCard1Github).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ayan-10"));
            startActivity(browserIntent);
        });

        findViewById(R.id.teamCard1LinkedIn).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/ayan-sarkar-44728a1a7/"));
            startActivity(browserIntent);
        });

        findViewById(R.id.MAILBOX).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:ayan10.dev@gmail.com"));
            startActivity(browserIntent);
        });

        findViewById(R.id.onblink).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ramotion.com"));
            startActivity(browserIntent);
        });

        findViewById(R.id.dblink).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ramotion/paper-onboarding-android"));
            startActivity(browserIntent);
        });
    }
}