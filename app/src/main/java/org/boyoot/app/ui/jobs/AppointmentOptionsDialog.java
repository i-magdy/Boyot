package org.boyoot.app.ui.jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import com.google.android.material.card.MaterialCardView;

import org.boyoot.app.R;

public class AppointmentOptionsDialog extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appointment_options_dialog);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }
}