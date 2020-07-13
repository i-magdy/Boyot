package org.boyoot.app.ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.DialogAddCarBinding;
import org.boyoot.app.model.Car;

import java.util.List;
import java.util.Objects;

public class AddCarDialog extends AppCompatActivity {

    DialogAddCarBinding binding;
    BranchViewModel viewModel;
    private List<Car> cars;

    private static final String BRANCH_ID_KEY = "branch id key";
    private static final String BRANCHES_PATH="branches";
    private static final String PATH_NUMBER = "pathNumber";
    private String BRANCH;
    private int pathNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this,R.layout.dialog_add_car);
        viewModel = new ViewModelProvider(this).get(BranchViewModel.class);
        binding.addCarDialogMainLayout.setVisibility(View.INVISIBLE);
        if (getIntent().hasExtra(BRANCH_ID_KEY)){
            BRANCH = Objects.requireNonNull(getIntent().getStringExtra(BRANCH_ID_KEY));
            if (getIntent().hasExtra(PATH_NUMBER)){
                pathNo = getIntent().getIntExtra(PATH_NUMBER,16);
            }
            viewModel.getBranch(this,BRANCH,null);

        }



        viewModel.getCarsList().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> car) {
                binding.addCarDialogMainLayout.setVisibility(View.VISIBLE);
                cars = car;
                if (getIntent().hasExtra(PATH_NUMBER)){
                    if (pathNo < car.size()) {
                        fillData(getCar(car));
                    }
                }
            }
        });

        binding.cancelAddCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.confirmAddCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCar();


            }
        });
    }


    void addNewCar(){
        String title = binding.carTitleEt.getEditableText().toString();
        String carNumber = binding.carNumberEt.getEditableText().toString();
        String workerNumber = binding.workerNumberEt.getEditableText().toString();

        if (title.isEmpty()){
            binding.carTitleEt.setError(getString(R.string.empty_message));
        }

        if (carNumber.isEmpty()){
            binding.carNumberEt.setError(getString(R.string.empty_message));
        }

        if (workerNumber.isEmpty()){
            binding.workerNumberEt.setError(getString(R.string.empty_message));
        }



        if (!title.isEmpty() && !carNumber.isEmpty() && !workerNumber.isEmpty()){
            if (!getIntent().hasExtra(PATH_NUMBER)) {
                int pNo = 0;
                if (cars != null) {
                    pNo = cars.size();
                }
                Car car = new Car(title, carNumber, pNo, getWorkers(workerNumber));

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference ref = db.collection(BRANCHES_PATH).document(BRANCH);
                ref.update("cars", FieldValue.arrayUnion(car))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
            }else {
                Car car = new Car(title, carNumber, pathNo, getWorkers(workerNumber));
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference ref = db.collection(BRANCHES_PATH).document(BRANCH);
                ref.update("cars", FieldValue.arrayRemove(getCar(cars)))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference ref = db.collection(BRANCHES_PATH).document(BRANCH);
                                ref.update("cars", FieldValue.arrayUnion(car))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                            }
                                        });
                            }
                        });
            }
        }

    }

    int getWorkers(String s){
        double d = Double.parseDouble(s);
        return  (int) d;
    }


    void fillData(Car car){
        if (car != null) {
            binding.carTitleEt.setText(car.getTitle());
            binding.carNumberEt.setText(car.getCarNumber());
            binding.workerNumberEt.setText(String.valueOf(car.getWorker()));
        }
    }

    Car getCar(List<Car> c){
        Car currentCar = new Car();
        for (int i=0;i<c.size();++i){
            int p = c.get(i).getPathNo();
            if (pathNo == p){
                currentCar= c.get(i);
                break;
            }
        }
        return currentCar;
    }
}