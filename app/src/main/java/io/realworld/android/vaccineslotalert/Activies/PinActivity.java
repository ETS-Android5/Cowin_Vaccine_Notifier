package io.realworld.android.vaccineslotalert.Activies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import io.realworld.android.vaccineslotalert.R;

/**
 * For getting the Pincode
 */
public class PinActivity extends AppCompatActivity {

    TextInputEditText editText;
    Button pinButton;
    TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pin);
        Init();

        pinButton.setOnClickListener(v ->{
            Editable pin = editText.getText();
            if (pin != null) {
                if(pin.length() == 6) {
                    Log.d("Pin", pin.length()+" pin ");
                    warning.setVisibility(View.GONE);
                    Intent intent = new Intent(this, OthersActivity.class);
                    intent.putExtra("pin", pin.toString());
                    startActivity(intent);
                } else {
                    warning.setVisibility(View.VISIBLE);
                    warning.setText("Pincode must contain 6 digits");
                }
            }
        });
    }

    /**
     * For initialize UI elements
     */
    private void Init() {
        editText = findViewById(R.id.pin_edit_text);
        pinButton = findViewById(R.id.pin_button);
        warning = findViewById(R.id.warning);
    }
}