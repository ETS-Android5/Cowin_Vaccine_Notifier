package io.realworld.android.vaccineslotalert.Activies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import io.realworld.android.vaccineslotalert.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        findViewById(R.id.teamCard1Github).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ayan-10"));
                startActivity(browserIntent);
            }
        });

        findViewById(R.id.teamCard1LinkedIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/ayan-sarkar-44728a1a7/"));
                startActivity(browserIntent);
            }
        });

        findViewById(R.id.MAILBOX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:ayan10.dev@gmail.com"));
                startActivity(browserIntent);
            }
        });

        findViewById(R.id.onblink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ramotion.com"));
                startActivity(browserIntent);
            }
        });

        findViewById(R.id.dblink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ramotion/paper-onboarding-android"));
                startActivity(browserIntent);
            }
        });
    }
}