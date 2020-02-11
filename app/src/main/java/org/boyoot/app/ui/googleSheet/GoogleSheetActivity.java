package org.boyoot.app.ui.googleSheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.boyoot.app.R;
import org.boyoot.app.data.GoogleSheetClient;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.ArrayList;
import java.util.List;


public class GoogleSheetActivity extends AppCompatActivity {

    List<GoogleSheetModel> data;

    GoogleSheetViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sheet);

        final Button b = findViewById(R.id.b);
        b.setVisibility(View.GONE);
        data = new ArrayList<>();
        String areas[][] ={
                {"R","رياض"},
                {"R","الرياض"},
                {"K","الخرج"},
                {"K","خرج"},
                {"K","الدلم"},
                {"M","شقراء"},
                {"M","أشقير"},
                {"M","اشقير"},
                {"M","شقراء"},
                {"M","القصب"},
                {"M","قصب"},
                {"M","ساجر"},
                {"M","الدوامي"},
                {"M","الدوامى"},
                {"M","المجمعه"},
                {"M","المجمعة"},
                {"M","الزلفى"},
                {"M","الزلفي"},
                {"M","سدير"},
                {"M","حوطة سدير"},
                {"M","حوطه سدير"},
                {"M","حوطة تميم"},
                {"M","حوطه تميم"},
                {"M","حوطةسدير"},
                {"M","حوطةتميم"},
                {"M","تمير"},
                {"M","ثرمداء"},
                {"Q","القصيم"},
                {"Qَ","بدائع"},
                {"Q","البدائع"},
                {"Q","عنيزه"},
                {"Q","عنيزة"},
                {"Q","بريده"},
                {"Q","بريدة"},
                {"Q","خبراء"},
                {"Q","الخبراء"},
                {"Q","الرس"},
                {"Q","المذنب"},
                {"Q","رياض الخبراء"},
                {"Q","البكيريه"},
                {"Q","بكيريه"},
                {"Q","البكيرية"},
                {"Q","بكيرية"},
                {"Q","الشحية"},
                {"Q","الشحيه"},
                {"Q","شحيه"},
                {"Q","شحية"},
                {"D","الدمام"},
                {"D","خبر"},
                {"D","الخبر"},
                {"D","سيهات"},
                {"D","جبيل"},
                {"D","الجبيل"},
                {"D","بقيق"},
                {"D","البقيق"},
                {"D","قطيف"},
                {"D","القطيف"},
                {"D","عنك"},
                {"D","ام الساهك"},
                {"D","أم الساهك"},
                {"D","ام ساهك"},
                {"D","أم ساهك"},
                {"W","إحساء"},
                {"W","الإحساء"},
                {"W","أحساء"},
                {"W","الأحساء"},
                {"W","هفوف"},
                {"W","الهفوف"},
                {"W","المبرز"},
                {"W","مبرز"},
                {"H","مكه المكرمه"},
                {"H","مكةالمكرمة"},
                {"H","مكه المكرمة"},
                {"H","مكةالمكرمه"},
                {"H","طائف"},
                {"H","الطائف"},
                {"J","جده"},
                {"J","جدة"},
                {"J","عسفان"},
                {"J","ابحر"},
                {"J","أبحر"},
                {"J","بحره"},
                {"J","بحرة"},


        };
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child("-M-elcbKfKaLROvEc0CV").child("test").setValue("worked");
        viewModel = new ViewModelProvider(this).get(GoogleSheetViewModel.class);
        viewModel.getData().observe(this, new Observer<List<GoogleSheetModel>>() {
            @Override
            public void onChanged(List<GoogleSheetModel> googleSheetModels) {
                data=googleSheetModels;
                Log.i("apiRetroView",data.get(15).getPhone());
                b.setVisibility(View.VISIBLE);
            }
        });

b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (data.size() != 0) {
            for (int i=0;i<data.size();++i){
                GoogleSheetModel d = data.get(i);
                System.out.println(d.getNumber()+" "+d.getPhone()+" "+d.getCode()+" "+d.getCity()+" "+d.getCover()+" "+d.getOffers());
            }

        }

    }
});


    }
}
