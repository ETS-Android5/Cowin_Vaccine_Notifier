package io.realworld.android.cowinvaccinenotifier.Activies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import io.realworld.android.cowinvaccinenotifier.R;
import okhttp3.HttpUrl;

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
    }

    public static boolean isValidUrl (String link) {

        if (link.trim().isEmpty()) 	return false;
        else if (!URLUtil.isNetworkUrl(link)) return false;
        else if (!Patterns.WEB_URL.matcher(link).matches()) return false;
        else return HttpUrl.parse(link) != null;

    }

    public static void openLink(final Context context, final String link) {

        if (isValidUrl(link)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            context.startActivity(Intent.createChooser(browserIntent, "Select the app to open the link"));
        } else {
            Toast.makeText(context, "Link is invalid", Toast.LENGTH_SHORT).show();
        }

    }

}