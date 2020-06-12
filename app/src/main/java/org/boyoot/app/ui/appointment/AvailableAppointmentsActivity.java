package org.boyoot.app.ui.appointment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityAvailableAppointmentsBinding;
import org.boyoot.app.model.Car;
import org.boyoot.app.model.CurrentCalenderDate;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.ui.config.BranchViewModel;

import java.sql.Time;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class AvailableAppointmentsActivity extends AppCompatActivity {

    ActivityAvailableAppointmentsBinding binding;
    private AppointmentsViewModel viewModel;
    private BranchViewModel branchViewModel;
    private ArrayList<String> carList;
    ArrayAdapter<String> carAdapter;
    private static final int CALENDER_DATE_CHOSEN_CODE = 1;
    private static final String CURRENT_CALENDER_KEY="current_calender";
    private static final String JOB_ID_KEY = "job id key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_available_appointments);
        setSupportActionBar(binding.appointmentTb);
        viewModel = new ViewModelProvider(this).get(AppointmentsViewModel.class);
        binding.setVm(viewModel);
        branchViewModel = new ViewModelProvider(this).get(BranchViewModel.class);
        viewModel.jobContent(Objects.requireNonNull(getIntent().getStringExtra(JOB_ID_KEY)));
        binding.setLifecycleOwner(this);
        viewModel.getJob().observe(this, new Observer<Job>() {
            @Override
            public void onChanged(Job job) {
                if (job != null){
                    branchViewModel.getBranch(job.getBranch());
                }
            }
        });
        branchViewModel.getCarsList().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                fillCarList(cars);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.appointment_calender_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_calender_view){
            Intent i = new Intent(getApplicationContext(),CalenderViewDialog.class);
            startActivityForResult(i,CALENDER_DATE_CHOSEN_CODE);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CALENDER_DATE_CHOSEN_CODE && resultCode == RESULT_OK){
            assert data != null;
            CurrentCalenderDate c = (CurrentCalenderDate) data.getSerializableExtra(CURRENT_CALENDER_KEY);
           // Calendar calendar = Calendar.getInstance();
            Calendar calendar =  Calendar.getInstance(TimeZone.getTimeZone("Asia/Riyadh"));
           // Date d = new Date();
            //calendar.setTime(d);
            //TimeZone timeZone = TimeZone.getTimeZone("");
           // calendar.setTimeZone(timeZone);
            calendar.set(Calendar.YEAR,c.getYear());
            calendar.set(Calendar.MONTH,c.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH,c.getDay());
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            //calendar.add(Calendar.HOUR_OF_DAY,10);
            //calendar.add(Calendar.MINUTE,85);
            //calendar.add(Calendar.MINUTE,16);
            //calendar.add(Calendar.HOUR_OF_DAY,3);
            calendar.setTimeInMillis(1591982060909L);

            //calendar.add(Calendar.MINUTE,66);
            //calendar.add(Calendar.MINUTE,70);
           // calendar.add(Calendar.HOUR_OF_DAY,0);
           // calendar.add(Calendar.MINUTE,40);


           // calendar.setTimeInMillis(System.currentTimeMillis());
            Log.i(CURRENT_CALENDER_KEY,calendar.getTime().toString()+" ||  "+calendar.get(Calendar.HOUR_OF_DAY)+
                    " HH ||  "+calendar.getTimeZone().getDisplayName()+
                    calendar.getTimeInMillis()+"  ||  "+
                    c.getTime()+" || "+c.getYear()+c.getMonth()+" - "+c.getDay());


        }
    }

    void fillCarList(List<Car> list){
        String s;
        if (list != null) {
            carList = new ArrayList<>();
            for (Car car : list) {
                s = car.getTitle();
                carList.add(s);
            }
        }else {
            carList = new ArrayList<>();
            carList.add("NULL");
        }
        carAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,carList);
        binding.carSpinner.setAdapter(carAdapter);
    }
}
