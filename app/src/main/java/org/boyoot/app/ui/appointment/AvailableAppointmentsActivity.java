package org.boyoot.app.ui.appointment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityAvailableAppointmentsBinding;
import org.boyoot.app.model.Car;
import org.boyoot.app.model.CurrentCalenderDate;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.ui.jobs.JobsListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import static org.boyoot.app.utilities.TimeUtility.dateFormatter;
import static org.boyoot.app.utilities.TimeUtility.getMonthOfYear;

public class AvailableAppointmentsActivity extends AppCompatActivity implements JobsListAdapter.ListItemOnClickListener{

    ActivityAvailableAppointmentsBinding binding;
    private AppointmentsViewModel viewModel;
    private ArrayList<String> carList;
    private List<Car> cars;
    ArrayAdapter<String> carAdapter;
    private String BRANCH;
    private CurrentCalenderDate currentCalenderDate;
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
        binding.addAppointmentFab.hide();
        viewModel.jobContent(Objects.requireNonNull(getIntent().getStringExtra(JOB_ID_KEY)));
        binding.setLifecycleOwner(this);
        viewModel.getJob().observe(this, new Observer<Job>() {
            @Override
            public void onChanged(Job job) {
                if (job != null){
                    BRANCH = job.getBranch();
                }
            }
        });
        viewModel.getCarsList().observe(this, this::fillCarList);

        viewModel.getCurrentCalender().observe(this, new Observer<CurrentCalenderDate>() {
            @Override
            public void onChanged(CurrentCalenderDate currentCalender) {
                binding.addAppointmentFab.show();
                currentCalenderDate = currentCalender;
            }
        });

        binding.carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.getSelectedCar(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        JobsListAdapter adapter = new JobsListAdapter(this);
        binding.availableAppointmentListRecycler.setAdapter(adapter);
        viewModel.getJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                adapter.setJobs(jobs);
            }
        });
        binding.availableAppointmentListRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.addAppointmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddJobToAppointmentListActivity.class);
                i.putExtra(CURRENT_CALENDER_KEY,currentCalenderDate);
                startActivity(i);
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
            Calendar calendar =  Calendar.getInstance(TimeZone.getTimeZone("Asia/Riyadh"));
            viewModel.setCurrentCalender(c,
                    viewModel.getSelectedPath(binding.carSpinner.getSelectedItemPosition()),BRANCH);
            calendar.set(Calendar.YEAR,c.getYear());
            calendar.set(Calendar.MONTH,c.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH,c.getDay());
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);


            viewModel.getList(BRANCH,viewModel.getSelectedPath(binding.carSpinner.getSelectedItemPosition()),
                    c.getYear(),c.getMonth(),c.getDay());



           // calendar.setTimeInMillis(System.currentTimeMillis());
            Log.i(CURRENT_CALENDER_KEY,calendar.getTime().toString()+" ||  "+calendar.get(Calendar.HOUR_OF_DAY)+
                    " HH ||  "+calendar.getTimeZone().getDisplayName()+
                    calendar.getTimeInMillis()+"  ||  "+
                    c.getTime()+" || "+c.getYear()+c.getMonth()+" - "+c.getDay()+
                    "  FORMATER ||  "+dateFormatter(calendar.getTime()));


        }
    }



    void fillCarList(List<Car> list){
        String s;
        cars = list;
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


    @Override
    public void onItemClickListener(int position) {

    }
}
