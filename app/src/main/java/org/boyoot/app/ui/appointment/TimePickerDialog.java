package org.boyoot.app.ui.appointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import org.boyoot.app.R;
import org.boyoot.app.databinding.DialogTimePickerBinding;
import org.boyoot.app.model.job.TimePickerModel;

public class TimePickerDialog extends AppCompatActivity {

    private DialogTimePickerBinding binding;
    private static final String TIME_PICKER_KEY="time picked";

    private TimePickerModel time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_time_picker);
        binding.aTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                time = new TimePickerModel(hourOfDay,minute);
            }
        });
        binding.timePickerConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(TIME_PICKER_KEY,time);
                setResult(RESULT_OK,i);
                finish();
            }
        });

        binding.timePickerCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_CANCELED,i);
                finish();
            }
        });
    }

}