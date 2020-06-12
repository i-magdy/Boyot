package org.boyoot.app.ui.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CalendarView;

import org.boyoot.app.R;
import org.boyoot.app.databinding.DialogCalenderViewBinding;
import org.boyoot.app.model.CurrentCalenderDate;

public class CalenderViewDialog extends AppCompatActivity {

    private DialogCalenderViewBinding binding;

    public static final int CALENDER_DATE_CHOSEN_CODE = 1;
    private static final String CURRENT_CALENDER_KEY="current_calender";
    private CurrentCalenderDate calenderDate;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_calender_view);
        calenderDate = new CurrentCalenderDate();
        resultIntent = new Intent();
        binding.calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                calenderDate.setTime(view.getDate());
                calenderDate.setYear(year);
                calenderDate.setMonth(month);
                calenderDate.setDay(dayOfMonth);
                resultIntent.putExtra(CURRENT_CALENDER_KEY,calenderDate);
                setResult(RESULT_OK,resultIntent);
                finish();

            }
        });
    }
}
