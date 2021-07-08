package io.realworld.android.vaccineslotalert.Activies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import io.realworld.android.vaccineslotalert.R;

public class PinActivity extends AppCompatActivity {

    TextInputEditText editText;
    Button pinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pin);
        Init();

        pinButton.setOnClickListener(v ->{
            String pin = editText.getText().toString();
            Intent intent = new Intent(this, OthersActivity.class);
            intent.putExtra("pin", pin);
            startActivity(intent);
        });
    }

    private void Init() {
        editText = findViewById(R.id.pin_edit_text);
        pinButton = findViewById(R.id.pin_button);
    }
}